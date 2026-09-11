---
description: Build or revise a DroidKit registry item through the full loop (decision → states → docs → code → gates → look → hand off)
argument-hint: "<item-name> [what to change]"
---
Load the `droidkit-item` skill (`.agents/skills/droidkit-item/SKILL.md`) and follow it end to end for the item **$1**.

${@:2}

Rules for this run:
- Start with the product-decision sentence and the state matrix. Show them to me before writing Kotlin if this is a new item or a change to states/API; for a small fix, just proceed.
- Ground every trigger topic with `android docs search` / `android docs fetch` and record the `kb://` URLs in `registry.json` → `sources`.
- Run `./gradlew :registry:check :apps:showcase:assembleDebug`. If a golden changed intentionally, re-record with `:registry:updateDebugScreenshotTest` and say which states changed and why.
- Read `registry/build/contact-sheets/$1.png` and every golden PNG for the item. Walk the state matrix row by row against the images.
- Set `status` to `review` when the gates are green and you have looked.
- Do not review your own work. Finish with a hand-off block: product decision, transition rules, `sources`, `unverified`, and the exact command `/review $1`.
