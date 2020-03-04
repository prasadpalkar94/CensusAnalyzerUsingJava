package censusanalyser;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    List<IndiaCensusCSV>  censusCSVList = null;
    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {

        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVInterface csvBuilder= CSVBuilderFactory.createCSVBuilder();
            censusCSVList=csvBuilder.getCSVFileList(reader,IndiaCensusCSV.class);
            return censusCSVList.size();

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


    public String getStateWiseSortedCensusData() throws CensusAnalyserException {
            if ( censusCSVList == null || censusCSVList.size() == 0){
                throw new CensusAnalyserException("Census_Data_Issue",
                        CensusAnalyserException.ExceptionType.CENSUS_DATA_PROBLEM);
            }
            Comparator<IndiaCensusCSV> censusComparator = Comparator.comparing(census -> census.state);
            this.sort(censusComparator);
            String sortedStatecensus = new Gson().toJson(censusCSVList);
            return sortedStatecensus;


    }

    private void sort(Comparator<IndiaCensusCSV> censusComparator) {
        for(int i=0;i<censusCSVList.size()-1;i++){
          for(int j=0;j<censusCSVList.size()-i-1;j++){
             IndiaCensusCSV census1 = censusCSVList.get(j);
              IndiaCensusCSV census2 = censusCSVList.get(j+1);
              if(censusComparator.compare(census1,census2)>0){
                  censusCSVList.set(j,census2);
                  censusCSVList.set(j+1,census1);
              }
          }
        }
    }
}

