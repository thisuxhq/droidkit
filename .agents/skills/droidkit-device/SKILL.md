---
name: droidkit-device
description: On-device verification of a DroidKit item through the showcase app and the `android` CLI (invoked by /device). Use after the Gradle gates are green, for anything with text input, IME, focus, motion, dialogs/sheets, or scrolling. Covers emulator setup, environment toggles (dark, font scale, RTL, animator scale, TalkBack), layout dumps, the 48 dp touch-target check, screenshots you must read, the feel pass (every ux moment at real speed and at animator scale 0, the signature recorded), journeys, and the report shape.
---

# Verify on device

Screenshot tests prove what a state *looks like*. The device proves what it *does*: IME, autofill, focus, back, scroll, motion at real speed. This skill is the second half of `docs/verification.md`. Also read `~/.agents/skills/android-cli/SKILL.md` and its `references/interact.md` for the raw `android layout` / `android screen` / `adb shell input` rules — this skill only adds the DroidKit-specific loop.

## Setup

```bash
adb devices                                   # need one device
android emulator list                         # Pixel_6, Medium_Phone_API_36.1 ...
android emulator start Pixel_6                # returns when booted
./gradlew :apps:showcase:installDebug --console=plain -q
tools/device/env.sh reset
tools/device/open.sh <item>                   # droidkit://item/<item>  (name from registry.json)
```

`open.sh` restarts the task, so run it again after every `env.sh font` or `env.sh rtl` change.

## The pass — every item goes through all of these

```bash
for env in "light" "dark" "font 2.0" "rtl on"; do
  tools/device/env.sh $env
  tools/device/open.sh <item>
  tools/device/check-layout.sh                          # 48 dp + labels; exit 1 on failure
  android screen capture -o out/<item>-$(echo $env | tr ' ' '-').png
done
tools/device/env.sh reset
```

**Read every PNG you capture** with the `read` tool before writing a word about it. A layout dump cannot tell you the spinner is invisible or the label is clipped.

Then run the item's `journey.xml` if it exists (`registry/<type>s/<item>/journey.xml`) following `references/journeys.md` from the android-cli skill. The XML is the truth; if the app disagrees, the app failed.

## Inspecting

```bash
android layout                       # interactive tree: text, contentDesc, interactions, state, bounds, center
android layout --full                # include non-interactive nodes (headings, supporting text)
tools/device/check-layout.sh --json  # same data with dp sizes + failures, for the report
android screen capture --annotate -o out/annotated.png   # numbered boxes when layout is not enough
```

Interact by `center` coordinates from the layout dump:

```bash
adb shell input tap X Y
adb shell input text "hello"            # only when the field's state contains "focused"
adb shell input keyevent 61             # TAB — keyboard focus order
adb shell input keyevent 66             # ENTER / IME action
adb shell input keyevent 67             # DEL
adb shell input keyevent 4              # BACK (predictive back on 14+: use swipe from left edge)
adb shell input swipe X Y X Y2 600      # slow scroll; the last arg is duration in ms
adb shell settings put system font_scale 1.3   # mid-flow font change; app must survive without restart
```

## Must-check by item kind

```text
text inputs   correct keyboard type · Next moves focus to the next field · Done fires the action
              autofill chip appears (TalkBack on: field announces role + error) · paste into the field
              error → typing: does it clear as the author declared? · rotate: value survives
otp           paste a full code · backspace on an empty cell moves back · auto-advance · SMS autofill hint
buttons       tap during loading does nothing · focus stays on the button while loading
              width does not change default → loading · font 2× does not clip
dialogs/sheet back dismisses · predictive-back swipe previews · focus lands inside on open
              scrim tap rule matches the declared decision · IME does not cover the primary action
lists/blocks  slow swipe with anim 1 — watch for stutter · imePadding when a row has an input
              tablet: `adb shell wm size 1280x800` then open again (`wm size reset` after)
everything    TalkBack on: swipe right through the item once — order sane? nothing read twice?
              anim 0: the item still reaches its end state (no animation-gated logic)
```

## The feel pass — every `ux` row, at real speed

Screenshots prove states. This proves moments. Take `registry.json` → `ux` and drive each row on the device with `tools/device/env.sh anim 1` (real speed), then again with `anim 0`.

```text
reach         press and hold the primary target: does it scale (anim 1) and stay put (anim 0)?
              does the haptic fire? (adb shell dumpsys vibrator_manager | tail, or feel it on hardware)
act           the signature row: record it. android screen record -o out/<item>-signature.mp4 (3–5 s),
              then read the recording before writing about it
mistake       trigger the error: one shake, not two; it stops; the message is on the field; reject fires
              anim 0: no shake, message still appears, haptic still fires
recover       type one character after the error: does the text hide the way the row says? does the form jump?
              refocus after error: where is the caret / selection?
succeed       auto-submit / success morph: does it happen at the declared delay? does "done" announce with TalkBack on?
loading       a fast toggle (< 150 ms) shows nothing; a 200 ms toggle stays ≥ 500 ms — use the showcase's
              "simulate 100 ms / 2 s" controls where the screen has them
```

Each row gets a line in `findings` or `feel`, even when it passes. "Did not check" is a finding, not silence.

`env.sh anim 0` is the reduced-motion check; the "Remove animations" accessibility toggle sets the same scale. If the emulator has no motor, haptic rows go in `unverified` — do not mark them passed.

## When something surprises you

Search the docs before touching code, and put the URL in the finding:

```bash
android docs search "autofill compose ContentType"
android docs fetch kb://android/develop/ui/compose/text/autofill
```

`android layout` may fail during an animation or on a WebView — wait and retry, or use `android screen capture --annotate`. `env.sh talkback on` needs a Play-image emulator (TalkBack is not on AOSP images); say so in `unverified` if it is missing.

## Report — exactly this shape

```json
{
  "item": "<name>",
  "device": "Pixel_6 · API 36 · density 420",
  "envs_checked": ["light", "dark", "font 2.0", "rtl", "anim 0", "talkback"],
  "screenshots": ["out/<item>-light.png", "..."],
  "layout_failures": ["touch target 40x40 dp < 48: Button 'Show'"],
  "journey": { "name": "...", "passed": true, "failed_step": null },
  "feel": [
    { "moment": "mistake", "anim1": "one shake, stopped, message on field", "anim0": "no shake, message shown", "haptic": "unverified: no motor", "recording": "out/<item>-signature.mp4 | null" }
  ],
  "findings": [
    { "severity": "blocker | should | taste", "env": "font 2.0", "what": "...", "repro": "...", "source": "kb://... | null" }
  ],
  "unverified": ["talkback: not installed on this image"]
}
```

Findings go back to `/item <name>`; `check-layout.sh` failures are blockers. Do not fix the component from here — you are the tester.
