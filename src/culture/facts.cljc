(ns culture.facts
  "Country-level regional-culture catalog for Tajikistan (TJK) -- national
  dishes, protected products, beverages, crafts, festivals and heritage
  sites, per ADR-2607171400 addendum 2 (cloud-itonami-municipality-
  culture-catalog Wave 1, in com-junkawasaki/root). Sibling namespace to
  `marketentry.facts` / `statute.facts` (ADR-2607141700); city-level
  counterparts live in the cloud-itonami-municipality-* repos.

  Catalog is keyed by UPPERCASE ISO3 (mirrors `statute.facts`); entries
  carry no :culture/municipality (that attribute is city-level only).

  Every entry cites a source URL that was actually fetched and read on
  :culture/retrieved-at -- never fabricated. Summaries state only what the
  cited source confirms. An item not in this table has NO spec-basis, full
  stop; extend `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of culture entries."
  {"TJK"
   [{:culture/id "tjk.dish.qurutob"
     :culture/name "Qurutob"
     :culture/country "TJK"
     :culture/kind :dish
     :culture/summary "Dish of Tajik cuisine made with dried cheese balls (qurut) softened and layered over flatbread topped with vegetables; ranks among the national dishes of Tajikistan."
     :culture/url "https://en.wikipedia.org/wiki/Qurutob"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "tjk.dish.sambusa"
     :culture/name "Sambusa"
     :culture/country "TJK"
     :culture/kind :dish
     :culture/summary "In Tajik cuisine, sambusa-i varaki are triangular turnovers filled with minced beef or mutton mixed with tail fat, flavoured with onions and spices, and baked in a tandoor oven; the wider sambusa/samosa family is shared with other Central and South Asian cuisines."
     :culture/url "https://en.wikipedia.org/wiki/Sambusa"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "tjk.dish.laghman"
     :culture/name "Laghman"
     :culture/country "TJK"
     :culture/kind :dish
     :culture/summary "Hand-pulled noodle dish common in Tajikistan as well as Russia, Uzbekistan, Turkmenistan, northeastern Afghanistan and parts of northern Pakistan."
     :culture/url "https://en.wikipedia.org/wiki/Laghman_(food)"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "tjk.dish.plov"
     :culture/name "Plov"
     :culture/name-local "Палов"
     :culture/country "TJK"
     :culture/kind :dish
     :culture/summary "Rice pilaf dish; Tajikistan's regional variation, oshi palav (Tajik plov), is considered a staple dish alongside neighbouring Kazakhstan and Azerbaijan, each with local ingredients and preparation."
     :culture/url "https://en.wikipedia.org/wiki/Plov"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "tjk.craft.chakan-embroidery"
     :culture/name "Chakan embroidery"
     :culture/country "TJK"
     :culture/kind :craft
     :culture/summary "Traditional Tajik practice of sewing symbolic images onto cotton or silk with brightly coloured thread, created by Tajik women for occasions such as marriages; inscribed on UNESCO's Representative List of the Intangible Cultural Heritage of Humanity in 2018."
     :culture/url "https://en.wikipedia.org/wiki/Culture_of_Tajikistan"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "tjk.product.tubeteika"
     :culture/name "Tubeteika"
     :culture/country "TJK"
     :culture/kind :product
     :culture/summary "Traditional Turkic skullcap worn today in Tajikistan, Kazakhstan, Kyrgyzstan and Uzbekistan; Tajik regional styles vary, square and mostly black-and-white in the north (Sughd) and round and brightly coloured in the south (Khatlon)."
     :culture/url "https://en.wikipedia.org/wiki/Tubeteika"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "tjk.festival.nowruz"
     :culture/name "Nowruz"
     :culture/country "TJK"
     :culture/kind :festival
     :culture/summary "Iranian-calendar New Year festival; Tajikistan observes it as a four-day public holiday, one of five Central Asian countries (with Kyrgyzstan, Uzbekistan, Turkmenistan and Kazakhstan) that celebrate it as such."
     :culture/url "https://en.wikipedia.org/wiki/Nowruz"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "tjk.heritage.sarazm"
     :culture/name "Sarazm"
     :culture/country "TJK"
     :culture/kind :heritage
     :culture/summary "Proto-urban archaeological site in Tajikistan's Sughd Region, inscribed as a UNESCO World Heritage Site in 2010 for its testimony to the development of human settlements in Central Asia from the 4th to 3rd millennium BCE."
     :culture/url "https://en.wikipedia.org/wiki/Sarazm"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}]})

(defn spec-basis [iso3] (get catalog iso3))

(defn coverage
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-tjk culture catalog "
                 "(ADR-2607171400 addendum 2, Wave 1): " (count (get catalog "TJK"))
                 " TJK entries, each with a fetched-and-read citation. "
                 "Extend `culture.facts/catalog`, never fabricate an id/url.")})))

(defn by-kind [iso3 kind]
  (filterv #(= (:culture/kind %) kind) (spec-basis iso3)))
