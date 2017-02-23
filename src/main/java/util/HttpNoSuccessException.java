package util;

public class HttpNoSuccessException extends Exception {

    private final int responseCode;

    public HttpNoSuccessException(String message, int responseCode) {
        super(message);
        this.responseCode = responseCode;
    }

    @Override
    public String toString() {
        return "Http call failed with status " + responseCode + ", message: " + getMessage();
    }
}
