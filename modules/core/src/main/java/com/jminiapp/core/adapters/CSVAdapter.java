package com.jminiapp.core.adapters;

import com.jminiapp.core.api.JMiniFormatAdapter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV-specific adapter interface with default implementations.
 *
 * <p>This interface extends JMiniFormatAdapter and provides CSV-specific methods
 * for converting individual objects to/from CSV rows. It includes default
 * implementations of read() and write() that use the toCSV() and fromCSV()
 * methods.</p>
 *
 * <p>Implementers only need to provide:</p>
 * <ul>
 *   <li>toCSV() - Convert object to CSV row (String array)</li>
 *   <li>fromCSV() - Convert CSV row to object</li>
 *   <li>getHeader() - Return column headers (optional)</li>
 * </ul>
 *
 * @param <T> the type of objects this adapter handles
 */
public interface CSVAdapter<T> extends JMiniFormatAdapter<T> {

    /**
     * Convert a single object to a CSV row.
     *
     * @param object the object to convert
     * @return array of strings representing the CSV fields
     */
    String[] toCSV(T object);

    /**
     * Convert a CSV row to an object.
     *
     * @param fields the array of strings from the CSV row
     * @return the reconstructed object
     */
    T fromCSV(String[] fields);

    /**
     * Get the CSV header row.
     *
     * <p>Default implementation returns an empty array (no header).</p>
     *
     * @return array of column names for the header
     */
    default String[] getHeader() {
        return new String[0];
    }

    /**
     * Get the field delimiter for CSV files.
     *
     * <p>Default is comma (","). Override to use different delimiters
     * like semicolon or tab.</p>
     *
     * @return the delimiter character(s)
     */
    default String getDelimiter() {
        return ",";
    }

    /**
     * Get the format name for CSV.
     *
     * @return "csv"
     */
    @Override
    default String getFormatName() {
        return "csv";
    }

    /**
     * Read CSV data from an input stream.
     *
     * <p>Default implementation reads line by line, splits by delimiter,
     * and calls fromCSV() for each row. Skips the header row if present.</p>
     *
     * @param input the input stream containing CSV data
     * @return list of objects parsed from CSV
     * @throws IOException if an I/O error occurs
     */
    @Override
    default List<T> read(InputStream input) throws IOException {
        List<T> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine && getHeader().length > 0) {
                    firstLine = false;
                    continue; // Skip header
                }
                firstLine = false;

                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }

                String[] fields = parseCsvLine(line);
                items.add(fromCSV(fields));
            }
        }
        return items;
    }

    /**
     * Write objects to CSV format.
     *
     * <p>Default implementation writes header (if present) followed by
     * one row per object using toCSV().</p>
     *
     * @param data the list of objects to write
     * @param output the output stream to write to
     * @throws IOException if an I/O error occurs
     */
    @Override
    default void write(List<T> data, OutputStream output) throws IOException {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(output))) {
            // Write header
            String[] header = getHeader();
            if (header.length > 0) {
                writer.println(String.join(getDelimiter(), header));
            }

            // Write data
            for (T item : data) {
                String[] fields = toCSV(item);
                writer.println(String.join(getDelimiter(), fields));
            }
        }
    }

    /**
     * Parse a CSV line handling quoted fields.
     *
     * <p>Simple CSV parser that handles basic quoting. For complex CSV
     * parsing needs, consider using Apache Commons CSV library.</p>
     *
     * @param line the CSV line to parse
     * @return array of field values
     */
    default String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        char prev = 0;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (prev == '\\') {
                    // Escaped quote, keep it as a literal character.
                    current.append(c);
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == getDelimiter().charAt(0) && !inQuotes) {
                fields.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
            prev = c;
        }
        fields.add(current.toString());

        return fields.toArray(new String[0]);
    }
}
