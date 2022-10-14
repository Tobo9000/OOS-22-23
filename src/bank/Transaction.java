package bank;

/**
 * Repraesentiert eine abstrakte Transaktion im Banksystem und definiert
 * grundlegene Eigenschaften und Methoden.
 * Die Klassen {@link Payment} und {@link Transfer} erben von dieser Klasse.
 * @author Tobias Schnuerpel
 * @version 2.0
 */
public abstract class Transaction {

    protected String date;              // Datum der Transaktion, Format: "DD.MM.YYYY"
    protected double amount;            // Betrag der Transaktion
    protected String description;       // Beschreibung der Transaktion

    //------------------------------------------------------------------------------------------------------------------
    // Konstruktoren
    //------------------------------------------------------------------------------------------------------------------

    public Transaction(String date, double amount, String description) {
        this.date = date;
        this.amount = amount;
        this.description = description;
    }


    //------------------------------------------------------------------------------------------------------------------
    // Getter und Setter
    //------------------------------------------------------------------------------------------------------------------

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
