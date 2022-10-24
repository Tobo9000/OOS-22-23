package bank;

/**
 * Repraesentiert Ein- und Auszahlungen im Banksystem.
 * Der Betrag kann entsprechend positiv (Einzahlung) oder negativ (Auszahlung) sein.
 * @author Tobias Schnuerpel
 * @version 1.0
 */
public class Payment {

    private String date;                // Datum der Transaktion, Format: "DD.MM.YYYY"
    private double amount;              // Betrag der Transaktion, positiv oder negativ
    private String description;         // Beschreibung der Transaktion

    private double incomingInterest;    // Zinsen, die bei einer Einzahlung (Deposit) anfallen, in Prozent (0.0 - 1.0)
    private double outgoingInterest;    // Zinsen, die bei einer Auszahlung (Withdrawal) anfallen, in Prozent (0.0 - 1.0)

    //------------------------------------------------------------------------------------------------------------------
    // Konstruktoren
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Konstruktor der Klasse Payment. Erstellt eine neue Transaktion. Wenn {@param amount} negativ ist,
     * handelt es sich um eine Auszahlung (Withdrawal), ansonsten um eine Einzahlung (Deposit).
     * Die Zinsen sind standardmaessig auf 0 gesetzt.
     * @param date Datum der Transaktion, Format: "DD.MM.YYYY"
     * @param amount Betrag der Transaktion, positiv oder negativ
     * @param description Beschreibung der Transaktion
     */
    public Payment(String date, double amount, String description) {
        setDate(date);
        setAmount(amount);
        setDescription(description);
    }

    /**
     * Konstruktor der Klasse Payment. Erstellt eine neue Transaktion. Wenn {@param amount} negativ ist,
     * handelt es sich um eine Auszahlung (Withdrawal), ansonsten um eine Einzahlung (Deposit).
     * @param date Datum der Transaktion, Format: "DD.MM.YYYY"
     * @param amount Betrag der Transaktion, positiv oder negativ
     * @param description Beschreibung der Transaktion
     * @param incomingInterest Zinsen, die bei einer Einzahlung (Deposit) anfallen, in Prozent (0.0 - 1.0)
     * @param outgoingInterest Zinsen, die bei einer Auszahlung (Withdrawal) anfallen, in Prozent (0.0 - 1.0)
     */
    public Payment(String date, double amount, String description, double incomingInterest, double outgoingInterest) {
        this(date, amount, description);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
    }

    /**
     * Copy-Konstruktor der Klasse Payment. Erstellt eine neue Transaktion,
     * die alle Werte der uebergebenen Transaktion uebernimmt.
     * @param payment Transaktion, die kopiert werden soll
     */
    public Payment(Payment payment) {
        this(
                payment.getDate(),
                payment.getAmount(),
                payment.getDescription(),
                payment.getIncomingInterest(),
                payment.getOutgoingInterest());
    }

    //------------------------------------------------------------------------------------------------------------------
    // Methoden
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Gibt alle Attribute des Objekts auf der Konsole aus.
     * Nutzt die Methode {@link #toString()}.
     */
    public void printObject() {
        System.out.println(this);
    }

    /**
     * Gibt alle Attribute des Objekts als String zurueck.
     * @return String mit allen Attributen des Objekts
     */
    @Override
    public String toString() {
        return "Payment{" +
                "date='" + date + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", incomingInterest=" + incomingInterest +
                ", outgoingInterest=" + outgoingInterest +
                '}';
    }

    //------------------------------------------------------------------------------------------------------------------
    // Getter und Setter
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Gibt das Datum der Transaktion zurueck.
     * @return Datum der Transaktion, Format: "DD.MM.YYYY"
     */
    public String getDate() {
        return date;
    }

    /**
     * Setzt das Datum der Transaktion.
     * @param date Datum der Transaktion, Format: "DD.MM.YYYY"
     */
    public void setDate(String date) {
        // TODO: Pruefen, ob das Datum gueltig ist
        this.date = date;
    }

    /**
     * Gibt den Betrag der Transaktion zurueck.
     * @return Betrag der Transaktion, positiv oder negativ
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Setzt den Betrag der Transaktion.
     * @param amount Betrag der Transaktion, positiv oder negativ
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Gibt die Beschreibung der Transaktion zurueck.
     * @return Beschreibung der Transaktion
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setzt die Beschreibung der Transaktion.
     * @param description Beschreibung der Transaktion
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gibt die Zinsen zurueck, die bei einer Einzahlung (Deposit) anfallen.
     * @return Zinsen bei einer Einzahlung, in Prozent (0.0 - 1.0)
     */
    public double getIncomingInterest() {
        return incomingInterest;
    }

    /**
     * Setzt die Zinsen, die bei einer Einzahlung (Deposit) anfallen.
     * @param incomingInterest Zinsen bei einer Einzahlung, in Prozent (0.0 - 1.0)
     * @throws IllegalArgumentException wenn incomingInterest nicht zwischen 0 und 1
     */
    public void setIncomingInterest(double incomingInterest) throws IllegalArgumentException {
        if (incomingInterest < 0.0 || incomingInterest > 1.0) {
            System.out.println("Fehlerhafte Eingabe: " + incomingInterest + " ist keine gueltige (0.0 - 1.0) Zinssatzangabe.");
            throw new IllegalArgumentException("Zinsen muessen zwischen 0.0 und 1.0 liegen.");
        }
        this.incomingInterest = incomingInterest;
    }

    /**
     * Gibt die Zinsen zurueck, die bei einer Auszahlung (Withdrawal) anfallen.
     * @return Zinsen bei einer Auszahlung, in Prozent (0.0 - 1.0)
     */
    public double getOutgoingInterest() {
        return outgoingInterest;
    }

    /**
     * Setzt die Zinsen, die bei einer Auszahlung (Withdrawal) anfallen.
     * @param outgoingInterest Zinsen bei einer Auszahlung, in Prozent (0.0 - 1.0)
     * @throws IllegalArgumentException wenn outgoingInterest nicht zwischen 0 und 1
     */
    public void setOutgoingInterest(double outgoingInterest) throws IllegalArgumentException {
        if (outgoingInterest < 0.0 || outgoingInterest > 1.0) {
            System.out.println("Fehlerhafte Eingabe: " + outgoingInterest + " ist keine gueltige (0.0 - 1.0) Zinssatzangabe.");
            throw new IllegalArgumentException("Zinsen muessen zwischen 0.0 und 1.0 liegen.");
        }
        this.outgoingInterest = outgoingInterest;
    }
}
