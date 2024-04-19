package tdd.tp.exception;

public class ObjectNotFoundException extends Exception {
    private final String message;

    public ObjectNotFoundException(String msg) {
        this.message = msg;
    }


    @Override
    public String getMessage() {
        return message;
    }
}
