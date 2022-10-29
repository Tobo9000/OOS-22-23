package bank;

/**
 * Repraesentiert Ein- und Auszahlungen im Banksystem.
 * Der Betrag kann entsprechend positiv (Einzahlung) oder negativ (Auszahlung) sein.
 * Erbt von der Klasse {@link Transaction}.
 * @author Tobias Schnuerpel
 * @version 2.0
 */
public class Payment extends Transaction {

    /** Zinsen, die bei einer Einzahlung (Deposit) anfallen, in Prozent (0.0 - 1.0) */
    private double incomingInterest = 0;
    /** Zinsen, die bei einer Auszahlung (Withdrawal) anfallen, in Prozent (0.0 - 1.0) */
    private double outgoingInterest = 0;

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
        super(date, amount, description);
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
        super(date, amount, description);
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
     * Gibt den Wert der Ein- oder Auszahlung nach
     * Abzug (Einzahlung) oder Addition (Auszahlung) der Zinsen zurueck.
     * @return Betrag der Transaktion nach Abzug der Zinsen
     */
    @Override
    public double calculate() {
        if (amount >= 0) {
            // Einzahlung
            return amount * (1 - incomingInterest);
        } else {
            // Auszahlung
            return amount * (1 + outgoingInterest);
        }
    }

    /**
     * Gibt alle Attribute des Objekts als String zurueck.
     * @return String mit allen Attributen des Objekts
     */
    @Override
    public String toString() {
        return "Payment{" +
                super.toString() +
                ", incomingInterest=" + incomingInterest + '\'' +
                ", outgoingInterest=" + outgoingInterest + '\'' +
                '}';
    }

    /**
     * Vergleicht zwei Objekte der Klasse Payment.
     * Gibt true zurueck, wenn alle Attribute gleich sind, sonst false.
     * @param o Objekt, mit dem die Transaktion verglichen werden soll
     * @return true, wenn alle Attribute gleich sind, sonst false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Payment payment = (Payment) o;

        if (payment.incomingInterest != this.incomingInterest) return false;
        return payment.outgoingInterest == this.outgoingInterest;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Getter und Setter
    //------------------------------------------------------------------------------------------------------------------

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
     * @throws IllegalArgumentException wenn {@param incomingInterest} nicht zwischen 0 und 1 liegt
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
     * @throws IllegalArgumentException wenn {@param outgoingInterest} nicht zwischen 0 und 1 liegt
     */
    public void setOutgoingInterest(double outgoingInterest) throws IllegalArgumentException {
        if (outgoingInterest < 0.0 || outgoingInterest > 1.0) {
            System.out.println("Fehlerhafte Eingabe: " + outgoingInterest + " ist keine gueltige (0.0 - 1.0) Zinssatzangabe.");
            throw new IllegalArgumentException("Zinsen muessen zwischen 0.0 und 1.0 liegen.");
        }
        this.outgoingInterest = outgoingInterest;
    }

}
