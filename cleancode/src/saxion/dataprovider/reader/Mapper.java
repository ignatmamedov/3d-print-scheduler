package saxion.dataprovider.reader;


import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Function;

public class Mapper {
    SourceAdapter sourceAdapter;
    public Mapper(SourceAdapter sourceAdapter){
        this.sourceAdapter = sourceAdapter;
    }

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

    public <T> Iterator<T> readEntities(Function<HashMap<String, Object>, T> mapper, boolean header) {
        return readAll(mapper, header);
    }
}

