package censusanalyser;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CensusAnalyserTest {

    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
    private static final String INDIA_STATE_CSV = "./src/test/resources/IndiaStateCode.csv" ;
    private static final String US_STATE_CSV = "./src/test/resources/USCensusData.csv" ;

    @Test
    public void givenIndianCensusCSVFileReturnsCorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfRecords = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_CSV);
            Assert.assertEquals(29,numOfRecords);
        } catch (CensusAnalyserException e) { }
    }

    @Test
    public void givenIndiaCensusData_WithWrongFile_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }
    }

    @Test
    public void givenIndianCensusData_WhenSortedOnState_ShouldReturn_Alphabetically_SortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        int csvFileSize = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_CSV);
        String sortedCensusData = censusAnalyser.getAlphabeticallySortedCensusData(SortField.STATE);
        IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
        Assert.assertEquals("Andhra Pradesh",censusCSV[0].state);
    }

    @Test
    public void givenIndianCensusData_WhenSortedOnState_ShouldReturn_Descending_SortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        int csvFileSize = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_CSV);
        String sortedCensusData = censusAnalyser.getReverseSortedCensusData(SortField.STATE);
        IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
        Assert.assertEquals("West Bengal",censusCSV[0].state);
    }


    @Test
    public void givenIndianStateCSV_ShouldReturnExactCount() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
       // censusAnalyser.loadIndiaCensusData(INDIA_STATE_CSV);
        int numofStateCode = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_CSV);
        Assert.assertEquals(29,numofStateCode);
    }

    @Test
    public void givenUSCensusData_ShouldReturnCorrectRecord () {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        int censusData = censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_STATE_CSV);
        Assert.assertEquals(51,censusData);
    }

    @Test
    public void givenIndianCensusData_WhenSortedOnPopulation_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        int csvFileSize = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_CSV);
        String sortedCensusData = censusAnalyser.getAlphabeticallySortedCensusData(SortField.POPULATION);
        IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
        Assert.assertEquals(1091014,censusCSV[1].population);
    }

    @Test
    public void givenIndianCensusData_WhenSortedOnPopulation_Density_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        int csvFileSize = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_CSV);
        String sortedCensusData = censusAnalyser.getReverseSortedCensusData(SortField.POPULATION_DENSITY);
        IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
        Assert.assertEquals(1102,censusCSV[0].densityPerSqKm);
    }

    @Test
    public void givenIndianCensusData_WhenSortedOnTotal_Area_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        int csvFileSize = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_CSV);
        String sortedCensusData = censusAnalyser.getAlphabeticallySortedCensusData(SortField.TOTAL_AREA);
        IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
        Assert.assertEquals(94163,censusCSV[0].areaInSqKm);
    }

    @Test
    public void givenUSCensusData_WhenSortedOnPopulation_Density_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        int csvFileSize = censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_STATE_CSV);
        String sortedCensusData = censusAnalyser.getReverseSortedCensusData(SortField.POPULATION_DENSITY);
        USCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
        Assert.assertEquals(3805.61,censusCSV[0].populationDensity,1);
    }

    @Test
    public void givenUSCensusData_WhenSortedOnTotal_Area_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        int csvFileSize = censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_STATE_CSV);
        String sortedCensusData = censusAnalyser.getReverseSortedCensusData(SortField.TOTAL_AREA);
        USCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
        Assert.assertEquals(1723338.01,censusCSV[0].totalArea,1);
    }

    @Test
    public void givenUSCensusData_WhenSortedOnPopulation_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        int csvFileSize = censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_STATE_CSV);
        String sortedCensusData = censusAnalyser.getReverseSortedCensusData(SortField.POPULATION);
        USCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
        Assert.assertEquals(3.7253956E7,censusCSV[0].population,1);
    }


  /*  @Test
    public void givenUSCensusData_WhenSortedOnState_ShouldReturnWrongSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_STATE_CSV);
            String sortedCensusData = censusAnalyser.getAlphabeticallySortedCensusData(SortField.STATE);
            USCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
            Assert.assertEquals("Andhra Pradesh", censusCSV[1].state);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }*/

}
