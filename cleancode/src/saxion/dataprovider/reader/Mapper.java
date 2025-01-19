package saxion.dataprovider.reader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Function;

/**
 * A utility class that provides mapping functionality to convert data from a {@link SourceAdapter}
 * into custom objects using a provided mapping function.
 */
public class Mapper {

    /**
     * The {@link SourceAdapter} instance used to read data.
     */
    private final SourceAdapter sourceAdapter;

    /**
     * Constructs a new {@code Mapper} with the specified {@link SourceAdapter}.
     *
     * @param sourceAdapter the {@code SourceAdapter} to use for reading data
     */
    public Mapper(SourceAdapter sourceAdapter) {
        this.sourceAdapter = sourceAdapter;
    }

    /**
     * Reads all records from the source adapter and maps them to custom objects using the specified mapper function.
     *
     * @param <T>    the type of objects to map to
     * @param mapper the mapping function to convert a {@code HashMap<String, Object>} to an object of type {@code T}
     * @param header a flag indicating if the first record should be treated as a header
     * @return an {@link Iterator} of mapped objects of type {@code T}
     */
    public <T> Iterator<T> readAll(Function<HashMap<String, Object>, T> mapper, boolean header) {
        Iterator<HashMap<String, Object>> iterator = sourceAdapter.readAll(header);

        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return mapper.apply(iterator.next());
            }
        };
    }
}
