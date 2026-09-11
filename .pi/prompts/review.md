---
description: Fresh-eyes adversarial review of a DroidKit item (spawns a subagent with no context, returns a structured verdict)
argument-hint: "<item-name>"
---
Spawn one `general-purpose` subagent with **no inherited context** (`inherit_context: false`, `run_in_background: false`) and this prompt, substituting the item name:

> You are reviewing the DroidKit registry item `$1` in the repo at `/Users/sanju/me/droidkit`. Load and follow `.agents/skills/droidkit-review/SKILL.md` exactly. You did not write this code; your job is to refute it. Run the gates, read the metadata, source, tests, previews, and every golden PNG for the item (`registry/src/screenshotTestDebug/reference/$1__*.png` and `registry/build/contact-sheets/$1.png`; run `./gradlew :registry:contactSheets` if the sheet is missing). Cite `kb://` docs via `android docs search` / `android docs fetch` for any behavioural claim. Return ONLY the JSON verdict described in the skill.

When the verdict comes back:
- Print it verbatim.
- If `verdict` is `not ready`: summarise the blockers in one line each and tell me to run `/item $1` with the verdict pasted in. Do not start fixing.
- If `verdict` is `ready`: say whether the item still needs `/device $1` (yes for anything with input, IME, focus, motion, dialogs, sheets, or scrolling) and remind me to look at the contact sheet before flipping `status` to `ready`.
