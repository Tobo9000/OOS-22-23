package bank.exceptions;

/**
 * Wird geworfen, wenn ein Attribut einer Transaktion ungültig ist.
 * @author Tobias Schnuerpel
 * @version 3.0
 */
public class TransactionAttributeException extends RuntimeException {

    /**
     * Konstruktor der Klasse TransactionAttributeException.
     * Ohne Fehlermeldung.
     */
    public TransactionAttributeException() {
        super();
    }

    /**
     * Konstruktor der Klasse TransactionAttributeException.
     * @param message Fehlermeldung
     */
    public TransactionAttributeException(String message) {
        super(message);
    }

    /**
     * Konstruktor der Klasse TransactionAttributeException.
     * @param message Fehlermeldung
     * @param cause Ursache
     */
    public TransactionAttributeException(String message, Throwable cause) {
        super(message, cause);
    }

}
