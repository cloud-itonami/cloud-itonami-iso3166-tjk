(ns marketentry.facts-test
  (:require [clojure.test :refer [deftest is testing]]
            [marketentry.facts :as facts]))

(deftest tjk-has-spec-basis
  (let [sb (facts/spec-basis "TJK")]
    (is (some? sb))
    (is (string? (:provenance sb)))
    (is (seq (:required-evidence sb)))
    (is (= 4 (count (:required-evidence sb))) "recommended check count is 4 evidence items, not padded")
    (is (some? (facts/rep-spec-basis "TJK")))
    (is (some? (facts/corporate-number-spec-basis "TJK")))
    (is (some? (facts/agency-disambiguation-spec-basis "TJK")))
    (is (some? (facts/investment-registration-spec-basis "TJK")))))

(deftest unknown-jurisdiction-has-no-spec-basis
  (is (nil? (facts/spec-basis "ATL")))
  (is (nil? (facts/spec-basis "ZZZ"))))

(deftest required-evidence-satisfied
  (let [sb (facts/spec-basis "TJK")
        all (:required-evidence sb)]
    (is (true? (facts/required-evidence-satisfied? "TJK" all)))
    (is (not (facts/required-evidence-satisfied? "TJK" (take 1 all))))
    (is (nil? (facts/required-evidence-satisfied? "ATL" all)))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["TJK" "USA" "ATL"])]
    (is (= 3 (:requested c)))
    (is (= 2 (:covered c)))
    (is (= ["ATL"] (:missing-jurisdictions c)))))

(deftest procurement-law-cited-by-title-and-year-only
  (testing "the 2006 Public Procurement Law is cited by title/year only -- no fabricated law number (e.g. NOT 'No. 190')"
    (let [sb (facts/spec-basis "TJK")]
      (is (re-find #"(?i)on public procurement of goods, works and services" (:legal-basis sb)))
      (is (re-find #"2006" (:legal-basis sb)))
      (is (not (re-find #"(?i)no\.?\s*190" (:legal-basis sb)))
          "must NOT cite the weakly-sourced/unconfirmed 'No. 190' law number"))))

;; ---- the central fabrication trap for this jurisdiction ----

(deftest procurement-agency-and-anti-corruption-agency-are-distinct-authorities
  (testing "agency-disambiguation spec-basis keeps the procurement regulator and the anti-corruption agency SEPARATE"
    (let [adb (facts/agency-disambiguation-spec-basis "TJK")]
      (is (some? adb))
      (is (some? (:procurement-regulator-authority adb)))
      (is (some? (:anti-corruption-agency-authority adb)))
      (is (not= (:procurement-regulator-authority adb) (:anti-corruption-agency-authority adb))
          "the procurement regulator and the anti-corruption agency must never be the same value")
      (is (re-find #"(?i)public procurement" (:procurement-regulator-authority adb)))
      (is (re-find #"(?i)anti-corruption|combating corruption" (:anti-corruption-agency-authority adb)))
      (is (not (re-find #"(?i)combating corruption" (:procurement-regulator-authority adb)))
          "the procurement-regulator value itself must not also name the anti-corruption agency (no fusion)")
      (is (not (re-find #"(?i)public procurement" (:anti-corruption-agency-authority adb)))
          "the anti-corruption-agency value itself must not also name the procurement regulator (no fusion)"))))

(deftest owner-authority-is-procurement-agency-not-anti-corruption-agency
  (testing "top-level :owner-authority (the general procurement-law authority) is the Agency on Public Procurement, distinct from the anti-corruption agency"
    (let [sb (facts/spec-basis "TJK")
          adb (facts/agency-disambiguation-spec-basis "TJK")]
      (is (re-find #"(?i)public procurement" (:owner-authority sb)))
      (is (not (re-find #"(?i)combating corruption" (:owner-authority sb))))
      (is (= (:owner-authority sb) (:procurement-regulator-authority adb))))))

(deftest no-agency-disambiguation-spec-basis-for-jurisdictions-without-one
  (is (nil? (facts/agency-disambiguation-spec-basis "USA")))
  (is (nil? (facts/agency-disambiguation-spec-basis "ATL"))))

(deftest investment-registration-grounded-in-law-2173
  (testing "foreign-investment registration cites Law No. 2173 (2025), not the superseded 2016 law as current"
    (let [irb (facts/investment-registration-spec-basis "TJK")]
      (is (some? irb))
      (is (re-find #"2173" (:investment-registration-legal-basis irb)))
      (is (re-find #"2025" (:investment-registration-legal-basis irb)))
      (is (re-find #"(?i)state committee on investments" (:investment-registration-owner-authority irb))))))
