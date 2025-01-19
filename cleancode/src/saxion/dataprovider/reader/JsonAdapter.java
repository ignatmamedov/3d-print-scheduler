package saxion.dataprovider.reader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * Adapter for reading JSON data from a given {@link Reader}.
 * This class provides functionality to parse JSON data into a collection
 * of HashMap objects, where each map represents a single JSON record.
 */
public class JsonAdapter implements SourceAdapter {

    /**
     * The {@link Reader} instance used to read JSON data.
     */
    private final Reader reader;

    /**
     * Constructs a new {@code JsonAdapter} with the specified {@link Reader}.
     *
     * @param reader the {@code Reader} instance to read JSON data from
     */
    public JsonAdapter(Reader reader) {
        this.reader = reader;
    }

    /**
     * Reads all records from the JSON data.
     *
     * @param header a flag indicating if the first record should be treated as a header
     * @return an {@link Iterator} of {@code HashMap<String, Object>} where each map represents a JSON record
     */
    @Override
    public Iterator<HashMap<String, Object>> readAll(boolean header) {
        JSONParser jsonParser = new JSONParser();

        try {
            JSONArray prints = (JSONArray) jsonParser.parse(reader);
            return new JsonArrayIterator(prints);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return Collections.emptyIterator();
    }

    /**
     * Reads a single JSON object and converts it into a {@code HashMap<String, Object>}.
     *
     * @param record the JSON object to read
     * @return a {@code HashMap<String, Object>} representing the JSON object
     */
    private HashMap<String, Object> read(JSONObject record) {
        HashMap<String, Object> resultMap = new HashMap<>();
        for (Object key : record.keySet()) {
            Object value = record.get(key);
            resultMap.put((String) key, parse(value));
        }
        return resultMap;
    }

    /**
     * Parses a value from the JSON data.
     *
     * @param value the value to parse, which could be a {@link JSONObject}, {@link JSONArray}, or a primitive type
     * @return the parsed value, converted to its corresponding Java representation
     */
    private Object parse(Object value) {
        if (value instanceof JSONObject) {
            return read((JSONObject) value);
        } else if (value instanceof JSONArray) {
            return ((JSONArray) value).stream().map(this::parse).toList();
        }
        return value;
    }

    /**
     * An iterator for traversing a {@link JSONArray}.
     */
    private class JsonArrayIterator implements Iterator<HashMap<String, Object>> {

        /**
         * The iterator for the underlying {@link JSONArray}.
         */
        private final Iterator<?> iterator;

        /**
         * Constructs a new {@code JsonArrayIterator} for the given {@link JSONArray}.
         *
         * @param jsonArray the {@code JSONArray} to iterate over
         */
        public JsonArrayIterator(JSONArray jsonArray) {
            this.iterator = jsonArray.iterator();
        }

        /**
         * Checks if there are more elements to iterate over.
         *
         * @return {@code true} if there are more elements; {@code false} otherwise
         */
        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next {@code HashMap<String, Object>} representing a JSON record
         * @throws IllegalStateException if the next element is not a {@link JSONObject}
         */
        @Override
        public HashMap<String, Object> next() {
            Object next = iterator.next();
            if (next instanceof JSONObject) {
                return read((JSONObject) next);
            }
            throw new IllegalStateException("Invalid JSON structure: expected JSONObject");
        }
    }
}
