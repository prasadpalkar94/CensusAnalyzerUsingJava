package censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        Iterator<IndiaCensusCSV> censusCSVIterator = null;
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            censusCSVIterator = getStateCSVIterator(reader,IndiaCensusCSV.class);
            Iterator<IndiaCensusCSV> finalCensusCSVIterator = censusCSVIterator;
            Iterable<IndiaCensusCSV> csvIterable=  ()-> finalCensusCSVIterator;
            int numOfEnteries = (int)StreamSupport.stream(csvIterable.spliterator(),false).count();
            return numOfEnteries;

        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (IllegalStateException e){
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        }



    }


    public int loadIndianStateCode(String indiastateCSV) throws CensusAnalyserException {

        try (Reader reader = Files.newBufferedReader(Paths.get(indiastateCSV));) {
            Iterator<IndiaStateCodeCSV> finalCensusCSVIterator = getStateCSVIterator(reader,IndiaStateCodeCSV.class);
            Iterable<IndiaStateCodeCSV> csvIterable=  ()-> finalCensusCSVIterator;
            int numOfStateCode = (int)StreamSupport.stream(csvIterable.spliterator(),false).count();
            return numOfStateCode;

        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (IllegalStateException e){
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        }

    }

    private <E>Iterator<E> getStateCSVIterator(Reader reader,Class csvClass) {
       try{ CsvToBeanBuilder<E> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
        csvToBeanBuilder.withType(csvClass);
        csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
        CsvToBean<E> csvToBean = csvToBeanBuilder.build();
         return csvToBean.iterator();

    }catch (IllegalStateException e){
           throw new CensusAnalyserException(e.getMessage(),
                   CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
       }
    }
}
