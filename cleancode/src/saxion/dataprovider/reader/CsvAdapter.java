package saxion.dataprovider.reader;

import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CsvAdapter implements SourceAdapter {
    private final Reader reader;

    public CsvAdapter(Reader reader) {
        this.reader = reader;
    }

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

    private class CsvIterator implements Iterator<HashMap<String, Object>> {
        private final Scanner scanner;
        private final List<String> headers;
        private List<String> firstLine;

        public CsvIterator(Scanner scanner, List<String> headers) {
            this.scanner = scanner;
            this.headers = headers;
            this.firstLine = null;
        }

        public CsvIterator(Scanner scanner, List<String> headers, List<String> firstLine) {
            this.scanner = scanner;
            this.headers = headers;
            this.firstLine = firstLine;
        }

        @Override
        public boolean hasNext() {
            return firstLine != null || scanner.hasNextLine();
        }

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

    private List<String> parseLine(String line) {
        List<String> values = new ArrayList<>();
        Matcher matcher = Pattern.compile("([^\",]+)|\"([^\"]*)\"").matcher(line);
        while (matcher.find()) {
            values.add(matcher.group(2) != null ? matcher.group(2) : matcher.group(1));
        }
        return values.stream().map(String::trim).collect(Collectors.toList());
    }

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
