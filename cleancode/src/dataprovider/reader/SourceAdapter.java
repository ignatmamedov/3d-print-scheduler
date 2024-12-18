package dataprovider.reader;

import java.util.HashMap;
import java.util.Iterator;

public interface SourceAdapter {
    Iterator<HashMap<String, Object>> readAll(boolean header);
}
