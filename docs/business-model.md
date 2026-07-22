# Business Model: Independent Public-Sector Market-Entry & Procurement Compliance Service — Tajikistan

## Classification

- Repository: `cloud-itonami-iso3166-tjk`
- ISO 3166: `TJK` (Tajikistan)
- Activity: public-procurement market-entry and ongoing regulatory-
  compliance navigation for an already-incorporated operator

## Customer

- an already-incorporated `cloud-itonami-cofog-{code}` /
  `cloud-itonami-isco-{code}` / `cloud-itonami-unspsc-{segment}` /
  `cloud-itonami-{ISIC}` operator wanting to bid on a Tajikistan public
  contract
- a foreign SME or civic-tech vendor entering the public sector in
  Tajikistan for the first time
- a `cloud-itonami-M6910` client that has just completed incorporation and
  now needs public-sector market access

## Offer

- business-registration walkthrough for the Tax Committee's Unified
  state register (andoz.tj), a "single window" registration process
- public-procurement navigation for the Agency on Public Procurement of
  Goods, Works and Services under the Government of the Republic of
  Tajikistan -- the regulator administering Law "On Public Procurement
  of Goods, Works and Services" (2006) -- **kept structurally distinct
  from the unrelated Agency for State Financial Control and Combating
  Corruption**, a wholly separate anti-corruption oversight body this
  service NEVER cites as the procurement regulator
  (`marketentry.governor`'s flagship `procurement-agency-conflated`
  check)
- tax-registration checklist: Tax Code of the Republic of Tajikistan
  (registered 17 September 2012), administered by the Tax Committee
- FOREIGN-INVESTMENT-SCREENING navigation: for engagements involving
  government interests (including Free Economic Zones), guiding the
  client through the State Committee on Investments and State Property
  Management's screening process under Law No. 2173 "On Capital and
  Promotion of Investment Activity" (adopted 14 May 2025, replacing the
  2016 investment law, 15-year investor stability guarantee) and
  Government Decree No. 590 (28 December 2006) BEFORE any filing
  submission
- ongoing regulatory-change monitoring subscription
- compliance-audit export package for the client's own records

## Revenue

- per-engagement market-entry fee (one-time registration + checklist
  completion)
- recurring regulatory-change monitoring subscription
- compliance-audit export package

## Trust Controls

- any actual portal registration or filing submission requires
  Market-Entry Compliance Governor clearance and always escalates to
  human sign-off (`:filing/submit` is never automated at any phase)
- a `:jurisdiction/assess` proposal that conflates the Agency on Public
  Procurement (the actual regulator) with the Agency for State
  Financial Control and Combating Corruption (a wholly separate anti-
  corruption body) is a HARD hold that cannot be overridden by human
  approval alone -- the governor's flagship check for this vertical
- a false or fabricated regulatory-requirement claim is a HARD hold that
  cannot be overridden by human approval alone — it must be corrected
  against a cited official source first (e.g. no fabricated "No. 190"
  law number for the 2006 procurement law, no single unverified
  e-procurement portal URL asserted as canonical)
- this service does **not** provide legal or tax advice; characterization
  and filing on the client's behalf beyond checklist/draft assistance
  routes to Tajikistan-licensed counsel or a registered agent
- every requirement cites the official portal or regulation, never
  invented

## Boundary with adjacent actors (read before forking)

- **`com-etzhayyim-ooyake`** (etzhayyim/root): read-only civic-wayfinding
  mirror of government structure, non-commercial, barred from acting as
  or for the government (G3 impersonation ban). This blueprint is
  commercial and never claims to be an official channel.
- **`matsurigoto`** (etzhayyim/root): sovereign e-government statecraft —
  literally the government, for etzhayyim's own covenant or an adopting
  nation-state. This blueprint is an independent operator the government
  contracts with or that bids into its procurement — never the
  government.
- **`com-etzhayyim-toritsugi`** (etzhayyim/root): guides a consenting
  INDIVIDUAL citizen through their OWN procedure, non-profit,
  donation-only. This blueprint's client is a business operator, not an
  individual citizen, and it is commercial.
- **`legal-entity.etzhayyim.com`**: read-only aggregated company-registry
  data, no execution. This blueprint executes (gated) registrations.
- **`cloud-itonami-M6910`**: helps a client BECOME a legal entity
  (incorporation, ISIC 6910) — a prior, different regulatory phase
  (company law). This blueprint assumes incorporation is already done and
  handles public-procurement market entry (a different regulatory domain).
- **`cloud-itonami-cofog-{code}`**: a jurisdiction-agnostic operator
  template for ONE public function. This blueprint is the orthogonal
  jurisdiction-specific axis — the two compose (fork a COFOG-function
  blueprint AND this one to operate in Tajikistan).
