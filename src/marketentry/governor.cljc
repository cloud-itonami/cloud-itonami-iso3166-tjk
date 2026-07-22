(ns marketentry.governor
  "Market-Entry Compliance Governor -- the independent compliance layer
  that earns the MarketEntry-LLM the right to commit. The LLM has no
  notion of jurisdictional procurement law, whether Tajikistan's Tax
  Committee (andoz.tj) business registration is actually on file,
  whether the actual procurement regulator has been kept distinct from
  the completely different anti-corruption agency or fused/confused
  into one fabricated fact, whether a claimed engagement fee actually
  equals base + months x rate, whether a Tax Code tax registration or a
  State Committee on Investments foreign-investment registration has
  been verified for a filing that requires it, or when a draft stops
  being a draft and becomes a real-world portal submission, so this
  MUST be a separate system able to *reject* a proposal and fall back
  to HOLD.

  `:itonami.blueprint/governor` is `:market-entry-compliance-governor`
  (shared family keyword on blueprints; this is one of several
  *running* implementations of that governor across the iso3166
  family).

  This blueprint's own text (docs/business-model.md Trust Controls:
  'any actual portal registration or filing submission requires
  Market-Entry Compliance Governor clearance and always escalates to
  human sign-off'; 'a false or fabricated regulatory-requirement claim
  is a HARD hold') names exactly the checks below.

  Eight checks, in priority order, ALL HARD violations: a human
  approver CANNOT override them. The confidence/actuation gate is
  SOFT: it asks a human to look (low confidence / actuation), and the
  human may approve -- but see `marketentry.phase`: for `:stake
  :actuation/draft-filing`/`:actuation/submit-filing` NO phase ever
  allows auto-commit either. Two independent layers agree that
  actuation is always a human call.

    1. Spec-basis                    -- did the jurisdiction proposal
                                         cite an OFFICIAL source
                                         (`marketentry.facts`), or
                                         invent one?
    2. Evidence incomplete           -- for `:filing/draft`/
                                         `:filing/submit`, has the
                                         jurisdiction actually been
                                         assessed with a full evidence
                                         checklist on file? (this is
                                         also where the 2006 Public
                                         Procurement Law compliance
                                         item lives, as one of the
                                         checklist entries -- title/
                                         year only, no fabricated law
                                         number.)
    3. Business-registration missing -- for `:filing/submit`, when the
                                         engagement declares
                                         `:requires-business-
                                         registration? true`,
                                         INDEPENDENTLY verify
                                         `:has-business-registration?`
                                         is true. Grounded in the Tax
                                         Committee's Unified state
                                         register (andoz.tj),
                                         'single window' registration.
    4. Procurement-agency conflation -- for `:jurisdiction/assess`,
                                         when the jurisdiction has a
                                         distinct agency-
                                         disambiguation spec-basis on
                                         file, INDEPENDENTLY verify the
                                         proposal keeps the actual
                                         procurement regulator (Agency
                                         on Public Procurement of
                                         Goods, Works and Services) and
                                         the DIFFERENT anti-corruption
                                         agency (Agency for State
                                         Financial Control and
                                         Combating Corruption) DISTINCT
                                         -- never collapses them into
                                         one fused authority, and never
                                         cites the anti-corruption
                                         agency as if it were the
                                         procurement regulator.
                                         FLAGSHIP genuinely new check
                                         for the iso3166 family (grep-
                                         verified absent as a governor
                                         check function name fleet-wide
                                         at build time) -- this is the
                                         central fabrication trap for
                                         this jurisdiction: both
                                         authorities use a similarly-
                                         named 'Agency ... under the
                                         Government' structure but have
                                         completely different scopes,
                                         and naive sources conflate
                                         them.
    5. Engagement fee mismatch       -- for `:filing/submit`,
                                         INDEPENDENTLY recompute
                                         whether the engagement's own
                                         `:claimed-fee` equals
                                         `base-fee + monthly-rate x
                                         monitoring-months` -- honest
                                         reapplication of the ground-
                                         truth-recompute discipline
                                         sibling actors use.
    6. Tax-registration unverified   -- for `:filing/submit`, when the
                                         engagement declares
                                         `:requires-tax-registration?
                                         true`, INDEPENDENTLY check
                                         `:tax-registration-verified?`.
                                         Grounded in the Tax Code of
                                         the Republic of Tajikistan
                                         (registered 17 September
                                         2012), administered by the Tax
                                         Committee. CONDITIONAL on the
                                         engagement's own ground truth.
    7. Investment-registration       -- for `:filing/submit`, when the
       missing                          engagement declares
                                         `:requires-investment-
                                         registration? true`,
                                         INDEPENDENTLY verify
                                         `:investment-registration-
                                         verified?` is true. SECOND
                                         flagship-adjacent check for
                                         this vertical. Grounded in the
                                         State Committee on Investments
                                         and State Property Management,
                                         Law No. 2173 'On Capital and
                                         Promotion of Investment
                                         Activity' (14 May 2025) and
                                         Government Decree No. 590
                                         (28 December 2006).
                                         CONDITIONAL on the
                                         engagement's own ground truth
                                         (only engagements involving
                                         government interests, e.g.
                                         Free Economic Zones, trigger
                                         this screening).
    8. Confidence floor / actuation
       gate                          -- LLM confidence below threshold,
                                         OR the op is `:filing/draft`/
                                         `:filing/submit` (REAL acts)
                                         -> escalate.

  Two more guards, double-draft/double-submit prevention, are enforced
  off dedicated `:drafted?`/`:submitted?` facts (never a `:status`
  value)."
  (:require [marketentry.facts :as facts]
            [marketentry.registry :as registry]
            [marketentry.store :as store]))

