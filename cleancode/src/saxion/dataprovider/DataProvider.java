package saxion.dataprovider;

import saxion.dataprovider.reader.JsonAdapter;
import saxion.dataprovider.reader.CsvAdapter;
import saxion.dataprovider.reader.Mapper;
import saxion.dataprovider.reader.SourceAdapter;
import saxion.utils.FileProvider;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import saxion.models.Print;
import saxion.models.Spool;
import saxion.printers.Printer;
import saxion.printers.PrinterFactory;

public class DataProvider {
    public final String DEFAULT_SPOOLS_FILE = "spools.json";
    public final String DEFAULT_PRINTS_FILE = "prints.json";
    public final String DEFAULT_PRINTERS_FILE = "printers.json";
    private SourceAdapter sourceAdapter;
    private final HashMap<Class<?>, Function<HashMap<String, Object>, ?>> typeMappers = new HashMap<>();

    public DataProvider() {
        registerMappers();
    }

    private void registerMappers() {
        typeMappers.put(Printer.class, PrinterFactory::fromMap);
        typeMappers.put(Print.class, Print::fromMap);
        typeMappers.put(Spool.class, Spool::fromMap);
    }

    private <T> Function<HashMap<String, Object>, T> getMapper(Class<T> type) {
        Function<HashMap<String, Object>, ?> mapper = typeMappers.get(type);
        if (mapper == null) {
            throw new IllegalArgumentException("No mapper found for type: " + type.getName());
        }
        return (Function<HashMap<String, Object>, T>) mapper;
    }

    public <T> List<T> readJson(String filename, Class<T> type) throws FileNotFoundException {
        Reader reader = FileProvider.getReaderFromResource(filename);
        sourceAdapter = new JsonAdapter(reader);
        Function<HashMap<String, Object>, T> mapper = getMapper(type);
        return this.loadData(m -> m.readEntities(mapper, true));
    }

    public <T> List<T> readCSV(String filename,  Class<T> type, boolean header) throws FileNotFoundException {
        Reader reader = FileProvider.getReaderFromResource(filename);
        sourceAdapter = new CsvAdapter(reader);
        Function<HashMap<String, Object>, T> mapper = getMapper(type);
        return this.loadData(m -> m.readEntities(mapper, header));
    }

    public <T> List<T> readFromFile(String filename, Class<T> type, boolean header) throws FileNotFoundException {
        if (filename.endsWith(".csv")) {
            return readCSV(filename, type, header);
        } else if (filename.endsWith(".json")) {
            return readJson(filename, type);
        } else {
            throw new IllegalArgumentException("Invalid file extension. Supported extensions are .csv and .json");
        }
    }

    public <T> List<T> loadData(Function<Mapper, Iterator<T>> readMethod) {
        Mapper mapper = new Mapper(sourceAdapter);
        Iterator<T> iterator = readMethod.apply(mapper);
        List<T> result = new ArrayList<>();
        while (iterator.hasNext()) {
            T item = iterator.next();
            result.add(item);
        }
        return result;
    }
}
