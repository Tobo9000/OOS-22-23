package bank;

import java.util.Objects;

/**
 * Repraesentiert eine abstrakte Transaktion im Banksystem und definiert
 * grundlegene Eigenschaften und Methoden.
 * Die Klassen {@link Payment} und {@link Transfer} erben von dieser Klasse.
 * @author Tobias Schnuerpel
 * @version 2.0
 */
public abstract class Transaction implements CalculateBill {

    protected String date;              // Datum der Transaktion, Format: "DD.MM.YYYY"
    protected double amount;            // Betrag der Transaktion
    protected String description;       // Beschreibung der Transaktion

    //------------------------------------------------------------------------------------------------------------------
    // Konstruktoren
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Konstruktor fuer die Klasse Transaction. Erstellt eine neue Transaktion.
     * @param date Datum der Transaktion, Format: "DD.MM.YYYY"
     * @param amount Betrag der Transaktion
     * @param description Beschreibung der Transaktion
     */
    public Transaction(String date, double amount, String description) {
        setDate(date);
        // Hinweis: setAmount() ist in Klasse Transfer ueberschrieben
        setAmount(amount);
        setDescription(description);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Methoden
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Gibt die Attribute der Transaktion als String zurueck.
     * Wird von den Klassen {@link Payment} und {@link Transfer} genutzt.
     * @return Attribute der Transaktion als String
     */
    @Override
    public String toString() {
        return  "date='" + date + '\'' +
                ", amount=" + calculate() +
                ", description='" + description + '\'';
    }

    /**
     * Vergleicht zwei Transaktionen miteinander.
     * Gibt true zurueck, wenn die Attribute der beiden Transaktionen uebereinstimmen, sonst false.
     * @param o Objekt, mit dem die Transaktion verglichen werden soll
     * @return true, wenn die Attribute der beiden Transaktionen uebereinstimmen, sonst false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;

        if (that.amount != this.amount) return false;
        if (!that.date.equals(this.date)) return false;
        return that.description.equals(this.description);
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
        // TODO: auf gueltiges Format pruefen
        this.date = date;
    }

    /**
     * Gibt den Betrag der Transaktion zurueck.
     * @return Betrag der Transaktion
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Setzt den Betrag der Transaktion.
     * Wird von der Unterklasse {@link Transfer} ueberschrieben.
     * @param amount Betrag der Transaktion
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
}
