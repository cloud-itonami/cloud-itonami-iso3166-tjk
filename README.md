# cloud-itonami-iso3166-tjk

Open ISO 3166 Blueprint for **TJK**: Tajikistan.

**`:implemented`.** Flagship governor check: `procurement-agency-conflated`
-- the Agency on Public Procurement of Goods, Works and Services vs. the
Agency for State Financial Control and Combating Corruption (see below).

```
clojure -M:dev:test    # governor contract + facts + phase + registry + store + culture
clojure -M:dev:run     # walk a demo engagement through the full actor graph
```

## Official surface

- Business registration: the Tax Committee under the Government of the
  Republic of Tajikistan ([andoz.tj](https://andoz.tj/)) operates the
  Unified state register via a "single window" registration process.
  Confirmed across multiple independent sources.
- Public procurement law: Law of the Republic of Tajikistan "On Public
  Procurement of Goods, Works and Services," 2006 -- cited by title and
  year ONLY. A commonly-repeated "No. 190" law number is only weakly
  sourced and UNCONFIRMED; this catalog does not carry it.
- **The central fabrication trap for this jurisdiction**: the
  procurement regulator is the **Agency on Public Procurement of Goods,
  Works and Services under the Government of the Republic of
  Tajikistan** -- a COMPLETELY DIFFERENT body from the **Agency for
  State Financial Control and Combating Corruption** (an anti-corruption
  oversight body). Both use a similarly-named "Agency ... under the
  Government" structure but have different scopes. WebFetch-verified
  live this session: [eprocurement.gov.tj](https://eprocurement.gov.tj/)'s
  own footer names the agency verbatim (Russian): "Агентство по
  государственным закупкам товаров, работ и услуг при Правительстве
  Республики Таджикистан."
- E-procurement: multiple candidate portal domains exist (goszakupki.tj,
  eprocurement.gov.tj, zakupki.gov.tj, tenders.tj) but the single
  canonical CURRENT one is UNRESOLVED -- this catalog models procurement
  STRUCTURALLY (an electronic system under the Agency on Public
  Procurement) rather than hard-coding one candidate domain as
  canonical.
- Tax registration: Tax Code of the Republic of Tajikistan (registered
  17 September 2012), administered by the Tax Committee (andoz.tj).
- Foreign investment: the State Committee on Investments and State
  Property Management of the Republic of Tajikistan screens inbound
  foreign investments involving government interests (including Free
  Economic Zones), per Government Decree No. 590 (28 December 2006).
  Current investment law: **Law No. 2173 "On Capital and Promotion of
  Investment Activity,"** adopted **14 May 2025**, replacing the 2016
  investment law, with a 15-year investor stability guarantee. This
  session independently WebFetch-verified
  [mmk.tj](https://mmk.tj/) as the National Center for Legislative
  Affairs under the President of the Republic of Tajikistan (the
  official legislative-repository site) -- used as the citation domain
  for Law No. 2173 / Decree No. 590 since the State Committee's own
  agency subdomain was not reachable this session (network error); no
  ministry-specific URL is asserted.

See `src/marketentry/facts.cljc` for the full citation trail, including
the explicitly disclaimed fabrication traps (no "No. 190" law number, no
single canonical e-procurement portal URL, no Ministry-of-Justice-vs-Tax-
Committee split).

**⚠️ Cross-contamination note**: Tajikistan and Turkmenistan are
visually/phonetically similar "-stan" Central Asian republics with
structurally analogous institutions (both have a "Tax Committee", a
"State Committee on Investments", Soviet-derived civil law). Every entry
in this catalog is Tajikistan-SPECIFIC, verified separately from
Turkmenistan's (`cloud-itonami-iso3166-tkm`).

This repository designs a forkable OSS business for an independent
public-sector market-entry consultant: an already-incorporated operator
(e.g. a `cloud-itonami-cofog-{code}`, `cloud-itonami-isco-{code}`,
`cloud-itonami-unspsc-{segment}` or `cloud-itonami-{ISIC}` blueprint
fork) gets a Compliance Advisor + independent **Market-Entry Compliance
Governor** to navigate public-procurement registration, business/tax
registration, and foreign-investment screening in Tajikistan, so the
operator can win and service a government contract without hiring a
full in-house compliance department.

## No robotics premise — digital/data service exemption

Market-entry and procurement-compliance navigation is a pure data/software
service with no physical-domain work (portal registration, document
checklists, regulatory-change monitoring) — the same exemption class as
`cloud-itonami-6310` (HR SaaS replacement) and `cloud-itonami-gtin-*`.
`blueprint.edn` sets `:itonami.blueprint/robotics false` and
`:required-technologies` lists only real capabilities (`:identity`,
`:forms`, `:dmn`, `:bpmn`, `:audit-ledger`), no `:robotics`.

## Core Contract

```text
operator intake + prior filing history
        |
        v
Compliance Advisor -> Market-Entry Compliance Governor -> filing draft, or human sign-off
        |
        v
gated portal registration / filing submission + audit ledger
```

No automated proposal can submit a portal registration or filing the
governor refuses, suppress a compliance record, or claim a legal/tax
conclusion the governor has not cleared. `:filing/submit` is never in any
phase's `:auto` set — it always requires human sign-off (mirrors
`cloud-itonami-M6910`'s `filing-submit-never-auto-at-any-phase`
invariant).

## What this is NOT

- **Not the government of Tajikistan.** See
  [`docs/business-model.md`](docs/business-model.md) for the boundary with
  `com-etzhayyim-ooyake` (read-only civic mirror), `matsurigoto` (sovereign
  statecraft), `com-etzhayyim-toritsugi` (individual citizen concierge),
  `legal-entity.etzhayyim.com` (read-only data aggregation), and
  `cloud-itonami-M6910` (company incorporation — a different regulatory
  phase this blueprint assumes is already complete).
- **Not legal or tax advice.** Every regulatory claim must cite the
  official source and route final filings to Tajikistan-licensed counsel
  or a registered agent where the law requires licensed representation.

## Capability layer

Resolves via [`kotoba-lang/iso3166`](https://github.com/kotoba-lang/iso3166)
(ISO 3166 `TJK`). Required capabilities:

- :identity
- :forms
- :dmn
- :bpmn
- :audit-ledger

See [`docs/business-model.md`](docs/business-model.md) and
[`docs/operator-guide.md`](docs/operator-guide.md).

## Implementation status

**`:implemented`.** `src/marketentry/*` is a running langgraph-clj
StateGraph actor (`operation/build`): a MarketEntry-LLM advisor
(`marketentryllm.cljc`) sealed into a single `:advise` node, whose
proposal is ALWAYS routed through the Market-Entry Compliance
Governor (`governor.cljc`) and the rollout phase gate (`phase.cljc`)
before anything touches the SSoT (`store.cljc`, MemStore +
DatomicStore via `io.github.kotoba-lang/langchain-store`).

### Governor checks (priority order, all HARD -- unoverridable by a human approver)

| # | Check | Grounded in |
|---|-------|-------------|
| 1 | Spec-basis (no fabricated jurisdiction) | `marketentry.facts/spec-basis` |
| 2 | Evidence incomplete | the jurisdiction's 4-item `:required-evidence` checklist (includes the 2006 Public Procurement Law compliance item) |
| 3 | `:business-registration-missing` | Tax Committee Unified state register (single window) — [andoz.tj](https://andoz.tj/) |
| 4 | `:procurement-agency-conflated` (**flagship**) | Agency on Public Procurement vs. anti-corruption agency — see below |
| 5 | Engagement-fee mismatch | independent recompute (`base-fee + monthly-rate x monitoring-months`) |
| 6 | `:tax-registration-unverified` | Tax Code of the Republic of Tajikistan (registered 17 September 2012), Tax Committee |
| 7 | `:investment-registration-missing` | State Committee on Investments and State Property Management — Law No. 2173 (2025) |
| 8 | Confidence floor / actuation gate | `:filing/draft`/`:filing/submit` always escalate |
| — | Double-draft / double-submit guards | dedicated `:drafted?`/`:submitted?` facts |

### The flagship check: Agency on Public Procurement is not the anti-corruption agency

The central fabrication trap for this jurisdiction is confusing two
entirely different agencies that happen to share a "Agency ... under
the Government" naming structure. This actor keeps them apart on
purpose, in the catalog
(`marketentry.facts/agency-disambiguation-spec-basis`) and in a
dedicated governor check
(`marketentry.governor/procurement-agency-conflation-violations`):

- **Agency on Public Procurement of Goods, Works and Services under the
  Government of the Republic of Tajikistan** is the **procurement
  regulator**. It administers Law "On Public Procurement of Goods,
  Works and Services" (2006) and runs the electronic procurement
  system referenced by [eprocurement.gov.tj](https://eprocurement.gov.tj/)
  (WebFetch-verified this session: the site's own footer names the
  agency verbatim).
- **Agency for State Financial Control and Combating Corruption** is a
  **wholly separate anti-corruption oversight body**. It is NOT the
  procurement regulator and has a different scope entirely (anti-
  corruption oversight, not procurement administration).

A `:jurisdiction/assess` proposal that states or implies the procurement
regulator IS the anti-corruption agency -- fuses the two into one
value, cites the anti-corruption agency as though it were the
procurement regulator, omits one, or cites either against the wrong
catalogued value -- is a HARD violation the governor rejects
unconditionally
(`test/marketentry/governor_contract_test.clj`'s
`conflated-procurement-agency-claim-is-held-and-unoverridable` and
`clean-assess-correctly-distinguishes-procurement-agency-from-anti-corruption-agency`).

### Sources cited per check

- Business registration: Tax Committee under the Government of the
  Republic of Tajikistan, Unified state register, single-window
  registration process — [andoz.tj](https://andoz.tj/)
- Procurement law: Law "On Public Procurement of Goods, Works and
  Services" (2006), title/year only, no fabricated law number;
  regulator — Agency on Public Procurement of Goods, Works and
  Services — [eprocurement.gov.tj](https://eprocurement.gov.tj/)
- Procurement-agency disambiguation: Agency on Public Procurement of
  Goods, Works and Services (the regulator) vs. Agency for State
  Financial Control and Combating Corruption (a wholly separate
  anti-corruption body) — confirmed via egov.tj / eprocurement.gov.tj
  domains
- Tax registration: Tax Code of the Republic of Tajikistan, registered
  17 September 2012, administered by the Tax Committee —
  [andoz.tj](https://andoz.tj/)
- Foreign investment: State Committee on Investments and State Property
  Management, Law No. 2173 "On Capital and Promotion of Investment
  Activity" (adopted 14 May 2025, replacing the 2016 investment law, 15-
  year investor stability guarantee), Government Decree No. 590
  (28 December 2006) — citation domain [mmk.tj](https://mmk.tj/)
  (National Center for Legislative Affairs; no ministry-specific URL
  independently confirmed this session)

### Actuation

- `:engagement/intake` may auto-commit at phase 3 when the governor is
  clean (no portal-facing risk).
- `:jurisdiction/assess` always escalates to human approval, at every
  phase, even when clean.
- `:filing/draft` and `:filing/submit` are **permanently excluded**
  from every phase's `:auto` set (`phase.cljc`) AND are members of the
  governor's own `high-stakes` set (`governor.cljc`) that forces
  escalation independently of phase. Two layers, not one, agree that
  drafting a real portal package or submitting a real portal
  registration is always a human market-entry operator's call.
- Every HARD violation is unoverridable: a human approver sees the
  `:hold` disposition and its `:violations`, but cannot commit past a
  HARD check. Only the confidence/actuation escalation is a genuine
  human decision point (`:approved`/rejected via `:request-approval`).
- Every commit or hold appends exactly one fact to the append-only
  ledger (`store/append-ledger!`, called from both the `:commit` and
  `:hold` StateGraph nodes) — nothing is ever rewritten or removed.

## License

AGPL-3.0-or-later.

## Culture catalog

Alongside the market-entry / statute catalogs, this repo carries a
**country-level regional-culture catalog** (ADR-2607171400 addendum 2,
`cloud-itonami-municipality-culture-catalog` Wave 1, in
`com-junkawasaki/root`) — national dishes, protected products, beverages,
crafts, festivals and heritage sites for Tajikistan:

- `src/culture/facts.cljc` — the catalog, source of truth (keyed by
  uppercase ISO3, mirroring `statute.facts`).
- `schema/culture.edn` — DataScript schema.
- `data/culture-tx.edn` — derived DataScript tx-data (regenerated from
  the catalog, never hand-edited).

City-level counterparts live in the `cloud-itonami-municipality-*` repos.
Same provenance discipline as the compliance catalogs: every entry cites a
source URL that was actually fetched and read on `:culture/retrieved-at`;
summaries state only what the cited source confirms. An item not in
`culture.facts/catalog` has no spec-basis — never fabricate one.
