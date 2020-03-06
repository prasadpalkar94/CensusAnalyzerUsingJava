package censusanalyser;

import java.util.HashMap;
import java.util.Map;

public class USCensusAdapter extends CensusAdapter {
    Map<String, CensusCSVDTO> censusMap = new HashMap<>();
    @Override
    public Map<String, CensusCSVDTO> loadCensusData(String[] csvFilePath) {
        censusMap = super.loadCensusData(USCensusCSV.class, csvFilePath[0]);
        return censusMap;
    }

}
