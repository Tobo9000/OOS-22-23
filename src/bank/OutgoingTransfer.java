package bank;

public class OutgoingTransfer extends Transfer {
    /**
     * Konstruktor der Klasse Transfer. Erstellt eine neue Ueberweisung.
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
     * Konstruktor der Klasse Transfer. Erstellt eine neue Ueberweisung.
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
     * Copy-Konstruktor der Klasse Transfer. Erstellt eine neue Ueberweisung,
     * die alle Werte der uebergebenen Ueberweisung uebernimmt.
     *
     * @param transfer Ueberweisung, deren Werte uebernommen werden sollen
     */
    public OutgoingTransfer(Transfer transfer) {
        super(transfer);
    }

    @Override
    public double calculate() {
        return super.calculate();
    }
}
