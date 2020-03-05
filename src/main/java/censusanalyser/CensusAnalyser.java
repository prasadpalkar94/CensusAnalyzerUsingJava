package censusanalyser;

import com.google.gson.Gson;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

    public enum Country{
        INDIA,US
    }

    List<CensusCSVDTO> censusList = null;
    Map<String, CensusCSVDTO> censusMap = null;
    public CensusAnalyser() {
       // this.censusMap = new HashMap<String, CensusCSVDTO>();
        // censusList= new ArrayList<CensusCSVDTO>();
    }

    Iterator<IndiaCensusCSV>  censusCSVIterator = null;
    public int loadIndiaCensusData(Country country,String... csvFilePath) throws CensusAnalyserException {

        censusMap = new CensusLoader().loadCensusData(country,csvFilePath);
        censusList = censusMap.values().stream().collect(Collectors.toList());
        return censusMap.size();
    }

    public int loadUSCensusData(Country country,String... csvFilePath) {
        censusMap = new CensusLoader().loadCensusData(country,csvFilePath);
        censusList = censusMap.values().stream().collect(Collectors.toList());
        return censusMap.size();
    }



    private <E> int getCount(Iterator <E> iterator){
        Iterable<E> csvIterable=  ()-> iterator;
        int numOfStateCode = (int)StreamSupport.stream(csvIterable.spliterator(),false).count();
        return numOfStateCode;
    }


    public String getStateWiseSortedCensusData() throws CensusAnalyserException {
            if ( censusMap == null || censusMap.size() == 0){
                throw new CensusAnalyserException("Census_Data_Issue",
                        CensusAnalyserException.ExceptionType.CENSUS_DATA_PROBLEM);
            }
            Comparator<CensusCSVDTO> censusComparator = Comparator.comparing(census -> census.state);
            this.sort(censusComparator);
            String sortedStatecensus = new Gson().toJson(censusList);
            return sortedStatecensus;


    }

    public String getPopulationWiseSortedCensusData() throws CensusAnalyserException {
        if ( censusList == null || censusList.size() == 0){
            throw new CensusAnalyserException("Census_Data_Issue",
                    CensusAnalyserException.ExceptionType.CENSUS_DATA_PROBLEM);
        }
        Comparator<CensusCSVDTO> censusComparator = Comparator.comparing(census -> census.population);
        this.sort(censusComparator);
        String sortedPopulation = new Gson().toJson(censusList);
        return sortedPopulation;
    }

    private void sort(Comparator<CensusCSVDTO> censusComparator) {
        for(int i=0;i<censusList.size()-1;i++){
          for(int j=0;j<censusList.size()-i-1;j++){
             CensusCSVDTO census1 = censusList.get(j);
              CensusCSVDTO census2 = censusList.get(j+1);
              if(censusComparator.compare(census1,census2)>0){
                  censusList.set(j,census2);
                  censusList.set(j+1,census1);
              }
          }
        }
    }


}

