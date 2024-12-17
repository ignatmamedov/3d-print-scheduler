package reader;

import models.Print;
import models.PrinterFactory;
import models.Spool;
import nl.saxion.Models.Printer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Function;

public class Mapper {
    ISourceAdapter sourceAdapter;
    public Mapper(ISourceAdapter sourceAdapter){
        this.sourceAdapter = sourceAdapter;
    }

    public <T> Iterator<T> readAll(Function<HashMap<String, Object>, T> mapper) {
        Iterator<HashMap<String, Object>> iterator = sourceAdapter.readAll();

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

    public Iterator<Print> readPrints() {
        return readAll(Print::fromMap);
    }

    public Iterator<Spool> readSpools() {
        return readAll(Spool::fromMap);
    }

    public Iterator<Printer> readPrinters() {
        return readAll(PrinterFactory::fromMap);
    }
}

