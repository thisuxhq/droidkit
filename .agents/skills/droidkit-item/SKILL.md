---
name: droidkit-item
description: Design and build a DroidKit registry item (component, pattern, block) or a core theme/foundation change. Use when creating, revising, or extending anything under registry/ or core/. Covers the experience spec (walk the moments, real-app references, the signature detail), the product decision, the state matrix, grounding in official Android docs via the android CLI, API shape, Compose/Android gotchas, the foundation vocabulary (haptics, press, shake, spinner timing, reduced motion), folder anatomy, registry.json, and the gates that must be green before hand-off.
---

# Build a DroidKit item

Read first, follow, do not paraphrase back: `AGENTS.md`, `docs/principles.md`, `docs/anatomy.md`, `docs/quality.md`. Settled choices are in `docs/decisions.md`; the loop you are inside is `docs/verification.md`.

You are the **author**. You never review your own item. When the gates are green and you have looked at every PNG, hand off to `/review <name>`.

## Step 0 — Walk the moments

Correct is the floor. 48 dp, `ContentType`, an IME chain — a reviewer can check those and the user never notices them. This step is where *designed* comes from, and it happens before the state matrix and before any code. Skipping it produces an item that passes every gate and feels like nothing.

### 0a. Look at real apps first

Search Mobbin for the job this item does, not the widget name. Three to five screens or flows; read the images, not the metadata.

```text
search_screens("create password screen with live strength meter and requirement checklist")
search_flows("sign in with wrong password and recover")
```

Write down, in one line each, what the good ones do that Material's primitive does not. Keep the URLs; they go in the hand-off. If every reference does the same thing, that thing is the floor, not the signature.

### 0b. The moments table → `registry.json` → `ux`

Walk one person through the item. For each moment they actually hit, one row: what they notice, what we do, why it matters *to them*.

```text
moment    the user is…                 decide
see       glancing at it               what reads at a glance · reserved space so nothing jumps later
reach     focusing / pressing          press scale · haptic · caret · where focus lands
act       typing / tapping / dragging  live feedback · clear button · auto-advance · paste
mistake   doing it wrong               shake · error text on the field · reject haptic · never colour-only
recover   fixing it                    error hides on first keystroke · select-all on refocus · focus back
succeed   done                         check morph · confirm haptic · auto-submit · what announces "done"
leave     moving on                    IME action · what persists · what resets
```

Rules:

- **One `signature: true`.** The detail a person would remember and mention. If you cannot name it, you have a wrapper, not an item. Stop and rethink the product decision.
- **Every row is observable.** A golden shows it, a behaviour test asserts it, or a `journey.xml` step drives it. A row nothing can observe gets deleted, not kept as intent.
- **Motion states `reducedMotion`.** Anything that shakes, scales, slides, crossfades, pulses, or pops says what happens at animator scale 0. `lintRegistryUx` rejects the row otherwise. The end state must be reached without the animation.
- **Haptics have a semantics twin.** `reject()` pairs with `error(...)`; `confirm()` with a `stateDescription`; `tick()` with a visible change. The moment must exist for someone with haptics off.
- **Build from `foundation`.** `pressScale`, `shake`, `rememberLoadingVisibility`, `rememberReducedMotion`, `rememberAppHaptics` are the vocabulary. Do not re-implement them in the item; if one is missing and a second item will need it, add it to `registry/foundation/` first.
- `why` is user-facing. "So the pill feels pressed" is a why. "Per Material spec" is not.

Example, password-field on sign-up:

```json
{ "moment": "act", "behaviour": "Rules under the field tick live as the user types, each with a tick haptic", "why": "Nobody learns the rules from an error after submit", "signature": true, "reducedMotion": "Checks appear without the morph" }
{ "moment": "mistake", "behaviour": "On the error edge the field shakes once, fires reject, and the supporting line shows the message", "why": "The refusal is felt before it is read", "reducedMotion": "No shake; haptic and text only" }
{ "moment": "recover", "behaviour": "Error text hides on the first keystroke and space stays reserved", "why": "The person is already fixing it; stop shouting and do not jump the form" }
```

### 0c. The product decision, one sentence

Now the sentence: what does this item decide that Material's primitive leaves open? It should be the summary of the table, not a separate thought.

```text
✓ password-field: rules tick live on sign-up, refusal is felt (shake + reject), error hides the moment you start fixing it
✓ empty-state: one title, one line, at most one action; never used for loading or errors
✗ card: Material Card with 16 dp radius        ← not an item. Stop.
```

