package exceptions;

public class ReadingActivityNotSavedException extends Exception {

    private final String message;

    public ReadingActivityNotSavedException(String message) {
        this.message = message;
    }
}
