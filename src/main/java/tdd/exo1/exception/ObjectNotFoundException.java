package tdd.exo1.exception;

public class ObjectNotFoundException extends Exception {
    private String message;

    public ObjectNotFoundException(String msg) {
        this.message = msg;
    }


    @Override
    public String getMessage() {
        return message;
    }
}
