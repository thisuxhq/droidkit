#!/usr/bin/env bash
# Mechanical accessibility pass over the current screen using the android CLI layout dump.
# Fails (exit 1) on: clickable node smaller than 48 dp · clickable node with no text and no
# contentDesc · focusable node with empty bounds. Prints every interactive node so you can
# read it alongside the screenshot.
# Usage: tools/device/check-layout.sh [--json]
set -euo pipefail

density="$(adb shell wm density | tr -d '\r' | sed -n 's/.*density: *\([0-9]*\).*/\1/p' | tail -1)"
density="${density:-420}"
layout="$(android layout --flat --full)"

python3 - "$density" "${1:-}" <<'PY' <<<"$layout"
import json, re, sys
density = int(sys.argv[1]); as_json = sys.argv[2] == "--json"
raw = sys.stdin.read()
start = raw.find('['); start = start if start >= 0 else raw.find('{')
data = json.loads(raw[start:])
nodes = data if isinstance(data, list) else [data]

def dp(px): return px * 160.0 / density
def bounds(s):
    m = re.findall(r'\[(-?\d+),(-?\d+)\]', s or '')
    if len(m) < 2: return None
    (x1, y1), (x2, y2) = [tuple(map(int, t)) for t in m[:2]]
    return x1, y1, x2, y2

failures, rows = [], []
for n in nodes:
    inter = set(n.get('interactions') or [])
    if not inter: continue
    b = bounds(n.get('bounds'))
    text = (n.get('text') or '').strip(); desc = (n.get('contentDesc') or '').strip()
    w = h = 0
    if b:
        w, h = dp(b[2]-b[0]), dp(b[3]-b[1])
    label = text or desc or '<no label>'
    row = {'class': n.get('class'), 'label': label, 'w_dp': round(w), 'h_dp': round(h),
           'interactions': sorted(inter), 'state': n.get('state') or [], 'offscreen': n.get('off-screen', False)}
    rows.append(row)
    if n.get('off-screen'): continue
    if 'clickable' in inter and b and (w < 47.5 or h < 47.5):
        failures.append(f"touch target {round(w)}x{round(h)} dp < 48: {row['class']} '{label}'")
    if 'clickable' in inter and not text and not desc and 'password' not in inter:
        failures.append(f"clickable without text or contentDesc: {row['class']}")
    if 'focusable' in inter and b and (b[2]-b[0] <= 0 or b[3]-b[1] <= 0):
        failures.append(f"focusable with empty bounds: {row['class']} '{label}'")

if as_json:
    print(json.dumps({'density': density, 'nodes': rows, 'failures': failures}, indent=2))
else:
    for r in rows:
        flag = ' (off-screen)' if r['offscreen'] else ''
        print(f"{r['w_dp']:>4}x{r['h_dp']:<4} dp  {','.join(r['interactions']):<28} {r['label']}{flag}")
    print()
    if failures:
        print('FAIL'); [print(' -', f) for f in failures]
    else:
        print(f'OK  {len(rows)} interactive nodes, none under 48 dp, all labelled')
sys.exit(1 if failures else 0)
PY
