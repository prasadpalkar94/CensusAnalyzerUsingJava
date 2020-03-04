package censusanalyser;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
//import java.util.List;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

   List<IndiaCensusCSVDTO>  censusList = null;
    //Map<String,IndiaCensusCSVDTO>
    public CensusAnalyser() {
        this.censusList = new ArrayList<IndiaCensusCSVDTO>();
    }



    Iterator<IndiaCensusCSV>  censusCSVIterator = null;
    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {

        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVInterface csvBuilder= CSVBuilderFactory.createCSVBuilder();
            censusCSVIterator=csvBuilder.getStateCSVIterator(reader,IndiaCensusCSV.class);

            while(censusCSVIterator.hasNext()){
                censusList.add(new IndiaCensusCSVDTO(censusCSVIterator.next()));
            }
            return censusList.size();

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
            if ( censusList == null || censusList.size() == 0){
                throw new CensusAnalyserException("Census_Data_Issue",
                        CensusAnalyserException.ExceptionType.CENSUS_DATA_PROBLEM);
            }
            Comparator<IndiaCensusCSVDTO> censusComparator = Comparator.comparing(census -> census.state);
            this.sort(censusComparator);
            String sortedStatecensus = new Gson().toJson(censusList);
            return sortedStatecensus;


    }

    public String getPopulationWiseSortedCensusData() throws CensusAnalyserException {
        if ( censusList == null || censusList.size() == 0){
            throw new CensusAnalyserException("Census_Data_Issue",
                    CensusAnalyserException.ExceptionType.CENSUS_DATA_PROBLEM);
        }
        Comparator<IndiaCensusCSVDTO> censusComparator = Comparator.comparing(census -> census.population);
        this.sort(censusComparator);
        String sortedPopulation = new Gson().toJson(censusList);
        return sortedPopulation;


    }

    private void sort(Comparator<IndiaCensusCSVDTO> censusComparator) {
        for(int i=0;i<censusList.size()-1;i++){
          for(int j=0;j<censusList.size()-i-1;j++){
             IndiaCensusCSVDTO census1 = censusList.get(j);
              IndiaCensusCSVDTO census2 = censusList.get(j+1);
              if(censusComparator.compare(census1,census2)>0){
                  censusList.set(j,census2);
                  censusList.set(j+1,census1);
              }
          }
        }
    }
}

