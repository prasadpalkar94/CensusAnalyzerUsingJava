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


    public <E> Map<String, CensusCSVDTO> loadCensusData(String csvFilePath,Class <E> censusCSVClass ) {

        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
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
            //censusList = censusMap.values().stream().collect(Collectors.toList());
            return censusMap;

        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (IllegalStateException e){
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        }
    }

}