(def confidence-floor 0.6)

(def high-stakes
  "Stakes grave enough to always require a human, even when clean.
  Drafting a real portal package and submitting a real portal
  registration are the two real-world actuation events this actor
  performs."
  #{:actuation/draft-filing :actuation/submit-filing})

;; ----------------------------- checks -----------------------------

(defn- spec-basis-violations
  "A `:jurisdiction/assess` (or `:filing/draft`/`:filing/submit`)
  proposal with no spec-basis citation is a HARD violation -- never
  invent a jurisdiction's market-entry requirements."
  [{:keys [op]} proposal]
  (when (contains? #{:jurisdiction/assess :filing/draft :filing/submit} op)
    (let [value (:value proposal)]
      (when (or (empty? (:cites proposal))
                (and (contains? value :spec-basis) (nil? (:spec-basis value))))
        [{:rule :no-spec-basis
          :detail "公式spec-basisの引用が無い提案は法域要件として扱えない"}]))))

(defn- evidence-incomplete-violations
  "For `:filing/draft`/`:filing/submit`, the jurisdiction's required
  registration evidence must actually be satisfied."
  [{:keys [op subject]} st]
  (when (contains? #{:filing/draft :filing/submit} op)
    (let [e (store/engagement st subject)
          assessment (store/assessment-of st subject)]
      (when-not (and assessment
                     (facts/required-evidence-satisfied?
                      (:jurisdiction e) (:checklist assessment)))
        [{:rule :evidence-incomplete
          :detail "法域の必要書類(統一国家登記簿/2006年公共調達法/Agency on Public Procurement電子システム登録/税法典2012登録等)が充足していない状態での提案"}]))))

(defn- business-registration-missing-violations
  "For `:filing/submit`, when the engagement declares
  `:requires-business-registration? true`, INDEPENDENTLY verify
  `:has-business-registration?` is true. Grounded in the Tax
  Committee's Unified state register (andoz.tj), 'single window'
  registration. CONDITIONAL on the engagement's own
  `:requires-business-registration?` ground truth."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when (and (true? (:requires-business-registration? e))
                 (not (true? (:has-business-registration? e))))
        [{:rule :business-registration-missing
          :detail (str subject " は税務委員会(Tax Committee)統一国家登記簿への登録確認を要するが未確認 -- 提出提案は進められない")}]))))

(defn- procurement-agency-conflation-violations
  "For `:jurisdiction/assess`, when the jurisdiction has a distinct
  agency-disambiguation spec-basis on file
  (`marketentry.facts/agency-disambiguation-spec-basis`),
  INDEPENDENTLY verify the proposal's own claim keeps the actual
  procurement regulator and the DIFFERENT anti-corruption agency
  DISTINCT. For TJK: the Agency on Public Procurement of Goods, Works
  and Services under the Government of the Republic of Tajikistan is
  the procurement regulator; the Agency for State Financial Control
  and Combating Corruption is a wholly separate anti-corruption
  oversight body -- both use a similarly-named 'Agency ... under the
  Government' structure but have completely different scopes. A
  proposal that collapses them into a single fused authority, cites
  the anti-corruption agency as if it were the procurement regulator,
  or omits one, or cites either against the wrong catalogued value, is
  a HARD violation -- FLAGSHIP genuinely new check for the iso3166
  family."
  [{:keys [op]} proposal]
  (when (= op :jurisdiction/assess)
    (let [value (:value proposal)
          iso3 (:jurisdiction value)
          adb (facts/agency-disambiguation-spec-basis iso3)]
      (when adb
        (let [regulator (:procurement-regulator-authority value)
              anti-corruption (:anti-corruption-agency-authority value)]
          (when (or (nil? regulator)
                    (nil? anti-corruption)
                    (= regulator anti-corruption)
                    (not= regulator (:procurement-regulator-authority adb))
                    (not= anti-corruption (:anti-corruption-agency-authority adb)))
            [{:rule :procurement-agency-conflated
              :detail (str iso3 " の公共調達規制機関の記載が Agency on Public Procurement of Goods, Works and Services と"
                          " Agency for State Financial Control and Combating Corruption(汚職対策機関、別主体)を"
                          "混同しているか、未記載/カタログ値と不一致 -- 別個の主体として検証できない")}]))))))

