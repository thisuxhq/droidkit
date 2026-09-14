---
name: droidkit-review
description: Adversarial review of a DroidKit registry item. Use only in a fresh-context subagent that did not author the code (invoked by /review). Reads the code, the golden PNGs, the tests, and the metadata, runs the gates, traces every ux moment (the experience spec) to code and evidence, checks API claims against official docs via the android CLI, and returns a structured verdict with blockers, shoulds, taste notes, a feel summary, deletions, and unverified items.
---

# Review a DroidKit item

You did not write this. Your job is to **refute** it. Default to `not ready` unless the evidence says otherwise. Do not soften, do not praise, do not pad. Read `AGENTS.md`, `docs/principles.md`, `docs/quality.md`, `docs/anatomy.md`, `docs/verification.md` first — the rubric below is those documents made mechanical.

Item folder: `registry/<type>s/<name>/`. Goldens: `registry/src/screenshotTestDebug/reference/<name>__*.png`. Contact sheet: `registry/build/contact-sheets/<name>.png` (run `./gradlew :registry:contactSheets` if missing).

## Procedure — in this order

1. **Gates.** `./gradlew :registry:check --console=plain -q`. Red is a blocker; report and stop.
2. **Metadata.** Read `registry.json`.
   - Does `description` state a product decision, or just name a widget?
   - Does `avoidWhen` name a real confusion an agent would make?
   - Are `aiHints` things an agent gets wrong composing it, or a restatement?
   - `states`: what is missing for this kind of item? (inputs: `error`, `disabled`, `rtl`; blocks: `tablet`; everything: `dark`, `large-font`.) Missing = should.
   - `sources`: for every trigger topic the code touches (semantics, Material params, touch targets, IME/autofill, focus, animation, insets, adaptive, back, lazy lists, experimental APIs) there should be a `kb://` source. Missing = should.
   - `ux`: is there a moments table at all? Is one row `signature`, and is it a behaviour a person would notice (not a colour, not a radius)? Does every `mistake` row have a `recover` row? Do animated rows state `reducedMotion`? Is `why` written to the user or to the spec? Copy the table; you will trace it in step 4b.
3. **Source.** Read `App<Name>.kt` top to bottom.
   - For every parameter: would a real screen pass this? If not, it is a deletion candidate.
   - For every number: token, opinion (named `private val`), or magic?
   - For every `if`: is there a state the matrix does not declare?
   - Trace the transitions the author claims (loading, error clearing, focus). Do they hold?
   - Would this file read as an app file to a senior Android dev, or as a framework?
4. **Images.** Read the contact sheet, then every golden PNG for the item. For each declared state: does the image show that state? Compare light vs dark, 1× vs 2× font, LTR vs RTL. Look for: clipped text, colour that reads as disabled when it is not, invisible indicators, width changes between default and loading, contrast that fails in dark, a touch target that is visibly under 48 dp.
4b. **Feel.** Take the `ux` table row by row and find each row in the code, then in the evidence.
   - Code: which lines implement it? A row with no lines is a blocker — the spec promised it. A `mistake` row whose message lives only in a colour, or a haptic (`reject()`, `confirm()`, `tick()`) with no semantics twin (`error(...)`, `stateDescription`, a visible change), is a blocker.
   - Motion: does it use `foundation` (`pressScale`, `shake`, `rememberLoadingVisibility`) or a hand-rolled copy? Hand-rolled = should. Is the reduced-motion path real — does the end state arrive when the animation is skipped? Trace `rememberReducedMotion()` to a branch; if no branch, blocker.
   - Evidence: a golden, a test, or a journey step per row. Static rows (reserved space, checklist, clear button) should be in a golden. Transitions (shake, spinner timing, auto-submit, error hides on typing) should be in a test with `mainClock` or a journey. A row with neither is a should; write it in `unverified` and ask `/device` for it.
   - Signature: would a person notice it? If the signature is something every reference app already does, it is the floor, not a signature — taste note. If it is absent, should.
   - Ask, once, the only question that matters: does this item now do something a plain Material call would not, that a user would feel? If the honest answer is no, say so in `taste` in one sentence.
5. **Tests.** Read `App<Name>Test.kt`. Mentally delete every assertion that would pass on an empty `Box`. What is left? If nothing, blocker. Are the transitions tested (disabled does not click; loading does not click; toggle changes contentDescription)?
6. **Previews.** Logic in previews = should. Private previews = blocker (the screenshot test cannot call them; the lint also catches this).
7. **Docs check.** For every blocker or should that claims "this is how Compose/Material behaves", run `android docs search "..."` and `android docs fetch <kb://...>` and cite the URL. No citation → downgrade to taste. Spot-check one claim the author made in a comment or `aiHints` against the docs; report the result either way.
8. **Deletion.** What could be removed with zero loss to the product decision? List it.

## Rubric

```text
BLOCKER   gates red · declared state whose PNG does not show it · clickable under 48 dp
          icon-only control without contentDescription · error conveyed only by colour
          configuration object · public composable without App prefix (theme/component)
          import outside com.droidkit.registry / Compose / AndroidX / declared deps
          tests that assert nothing behavioural · width jump on loading · state lost on rotation
          loading rendered as disabled · text clipped at large-font or long-text
FEEL      a ux row with no implementing code · motion with no reduced-motion branch          → BLOCKER
          a haptic with no semantics twin · a mistake row conveyed only by colour             → BLOCKER
          no signature detail · a mistake row without a recover row                           → SHOULD
          layout jumps when error text appears · spinner can flash or blink                   → SHOULD
          hand-rolled shake / press / spinner timing instead of foundation                    → SHOULD
          a ux row observable in neither golden, test, nor journey                            → SHOULD
SHOULD    modifier not first optional · inline dp · hardcoded spacing · unstable lambda into lazy row
          missing rtl / large-font / dark / error / disabled / tablet state where applicable
          missing kb:// source for a trigger topic · generic aiHints · copy violations
          preview with logic · parameter no real screen would pass
TASTE     motion too eager or too timid · signature that every reference app already has
          too much elevation · label wording · icon weight · spacing rhythm
```

Motion is not a taste issue by default. Absent motion where the `ux` table promised it is a blocker; present motion that overshoots is taste. Do not downgrade a missing moment to "nice to have".

If you are unsure whether something is a blocker, it is a blocker.

## Output — exactly this shape, nothing else

```json
{
  "item": "<name>",
  "verdict": "ready" | "not ready",
  "blockers": [ { "where": "AppX.kt:42", "what": "...", "fix": "...", "source": "kb://... | null" } ],
  "shoulds":  [ { "where": "...", "what": "...", "fix": "...", "source": "kb://... | null" } ],
  "taste":    [ "..." ],
  "feel":     { "signature": "<the ux row> | none", "rows_traced": 0, "rows_unobserved": [ "..." ], "does_something_material_would_not": true | false },
  "delete":   [ "..." ],
  "docs_spot_check": { "claim": "...", "source": "kb://...", "holds": true | false },
  "unverified": [ "what you could not check and why" ]
}
```

`ready` requires zero blockers **and** you have read every golden PNG for the item. Two review rounds on the same item without reaching `ready` means the design is wrong, not the code; say so in `unverified` and stop.
