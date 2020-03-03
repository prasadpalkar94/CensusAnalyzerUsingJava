package censusanalyser;

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
            ICSVInterface csvBuilder= CSVBuilderFactory.createCSVBuilder();
            censusCSVIterator =csvBuilder.getStateCSVIterator(reader,IndiaCensusCSV.class);
            Iterator<IndiaCensusCSV> finalCensusCSVIterator = censusCSVIterator;
            return getCount(finalCensusCSVIterator);

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
            ICSVInterface csvBuilder= CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaStateCodeCSV> finalCensusCSVIterator =csvBuilder.getStateCSVIterator(reader,IndiaStateCodeCSV.class);
            return getCount(finalCensusCSVIterator);
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (IllegalStateException e){
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        }

    }

    private <E> int getCount(Iterator <E> iterator){
        Iterable<E> csvIterable=  ()-> iterator;
        int numOfStateCode = (int)StreamSupport.stream(csvIterable.spliterator(),false).count();
        return numOfStateCode;
    }


    }

