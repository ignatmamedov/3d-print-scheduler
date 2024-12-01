package reader;

import java.util.HashMap;
import java.util.Iterator;

public interface ISourceAdapter {
    Iterator<HashMap<String, Object>> readAll();
}
