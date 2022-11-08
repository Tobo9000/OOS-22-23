package bank;

import bank.exceptions.*;

import java.util.*;

/**
 * Stellt eine private Bank dar, welche Konten verwaltet.
 * Implementiert das Interface Bank.
 * @author Tobias Schnuerpel
 * @version 3.0
 */
public class PrivateBank implements Bank {

    /** Name der Bank */
    private String name;
    /** Zinsen, die bei einer Einzahlung (Deposit) für diese Bank anfallen, in Prozent (0.0 - 1.0) */
    private double incomingInterest = 0;
    /** Zinsen, die bei einer Auszahlung (Withdrawal) für diese Bank anfallen, in Prozent (0.0 - 1.0) */
    private double outgoingInterest = 0;
    /**
     * Liste aller Konten mit beliebig vielen zugehörigen Transaktionen dieser Bank.
     * Der Schlüssel ist der Name des Kontos, der Wert ist eine Liste aller Transaktionen dieses Kontos.
     */
    private final Map<String, List<Transaction>> accountsToTransactions = new HashMap<>();

    //------------------------------------------------------------------------------------------------------------------
    // Konstruktoren
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Konstruktor der Klasse PrivateBank. Erstellt eine neue Bank.
     * Die Zinsen sind standardmaessig auf 0 gesetzt.
     * @param name Name der Bank
     */
    public PrivateBank(String name, double incomingInterest, double outgoingInterest) {
        setName(name);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
    }

    /**
     * Copy-Konstruktor der Klasse PrivateBank. Erstellt eine neue Bank,
     * die die Werte der uebergebenen Bank uebernimmt.
     * Die Liste der Konten wird nicht kopiert.
     * @param bank Bank, die kopiert werden soll
     */
    public PrivateBank(PrivateBank bank) {
        this(
                bank.getName(),
                bank.getIncomingInterest(),
                bank.getOutgoingInterest());
    }

    //------------------------------------------------------------------------------------------------------------------
    // Methoden
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Gibt einen String mit allen Attribute der Bank zurueck.
     * @return String mit allen Attribute der Bank
     */
    @Override
    public String toString() {
        return "PrivateBank{" +
                "name='" + name + '\'' +
                ", incomingInterest=" + incomingInterest +
                ", outgoingInterest=" + outgoingInterest +
                ", accountsToTransactions=" + accountsToTransactions +
                '}';
    }

    /**
     * Vergleicht zwei Banken miteinander.
     * Gibt true zurueck, wenn alle Attribute gleich sind, sonst false.
     * @param o Bank, mit der die Bank verglichen werden soll
     * @return true, wenn alle Attribute gleich sind, sonst false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrivateBank that = (PrivateBank) o;
        if (incomingInterest != that.incomingInterest) return false;
        if (outgoingInterest != that.outgoingInterest) return false;
        if (!name.equals(that.name)) return false;
        return accountsToTransactions.equals(that.accountsToTransactions);
    }

    /**
     * Adds an account to the bank. If the account already exists, an exception is thrown.
     *
     * @param account the account to be added
     * @throws AccountAlreadyExistsException if the account already exists
     */
    @Override
    public void createAccount(String account) throws AccountAlreadyExistsException {
        if (accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException("Account already exists: " + account);
        }
        accountsToTransactions.put(account, new ArrayList<>());
    }

    /**
     * Adds an account (with all specified transactions) to the bank. If the account already exists,
     * an exception is thrown.
     *
     * @param account      the account to be added
     * @param transactions the transactions to be added to the account
     * @throws AccountAlreadyExistsException    if the account already exists
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
     */
    @Override
    public void createAccount(String account, List<Transaction> transactions) throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException {
        if (accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException("Account already exists: " + account);
        }
        accountsToTransactions.put(account, new ArrayList<>());
        for (Transaction transaction : transactions) {
            try {
                addTransaction(account, transaction);
            } catch (AccountDoesNotExistException e) {
                // Kann nicht auftreten, da das Konto zuvor erstellt wurde
                System.out.println("AccountDoesNotExistException: " + e.getMessage());
            }
        }
    }

