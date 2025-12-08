package com.jminiapp.examples.musicwrapped;

import com.jminiapp.core.adapters.CSVAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * CSV adapter for MusicWrappedState. Complex fields are serialized as pipe-delimited strings
 * with simple escaping (no JSON dependency).
 */
public class MusicWrappedCSVAdapter implements CSVAdapter<MusicWrappedState> {

    private static final char ENTRY_DELIM = '|';
    private static final char KV_DELIM = ':';
    private static final char ESCAPE_CHAR = '\\';

    @Override
    public String[] toCSV(MusicWrappedState state) {
        List<String> row = new ArrayList<>();
        row.add(String.valueOf(state.getTotalMinutesListened()));
        row.add(serializeMap(state.getArtistMinutes()));
        row.add(serializeMap(state.getSongMinutes()));
        row.add(serializeList(state.getTopArtists()));
        row.add(serializeList(state.getTopSongs()));
        return row.toArray(new String[0]);
    }

    @Override
    public MusicWrappedState fromCSV(String[] fields) {
        MusicWrappedState state = new MusicWrappedState();
        if (fields.length > 0) {
            try {
                state.setTotalMinutesListened(Integer.parseInt(fields[0]));
            } catch (NumberFormatException ignored) {
                state.setTotalMinutesListened(0);
            }
        }
        if (fields.length > 1) {
            state.setArtistMinutes(parseMap(fields[1]));
        }
        if (fields.length > 2) {
            state.setSongMinutes(parseMap(fields[2]));
        }
        if (fields.length > 3) {
            state.setTopArtists(parseList(fields[3]));
        }
        if (fields.length > 4) {
            state.setTopSongs(parseList(fields[4]));
        }
        return state;
    }

    @Override
    public String[] getHeader() {
        return new String[] { "totalMinutes", "artistMinutes", "songMinutes", "topArtists", "topSongs" };
    }

    private String serializeMap(Map<String, Integer> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        return map.entrySet().stream()
                .map(e -> escape(e.getKey()) + KV_DELIM + e.getValue())
                .collect(Collectors.joining(String.valueOf(ENTRY_DELIM)));
    }

    private Map<String, Integer> parseMap(String encoded) {
        Map<String, Integer> result = new HashMap<>();
        if (encoded == null || encoded.isEmpty()) {
            return result;
        }

        for (String entry : splitEscaped(encoded, ENTRY_DELIM)) {
            if (entry.isEmpty()) {
                continue;
            }
            List<String> kvParts = splitEscaped(entry, KV_DELIM);
            if (kvParts.isEmpty()) {
                continue;
            }
            String key = kvParts.get(0);
            String valuePart = kvParts.size() > 1
                    ? String.join(String.valueOf(KV_DELIM), kvParts.subList(1, kvParts.size()))
                    : "0";
            try {
                int minutes = Integer.parseInt(valuePart);
                result.put(key, minutes);
            } catch (NumberFormatException ignored) {
                // skip bad number
            }
        }
        return result;
    }

    private String serializeList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return list.stream()
                .map(this::escape)
                .collect(Collectors.joining(String.valueOf(ENTRY_DELIM)));
    }

    private List<String> parseList(String encoded) {
        if (encoded == null || encoded.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(splitEscaped(encoded, ENTRY_DELIM));
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (char c : value.toCharArray()) {
            if (c == ENTRY_DELIM || c == KV_DELIM || c == ESCAPE_CHAR) {
                sb.append(ESCAPE_CHAR);
            }
            sb.append(c);
        }
        return sb.toString();
    }

    private List<String> splitEscaped(String input, char delimiter) {
        List<String> parts = new ArrayList<>();
        if (input == null) {
            return parts;
        }
        StringBuilder current = new StringBuilder();
        boolean escaping = false;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (escaping) {
                current.append(c);
                escaping = false;
            } else if (c == ESCAPE_CHAR) {
                escaping = true;
            } else if (c == delimiter) {
                parts.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        parts.add(current.toString());
        return parts;
    }
}
