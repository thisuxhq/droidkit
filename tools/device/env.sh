#!/usr/bin/env bash
# Put the connected device into one of the environments every DroidKit item must survive.
# Usage: tools/device/env.sh light|dark | font <scale> | rtl on|off | anim 0|1 | talkback on|off | reset | show
# Restart the showcase after rtl/font changes (open.sh does).
set -euo pipefail

adbs() { adb shell "$@"; }

case "${1:-show}" in
  light) adbs cmd uimode night no ;;
  dark)  adbs cmd uimode night yes ;;
  font)  adbs settings put system font_scale "${2:?scale, e.g. 1.0 1.3 2.0}" ;;
  rtl)
    case "${2:?on|off}" in
      on)  adbs settings put global debug.force_rtl 1 ;;
      off) adbs settings put global debug.force_rtl 0 ;;
    esac ;;
  anim)
    s="${2:?0|1}"
    adbs settings put global animator_duration_scale "$s"
    adbs settings put global transition_animation_scale "$s"
    adbs settings put global window_animation_scale "$s" ;;
  talkback)
    case "${2:?on|off}" in
      on)
        adbs settings put secure enabled_accessibility_services \
          com.google.android.marvin.talkback/com.google.android.marvin.talkback.TalkBackService
        adbs settings put secure accessibility_enabled 1 ;;
      off)
        adbs settings put secure enabled_accessibility_services ''
        adbs settings put secure accessibility_enabled 0 ;;
    esac ;;
  reset)
    "$0" light; "$0" font 1.0; "$0" rtl off; "$0" anim 1; "$0" talkback off ;;
  show)
    echo "night:     $(adbs cmd uimode night | tr -d '\r')"
    echo "font:      $(adbs settings get system font_scale | tr -d '\r')"
    echo "rtl:       $(adbs settings get global debug.force_rtl | tr -d '\r')"
    echo "anim:      $(adbs settings get global animator_duration_scale | tr -d '\r')"
    echo "talkback:  $(adbs settings get secure enabled_accessibility_services | tr -d '\r')"
    echo "density:   $(adbs wm density | tr -d '\r')" ;;
  *) echo "unknown: $1" >&2; exit 2 ;;
esac
