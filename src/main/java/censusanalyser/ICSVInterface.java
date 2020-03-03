package censusanalyser;

import java.io.Reader;
import java.util.Iterator;

public interface ICSVInterface {
    public <E> Iterator<E> getStateCSVIterator(Reader reader, Class csvClass);
}
