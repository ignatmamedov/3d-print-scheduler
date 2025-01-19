package saxion.dataprovider;

import saxion.dataprovider.reader.JsonAdapter;
import saxion.dataprovider.reader.CsvAdapter;
import saxion.dataprovider.reader.Mapper;
import saxion.dataprovider.reader.SourceAdapter;

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

/**
 * A class responsible for providing data from various sources (e.g., JSON, CSV)
 * and mapping it into specified object types.
 */
public class DataProvider {

    /** Default file name for spool data. */
    public final String DEFAULT_SPOOLS_FILE = "spools.json";

    /** Default file name for print data. */
    public final String DEFAULT_PRINTS_FILE = "prints.json";

    /** Default file name for printer data. */
    public final String DEFAULT_PRINTERS_FILE = "printers.json";

    /** Source adapter used to read data from a specific source format. */
    private SourceAdapter sourceAdapter;

    /**
     * A mapping of classes to functions that map raw data into corresponding objects.
     */
    private final HashMap<Class<?>, Function<HashMap<String, Object>, ?>> typeMappers = new HashMap<>();

    /**
     * Constructs a new {@code DataProvider} and initializes type mappers.
     */
    public DataProvider() {
        registerMappers();
    }

    /**
     * Registers default mappers for supported types.
     */
    private void registerMappers() {
        typeMappers.put(Printer.class, PrinterFactory::fromMap);
        typeMappers.put(Print.class, Print::fromMap);
        typeMappers.put(Spool.class, Spool::fromMap);
    }

    /**
     * Retrieves a mapper function for the specified type.
     *
     * @param <T>  the target object type
     * @param type the class of the target type
     * @return a {@link Function} that maps raw data to the specified type
     * @throws IllegalArgumentException if no mapper is registered for the given type
     */
    private <T> Function<HashMap<String, Object>, T> getMapper(Class<T> type) {
        Function<HashMap<String, Object>, ?> mapper = typeMappers.get(type);
        if (mapper == null) {
            throw new IllegalArgumentException("No mapper found for type: " + type.getName());
        }
        return (Function<HashMap<String, Object>, T>) mapper;
    }

    /**
     * Reads data from a JSON file and maps it to the specified type.
     *
     * @param <T>      the target object type
     * @param filename the name of the JSON file
     * @param type     the class of the target type
     * @return a {@link List} of objects of the specified type
     * @throws FileNotFoundException if the file cannot be found
     */
    public <T> List<T> readJson(String filename, Class<T> type) throws FileNotFoundException {
        Reader reader = FileProvider.getReaderFromResource(filename);
        sourceAdapter = new JsonAdapter(reader);
        Function<HashMap<String, Object>, T> mapper = getMapper(type);
        return this.loadData(m -> m.readAll(mapper, true));
    }

    /**
     * Reads data from a CSV file and maps it to the specified type.
     *
     * @param <T>      the target object type
     * @param filename the name of the CSV file
     * @param type     the class of the target type
     * @param header   whether the first row contains column headers
     * @return a {@link List} of objects of the specified type
     * @throws FileNotFoundException if the file cannot be found
     */
    public <T> List<T> readCSV(String filename, Class<T> type, boolean header) throws FileNotFoundException {
        Reader reader = FileProvider.getReaderFromResource(filename);
        sourceAdapter = new CsvAdapter(reader);
        Function<HashMap<String, Object>, T> mapper = getMapper(type);
        return this.loadData(m -> m.readAll(mapper, header));
    }

    /**
     * Reads data from a file (JSON or CSV) and maps it to the specified type.
     *
     * @param <T>      the target object type
     * @param filename the name of the file
     * @param type     the class of the target type
     * @param header   whether the first row contains column headers (used for CSV files)
     * @return a {@link List} of objects of the specified type
     * @throws FileNotFoundException    if the file cannot be found
     * @throws IllegalArgumentException if the file extension is not supported
     */
    public <T> List<T> readFromFile(String filename, Class<T> type, boolean header) throws FileNotFoundException {
        if (filename.isEmpty()) {
            if (type.equals(Spool.class)) {
                filename = DEFAULT_SPOOLS_FILE;
            } else if (type.equals(Print.class)) {
                filename = DEFAULT_PRINTS_FILE;
            } else if (type.equals(Printer.class)) {
                filename = DEFAULT_PRINTERS_FILE;
            } else {
                throw new IllegalArgumentException("No default file defined for the given type: " + type.getSimpleName());
            }
        }
        if (filename.endsWith(".csv")) {
            return readCSV(filename, type, header);
        } else if (filename.endsWith(".json")) {
            return readJson(filename, type);
        } else {
            throw new IllegalArgumentException("Invalid file extension. Supported extensions are .csv and .json");
        }
    }

    /**
     * Loads data using the provided read method and collects it into a list.
     *
     * @param <T>        the target object type
     * @param readMethod a function that defines how data should be read using a {@link Mapper}
     * @return a {@link List} of objects of the specified type
     */
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
