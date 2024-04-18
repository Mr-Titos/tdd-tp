package tdd.exo1.exception;

public class ISBNFormatException extends Exception {
    private String message;

    public ISBNFormatException(String msg) {
        this.message = msg;
    }


    @Override
    public String getMessage() {
        return message;
    }
}
