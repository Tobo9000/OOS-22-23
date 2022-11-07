package bank.exceptions;

/**
 * Wird geworfen, wenn ein Konto bereits existiert.
 * @author Tobias Schnuerpel
 * @version 3.0
 */
public class AccountAlreadyExistsException extends Exception {

    /**
     * Konstruktor der Klasse AccountAlreadyExistsException.
     * Ohne Fehlermeldung.
     */
    public AccountAlreadyExistsException() {
        super();
    }

    /**
     * Konstruktor der Klasse AccountAlreadyExistsException.
     * @param message Fehlermeldung
     */
    public AccountAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * Konstruktor der Klasse AccountAlreadyExistsException.
     * @param message Fehlermeldung
     * @param cause Ursache
     */
    public AccountAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
