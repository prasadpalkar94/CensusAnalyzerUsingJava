package censusanalyser;

import java.util.Map;

public class CensusAdapterFactory extends CensusAdapter {
    public static Map<String, CensusCSVDTO> getCensusAdapter(CensusAnalyser.Country country, String... csvFilePath){
        if(country.equals(CensusAnalyser.Country.INDIA))
            return new IndianCensusAdapter().loadCensusData(csvFilePath);
        if(country.equals(CensusAnalyser.Country.US))
            return new USCensusAdapter().loadCensusData(csvFilePath);
        else
            throw new CensusAnalyserException("INVALID_COUNTRY",
                    CensusAnalyserException.ExceptionType.INVALID_COUNTRY);

    }

    @Override
    public Map<String, CensusCSVDTO> loadCensusData(String[] csvFilePath) {
        return null;
    }
}
