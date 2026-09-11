(ns marketentry.facts
  "Tajikistan market-entry catalog.

  Verified facts (from the pre-session research this repo's build task
  was scoped against, cross-checked against multiple independent
  sources, plus two live re-checks this session -- see below):

  - Business registration: the Tax Committee under the Government of
    the Republic of Tajikistan (andoz.tj) operates the Unified state
    register via a \"single window\" registration process. Confirmed
    across multiple independent sources.
  - Public procurement law: Law of the Republic of Tajikistan \"On
    Public Procurement of Goods, Works and Services,\" 2006 -- cited by
    title and year ONLY. A commonly-repeated \"No. 190\" law number is
    only weakly sourced and UNCONFIRMED; this catalog deliberately does
    not carry a law number.
  - **The central fabrication trap for this jurisdiction**: the
    procurement regulator is the **Agency on Public Procurement of
    Goods, Works and Services under the Government of the Republic of
    Tajikistan** -- a COMPLETELY DIFFERENT body from the **Agency for
    State Financial Control and Combating Corruption** (an
    anti-corruption oversight body). Both use a similarly-named
    'Agency ... under the Government' structure but have different
    scopes; this catalog and `marketentry.governor`'s
    `procurement-agency-conflation-violations` keep them apart on
    purpose -- see `agency-disambiguation-spec-basis` below. WebFetch-
    verified live this session: eprocurement.gov.tj's own footer names
    the agency verbatim (Russian): \"Агентство по государственным
    закупкам товаров, работ и услуг при Правительстве Республики
    Таджикистан\" (Agency on Public Procurement of Goods, Works and
    Services under the Government of the Republic of Tajikistan).
  - E-procurement: multiple candidate portal domains exist
    (goszakupki.tj, eprocurement.gov.tj, zakupki.gov.tj, tenders.tj)
    but the canonical CURRENT one is UNRESOLVED -- this catalog does
    NOT assert a single specific portal URL as definitively 'the'
    current system. `:national-spec` below models procurement
    STRUCTURALLY (an electronic system under the Agency on Public
    Procurement) rather than hard-coding one candidate domain as
    canonical. eprocurement.gov.tj is cited only as the domain that,
    fetched live this session, verbatim named the Agency -- not as an
    assertion that it is uniquely 'the' current portal.
  - Tax registration: Tax Code of the Republic of Tajikistan
    (registered 17 September 2012), administered by the Tax Committee
    (andoz.tj).
  - Foreign investment: the State Committee on Investments and State
    Property Management of the Republic of Tajikistan screens inbound
    foreign investments involving government interests (including Free
    Economic Zones), per Government Decree No. 590 (28 December 2006).
    New investment law: Law No. 2173 \"On Capital and Promotion of
    Investment Activity,\" adopted 14 May 2025, replacing the 2016
    investment law, with a 15-year investor stability guarantee. This
    session independently WebFetch-verified https://mmk.tj/ as the
    National Center for Legislative Affairs under the President of the
    Republic of Tajikistan (Tajikistan's official legislative-
    repository site) -- used here as the citation domain for Law
    No. 2173 / Decree No. 590 since the State Committee's own agency
    subdomain was not reachable this session (network error); no
    ministry-specific URL is asserted.

  Explicitly NOT claimed (fabrication traps this catalog defends
  against):
  - NOT \"Law No. 190\" for the 2006 procurement law -- unconfirmed,
    omitted.
  - NOT a single canonical e-procurement portal URL among the several
    candidate domains -- modeled structurally instead.
  - NOT a Ministry-of-Justice-vs-Tax-Committee split for business
    registration -- kept simply \"Tax Committee\".
  - NOT the Agency for State Financial Control and Combating
    Corruption as the procurement regulator -- see
    `agency-disambiguation-spec-basis`.

  ** ELEVATED cross-contamination risk **: Tajikistan and Turkmenistan
  are visually/phonetically similar '-stan' Central Asian republics
  with structurally analogous institutions (both have a 'Tax
  Committee', a 'State Committee on Investments', Soviet-derived civil
  law). Every entry below is Tajikistan-SPECIFIC, verified separately
  from Turkmenistan's -- do not copy TKM facts here, or vice versa.

  Coverage is reported HONESTLY (see `coverage`): a jurisdiction not in
  this table has NO spec-basis, full stop -- the advisor must not
  fabricate one, and the governor holds if it tries. The non-TJK
  entries below (`USA`/`JPN`/`DEU`/`GBR`) are not fresh research for
  this repo -- they are copied verbatim from already-implemented
  sibling iso3166 actors, kept here only as known-good fixtures for
  `coverage`/multi-jurisdiction tests, the same fleet-wide convention
  every sibling catalog follows.

  Recommended check count for this vertical is 5 (not padded to 6-7):
  (1) Tax Committee business registration, (2) 2006 Public Procurement
  Law compliance (folded into the evidence checklist -- title/year
  only, no fabricated law number), (3) Agency on Public Procurement
  regulator awareness with an explicit boundary/negative check against
  the anti-corruption agency (FLAGSHIP -- `agency-disambiguation-spec-
  basis`, mirroring how `cloud-itonami-iso3166-lva`/`-svk` distinguish
  regulator-vs-operator pairs, reapplied here to a regulator-vs-wholly-
  different-agency pair), (4) Tax Code (2012) + Tax Committee tax
  registration, (5) State Committee on Investments and State Property
  Management foreign-investment registration, grounded in Law
  No. 2173 (2025).")

