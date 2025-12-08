package com.jminiapp.examples.musicwrapped;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.jminiapp.core.api.JMiniApp;
import com.jminiapp.core.api.JMiniAppConfig;

public class MusicWrappedApp extends JMiniApp {
    private static final String DATA_PATH = "data/music_stats.csv";

    private MusicWrappedState stats;
    private boolean running;
    private Scanner scanner;

    public MusicWrappedApp(JMiniAppConfig config) {
        super(config);
    }

    @Override
    protected void initialize() {
        System.out.println("\nSpopi Wrapped");
        System.out.println("Loading your listening stats...\n");

        scanner = new Scanner(System.in);
        running = true;

        importStats();
    }

    @Override
    protected void run() {
        while (running) {
            printSummary();
            System.out.println();
            System.out.println("Options: 1) import  2) export  3) add play  4) quit");
            System.out.print("> ");

            String input = scanner.hasNextLine() ? scanner.nextLine().trim().toLowerCase() : "";
            switch (input) {
                case "1":
                    importStats();
                    break;
                case "2":
                    exportStats();
                    break;
                case "3":
                    addPlayEvent();
                    break;
                case "4":
                    running = false;
                    break;
                default:
                    System.out.println("Unknown option. Choose 1/2/3/4.");
            }
            System.out.println();
        }
    }

    @Override
    protected void shutdown() {
        context.setData(List.of(stats));
        if (scanner != null) {
            scanner.close();
        }
        System.out.println("\nThanks for checking your Spopi Wrapped!\n");
    }

    private void importStats() {
        try {
            context.importData(DATA_PATH, "csv");
            loadFromContext();
            recomputeTopLists();
            System.out.println("Imported stats from " + DATA_PATH);
        } catch (IOException e) {
            System.out.println("Could not import stats: " + e.getMessage());
            loadFromContext();
        }
    }

    private void exportStats() {
        try {
            context.setData(List.of(stats));
            context.exportData(DATA_PATH, "csv");
            System.out.println("Exported stats to " + DATA_PATH);
        } catch (IOException e) {
            System.out.println("Could not export stats: " + e.getMessage());
        }
    }

    private void loadFromContext() {
        List<MusicWrappedState> data = context.getData();
        if (data != null && !data.isEmpty()) {
            stats = data.get(0);
        } else {
            stats = new MusicWrappedState();
        }
    }

    private void addPlayEvent() {
        System.out.print("Song name: ");
        String song = scanner.hasNextLine() ? scanner.nextLine().trim() : "";

        System.out.print("Artist name: ");
        String artist = scanner.hasNextLine() ? scanner.nextLine().trim() : "";

        System.out.print("Minutes listened: ");
        String minutesStr = scanner.hasNextLine() ? scanner.nextLine().trim() : "0";
        int minutes = 0;
        try {
            minutes = Integer.parseInt(minutesStr);
            if (minutes < 0) {
                minutes = 0;
            }
        } catch (NumberFormatException ignored) {
            System.out.println("Invalid minutes; using 0.");
        }

        if (song.isEmpty() && artist.isEmpty() && minutes == 0) {
            System.out.println("No event captured.");
            return;
        }

        applyPlayEvent(song, artist, minutes);
        recomputeTopLists();
        System.out.println("Recorded play for '" + song + "' by " + artist + " (+" + minutes + " min)");
    }

    private void applyPlayEvent(String song, String artist, int minutes) {
        if (minutes < 0) {
            minutes = 0;
        }
        stats.setTotalMinutesListened(stats.getTotalMinutesListened() + minutes);

        Map<String, Integer> artistMinutes = stats.getArtistMinutes();
        Map<String, Integer> songMinutes = stats.getSongMinutes();

        if (artist != null && !artist.isEmpty()) {
            artistMinutes.merge(artist, minutes, Integer::sum);
        }
        if (song != null && !song.isEmpty()) {
            songMinutes.merge(song, minutes, Integer::sum);
        }
    }

    private void recomputeTopLists() {
        stats.setTopArtists(topN(stats.getArtistMinutes(), 3));
        stats.setTopSongs(topN(stats.getSongMinutes(), 3));
    }

    private List<String> topN(Map<String, Integer> counts, int n) {
        if (counts == null || counts.isEmpty()) {
            return Collections.emptyList();
        }
        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(n)
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void printSummary() {
        if (stats == null) {
            System.out.println("No music stats available.");
            return;
        }

        List<String> topArtists = stats.getTopArtists() != null ? stats.getTopArtists() : Collections.emptyList();
        List<String> topSongs = stats.getTopSongs() != null ? stats.getTopSongs() : Collections.emptyList();

        System.out.println("Your Spopi Wrapped:");
        System.out.println();

        System.out.println("Total minutes listened: " + stats.getTotalMinutesListened());
        System.out.println();

        System.out.println("Top Artists:");
        if (topArtists.isEmpty()) {
            System.out.println("  (no artists yet)");
        } else {
            for (int i = 0; i < topArtists.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + topArtists.get(i));
            }
        }

        System.out.println();
        System.out.println("Top Songs:");
        if (topSongs.isEmpty()) {
            System.out.println("  (no songs yet)");
        } else {
            for (int i = 0; i < topSongs.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + topSongs.get(i));
            }
        }
    }
}
