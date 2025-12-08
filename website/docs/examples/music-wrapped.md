---
sidebar_position: 2
---

# Music Wrapped (Spopi)

A listening-stats example that shows how to import/export CSV data (no JSON dependency), ingest new play events, and keep live top artists/songs with JMiniApp.

**Source Code:** [examples/music-wrapped](https://github.com/jminiapp/jminiapp/tree/main/examples/music-wrapped)

## Features

- **Aggregated Stats** – total minutes listened, minutes per artist/song
- **CSV Import/Export** – load and persist state from/to `data/music_stats.csv` using pipe/colon encoding for maps/lists
- **Live Updates** – add play events (song, artist, minutes) and recompute top lists
- **Interactive Menu** – numeric console options for import/export/add/quit

## Quick Start

```bash
cd examples/music-wrapped
mvn clean install
mvn -q exec:java "-Dexec.mainClass=com.jminiapp.examples.musicwrapped.MusicWrappedAppRunner"
```

## Key Components

### State Model

Holds aggregates and derived rankings:

```java
public class MusicWrappedState {
	private Map<String, Integer> artistMinutes;
	private Map<String, Integer> songMinutes;
	private int totalMinutesListened;
	private List<String> topArtists;
	private List<String> topSongs;
}
```

### Application Lifecycle

Imports stats on startup, loops with menu, saves on exit:

```java
public class MusicWrappedApp extends JMiniApp {
	@Override
	protected void initialize() {
		context.importData("data/music_stats.csv", "csv");
		// load state into memory
	}

	@Override
	protected void run() {
		// menu: 1 import, 2 export, 3 add play, 4 quit
	}

	@Override
	protected void shutdown() {
		context.setData(List.of(stats));
	}
}
```

### CSV Adapter

Serializes maps/lists without JSON, using pipe-separated entries and `key:value` pairs:

```java
public class MusicWrappedCSVAdapter implements CSVAdapter<MusicWrappedState> {
	@Override
	public String[] toCSV(MusicWrappedState state) {
		// totalMinutes, artistMinutes, songMinutes, topArtists, topSongs
	}

	@Override
	public MusicWrappedState fromCSV(String[] fields) {
		// parse pipe/colon encoded fields back into the state
	}
}
```

### Bootstrap

Configures the runner with state, adapter, and resources path:

```java
public class MusicWrappedAppRunner {
	public static void main(String[] args) {
		JMiniAppRunner
			.forApp(MusicWrappedApp.class)
			.withState(MusicWrappedState.class)
			.withAdapters(new MusicWrappedCSVAdapter())
			.withResourcesPath("examples/music-wrapped/src/main/resources/")
			.named("MusicWrapped")
			.run(args);
	}
}
```

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

The exported CSV (after updates) stays in `src/main/resources/data/music_stats.csv` using the pipe/colon encoding.