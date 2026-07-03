# G700 Remote v2.3.1

UI polish release on top of v2.3.0.

## Bottom bar
- The "More" button is no longer a separate detached button: it is now a compact
  icon-only sixth pill inside the same bar, so the five main tabs get their full width back.
- When you are on any screen opened from "More" (Audio, Cabin safety, Performance,
  Engine data, and also Settings / Shared links / Vehicle map), the bar keeps the
  highlight on the More pill instead of highlighting the wrong tab.

## More sheet
- Removed Shared links, Vehicle map, and Settings from the More sheet — they already
  have their own entry points (header buttons and the Home map card). The sheet now
  lists only Audio, Cabin safety, Performance, and Engine data.

## Audio
- Audio controls now live only in the Audio screen (More → Audio). The duplicate
  Audio section on the Climate screen was removed; the Audio screen has the full set:
  EQ mode, surround, loudness, balance, and fade.

## Seats
- Fixed the driver-seat ventilation showing one bar lit by default. Seat heat/vent
  levels are no longer restored from a previous session: the car does not report seat
  status back, so the app now always starts with them off instead of showing a stale
  value from the last command.