(def catalog
  "iso3 -> requirement map. `:required-evidence` is the 4-item evidence
  checklist the governor's `evidence-incomplete-violations` checks
  against `marketentry.facts/required-evidence-satisfied?`; one item
  per distinct Tajikistan regulatory surface this blueprint touches --
  Unified state register (business registration), the 2006 Public
  Procurement Law, the Agency on Public Procurement's electronic
  system, Tax Code (2012) tax registration. `:legal-basis`/
  `:owner-authority`/`:provenance` are the G2 citation the governor
  requires before any `:jurisdiction/assess` proposal can commit.
  `:rep-*` carries the Tax Committee business-registration citation.
  `:corporate-number-*` carries the Tax Code / tax-registration
  citation. `:procurement-regulator-authority`/
  `:anti-corruption-agency-authority`/`:agency-disambiguation-note`/
  `:agency-disambiguation-provenance` are the FLAGSHIP new field group
  for this vertical -- see the namespace docstring; they keep the
  actual procurement regulator and the DIFFERENT anti-corruption
  agency apart on purpose. `:investment-registration-*` carries the
  State Committee on Investments / Law No. 2173 citation."
  {"TJK" {:name "Tajikistan"
          :owner-authority "Agency on Public Procurement of Goods, Works and Services under the Government of the Republic of Tajikistan"
          :legal-basis "Law of the Republic of Tajikistan \"On Public Procurement of Goods, Works and Services\" (2006)"
          :national-spec "electronic procurement system under the Agency on Public Procurement of Goods, Works and Services (multiple candidate portal domains exist -- goszakupki.tj, eprocurement.gov.tj, zakupki.gov.tj, tenders.tj -- the single canonical CURRENT one is unresolved; modeled structurally, no one domain asserted as definitively 'the' current system) + Unified state register (Tax Committee single-window) business registration"
          :provenance "https://andoz.tj/"
          :required-evidence ["Unified state register (Tax Committee single-window) business-registration record"
                              "Law \"On Public Procurement of Goods, Works and Services\" (2006) compliance record"
                              "Agency on Public Procurement electronic-system registration record"
                              "Tax Code (2012) / Tax Committee tax-registration record"]
          :rep-owner-authority "Tax Committee under the Government of the Republic of Tajikistan (andoz.tj)"
          :rep-legal-basis "Unified state register, single-window registration process"
          :rep-provenance "https://andoz.tj/"
          :corporate-number-owner-authority "Tax Committee under the Government of the Republic of Tajikistan (andoz.tj)"
          :corporate-number-legal-basis "Tax Code of the Republic of Tajikistan, registered 17 September 2012"
          :corporate-number-provenance "https://andoz.tj/"
          ;; FLAGSHIP field group -- see namespace docstring. The
          ;; central fabrication trap for this jurisdiction: NEVER
          ;; conflate the procurement regulator with the anti-
          ;; corruption agency.
          :procurement-regulator-authority "Agency on Public Procurement of Goods, Works and Services under the Government of the Republic of Tajikistan"
          :anti-corruption-agency-authority "Agency for State Financial Control and Combating Corruption"
          :agency-disambiguation-note "The procurement regulator is the Agency on Public Procurement of Goods, Works and Services. This is a COMPLETELY DIFFERENT body from the Agency for State Financial Control and Combating Corruption (an anti-corruption oversight body). Both use a similarly-named 'Agency ... under the Government' structure but have different scopes -- do not conflate them, do not attribute procurement-regulator authority to the anti-corruption agency, and do not fuse the two into one value."
          :agency-disambiguation-provenance "https://eprocurement.gov.tj/"
          :investment-registration-owner-authority "State Committee on Investments and State Property Management of the Republic of Tajikistan"
          :investment-registration-legal-basis "Law No. 2173 \"On Capital and Promotion of Investment Activity\" (adopted 14 May 2025, replacing the 2016 investment law, 15-year investor stability guarantee); screening of inbound foreign investments involving government interests (including Free Economic Zones) per Government Decree No. 590 (28 December 2006)"
          :investment-registration-provenance "https://mmk.tj/"}
   "USA" {:name "United States" :owner-authority "GSA/SAM.gov" :legal-basis "FAR" :national-spec "SAM.gov" :provenance "https://sam.gov/"
          :required-evidence ["EIN record" "SAM.gov registration record" "State business registration record" "SAM UEI verification record"]}
   "JPN" {:name "Japan" :owner-authority "デジタル庁 / 全省庁統一資格 審査機関" :legal-basis "全省庁統一資格 / GEPS"
          :national-spec "unified central-government tender qualification" :provenance "https://www.chotatujoho.go.jp/va/com/ShikakuTop.html"
          :required-evidence ["法人番号確認記録" "全省庁統一資格申請記録" "GEPS 事業者登録記録" "日本居住代理人確認記録"]}
   "DEU" {:name "Germany" :owner-authority "e-Vergabe platforms" :legal-basis "GWB / VgV" :national-spec "e-Vergabe supplier registration"
          :provenance "https://www.evergabe-online.de/"
          :required-evidence ["Handelsregister extract" "e-Vergabe registration record" "USt-IdNr record" "Authorized-representative record"]}
   "GBR" {:name "United Kingdom" :owner-authority "Crown Commercial Service / Find a Tender" :legal-basis "Public Contracts Regulations 2015"
          :national-spec "Find a Tender Service registration" :provenance "https://www.find-tender.service.gov.uk/"
          :required-evidence ["Companies House record" "Find a Tender registration record" "VAT registration record" "Authorized-representative record"]}})

