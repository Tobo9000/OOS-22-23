package bank.exceptions;

/**
 * Wird geworfen, wenn eine Transaktion nicht existiert.
 * @author Tobias Schnuerpel
 * @version 3.0
 */
public class TransactionDoesNotExistException extends Exception {

    /**
     * Konstruktor der Klasse TransactionDoesNotExistException.
     * Ohne Fehlermeldung.
     */
    public TransactionDoesNotExistException() {
        super();
    }

    /**
     * Konstruktor der Klasse TransactionDoesNotExistException.
     * @param message Fehlermeldung
     */
    public TransactionDoesNotExistException(String message) {
        super(message);
    }

    /**
     * Konstruktor der Klasse TransactionDoesNotExistException.
     * @param message Fehlermeldung
     * @param cause Ursache
     */
    public TransactionDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

}
