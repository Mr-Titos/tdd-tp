package tdd.tp.exception;

public class ISBNFormatException extends Exception {
    private final String message;

    public ISBNFormatException(String msg) {
        this.message = msg;
    }


    @Override
    public String getMessage() {
        return message;
    }
}
