package tdd.tp.exception;

public class BookFormatException extends Exception {
    private final String message;

    public BookFormatException(String msg) {
        this.message = msg;
    }


    @Override
    public String getMessage() {
        return message;
    }
}