(defn spec-basis
  "The jurisdiction's requirement map, or nil -- nil means NO
  spec-basis, and the governor must hold any proposal that tries to
  assess or file on it."
  [iso3]
  (get catalog iso3))

(defn coverage
  "Honest coverage report: how many of the requested jurisdictions
  actually have a spec-basis entry. Never report a missing
  jurisdiction as covered."
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-tjk R0: " (count catalog)
                 " jurisdictions seeded with an official spec-basis. "
                 "This is a starting catalog for market-entry navigation, "
                 "not a survey of all ~194 jurisdictions -- extend "
                 "`marketentry.facts/catalog`, never fabricate a "
                 "jurisdiction's requirements.")})))

(defn required-evidence-satisfied?
  "Does `submitted` (a set/coll of evidence keywords or strings)
  satisfy every evidence item listed for `iso3`? Missing spec-basis ->
  never satisfied."
  [iso3 submitted]
  (when-let [{:keys [required-evidence]} (spec-basis iso3)]
    (let [need (count required-evidence)
          have (count (filter (set submitted) required-evidence))]
      (= need have))))

(defn evidence-checklist [iso3]
  (:required-evidence (spec-basis iso3) []))

(defn rep-spec-basis
  "The jurisdiction's local business-registration requirement map, or
  nil when this catalog has no such regime. For TJK this is the Tax
  Committee's Unified state register (single-window) requirement --
  real and applicable to any commercial operator bidding into
  Tajikistan public procurement."
  [iso3]
  (when-let [sb (spec-basis iso3)]
    (when (:rep-owner-authority sb)
      (select-keys sb [:rep-owner-authority :rep-legal-basis :rep-provenance]))))

(defn corporate-number-spec-basis
  "The jurisdiction's corporate-number / tax-id regime, or nil. For TJK
  this is Tax Committee tax registration under the Tax Code of the
  Republic of Tajikistan (registered 17 September 2012)."
  [iso3]
  (when-let [sb (spec-basis iso3)]
    (when (:corporate-number-owner-authority sb)
      (select-keys sb [:corporate-number-owner-authority
                       :corporate-number-legal-basis
                       :corporate-number-provenance]))))

(defn agency-disambiguation-spec-basis
  "The jurisdiction's procurement-regulator-vs-anti-corruption-agency
  DISAMBIGUATION citation, or nil -- the FLAGSHIP field group for this
  vertical. Keeps the actual procurement regulator
  (`:procurement-regulator-authority`) and the DIFFERENT anti-
  corruption agency (`:anti-corruption-agency-authority`) as two
  DISTINCT values so a consumer (governor, advisor, UI) is
  structurally prevented from conflating them into one fused fact or
  mistakenly citing the anti-corruption agency as the procurement
  regulator. For TJK: the Agency on Public Procurement of Goods, Works
  and Services is the procurement regulator; the Agency for State
  Financial Control and Combating Corruption is a wholly separate
  anti-corruption oversight body with a similarly-named 'Agency ...
  under the Government' structure but a different scope."
  [iso3]
  (when-let [sb (spec-basis iso3)]
    (when (:procurement-regulator-authority sb)
      (select-keys sb [:procurement-regulator-authority
                       :anti-corruption-agency-authority
                       :agency-disambiguation-note
                       :agency-disambiguation-provenance]))))

(defn investment-registration-spec-basis
  "The jurisdiction's foreign-investment-registration regime, or nil.
  For TJK this is the State Committee on Investments and State
  Property Management, grounded in Law No. 2173 \"On Capital and
  Promotion of Investment Activity\" (14 May 2025) and Government
  Decree No. 590 (28 December 2006)."
  [iso3]
  (when-let [sb (spec-basis iso3)]
    (when (:investment-registration-owner-authority sb)
      (select-keys sb [:investment-registration-owner-authority
                       :investment-registration-legal-basis
                       :investment-registration-provenance]))))
