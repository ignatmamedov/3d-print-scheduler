package saxion.dataprovider.reader;

import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Adapter for reading CSV data from a given {@link Reader}.
 * This class provides functionality to parse CSV data into a collection
 * of HashMap objects, where each map represents a single CSV record.
 */
public class CsvAdapter implements SourceAdapter {

    /**
     * The {@link Reader} instance used to read CSV data.
     */
    private final Reader reader;

    /**
     * Constructs a new {@code CsvAdapter} with the specified {@link Reader}.
     *
     * @param reader the {@code Reader} instance to read CSV data from
     */
    public CsvAdapter(Reader reader) {
        this.reader = reader;
    }

    /**
     * Reads all records from the CSV data.
     *
     * @param header a flag indicating if the first line should be treated as headers
     * @return an {@link Iterator} of {@code HashMap<String, Object>} where each map represents a CSV record
     */
    @Override
    public Iterator<HashMap<String, Object>> readAll(boolean header) {
        List<String> headers = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(reader);

            if (header && scanner.hasNextLine()) {
                headers = parseLine(scanner.nextLine());
            }

            if (!header && scanner.hasNextLine()) {
                List<String> firstLine = parseLine(scanner.nextLine());
                for (int i = 0; i < firstLine.size(); i++) {
                    headers.add(String.valueOf(i + 1));
                }
                return new CsvIterator(scanner, headers, firstLine);
            }

            return new CsvIterator(scanner, headers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyIterator();
    }

    /**
     * An iterator for traversing CSV records.
     */
    private class CsvIterator implements Iterator<HashMap<String, Object>> {

        /**
         * The scanner used to read the CSV data.
         */
        private final Scanner scanner;

        /**
         * The list of headers for the CSV records.
         */
        private final List<String> headers;

        /**
         * The first line of the CSV data, if available.
         */
        private List<String> firstLine;

        /**
         * Constructs a new {@code CsvIterator} with the given scanner and headers.
         *
         * @param scanner the {@link Scanner} used to read the CSV data
         * @param headers the list of headers for the CSV records
         */
        public CsvIterator(Scanner scanner, List<String> headers) {
            this.scanner = scanner;
            this.headers = headers;
            this.firstLine = null;
        }

        /**
         * Constructs a new {@code CsvIterator} with the given scanner, headers, and first line.
         *
         * @param scanner the {@link Scanner} used to read the CSV data
         * @param headers the list of headers for the CSV records
         * @param firstLine the first line of the CSV data
         */
        public CsvIterator(Scanner scanner, List<String> headers, List<String> firstLine) {
            this.scanner = scanner;
            this.headers = headers;
            this.firstLine = firstLine;
        }

        /**
         * Checks if there are more records to read from the CSV data.
         *
         * @return {@code true} if there are more records; {@code false} otherwise
         */
        @Override
        public boolean hasNext() {
            return firstLine != null || scanner.hasNextLine();
        }

        /**
         * Reads the next record from the CSV data.
         *
         * @return a {@code HashMap<String, Object>} representing the next CSV record
         */
        @Override
        public HashMap<String, Object> next() {
            List<String> values;

            if (firstLine != null) {
                values = new ArrayList<>(firstLine);
                firstLine = null;
            } else {
                String line;
                do {
                    if (!scanner.hasNextLine()) return new HashMap<>();
                    line = scanner.nextLine().trim();
                } while (line.isEmpty());

                values = parseLine(line);
            }

            return parseLineToMap(values, headers);
        }
    }

    /**
     * Parses a single line of CSV data into a list of values.
     *
     * @param line the line to parse
     * @return a list of values extracted from the line
     */
    private List<String> parseLine(String line) {
        List<String> values = new ArrayList<>();
        Matcher matcher = Pattern.compile("([^\",]+)|\"([^\"]*)\"").matcher(line);
        while (matcher.find()) {
            values.add(matcher.group(2) != null ? matcher.group(2) : matcher.group(1));
        }
        return values.stream().map(String::trim).collect(Collectors.toList());
    }

    /**
     * Converts a list of values into a {@code HashMap<String, Object>} using the given headers.
     *
     * @param values the list of values to convert
     * @param headers the list of headers for the CSV data
     * @return a {@code HashMap<String, Object>} representing the parsed CSV record
     */
    private HashMap<String, Object> parseLineToMap(List<String> values, List<String> headers) {
        HashMap<String, Object> record = new HashMap<>();
        int maxColumns = Math.max(values.size(), headers.size());
        for (int i = 0; i < maxColumns; i++) {
            String key = i < headers.size() ? headers.get(i) : String.valueOf(i + 1);
            String value = i < values.size() ? values.get(i) : null;
            if (value != null && value.contains(",")) {
                record.put(key, Arrays.stream(value.split(","))
                        .map(String::trim)
                        .collect(Collectors.toList()));
            } else {
                record.put(key, value);
            }
        }
        return record;
    }
}
