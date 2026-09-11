---
description: On-device pass for a DroidKit item via the showcase app and the android CLI (dark, font 2×, RTL, anim 0, TalkBack, 48 dp, journeys)
argument-hint: "<item-name>"
---
Load the `droidkit-device` skill (`.agents/skills/droidkit-device/SKILL.md`) and run the full pass for the item **$1**.

- Confirm a device is attached (`adb devices`); if not, start `Pixel_6` with `android emulator start Pixel_6`.
- Install the showcase, `tools/device/env.sh reset`, then loop the environments, run `tools/device/check-layout.sh` in each, capture screenshots to `out/`, and **read every screenshot** before writing findings.
- Run `registry/*/$1/journey.xml` if it exists.
- Search `android docs` for anything that surprises you and cite the `kb://` URL in the finding.
- You are the tester, not the author: do not edit the component. Return the JSON report from the skill, then one line telling me whether the item can move to `status: ready` or must go back through `/item $1`.
- Finish with `tools/device/env.sh reset`.