If the sentence is about corners, colours, or "a nicer default", it is not an item.

## Step 1 — State matrix, before code

Write it into `registry.json` → `states`. Walk every axis; cross out what does not apply and say why in one line.

```text
state         default · focused · pressed · disabled · loading · error · success · readonly · empty
content       short · long-text · rtl · large-font (2×) · with-icon / no-icon
environment   light · dark · phone · tablet (widthDp 840) · ime-open · reduced-motion
transitions   → loading → success | error
              error → typing        (clears when? on keystroke or on blur — decide)
              focus after error     (where does focus land?)
              rotation              (what must survive? rememberSaveable)
              double-tap during transition
              ImeAction.Next chain  (who receives focus?)
```

Every item declares at least `default`, `dark`, `large-font`. Inputs add `error`, `disabled`, `rtl`. Blocks add `tablet`. Keep it 5–9 states; do not invent states the API cannot produce.

For each transition, one line, in the file's KDoc or a comment above the composable:

```kotlin
// error → typing: isError clears on the first keystroke; supportingText stays until blur.
```

## Step 2 — Ground in official docs (required)

Training data is stale for Compose. The `android` CLI has a local, offline index of developer.android.com:

```bash
android docs search "<topic>"        # pick the kb://android/develop/ui/compose/... hit
android docs fetch <kb-url>          # read it, then act
```

If it prints "Waiting for index" on first use, wait; it is a one-off download.

Search **whenever the item touches one of these**:

```text
semantics        contentDescription · stateDescription · liveRegion · mergeDescendants · role · heading
material         the exact Material 3 component you wrap — read its parameter list, do not guess defaults
touch targets    minimumInteractiveComponentSize · when Material pads to 48 dp and when it does not
text input       KeyboardOptions · ImeAction · ContentType / autofill · VisualTransformation · TextFieldState
focus            FocusRequester · focus order · keyboard navigation · onKeyEvent
animation        AnimatedContent · SizeTransform · animateContentSize · MotionScheme · reduced motion
insets           WindowInsets · imePadding · edge-to-edge
adaptive         WindowSizeClass · ListDetailPaneScaffold · Material 3 Adaptive
back             predictive back · BackHandler · NavigationEventHandler
lazy lists       key · contentType · stability · recomposition
experimental     anything @Experimental*Api — confirm it is still experimental in the current BOM
```

Rules:

- Docs beat memory. If the doc contradicts what you know, the doc wins.
- If the docs are silent, say so under `unverified` in your hand-off. Do not fill the gap with a guess.
- Record what you read in `registry.json` → `sources` as `kb://` URLs. Review checks trigger topics against this list.

Google ships agent skills in the same index; use them instead of improvising: `android skills add adaptive | edge-to-edge | testing-setup | navigation-event | android-profiler`.

## Step 3 — API shape

- Two entry points on the same name: a friendly overload (`text: String`) and a slot overload (`content: @Composable () -> Unit`). Never a configuration object; never an enum for every option.
- Parameter order: required data → callbacks → `modifier: Modifier = Modifier` (first optional) → behaviour → appearance → slots.
- `modifier` is applied to the root exactly once.
- Colour, type, shape from `MaterialTheme.*`; spacing, motion, elevation from `AppTheme.*`.
- Hardcoded dp only where the number *is* the opinion. Named `private val` at the top of the file. The conventions lint rejects inline `.dp` literals in source files.
- State that must survive rotation → `rememberSaveable`. Everything else the caller owns.
- Blocks: stateless. State + callbacks in, own `data class`, no ViewModel/Nav/DI.
- Experimental Material opt-ins go on the file **and** in `experimentalApis`.
- Prefix `App` on theme and components; patterns and blocks use plain names (`EmptyState`, `SettingsScreen`). Decisions #2.

## Step 4 — Gotchas (tick every one; these are the usual misses)

