package censusanalyser;


import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.StreamSupport;

public class CensusLoader {
    List<CensusCSVDTO> censusList = new ArrayList<>();
    Map<String, CensusCSVDTO> censusMap = new HashMap<>();


    private <E> Map<String, CensusCSVDTO> loadCensusData(Class <E> censusCSVClass,String... csvFilePath ) {

        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath[0]));) {
            ICSVInterface csvBuilder= CSVBuilderFactory.createCSVBuilder();
            Iterator<E> censusCSVIterator=csvBuilder.getStateCSVIterator(reader,censusCSVClass);
            Iterable<E> csvIterable = () -> censusCSVIterator;
            if(censusCSVClass.getName().equals("censusanalyser.IndiaCensusCSV")) {
                StreamSupport.stream(csvIterable.spliterator(), false)
                        .map(IndiaCensusCSV.class::cast)
                        .forEach(census -> censusMap.put(census.state, new CensusCSVDTO(census)));
            }else if(censusCSVClass.getName().equals("censusanalyser.USCensusCSV")){
                StreamSupport.stream(csvIterable.spliterator(), false)
                        .map(USCensusCSV.class::cast)
                        .forEach(census -> censusMap.put(census.state, new CensusCSVDTO(census)));
            }
            if(csvFilePath.length == 1) return censusMap;
            this.loadIndianStateCode(censusMap,csvFilePath[1]);

            return censusMap;

        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (IllegalStateException e){
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        }
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

    public Map<String, CensusCSVDTO> loadCensusData(CensusAnalyser.Country country, String[] csvFilePath) {
        if(country.equals(CensusAnalyser.Country.INDIA))
        {
            return this.loadCensusData(IndiaCensusCSV.class,csvFilePath);
        }else if(country.equals(CensusAnalyser.Country.US)){
            return this.loadCensusData(USCensusCSV.class,csvFilePath);
        }else{
            throw new CensusAnalyserException("INVALID_COUNTRY",
                    CensusAnalyserException.ExceptionType.INVALID_COUNTRY);
        }

    }
}
