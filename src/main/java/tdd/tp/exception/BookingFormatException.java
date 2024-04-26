package tdd.tp.exception;

public class BookingFormatException extends Exception {
    private final String message;

    public BookingFormatException(String msg) {
        this.message = msg;
    }


    @Override
    public String getMessage() {
        return message;
    }
}
