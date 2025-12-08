# Music Wrapped "Spopi" Example

A small example app that ingests listening events, keeps aggregated stats, and imports/exports them as CSV (no JSON dependency) using the JMiniApp framework.

## Overview

This example demonstrates how to build a mini-app that:
- Imports existing listening stats from CSV
- Lets you add play events (song, artist, minutes) and updates new additions
- Updates top artists/songs
- Exports the current state back to CSV file

## Features

- **Import stats**: Load `data/music_stats.csv` into the app state.
- **Export stats**: Save the current state back to `data/music_stats.csv`.
- **Add play event**: Enter song/artist/minutes; totals and rankings update immediately.
- **Live top lists**: Top artists/songs are derived from aggregated minutes.

## Project Structure

```
music-wrapped/
├── pom.xml
├── README.md
├── src/main/java/com/jminiapp/examples/musicwrapped/
│   ├── MusicWrappedApp.java        # Main app with menu, import/export, play events
│   ├── MusicWrappedAppRunner.java  # Bootstrap using JMiniAppRunner
│   ├── MusicWrappedState.java      # Aggregated state (minutes per artist/song)
│   └── MusicWrappedCSVAdapter.java # CSV adapter wiring (pipe/colon encoding)
└── src/main/resources/
    └── data/music_stats.csv        # Sample stats data (CSV, no JSON)
```

## Key Components

### MusicWrappedState
- Holds `artistMinutes`, `songMinutes`, `totalMinutesListened`, and derived `topArtists` / `topSongs`.

### MusicWrappedCSVAdapter
- Implements `CSVAdapter<MusicWrappedState>` to handle CSV import/export.
- Serializes maps/lists using simple `|`-delimited entries and `key:value` pairs (escaped with `\` when needed) to avoid JSON entirely.

### MusicWrappedApp
- Extends `JMiniApp`.
- `initialize()`: Imports stats file and prepares the app.
- `run()`: Interactive loop with numeric menu:
  - `1` import stats
  - `2` export stats
  - `3` add play event (song, artist, minutes)
  - `4` quit
- `shutdown()`: Persists state and closes resources.

### MusicWrappedAppRunner
- Uses `JMiniAppRunner` to wire app, state, adapter, and resources path.

## Building and Running

### Prerequisites
- Java 17+
- Maven 3.6+

### Build from project root
```powershell
cd <your-clone-root>/jminiapp-spopi
mvn clean install
```

### Run the example (Maven exec, from module dir)
```powershell
cd <your-clone-root>/jminiapp-spopi/examples/music-wrapped
mvn -q exec:java "-Dexec.mainClass=com.jminiapp.examples.musicwrapped.MusicWrappedAppRunner"
```

### Run from VS Code
Open `MusicWrappedAppRunner.java` and use the Run button on `main`.

## Usage Example

```
Spopi Wrapped
Loading your listening stats...

Your Spopi Wrapped:

Total minutes listened: 1234

Top Artists:
  1. Artist A
  2. Artist B
  3. Artist C

Top Songs:
  1. Song 1
  2. Song 2
  3. Song 3

Options: 1) import  2) export  3) add play  4) quit
> 3
Song name: New Song
Artist name: New Artist
Minutes listened: 5
Recorded play for 'New Song' by New Artist (+5 min)
```

After adding plays, export with option `2` to persist updates to `data/music_stats.csv`.
