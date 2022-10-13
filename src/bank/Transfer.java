package bank;

/**
 * Repraesentiert Ueberweisungen im Banksystem.
 * Der Betrag kann nur positiv sein.
 * @author Tobias Schnuerpel
 * @version 1.0
 */
public class Transfer {

    private String date;                // Datum der Ueberweisung, Format: "DD.MM.YYYY"
    private double amount;              // Betrag der Ueberweisung, positiv
    private String description;         // Beschreibung der Ueberweisung

    private String sender;              // Akteur, der die Ueberweisung initiiert hat
    private String recipient;           // Akteur, der die Ueberweisung empfaengt

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
        this.date = date;
        this.description = description;
        setAmount(amount);
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
        this(date, amount, description);
        this.sender = sender;
        this.recipient = recipient;
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
     * Gibt alle Attribute der Ueberweisung auf der Konsole aus.
     * Nutzt die Methode {@link #toString()}.
     */
    public void printObject() {
        System.out.println(this);
    }

    /**
     * Gibt alle Attribute der Ueberweisung als String zurueck.
     * @return String mit allen Attributen der Ueberweisung
     */
    @Override
    public String toString() {
        return "Transfer{" +
                "date='" + date + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                '}';
    }

    //------------------------------------------------------------------------------------------------------------------
    // Getter und Setter
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Gibt das Datum der Ueberweisung zurueck.
     * @return Datum der Ueberweisung, Format: "DD.MM.YYYY"
     */
    public String getDate() {
        return date;
    }

    /**
     * Setzt das Datum der Ueberweisung.
     * @param date Datum der Ueberweisung, Format: "DD.MM.YYYY"
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gibt den Betrag der Ueberweisung zurueck.
     * @return Betrag der Ueberweisung, positiv
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Setzt den Betrag der Ueberweisung.
     * @param amount Betrag der Ueberweisung, positiv
     */
    public void setAmount(double amount) {
        if (amount <= 0) {
            System.out.println("Der Betrag einer Ueberweisung muss positiv sein, war aber " + amount + "!");
            throw new IllegalArgumentException("Der Betrag einer Ueberweisung muss positiv sein.");
        }
        this.amount = amount;
    }

    /**
     * Gibt die Beschreibung der Ueberweisung zurueck.
     * @return Beschreibung der Ueberweisung
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setzt die Beschreibung der Ueberweisung.
     * @param description Beschreibung der Ueberweisung
     */
    public void setDescription(String description) {
        this.description = description;
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
