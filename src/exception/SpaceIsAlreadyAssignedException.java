package exception;

public class SpaceIsAlreadyAssignedException extends Exception{

    public SpaceIsAlreadyAssignedException() { }

    public SpaceIsAlreadyAssignedException(String message) {
        super(message);
    }
}
