package censusanalyser;

import com.google.gson.Gson;

import java.util.*;
import java.util.stream.Collectors;

public class CensusAnalyser {

    public enum Country {
        INDIA, US
    }

    List<CensusCSVDTO> censusList = null;
    Map<String, CensusCSVDTO> censusMap = null;
    Map<SortField, Comparator<CensusCSVDTO>> sortMap = null;

    public CensusAnalyser() {
        this.sortMap = new HashMap<>();
        this.sortMap.put(SortField.STATE, Comparator.comparing(census -> census.state));
        this.sortMap.put(SortField.POPULATION, Comparator.comparing(census -> census.population));
        this.sortMap.put(SortField.POPULATION_DENSITY, Comparator.comparing(census -> census.populationDensity));
        this.sortMap.put(SortField.TOTAL_AREA, Comparator.comparing(census -> census.totalArea));
    }

    Iterator<IndiaCensusCSV> censusCSVIterator = null;

    public int loadCensusData(Country country, String... csvFilePath) throws CensusAnalyserException {

        censusMap = new CensusAdapterFactory().getCensusAdapter(country, csvFilePath);
        censusList = censusMap.values().stream().collect(Collectors.toList());
        return censusMap.size();
    }

    public String getAlphabeticallySortedCensusData(SortField field) throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0) {
            throw new CensusAnalyserException("Census_Data_Issue",
                    CensusAnalyserException.ExceptionType.CENSUS_DATA_PROBLEM);
        }
        censusList = censusMap.values().stream().collect(Collectors.toList());
        this.sort(censusList, this.sortMap.get(field));
        String sorted = new Gson().toJson(censusList);
        return sorted;
    }

    public String getReverseSortedCensusData(SortField field) throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0) {
            throw new CensusAnalyserException("Census_Data_Issue",
                    CensusAnalyserException.ExceptionType.CENSUS_DATA_PROBLEM);
        }
        censusList = censusMap.values().stream().collect(Collectors.toList());
        this.sort(censusList, this.sortMap.get(field).reversed());
        String sorted = new Gson().toJson(censusList);
        return sorted;
    }

    private void sort(List<CensusCSVDTO> censusList, Comparator<CensusCSVDTO> censusComparator) {
        for (int i = 0; i < censusList.size() - 1; i++) {
            for (int j = 0; j < censusList.size() - i - 1; j++) {
                CensusCSVDTO census1 = censusList.get(j);
                CensusCSVDTO census2 = censusList.get(j + 1);
                if (censusComparator.compare(census1, census2) > 0) {
                    censusList.set(j, census2);
                    censusList.set(j + 1, census1);
                }
            }
        }
    }
}

