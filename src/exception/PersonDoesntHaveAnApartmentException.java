package exception;

public class PersonDoesntHaveAnApartmentException extends Exception {
    public PersonDoesntHaveAnApartmentException() { }

    public PersonDoesntHaveAnApartmentException(String message) {
        super(message);
    }
}
