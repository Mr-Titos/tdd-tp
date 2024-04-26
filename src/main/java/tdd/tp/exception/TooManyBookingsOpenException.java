package tdd.tp.exception;

public class TooManyBookingsOpenException extends Exception {
    private final String message;

    public TooManyBookingsOpenException(String msg) {
        this.message = msg;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
