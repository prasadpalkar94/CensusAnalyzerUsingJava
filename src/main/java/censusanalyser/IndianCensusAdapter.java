package censusanalyser;


import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.StreamSupport;

public class IndianCensusAdapter extends CensusAdapter {
    List<CensusCSVDTO> censusList = new ArrayList<>();
    Map<String, CensusCSVDTO> censusMap = new HashMap<>();

    @Override
    public Map<String, CensusCSVDTO> loadCensusData(String[] csvFilePath) {
        censusMap = super.loadCensusData(IndiaCensusCSV.class,csvFilePath[0]);
        this.loadIndianStateCode(censusMap,csvFilePath[1]);
        return censusMap;
    }


    private int loadIndianStateCode(Map<String,CensusCSVDTO> censusMap,String indiastateCSV) throws CensusAnalyserException {

        try (Reader reader = Files.newBufferedReader(Paths.get(indiastateCSV));) {
            ICSVInterface csvBuilder= CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaStateCodeCSV> finalCensusCSVIterator =csvBuilder.getStateCSVIterator(reader,IndiaStateCodeCSV.class);
            Iterable<IndiaStateCodeCSV> csvIterable = () -> finalCensusCSVIterator;
            StreamSupport.stream(csvIterable.spliterator(),false)
                    .filter(csvState -> censusMap.get(csvState.stateName) != null)
                    .forEach(census -> censusMap.get(census.stateName).stateCode = census.stateCode );
            return censusMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (IllegalStateException e){
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        }

    }




}
