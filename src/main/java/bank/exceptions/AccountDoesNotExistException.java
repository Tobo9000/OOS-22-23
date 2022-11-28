package bank.exceptions;

/**
 * Wird geworfen, wenn ein Konto nicht existiert.
 * @author Tobias Schnuerpel
 * @version 3.0
 */
public class AccountDoesNotExistException extends Exception {

    /**
     * Konstruktor der Klasse AccountDoesNotExistException.
     * Ohne Fehlermeldung.
     */
    public AccountDoesNotExistException() {
        super();
    }

    /**
     * Konstruktor der Klasse AccountDoesNotExistException.
     * @param message Fehlermeldung
     */
    public AccountDoesNotExistException(String message) {
        super(message);
    }

    /**
     * Konstruktor der Klasse AccountDoesNotExistException.
     * @param message Fehlermeldung
     * @param cause Ursache
     */
    public AccountDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
