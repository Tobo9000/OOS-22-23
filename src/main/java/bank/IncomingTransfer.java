package bank;

/**
 * Repraesentiert eingehende Ueberweisungen im Banksystem.
 * Erbt von der Klasse {@link Transfer}.
 * @author Tobias Schnuerpel
 * @version 3.0
 */
public class IncomingTransfer extends Transfer {

    //------------------------------------------------------------------------------------------------------------------
    // Konstruktoren
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Konstruktor der Klasse IncomingTransfer. Erstellt eine neue eingehende Ueberweisung.
     * Sender und Empfaenger sind standardmaessig auf null gesetzt.
     *
     * @param date        Datum der Ueberweisung, Format: "DD.MM.YYYY"
     * @param amount      Betrag der Ueberweisung, positiv
     * @param description Beschreibung der Ueberweisung
     */
    public IncomingTransfer(String date, double amount, String description) {
        super(date, amount, description);
    }

    /**
     * Konstruktor der Klasse IncomingTransfer. Erstellt eine neue eingehende Ueberweisung.
     *
     * @param date        Datum der Ueberweisung, Format: "DD.MM.YYYY"
     * @param amount      Betrag der Ueberweisung, positiv
     * @param description Beschreibung der Ueberweisung
     * @param sender      Akteur, der die Ueberweisung initiiert hat
     * @param recipient   Akteur, der die Ueberweisung empfaengt
     */
    public IncomingTransfer(String date, double amount, String description, String sender, String recipient) {
        super(date, amount, description, sender, recipient);
    }

    /**
     * Copy-Konstruktor der Klasse IncomingTransfer. Erstellt eine neue Ueberweisung,
     * die alle Werte der uebergebenen Ueberweisung uebernimmt.
     *
     * @param transfer Ueberweisung, deren Werte uebernommen werden sollen
     */
    public IncomingTransfer(Transfer transfer) {
        super(transfer);
    }

    /**
     * Berechnet den Betrag der eingehenden Ueberweisung.
     *
     * @return Betrag der Ueberweisung
     */
    @Override
    public double calculate() {
        return super.calculate();
    }
}
