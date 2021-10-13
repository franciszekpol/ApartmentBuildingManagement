package exception;

public class MoreThanFiveSpacesLeasedException extends Exception {
    public MoreThanFiveSpacesLeasedException() {
    }

    public MoreThanFiveSpacesLeasedException(String message) {
        super(message);
    }
}
