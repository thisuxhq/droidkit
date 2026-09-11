#!/usr/bin/env bash
# Open one registry item in the showcase app via deep link: droidkit://item/<name>
# Usage: tools/device/open.sh <item-name>   (e.g. button, password-field, settings)
# Restarts the task so font/RTL changes made with env.sh take effect.
set -euo pipefail
item="${1:?item name}"
adb shell am start -W --activity-clear-task --activity-new-task \
  -a android.intent.action.VIEW -d "droidkit://item/${item}" com.droidkit.showcase >/dev/null
sleep 1
echo "opened ${item}"
