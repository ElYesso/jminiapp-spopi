package com.jminiapp.examples.musicwrapped;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicWrappedState {
    private Map<String, Integer> artistMinutes = new HashMap<>();
    private Map<String, Integer> songMinutes = new HashMap<>();
    private int totalMinutesListened;

    // Derived, persisted for convenience
    private List<String> topArtists = new ArrayList<>();
    private List<String> topSongs = new ArrayList<>();

    public Map<String, Integer> getArtistMinutes() {
        return artistMinutes;
    }

    public void setArtistMinutes(Map<String, Integer> artistMinutes) {
        this.artistMinutes = artistMinutes != null ? artistMinutes : new HashMap<>();
    }

    public Map<String, Integer> getSongMinutes() {
        return songMinutes;
    }

    public void setSongMinutes(Map<String, Integer> songMinutes) {
        this.songMinutes = songMinutes != null ? songMinutes : new HashMap<>();
    }

    public int getTotalMinutesListened() {
        return totalMinutesListened;
    }

    public void setTotalMinutesListened(int totalMinutesListened) {
        this.totalMinutesListened = totalMinutesListened;
    }

    public List<String> getTopArtists() {
        return topArtists;
    }

    public void setTopArtists(List<String> topArtists) {
        this.topArtists = topArtists != null ? topArtists : new ArrayList<>();
    }

    public List<String> getTopSongs() {
        return topSongs;
    }

    public void setTopSongs(List<String> topSongs) {
        this.topSongs = topSongs != null ? topSongs : new ArrayList<>();
    }
}
