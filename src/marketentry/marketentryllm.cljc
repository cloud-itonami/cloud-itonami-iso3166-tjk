(ns marketentry.marketentryllm
  "MarketEntry-LLM client -- the *contained intelligence node* for
  the Tajikistan public-sector market-entry compliance actor.

  It normalizes engagement intake, drafts a per-jurisdiction market-
  entry evidence checklist (including the procurement-agency
  disambiguation citation -- see `marketentry.facts`/
  `marketentry.governor`), drafts the filing-draft action, and drafts
  the filing-submit action. CRITICAL: it is a smart-but-untrusted
  advisor. It returns a *proposal* (with a rationale + the fields it
  cited), never a committed record or a real portal submission. Every
  output is censored downstream by `marketentry.governor` before
  anything touches the SSoT, and `:filing/draft`/`:filing/submit`
  proposals NEVER auto-commit at any phase -- see README Actuation.

  Like every sibling actor's advisor, this is a deterministic mock so
  the actor graph runs offline and the governor contract is exercised
  end-to-end."
  (:require #?(:clj  [clojure.edn :as edn]
               :cljs [cljs.reader :as edn])
            [clojure.string :as str]
            [marketentry.facts :as facts]
            [marketentry.store :as store]))

(defn- normalize-intake
  [_db {:keys [patch]}]
  {:summary    (str "参入案件記録更新: " (pr-str (keys patch)))
   :rationale  "入力 patch の正規化のみ。新規事実の生成なし。"
   :cites      (vec (keys patch))
   :effect     :engagement/upsert
   :value      patch
   :stake      nil
   :confidence 0.97})

(defn- agency-disambiguation-flags
  "Builds the proposal's procurement-agency-vs-anti-corruption-agency
  claim from the catalog. When `conflate-procurement-agency?` is true,
  injects the failure mode the governor's
  `procurement-agency-conflation-violations` check must catch: citing
  the anti-corruption agency as if it were the procurement regulator
  (or fusing both into one value) -- the central fabrication trap for
  this jurisdiction."
  [adb conflate-procurement-agency?]
  (when adb
    (if conflate-procurement-agency?
      {:procurement-regulator-authority (:anti-corruption-agency-authority adb)
       :anti-corruption-agency-authority (:anti-corruption-agency-authority adb)}
      {:procurement-regulator-authority (:procurement-regulator-authority adb)
       :anti-corruption-agency-authority (:anti-corruption-agency-authority adb)})))

(defn- assess-jurisdiction
  "Per-jurisdiction market-entry evidence checklist draft. `:no-spec?`
  injects the failure mode we must defend against: proposing a
  checklist for a jurisdiction with NO official spec-basis.
  `:conflate-procurement-agency?` injects the OTHER failure mode this
  vertical must defend against: conflating the actual procurement
  regulator (Agency on Public Procurement of Goods, Works and
  Services) with the DIFFERENT anti-corruption agency (Agency for
  State Financial Control and Combating Corruption)."
  [db {:keys [subject no-spec? conflate-procurement-agency?]}]
  (let [e (store/engagement db subject)
        iso3 (if no-spec? "ATL" (:jurisdiction e))
        sb (facts/spec-basis iso3)
        adb (facts/agency-disambiguation-spec-basis iso3)
        agency-flags (agency-disambiguation-flags adb conflate-procurement-agency?)]
    (if (nil? sb)
      {:summary    (str iso3 " の公式spec-basisが見つかりません")
       :rationale  "marketentry.facts に未登録の法域。要件を推測で作らない。"
       :cites      []
       :effect     :assessment/set
       :value      (merge {:jurisdiction iso3 :checklist [] :spec-basis nil} agency-flags)
       :stake      nil
       :confidence 0.9}
      {:summary    (str iso3 " (" (:owner-authority sb) ") 向け必要書類 "
                        (count (:required-evidence sb)) " 件を提案")
       :rationale  (str "公式ソース: " (:provenance sb) " / 法的根拠: " (:legal-basis sb)
                        (when adb
                          (str " / 公共調達規制機関: " (:procurement-regulator-authority adb)
                               " (汚職対策機関 " (:anti-corruption-agency-authority adb) " とは別主体)")))
       :cites      (cond-> [(:legal-basis sb) (:provenance sb)]
                     adb (conj (:agency-disambiguation-provenance adb)))
       :effect     :assessment/set
       :value      (merge {:jurisdiction iso3
                           :checklist (:required-evidence sb)
                           :spec-basis (:provenance sb)
                           :legal-basis (:legal-basis sb)}
                          agency-flags)
       :stake      nil
       :confidence 0.9})))

(defn- propose-draft
  "Draft the actual FILING-DRAFT action. ALWAYS `:stake
  :actuation/draft-filing`."
  [db {:keys [subject]}]
  (let [e (store/engagement db subject)]
    {:summary    (str subject " 向け提出ドラフト提案"
                      (when e (str " (operator=" (:operator e) ")")))
     :rationale  (if e
                   (str "jurisdiction=" (:jurisdiction e)
                        " portal=" (:portal e))
                   "engagementが見つかりません")
     :cites      (if e [subject] [])
     :effect     :engagement/mark-drafted
     :value      {:engagement-id subject}
     :stake      :actuation/draft-filing
     :confidence (if e 0.9 0.3)}))

(defn- propose-submit
  "Draft the actual FILING-SUBMIT action. ALWAYS `:stake
  :actuation/submit-filing` -- real-world portal submission."
  [db {:keys [subject]}]
  (let [e (store/engagement db subject)]
    {:summary    (str subject " 向けポータル提出提案"
                      (when e (str " (operator=" (:operator e) ")")))
     :rationale  (if e
                   (str "has-business-registration?=" (:has-business-registration? e)
                        " tax-registration-verified?=" (:tax-registration-verified? e)
                        " investment-registration-verified?=" (:investment-registration-verified? e)
                        " claimed-fee=" (:claimed-fee e))
                   "engagementが見つかりません")
     :cites      (if e [subject] [])
     :effect     :engagement/mark-submitted
     :value      {:engagement-id subject}
     :stake      :actuation/submit-filing
     :confidence (if (and e
                          (or (not (:requires-business-registration? e))
                              (:has-business-registration? e))
                          (or (not (:requires-tax-registration? e))
                              (:tax-registration-verified? e))
                          (or (not (:requires-investment-registration? e))
                              (:investment-registration-verified? e)))
                   0.9 0.3)}))

(defprotocol Advisor
  (-advise [this db request] "Return a proposal map for `request`."))

(defrecord MockAdvisor []
  Advisor
  (-advise [_ db {:keys [op] :as request}]
    (case op
      :engagement/intake    (normalize-intake db request)
      :jurisdiction/assess (assess-jurisdiction db request)
      :filing/draft        (propose-draft db request)
      :filing/submit       (propose-submit db request)
      {:summary "unknown op" :rationale "unsupported" :cites []
       :effect :noop :value {} :stake nil :confidence 0.0})))

(defn mock-advisor [] (->MockAdvisor))

(defn trace [request proposal]
  {:t :advisor-proposal
   :op (:op request)
   :subject (:subject request)
   :summary (:summary proposal)
   :confidence (:confidence proposal)
   :stake (:stake proposal)})
