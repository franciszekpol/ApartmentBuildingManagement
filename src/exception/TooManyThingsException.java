package exception;

public class TooManyThingsException extends Exception{

    public TooManyThingsException() { }

    public TooManyThingsException(String message) {
        super(message);
    }
}
