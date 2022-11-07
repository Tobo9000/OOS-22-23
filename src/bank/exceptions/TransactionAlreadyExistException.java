package bank.exceptions;

/**
 * Wird geworfen, wenn eine Transaktion bereits existiert.
 * @author Tobias Schnuerpel
 * @version 3.0
 */
public class TransactionAlreadyExistException extends Exception {

    /**
     * Konstruktor der Klasse TransactionAlreadyExistException.
     * Ohne Fehlermeldung.
     */
    public TransactionAlreadyExistException() {
        super();
    }

    /**
     * Konstruktor der Klasse TransactionAlreadyExistException.
     * @param message Fehlermeldung
     */
    public TransactionAlreadyExistException(String message) {
        super(message);
    }

    /**
     * Konstruktor der Klasse TransactionAlreadyExistException.
     * @param message Fehlermeldung
     * @param cause Ursache
     */
    public TransactionAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

}
