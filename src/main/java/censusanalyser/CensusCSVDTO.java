package censusanalyser;

public class CensusCSVDTO {
    public String state;
    public int population;
    public int areaInSqKm;
    public int densityPerSqKm;
    public String stateCode;
    public double totalArea;
    public double populationDensity;
    public String stateId;

    public CensusCSVDTO(IndiaCensusCSV indiaCensusCSV) {
        state=indiaCensusCSV.state;
        population=indiaCensusCSV.population;
        areaInSqKm=indiaCensusCSV.areaInSqKm;
        densityPerSqKm=indiaCensusCSV.densityPerSqKm;
    }

    public CensusCSVDTO(USCensusCSV usCensusCSV) {
        stateId=usCensusCSV.stateId;
        state=usCensusCSV.state;
        population=usCensusCSV.population;
        totalArea=usCensusCSV.totalArea;
        populationDensity=usCensusCSV.populationDensity;
    }

}
