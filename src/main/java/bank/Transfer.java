package bank;

import bank.exceptions.TransactionAttributeException;

/**
 * Repraesentiert Ueberweisungen im Banksystem.
 * Der Betrag kann nur positiv sein.
 * Erbt von der Klasse {@link Transaction}.
 * @author Tobias Schnuerpel
 * @version 3.0
 */
public class Transfer extends Transaction {

    /** Akteur, der die Ueberweisung initiiert hat. */
    private String sender = "";
    /** Akteur, der die Ueberweisung empfaengt. */
    private String recipient = "";

    //------------------------------------------------------------------------------------------------------------------
    // Konstruktoren
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Konstruktor der Klasse Transfer. Erstellt eine neue Ueberweisung.
     * Sender und Empfaenger sind standardmaessig auf null gesetzt.
     * @param date Datum der Ueberweisung, Format: "DD.MM.YYYY"
     * @param amount Betrag der Ueberweisung, positiv
     * @param description Beschreibung der Ueberweisung
     */
    public Transfer(String date, double amount, String description) {
        super(date, amount, description);
    }

    /**
     * Konstruktor der Klasse Transfer. Erstellt eine neue Ueberweisung.
     * @param date Datum der Ueberweisung, Format: "DD.MM.YYYY"
     * @param amount Betrag der Ueberweisung, positiv
     * @param description Beschreibung der Ueberweisung
     * @param sender Akteur, der die Ueberweisung initiiert hat
     * @param recipient Akteur, der die Ueberweisung empfaengt
     */
    public Transfer(String date, double amount, String description, String sender, String recipient) {
        super(date, amount, description);
        setSender(sender);
        setRecipient(recipient);
    }

    /**
     * Copy-Konstruktor der Klasse Transfer. Erstellt eine neue Ueberweisung,
     * die alle Werte der uebergebenen Ueberweisung uebernimmt.
     * @param transfer Ueberweisung, deren Werte uebernommen werden sollen
     */
    public Transfer(Transfer transfer) {
        this(
                transfer.getDate(),
                transfer.getAmount(),
                transfer.getDescription(),
                transfer.getSender(),
                transfer.getRecipient());
    }

    //------------------------------------------------------------------------------------------------------------------
    // Methoden
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Da bei Ueberweisungen keine Zinsen anfallen,
     * wird der Betrag unveraendert zurueckgegeben.
     * @return Betrag der Ueberweisung
     */
    @Override
    public double calculate() {
        return amount;
    }

    /**
     * Da bei Ueberweisungen (in diesem Szenario) keine
     * Zinsen anfallen, wird der Betrag unveraendert zurueckgegeben.
     */
    @Override
    public String toString() {
        return "Transfer{" +
                super.toString() +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                '}';
    }

    /**
     * Ueberprueft, ob zwei Ueberweisungen gleich sind.
     * Gibt true zurueck, wenn alle Werte gleich sind, sonst false.
     * @param o Objekt, mit dem die Transaktion verglichen werden soll
     * @return true, wenn alle Werte gleich sind, sonst false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Transfer transfer = (Transfer) o;

        if (!transfer.sender.equals(this.sender)) return false;
        return transfer.recipient.equals(this.recipient);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Getter und Setter
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Setzt den Betrag der Ueberweisung und ueberprueft, ob er positiv ist.
     * @param amount Betrag der Ueberweisung, positiv
     * @throws TransactionAttributeException wenn der Betrag negativ ist
     */
    @Override
    public void setAmount(double amount) throws TransactionAttributeException {
        if (amount <= 0) {
            System.out.println("Der Betrag einer Ueberweisung muss positiv sein, war aber " + amount + "!");
            throw new TransactionAttributeException("Der Betrag einer Ueberweisung muss positiv sein.");
        }
        super.setAmount(amount);
    }
    /**
     * Gibt den Akteur zurueck, der die Ueberweisung initiiert hat.
     * @return Akteur, der die Ueberweisung initiiert hat
     */
    public String getSender() {
        return sender;
    }

    /**
     * Setzt den Akteur, der die Ueberweisung initiiert hat.
     * @param sender Akteur, der die Ueberweisung initiiert hat
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Gibt den Akteur zurueck, der die Ueberweisung empfaengt.
     * @return Akteur, der die Ueberweisung empfaengt
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * Setzt den Akteur, der die Ueberweisung empfaengt.
     * @param recipient Akteur, der die Ueberweisung empfaengt
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

}
