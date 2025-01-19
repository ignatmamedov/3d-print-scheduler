package saxion.dataprovider.reader;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Interface for adapting various data sources to provide a standardized way
 * to read data as an {@link Iterator} of {@code HashMap<String, Object>}.
 */
public interface SourceAdapter {

    /**
     * Reads all records from the data source.
     *
     * @param header a flag indicating if the first record should be treated as a header
     * @return an {@link Iterator} of {@code HashMap<String, Object>} where each map represents a data record
     */
    Iterator<HashMap<String, Object>> readAll(boolean header);
}
