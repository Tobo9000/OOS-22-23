package bank;

import bank.exceptions.*;

import java.util.*;

/**
 * Stellt eine private Bank dar, welche Konten verwaltet.
 * Implementiert das Interface Bank.
 * @author Tobias Schnuerpel
 * @version 4.0
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
    private Map<String, List<Transaction>> accountsToTransactions = new HashMap<>();

    /** Speicherort (Pfad) der Konten bzw. Transaktionen des PrivateBank-Objektes */
    private String directoryName;

    //------------------------------------------------------------------------------------------------------------------
    // Konstruktoren
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Konstruktor der Klasse PrivateBank. Erstellt eine neue Bank.
     * Die Zinsen sind standardmaessig auf 0 gesetzt.
     * @param name Name der Bank
     * @param incomingInterest Zinsen, die bei einer Einzahlung (Deposit) für diese Bank anfallen, in Prozent (0.0 - 1.0)
     * @param outgoingInterest Zinsen, die bei einer Auszahlung (Withdrawal) für diese Bank anfallen, in Prozent (0.0 - 1.0)
     * @param directoryName Speicherort für die Konten bzw. Transaktionen des PrivateBank-Objektes
     */
    public PrivateBank(String name, double incomingInterest, double outgoingInterest, String directoryName) {
        setName(name);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
        setDirectoryName(directoryName);
        readAccounts();
    }

    /**
     * Copy-Konstruktor der Klasse PrivateBank. Erstellt eine neue Bank,
     * die die Werte der uebergebenen Bank uebernimmt.
     * Die Liste der Konten wird nicht kopiert, sondern über das Dateisystem geladen.
     * @param bank Bank, die kopiert werden soll
     */
    public PrivateBank(PrivateBank bank) {
        this(
                bank.getName(),
                bank.getIncomingInterest(),
                bank.getOutgoingInterest(),
                bank.getDirectoryName());
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
                ", directoryName='" + directoryName + '\'' +
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
        if (!directoryName.equals(that.directoryName)) return false;
        return accountsToTransactions.equals(that.accountsToTransactions);
    }

    /**
     * Adds an account to the bank.
     *
     * @param account the account to be added
     * @throws AccountAlreadyExistsException if the account already exists
     */
    @Override
    public void createAccount(String account) throws AccountAlreadyExistsException {
        if (accountsToTransactions.containsKey(account))
            throw new AccountAlreadyExistsException("Account already exists: " + account);
        accountsToTransactions.put(account, new ArrayList<>());
        writeAccount(account);
    }

    /**
     * Adds an account (with specified transactions) to the bank.
     * Important: duplicate transactions must not be added to the account!
     *
     * @param account      the account to be added
     * @param transactions a list of already existing transactions which should be added to the newly created account
     * @throws AccountAlreadyExistsException    if the account already exists
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
     */
    @Override
    public void createAccount(String account, List<Transaction> transactions)
            throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException {
        if (accountsToTransactions.containsKey(account))
            throw new AccountAlreadyExistsException("Account already exists: " + account);
        accountsToTransactions.put(account, new ArrayList<>());

        for (Transaction transaction : transactions) {
            try {
                addTransaction(account, transaction);
            } catch (AccountDoesNotExistException e) {
                // Kann nicht auftreten, da das Konto zuvor erstellt wurde
                System.out.println("Account does not exists: " + e.getMessage());
            }
        }
        writeAccount(account);
    }

    /**
     * Deletes an account from the bank.
     *
     * @param account the account to be deleted
     * @throws AccountDoesNotExistException if the account does not exist
     */
    @Override
    public void deleteAccount(String account) throws AccountDoesNotExistException {
        if (!accountsToTransactions.containsKey(account))
            throw new AccountDoesNotExistException("Account does not exist: " + account);
        accountsToTransactions.remove(account);
        deleteAccountFile(account);
    }

    /**
     * Adds a transaction to an already existing account.
     *
     * @param account     the account to which the transaction is added
     * @param transaction the transaction which should be added to the specified account
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
     */
    @Override
    public void addTransaction(String account, Transaction transaction)
            throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException {
        if (!accountsToTransactions.containsKey(account))
            throw new AccountDoesNotExistException("Account does not exist: " + account);

        if (transaction instanceof Payment payment) {
            // update interest BEFORE checking for duplicates in account list!
            // TransactionAttributeException kann hier nach oben gleitet werden
            payment.setIncomingInterest(this.getIncomingInterest());
            payment.setOutgoingInterest(this.getOutgoingInterest());
        }

        if (accountsToTransactions.get(account).contains(transaction))
            throw new TransactionAlreadyExistException("Transaction already exists: " + transaction);

        accountsToTransactions.get(account).add(transaction);
        writeAccount(account);
    }

    /**
     * Removes a transaction from an account. If the transaction does not exist, an exception is
     * thrown.
     *
     * @param account     the account from which the transaction is removed
     * @param transaction the transaction which is removed from the specified account
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionDoesNotExistException if the transaction cannot be found
     *
     */
    @Override
    public void removeTransaction(String account, Transaction transaction)
            throws AccountDoesNotExistException, TransactionDoesNotExistException {
        if (!accountsToTransactions.containsKey(account))
            throw new AccountDoesNotExistException("Account does not exist: " + account);
        if (!accountsToTransactions.get(account).contains(transaction))
            throw new TransactionDoesNotExistException("Transaction does not exist: " + transaction);
        accountsToTransactions.get(account).remove(transaction);
        writeAccount(account);
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
     * Returns the names of all accounts in the bank in a list.
     *
     * @return a list of all account names
     */
    @Override
    public List<String> getAllAccounts() {
        return new ArrayList<>(accountsToTransactions.keySet());
    }

    /**
     * Calculates and returns the current account balance.
     *
     * @param account the selected account
     * @return the current account balance
     */
    @Override
    public double getAccountBalance(String account) {
        if (!accountsToTransactions.containsKey(account))
            return 0;
        double balance = 0;
        //for (Transaction transaction : accountsToTransactions.get(account)) {
            //System.out.println(transaction);
           // balance += transaction.calculate();
        //}
        return balance;
    }

    /**
     * Returns a list of transactions for an account.
     *
     * @param account the selected account
     * @return the list of transactions
     */
    @Override
    public List<Transaction> getTransactions(String account) {
        if (!accountsToTransactions.containsKey(account))
            return new ArrayList<>();
        return accountsToTransactions.get(account);
    }

    /**
     * Returns a sorted list (-> calculated amounts) of transactions for a specific account.
     * Sorts the list either in ascending or descending order (or empty).
     *
     * @param account the selected account
     * @param asc     selects if the transaction list is sorted in ascending or descending order
     * @return the sorted list of all transactions for the specified account
     */
    @Override
    public List<Transaction> getTransactionsSorted(String account, boolean asc) {
        if (!accountsToTransactions.containsKey(account))
            return new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>(accountsToTransactions.get(account));
        if (asc) {
            transactions.sort(Comparator.comparingDouble(Transaction::calculate));
        } else {
            transactions.sort(Comparator.comparingDouble(Transaction::calculate).reversed());
        }
        return transactions;
    }

    /**
     * Returns a list of either positive or negative transactions (-> calculated amounts).
     *
     * @param account  the selected account
     * @param positive selects if positive or negative transactions are listed
     * @return the list of all transactions by type
     */
    @Override
    public List<Transaction> getTransactionsByType(String account, boolean positive) {
        if (!accountsToTransactions.containsKey(account))
            return new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>(accountsToTransactions.get(account));
        if (positive)
            transactions.removeIf(transaction -> transaction.calculate() < 0);
        else
            transactions.removeIf(transaction -> transaction.calculate() >= 0);
        return transactions;
    }

    //--------------------- Methoden für Serialisierung ---------------------

    /**
     * Serialisiert den übergebenen Account und speichert ihn in einer Datei.
     * @param account der zu serialisierende Account
     */
    private void writeAccount(String account) {
        boolean result = BankFileHandler.writeAccount(directoryName, account, accountsToTransactions.get(account));
        if (!result) {
            System.out.println("Error - Could not write account: " + account);
        }
    }

    /**
     * Löscht die Datei des übergebenen Accounts mithilfe des BankFileHandlers.
     * @param account der Account, dessen Datei gelöscht werden soll
     */
    private void deleteAccountFile(String account) {
        boolean result = BankFileHandler.deleteAccount(directoryName, account);
        if (!result) {
            System.out.println("Error - Could not delete account: " + account);
        }
    }

    /**
     * Deserialisiert alle Accounts und speichert sie in der Map.
     */
    private void readAccounts() {
        Map<String, List<Transaction>> result = BankFileHandler.readAccounts(directoryName);
        if (!result.isEmpty())
            accountsToTransactions = result;
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
     * @throws TransactionAttributeException wenn {@param incomingInterest} nicht zwischen 0 und 1 liegt
     */
    public void setIncomingInterest(double incomingInterest) throws TransactionAttributeException {
        if (incomingInterest < 0.0 || incomingInterest > 1.0) {
            System.out.println("Fehlerhafte Eingabe: " + incomingInterest + " ist keine gueltige (0.0 - 1.0) Zinssatzangabe.");
            throw new TransactionAttributeException("Zinsen muessen zwischen 0.0 und 1.0 liegen.");
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
     * @throws TransactionAttributeException wenn {@param outgoingInterest} nicht zwischen 0 und 1 liegt
     */
    public void setOutgoingInterest(double outgoingInterest) throws TransactionAttributeException {
        if (outgoingInterest < 0.0 || outgoingInterest > 1.0) {
            System.out.println("Fehlerhafte Eingabe: " + outgoingInterest + " ist keine gueltige (0.0 - 1.0) Zinssatzangabe.");
            throw new TransactionAttributeException("Zinsen muessen zwischen 0.0 und 1.0 liegen.");
        }
        this.outgoingInterest = outgoingInterest;
    }

    /**
     * Gibt den Speicherort für Konten und Transaktionen des Bank-Objekts zurück.
     * @return Speicherort (Pfad)
     */
    public String getDirectoryName() {
        return directoryName;
    }

    /**
     * Setzt den Speicherort für Konten und Transaktionen des Bank-Objekts.
     * @param directoryName Speicherort (Pfad)
     */
    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    /**
     * Schreibt alle existierenden Konten auf die Konsole.
     * Zum Testen der Funktionalität.
     */
    public void printAccounts() {
        for (String account : accountsToTransactions.keySet()) {
            System.out.print(account + ", ");
        }
    }
}
