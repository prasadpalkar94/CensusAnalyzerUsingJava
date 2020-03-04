package censusanalyser;

public class CSVBuilderException extends RuntimeException {
    enum ExceptionType {
        UNABLE_TO_PARSE;
    }

    CSVBuilderException.ExceptionType type;

    public CSVBuilderException(String message, CSVBuilderException.ExceptionType type) {
        super(message);
        this.type = type;
    }

    public CSVBuilderException(String message, CSVBuilderException.ExceptionType type, Throwable cause) {
        super(message, cause);
        this.type = type;
    }
}
