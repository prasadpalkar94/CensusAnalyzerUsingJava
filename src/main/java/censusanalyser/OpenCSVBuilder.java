package censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public class OpenCSVBuilder implements ICSVInterface{
    public Iterator<ICSVInterface> getStateCSVIterator(Reader reader, Class csvClass)  {
      return  getCSVBean(reader, csvClass).iterator();
    }

    @Override
    public List<ICSVInterface> getCSVFileList(Reader reader, Class csvClass) throws CSVBuilderException {
        return getCSVBean(reader, csvClass).parse();
    }

    private CsvToBean<ICSVInterface> getCSVBean(Reader reader ,Class csvClass) throws CSVBuilderException{
        try {
            CsvToBeanBuilder<ICSVInterface> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
            csvToBeanBuilder.withType(csvClass);
            csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
            return csvToBeanBuilder.build();

        } catch (IllegalStateException e) {
            throw new CSVBuilderException(e.getMessage(),
                    CSVBuilderException.ExceptionType.UNABLE_TO_PARSE);
        }
    }

}