(defn- engagement-fee-mismatch-violations
  "For `:filing/submit`, INDEPENDENTLY recompute whether the
  engagement's own claimed fee equals base + months x rate."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when-not (registry/engagement-fee-matches-claim? e)
        [{:rule :engagement-fee-mismatch
          :detail (str subject " の申告手数料(" (:claimed-fee e)
                      ")が独立再計算値(" (registry/compute-engagement-fee e) ")と一致しない")}]))))

(defn- tax-registration-unverified-violations
  "For `:filing/submit`, when the engagement declares
  `:requires-tax-registration? true`, INDEPENDENTLY check
  `:tax-registration-verified?` -- grounded in the Tax Code of the
  Republic of Tajikistan (registered 17 September 2012), administered
  by the Tax Committee. CONDITIONAL on the engagement's own ground
  truth."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when (and (true? (:requires-tax-registration? e))
                 (not (true? (:tax-registration-verified? e))))
        [{:rule :tax-registration-unverified
          :detail (str subject " は税務委員会(Tax Committee)の税法典(2012年)登録確認を要するが未確認 -- 提出提案は進められない")}]))))

(defn- investment-registration-missing-violations
  "For `:filing/submit`, when the engagement declares
  `:requires-investment-registration? true`, INDEPENDENTLY verify
  `:investment-registration-verified?` is true -- SECOND flagship-
  adjacent check for this vertical. Grounded in the State Committee on
  Investments and State Property Management, Law No. 2173 'On Capital
  and Promotion of Investment Activity' (14 May 2025) and Government
  Decree No. 590 (28 December 2006). CONDITIONAL on the engagement's
  own `:requires-investment-registration?` ground truth (only
  engagements involving government interests, e.g. Free Economic
  Zones, trigger this screening)."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when (and (true? (:requires-investment-registration? e))
                 (not (true? (:investment-registration-verified? e))))
        [{:rule :investment-registration-missing
          :detail (str subject " は国家投資・国有財産管理委員会(State Committee on Investments)の外国投資登録"
                      "(Law No. 2173)確認を要するが未確認 -- 提出提案は進められない")}]))))

(defn- already-drafted-violations
  "For `:filing/draft`, refuses to draft the SAME engagement twice."
  [{:keys [op subject]} st]
  (when (= op :filing/draft)
    (when (store/engagement-already-drafted? st subject)
      [{:rule :already-drafted
        :detail (str subject " は既にドラフト済み")}])))

(defn- already-submitted-violations
  "For `:filing/submit`, refuses to submit the SAME engagement twice."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (when (store/engagement-already-submitted? st subject)
      [{:rule :already-submitted
        :detail (str subject " は既に提出済み")}])))

(defn check
  "Censors a MarketEntry-LLM proposal against the governor rules.
  Returns {:ok? bool :violations [..] :confidence c :escalate? bool
  :high-stakes? bool :hard? bool}."
  [request _context proposal st]
  (let [hard (into []
                   (concat (spec-basis-violations request proposal)
                           (evidence-incomplete-violations request st)
                           (business-registration-missing-violations request st)
                           (procurement-agency-conflation-violations request proposal)
                           (engagement-fee-mismatch-violations request st)
                           (tax-registration-unverified-violations request st)
                           (investment-registration-missing-violations request st)
                           (already-drafted-violations request st)
                           (already-submitted-violations request st)))
        conf (:confidence proposal 0.0)
        low? (< conf confidence-floor)
        stakes? (boolean (high-stakes (:stake proposal)))
        hard? (boolean (seq hard))]
    {:ok?          (and (not hard?) (not low?) (not stakes?))
     :violations   hard
     :confidence   conf
     :hard?        hard?
     :escalate?    (and (not hard?) (or low? stakes?))
     :high-stakes? stakes?}))

(defn hold-fact
  "The audit fact written when a proposal is rejected (HOLD)."
  [request context verdict]
  {:t          :governor-hold
   :op         (:op request)
   :actor      (:actor-id context)
   :subject    (:subject request)
   :disposition :hold
   :basis      (mapv :rule (:violations verdict))
   :violations (:violations verdict)
   :confidence (:confidence verdict)})
