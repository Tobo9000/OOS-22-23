package bank;

/**
 * Repraesentiert ausgehende Ueberweisungen im Banksystem.
 * Erbt von der Klasse {@link Transfer}.
 * @author Tobias Schnuerpel
 * @version 3.0
 */
public class OutgoingTransfer extends Transfer {

    //------------------------------------------------------------------------------------------------------------------
    // Konstruktoren
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Konstruktor der Klasse OutgoingTransfer. Erstellt eine neue ausgehende Ueberweisung.
     * Sender und Empfaenger sind standardmaessig auf null gesetzt.
     *
     * @param date        Datum der Ueberweisung, Format: "DD.MM.YYYY"
     * @param amount      Betrag der Ueberweisung, positiv
     * @param description Beschreibung der Ueberweisung
     */
    public OutgoingTransfer(String date, double amount, String description) {
        super(date, amount, description);
    }

    /**
     * Konstruktor der Klasse OutgoingTransfer. Erstellt eine neue ausgehende Ueberweisung.
     *
     * @param date        Datum der Ueberweisung, Format: "DD.MM.YYYY"
     * @param amount      Betrag der Ueberweisung, positiv
     * @param description Beschreibung der Ueberweisung
     * @param sender      Akteur, der die Ueberweisung initiiert hat
     * @param recipient   Akteur, der die Ueberweisung empfaengt
     */
    public OutgoingTransfer(String date, double amount, String description, String sender, String recipient) {
        super(date, amount, description, sender, recipient);
    }

    /**
     * Copy-Konstruktor der Klasse OutgoingTransfer. Erstellt eine neue Ueberweisung,
     * die alle Werte der uebergebenen Ueberweisung uebernimmt.
     *
     * @param transfer Ueberweisung, deren Werte uebernommen werden sollen
     */
    public OutgoingTransfer(Transfer transfer) {
        super(transfer);
    }

    /**
     * Berechnet den Betrag der ausgehenden Ueberweisung.
     *
     * @return Betrag der Ueberweisung
     */
    @Override
    public double calculate() {
        return (-1 * super.calculate());
    }
}
