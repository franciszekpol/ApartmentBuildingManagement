package exception;

public class CarCannotBeStoredInAnApartmentException extends Exception {
    public CarCannotBeStoredInAnApartmentException() {
    }

    public CarCannotBeStoredInAnApartmentException(String message) {
        super(message);
    }
}