    /**
     * Adds a transaction to an account. If the specified account does not exist, an exception is
     * thrown. If the transaction already exists, an exception is thrown.
     * Incoming and outgoing interests of the bank are added to the transaction.
     *
     * @param account     the account to which the transaction is added
     * @param transaction the transaction which is added to the account
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
     */
    @Override
    public void addTransaction(String account, Transaction transaction) throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException {
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("Account does not exist: " + account);
        }
        if (accountsToTransactions.get(account).contains(transaction)) {
            throw new TransactionAlreadyExistException("Transaction already exists: " + transaction);
        }
        if (transaction instanceof Payment payment) {
            // TransactionAttributeException kann hier nach oben gleitet werden
            payment.setIncomingInterest(incomingInterest);
            payment.setOutgoingInterest(outgoingInterest);
        }
        accountsToTransactions.get(account).add(transaction);
    }

    /**
     * Removes a transaction from an account. If the transaction does not exist, an exception is
     * thrown.
     *
     * @param account     the account from which the transaction is removed
     * @param transaction the transaction which is removed from the account
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionDoesNotExistException if the transaction cannot be found
     */
    @Override
    public void removeTransaction(String account, Transaction transaction) throws AccountDoesNotExistException, TransactionDoesNotExistException {
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("Account does not exist: " + account);
        }
        if (!accountsToTransactions.get(account).contains(transaction)) {
            throw new TransactionDoesNotExistException("Transaction does not exist: " + transaction);
        }
        accountsToTransactions.get(account).remove(transaction);
    }

    /**
     * Checks whether the specified transaction for a given account exists.
     *
     * @param account     the account from which the transaction is checked
     * @param transaction the transaction which is added to the account
     */
    @Override
    public boolean containsTransaction(String account, Transaction transaction) {
        return accountsToTransactions.containsKey(account) && accountsToTransactions.get(account).contains(transaction);
    }

    /**
     * Calculates and returns the current account balance.
     *
     * @param account the selected account
     * @return the current account balance
     */
    @Override
    public double getAccountBalance(String account) {
        return 0;
    }

    /**
     * Returns a list of transactions for an account.
     *
     * @param account the selected account
     * @return the list of transactions
     */
    @Override
    public List<Transaction> getTransactions(String account) {
        return accountsToTransactions.get(account);
    }

    /**
     * Returns a sorted list (-> calculated amounts) of transactions for a specific account. Sorts the list either in ascending or descending order
     * (or empty).
     *
     * @param account the selected account
     * @param asc     selects if the transaction list is sorted ascending or descending
     * @return the list of transactions
     */
    @Override
    public List<Transaction> getTransactionsSorted(String account, boolean asc) {
        List<Transaction> transactions = accountsToTransactions.get(account);
        if (transactions != null) {
            if (asc) {
                transactions.sort(Comparator.comparingDouble(Transaction::calculate));
            } else {
                transactions.sort(Comparator.comparingDouble(Transaction::calculate).reversed());
            }
        }
        return transactions;
    }

    /**
     * Returns a list of either positive or negative transactions (-> calculated amounts).
     *
     * @param account the selected account
     * @param positive selects if positive or negative transactions are listed
     * @return the list of transactions
     */
    @Override
    public List<Transaction> getTransactionsByType(String account, boolean positive) {
        List<Transaction> transactions = accountsToTransactions.get(account);
        if (transactions != null) {
            if (positive) {
                transactions.removeIf(transaction -> transaction.calculate() < 0);
            } else {
                transactions.removeIf(transaction -> transaction.calculate() >= 0);
            }
        }
        return transactions;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Getter und Setter
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Gibt den Namen der Bank zurück.
     * @return Name der Bank
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen der Bank.
     * @param name Name der Bank
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt die Zinsen zurück, die bei einer Einzahlung (Deposit) für diese Bank anfallen, in Prozent (0.0 - 1.0).
     * @return Zinsen bei Einzahlung
     */
    public double getIncomingInterest() {
        return incomingInterest;
    }

    /**
     * Setzt die Zinsen, die bei einer Einzahlung (Deposit) für diese Bank anfallen, in Prozent (0.0 - 1.0).
     * @param incomingInterest Zinsen bei Einzahlung
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
     * Gibt die Zinsen zurück, die bei einer Auszahlung (Withdrawal) für diese Bank anfallen, in Prozent (0.0 - 1.0).
     * @return Zinsen bei Auszahlung
     */
    public double getOutgoingInterest() {
        return outgoingInterest;
    }

    /**
     * Setzt die Zinsen, die bei einer Auszahlung (Withdrawal) für diese Bank anfallen, in Prozent (0.0 - 1.0).
     * @param outgoingInterest Zinsen bei Auszahlung
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
