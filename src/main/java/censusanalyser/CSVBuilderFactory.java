package censusanalyser;

public class CSVBuilderFactory {
public static ICSVInterface createCSVBuilder(){
    return new OpenCSVBuilder();
}
}
