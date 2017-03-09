package exceptions;

public class NoReadingActivitiesException extends Exception {

    private final String message;

    public NoReadingActivitiesException(String message) {
        this.message = message;
    }
}
