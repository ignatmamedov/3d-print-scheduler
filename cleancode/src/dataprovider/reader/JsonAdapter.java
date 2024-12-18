package dataprovider.reader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class JsonAdapter implements SourceAdapter {
    private final Reader reader;

    public JsonAdapter(Reader reader) {
        this.reader = reader;
    }
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

    private HashMap<String, Object> read(JSONObject record) {
        HashMap<String, Object> resultMap = new HashMap<>();
        for (Object key : record.keySet()) {
            Object value = record.get(key);
            resultMap.put((String) key, parse(value));
        }
        return resultMap;
    }

    private Object parse(Object value) {
        if (value instanceof JSONObject) {
            return read((JSONObject) value);
        } else if (value instanceof JSONArray) {
            return ((JSONArray) value).stream().map(this::parse).toList();
        }
        return value;
    }

    private class JsonArrayIterator implements Iterator<HashMap<String, Object>> {
        private final Iterator<?> iterator;

        public JsonArrayIterator(JSONArray jsonArray) {
            this.iterator = jsonArray.iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

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
