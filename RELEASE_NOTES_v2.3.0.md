# G700 Remote v2.3.0

## Cloud connectivity fixed
- Fixed cloud control through your account. The relay phone leg now uses the correct raw
  protocol framing, so commands reach the car and the handshake completes instead of
  falsely reporting the car as "offline over the cloud."
- Clearer connection messages for the common failure cases (car not signed in / Cloud
  disabled, wrong or locked pairing code, app/car update required).

## Location & background sync
- Vehicle location now keeps updating while the app is in the background (previously only
  battery/status refreshed and the map position went stale).
- Automatic fallback to phone location when the car reports no fresh GPS fix for ~90 seconds,
  and automatic switch back to vehicle GPS once the car fix returns.
- Stale last-known phone fixes (older than 10 minutes) are no longer shown as the current
  location.

## Connectivity robustness
- Switching connection modes (BLE / LAN / Cloud) now cleanly tears down the previous
  transport, preventing a lingering connection when you change modes mid-session.

## New screens (feature parity with the DisplayMirror car-key app)
Reachable from the bottom bar's "More" button:
- **Audio** — full audio settings including the EQ mode selector, surround/loudness, and
  balance/fade.
- **Cabin safety** — seat occupancy, child- and pet-in-cabin detection, seatbelt status, and
  driver drowsiness/distraction monitoring.
- **Performance** — a graphical G-force meter plus steering angle, yaw rate, per-corner ride
  height, and speed.
- **Engine data** — live OBD-II readouts: rpm, speed, engine load, throttle, coolant/intake/
  ambient temperatures, fuel level, module and battery voltage (flagged when low), and VIN.

All four read only data the car actually reports and update live while the screen is open.

## Controls & UI
- Added a Recirculate / fresh-air toggle to Climate → Modes.
- New far-right "More" button on the bottom bar opens a sheet with the screens above plus
  Vehicle Map, Shared Links, and Settings — leaving room to grow without crowding the main tabs.
- Minimal status bar: removed the device-name line; now two clean lines (connection state and
  last refresh).

## Notes
- Background phone-location fallback is best-effort while backgrounded (uses last-known fix);
  live vehicle GPS continues to update over the active connection.