```text
[ ] text ↔ spinner swap: SizeTransform or a min width — the button must not change width
[ ] loading: not clickable, still focusable, stateDescription + liveRegion announce it
[ ] loading is not "disabled": keep the primary container colour; a greyed loading button reads as broken
[ ] icon-only control: contentDescription that changes with state ("Show password" / "Hide password")
[ ] compound rows: mergeDescendants so TalkBack reads one item, not six
[ ] 48 dp: Material pads only when the control is interactive; a clickable Icon is not an IconButton
[ ] KeyboardType + ImeAction + ContentType set; onDone / onNext actually wired
[ ] focus requests inside LaunchedEffect, never during composition
[ ] error: text attached to the field, not only a colour; announced
[ ] RTL: padding(start=), mirrored chevrons and progress; rtl preview exists
[ ] long text: wraps or ellipsises by decision; fixed height() clips — use heightIn(min=)
[ ] large font 2×: nothing clips, nothing overlaps
[ ] infinite transitions stop when the item is not visible (skeletons)
[ ] lazy lists: keys; no unstable lambdas into rows; no spring per row
[ ] motion respects animator scale 0 / reduced motion
[ ] insets in blocks: imePadding, navigationBarsPadding, edge-to-edge
[ ] copy: sentence case, verb-first actions, no "Oops", no "!" — the lint rejects both
[ ] press: pressScale + click() on anything that is a primary tap target; same interactionSource as the ripple
[ ] spinner: rememberLoadingVisibility for what shows; the raw flag for what is ignored — never flash, never blink
[ ] refusal: shake fires on the error edge, once; reject() beside it; the message is on the field
[ ] error text: the supporting line is reserved (or animateContentSize) so the form does not jump
[ ] reduced motion: every animated ux row has its reducedMotion path and still reaches the end state
```

## Step 5 — Folder

```text
registry/<type>s/<name>/
├── App<Name>.kt              source   package com.droidkit.registry.<type>s
├── App<Name>Preview.kt       preview  one internal fun per state, @Preview(name = "<name> <state>")
├── App<Name>Test.kt          test     behaviour through semantics, not existence
├── registry.json             type · status · ux · states · sources · avoidWhen · aiHints
└── journey.xml               optional; on-device script for /device (see droidkit-device)
```

Items that use `foundation` (haptics, motion) declare it in `registryDependencies`.

Preview file rules: a tiny `private fun XPreviewSurface(darkTheme, content)` helper, then one `internal fun` per state. `uiMode`, `fontScale`, `widthDp` go on the `@Preview` annotation — the screenshot generator copies it verbatim. RTL is `CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl)` inside the preview. No logic in previews. Copy `registry/components/button/AppButtonPreview.kt`.

Test rules: an assertion that would pass on an empty `Box` is not a test. Assert behaviour: disabled does not fire `onClick`; loading does not fire `onClick`; the show/hide toggle changes `contentDescription`; error text is present in the semantics tree. Use `mainClock` for transitions.

Metadata rules: `description` says when to use it; `avoidWhen` names the real confusion (`otp-input` vs `password-field`); `aiHints` are the things an agent will get wrong composing it, not a restatement of the description. Register the item in `registry/registry.json` and add a showcase screen in `apps/showcase` (every item is used there — Decisions #14).

## Step 6 — Gates, then look

```bash
./gradlew :registry:check :apps:showcase:assembleDebug        # imports · states · conventions · ux · tests · screenshots
./gradlew :registry:updateDebugScreenshotTest :registry:contactSheets   # (re)record goldens after intentional visual change
```

Then **read** `registry/build/contact-sheets/<name>.png` and every PNG under `registry/src/screenshotTestDebug/reference/` for the item. Go through the state matrix row by row: does the image show the state it claims? A "loading" image with no visible spinner is a lie; fix it before hand-off.

Then go through the `ux` table row by row: where is each row observable — which golden, which test, which journey step? Write it down. A row you cannot point at is a row you did not build.

Set `status` to `review` and hand off with:

```text
/review <name>
```

Include in the hand-off: the product decision sentence, the `ux` table with where each row is observable, the Mobbin references, the transition rules, `sources`, and an `unverified` list.

## Slop list — reject on sight, in your own code first

```text
config objects · 10+ params · wrapper with no decision · modifier missing or mid-list · inline .dp
"Oops!" · previews that show only default · tests asserting a count of text · a helper file "for reuse"
spring animations on list rows · if (isTablet) forks · a second colour system beside MaterialTheme
aiHints that restate description · avoidWhen copy-pasted from another item · generated-looking KDoc
ux rows that restate states ("error: shows error colour") · a signature that is a colour or a radius
motion with no reducedMotion · a haptic with no semantics twin · a shake that loops · a spinner that flashes
```

Less code with the right intention beats more code with coverage. If you can delete a parameter, delete it; a block will ask for it back when it is real.
