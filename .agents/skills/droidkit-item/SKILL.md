---
name: droidkit-item
description: Design and build a DroidKit registry item (component, pattern, block) or a core theme/foundation change. Use when creating, revising, or extending anything under registry/ or core/. Covers the product decision, the state matrix, grounding in official Android docs via the android CLI, API shape, Compose/Android gotchas, folder anatomy, registry.json, and the gates that must be green before hand-off.
---

# Build a DroidKit item

Read first, follow, do not paraphrase back: `AGENTS.md`, `docs/principles.md`, `docs/anatomy.md`, `docs/quality.md`. Settled choices are in `docs/decisions.md`; the loop you are inside is `docs/verification.md`.

You are the **author**. You never review your own item. When the gates are green and you have looked at every PNG, hand off to `/review <name>`.

## Step 0 — Name the product decision

One sentence: what does this item decide that Material's primitive leaves open?

```text
✓ password-field: show/hide on a 48 dp target, ContentType.Password, IME chain, error clears on typing
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
```

## Step 5 — Folder

```text
registry/<type>s/<name>/
├── App<Name>.kt              source   package com.droidkit.registry.<type>s
├── App<Name>Preview.kt       preview  one internal fun per state, @Preview(name = "<name> <state>")
├── App<Name>Test.kt          test     behaviour through semantics, not existence
├── registry.json             type · status · states · sources · avoidWhen · aiHints
└── journey.xml               optional; on-device script for /device (see droidkit-device)
```

Preview file rules: a tiny `private fun XPreviewSurface(darkTheme, content)` helper, then one `internal fun` per state. `uiMode`, `fontScale`, `widthDp` go on the `@Preview` annotation — the screenshot generator copies it verbatim. RTL is `CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl)` inside the preview. No logic in previews. Copy `registry/components/button/AppButtonPreview.kt`.

Test rules: an assertion that would pass on an empty `Box` is not a test. Assert behaviour: disabled does not fire `onClick`; loading does not fire `onClick`; the show/hide toggle changes `contentDescription`; error text is present in the semantics tree. Use `mainClock` for transitions.

Metadata rules: `description` says when to use it; `avoidWhen` names the real confusion (`otp-input` vs `password-field`); `aiHints` are the things an agent will get wrong composing it, not a restatement of the description. Register the item in `registry/registry.json` and add a showcase screen in `apps/showcase` (every item is used there — Decisions #14).

## Step 6 — Gates, then look

```bash
./gradlew :registry:check :apps:showcase:assembleDebug        # imports · states · conventions · tests · screenshots
./gradlew :registry:updateDebugScreenshotTest :registry:contactSheets   # (re)record goldens after intentional visual change
```

Then **read** `registry/build/contact-sheets/<name>.png` and every PNG under `registry/src/screenshotTestDebug/reference/` for the item. Go through the state matrix row by row: does the image show the state it claims? A "loading" image with no visible spinner is a lie; fix it before hand-off.

Set `status` to `review` and hand off with:

```text
/review <name>
```

Include in the hand-off: the product decision sentence, the transition rules, `sources`, and an `unverified` list.

## Slop list — reject on sight, in your own code first

```text
config objects · 10+ params · wrapper with no decision · modifier missing or mid-list · inline .dp
"Oops!" · previews that show only default · tests asserting a count of text · a helper file "for reuse"
spring animations on list rows · if (isTablet) forks · a second colour system beside MaterialTheme
aiHints that restate description · avoidWhen copy-pasted from another item · generated-looking KDoc
```

Less code with the right intention beats more code with coverage. If you can delete a parameter, delete it; a block will ask for it back when it is real.
