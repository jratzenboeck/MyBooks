package exceptions;

public class BookNotSavedException extends Exception {

    private final String message;

    public BookNotSavedException(String message) {
        this.message = message;
    }
}
