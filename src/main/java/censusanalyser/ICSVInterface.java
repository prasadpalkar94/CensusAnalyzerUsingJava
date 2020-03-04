package censusanalyser;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public interface ICSVInterface<E> {
    public <E> Iterator<E> getStateCSVIterator(Reader reader, Class csvClass) throws CSVBuilderException;
    public <E> List<E> getCSVFileList(Reader reader, Class csvClass) throws CSVBuilderException;
}
