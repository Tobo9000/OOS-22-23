package ui;

import bank.*;
import bank.exceptions.AccountDoesNotExistException;
import bank.exceptions.TransactionAlreadyExistException;
import bank.exceptions.TransactionDoesNotExistException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Controller für die Accountview.fxml.
 * Stellt eine Übersicht über alle Transaktionen des Kontos dar.
 * Ermöglicht das Hinzufügen und Löschen von Transaktionen.
 * @author Tobias Schnuerpel
 * @version 5.0
 */
public class AccountController implements Initializable {

    /**
     * Liste mit allen Transaktionen des Kontos.
     */
    private final ObservableList<Transaction> transactionList = FXCollections.observableArrayList();
    /**
     * Referenz auf das Bank-Objekt.
     */
    private PrivateBank bank;
    /**
     * Kontoname des Kontos, das angezeigt werden soll.
     */
    private String name;
    /**
     * Referenz auf die derzeit in der Liste ausgewählte Transaktion.
     */
    AtomicReference<Transaction> selectedTransaction = new AtomicReference<>();

    /** Text unterhalb der Transaktionsliste. Zeigt Statusmeldungen an. */
    @FXML
    public Text text;
    /** Button um Transaktionen hinzuzufügen */
    @FXML
    public MenuButton addButton;
    /** Eintrag, um Payments hinzuzufügen */
    @FXML
    public MenuItem payment;
    /** Eintrag, um Transfers (Incoming & Outgoing) hinzuzufügen */
    @FXML
    public MenuItem transfer;
    /** Elternelement der Szene, um Dialoge anzuzeigen */
    @FXML
    public Parent root;
    /** Button, um die Sortierung der Transaktionsliste anzupassen */
    @FXML
    public MenuButton optionsButton;
    @FXML
    public MenuItem allTransaction;
    @FXML
    public MenuItem ascending;
    @FXML
    public MenuItem descending;
    @FXML
    public MenuItem positive;
    @FXML
    public MenuItem negative;
    /** ListView für die Transaktionen */
    @FXML
    public ListView<Transaction> transactionsListView;
    /** Accountname (und Kontostand) oberhalb der Transaktionsliste */
    @FXML
    public Text accountName;
    /** Button, um zur Mainview zurückzukehren */
    @FXML
    public Button backButton;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTransactionListView();
        allTransaction.setOnAction(event -> updateTransactionList(bank.getTransactions(name)));
        ascending.setOnAction(event -> updateTransactionList(bank.getTransactionsSorted(name, true)));
        descending.setOnAction(event -> updateTransactionList(bank.getTransactionsSorted(name, false)));
        positive.setOnAction(event -> updateTransactionList(bank.getTransactionsByType(name, true)));
        negative.setOnAction(event -> updateTransactionList(bank.getTransactionsByType(name, false)));

        payment.setOnAction(event -> openDialogAddTransaction(payment));
        transfer.setOnAction(event -> openDialogAddTransaction(transfer));
        backButton.setOnMouseClicked(mouseEvent -> FxApplication.changeToMainView());
    }

    /**
     * Initialisiert die Detailansicht für das gewünschte Konto aus der gegebenen Bank.
     * @param bank Bank, aus der das Konto geladen werden soll.
     * @param account Konto, das angezeigt werden soll.
     */
    public void initData(PrivateBank bank, String account) {
        this.bank = bank;
        this.name = account;
        updateAccountName();
        updateTransactionList(bank.getTransactionsSorted(account, false));
    }

    /**
     * Aktualisiert die Transaktionsliste mit den gegebenen Transaktionen.
     * @param transactions Transaktionen, die angezeigt werden sollen.
     */
    private void updateTransactionList(List<Transaction> transactions) {
        transactionList.clear();
        transactionList.addAll(transactions);
    }

    /**
     * Aktualisiert die Anzeige des Kontonamens und des Kontostandes.
     */
    private void updateAccountName() {
        accountName.setText(name + " (" + bank.getAccountBalance(name) + "€)");
    }

    /**
     * Initialisiert die Transaktionsliste. Registriert einen EventHandler
     * für das Löschen einer ausgewählten Transaktion.
     */
    private void initializeTransactionListView() {
        transactionsListView.setItems(transactionList);

        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteTransaction = new MenuItem("Delete Transaction");
        contextMenu.getItems().add(deleteTransaction);
        transactionsListView.setContextMenu(contextMenu);

        transactionsListView.setOnMouseClicked(event -> {
            selectedTransaction.set(transactionsListView.getSelectionModel().getSelectedItem());
            if (selectedTransaction.get() != null)
                System.out.println("Selected " + selectedTransaction.get().toString().replace("\n", " "));
        });
        deleteTransaction.setOnAction(event -> deleteTransaction());
    }

    /**
     * Löscht die aktuell ausgewählte Transaktion.
     */
    private void deleteTransaction() {
        if (selectedTransaction.get() == null)
            return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Transaction");
        alert.setHeaderText("Are you sure you want to delete this transaction?");
        alert.setContentText(selectedTransaction.get().toString().replace("\n", " "));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                bank.removeTransaction(this.name, selectedTransaction.get());
                System.out.println("Deleted " + selectedTransaction.get().toString().replace("\n", " "));
                text.setText("Deleted " + selectedTransaction.toString().replace("\n", " "));
                updateTransactionList(bank.getTransactions(this.name));
                updateAccountName();
            } catch (AccountDoesNotExistException | TransactionDoesNotExistException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Öffnet einen Dialog zum Hinzufügen einer Transaktion.
     * Es können Payment, IncomingTransfer und OutgoingTransfer hinzugefügt werden.
     * Der Nutzer wählt zwischen Payment und Transfer aus. Durch die Eingaben Sender / Recipient bei
     * Transfers wird automatisch der richtige Typ (Incoming, Outgoing) erkannt.
     * @param menuItem MenuItem, das den Dialog geöffnet hat.
     */
    private void openDialogAddTransaction(MenuItem menuItem) {
        Dialog<Transaction> dialog = new Dialog<>();
        dialog.initOwner(root.getScene().getWindow());
        dialog.getDialogPane().setMinWidth(350);
        dialog.getDialogPane().setMinHeight(250);
        dialog.setTitle("Add Transaction");
        dialog.setHeaderText("Add a new " + menuItem.getText() + " transaction");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Label lDate = new Label("Date: ");
        TextField tDate = new TextField();
        Label lAmount = new Label("Amount: ");
        TextField tAmount = new TextField();
        Label lDescription = new Label("Description: ");
        TextField tDescription = new TextField();

        Label lIncomingInterest_OrSender;
        TextField tIncomingInterest_OrSender = new TextField();
        Label lOutgoingInterest_OrRecipient;
        TextField tOutgoingInterest_OrRecipient = new TextField();

        if (menuItem.getId().equals("payment")) {
            lIncomingInterest_OrSender = new Label("Incoming interest: ");
            lOutgoingInterest_OrRecipient = new Label("Outgoing interest: ");
        } else {
            lIncomingInterest_OrSender = new Label("Sender: ");
            lOutgoingInterest_OrRecipient = new Label("Recipient: ");
        }

        GridPane grid = new GridPane();
        grid.add(lDate, 1, 1);
        grid.add(tDate, 2, 1);
        grid.add(lAmount, 1, 2);
        grid.add(tAmount, 2, 2);
        grid.add(lDescription, 1, 3);
        grid.add(tDescription, 2, 3);
        grid.add(lIncomingInterest_OrSender, 1, 4);
        grid.add(tIncomingInterest_OrSender, 2, 4);
        grid.add(lOutgoingInterest_OrRecipient, 1, 5);
        grid.add(tOutgoingInterest_OrRecipient, 2, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.setResizable(true);

        Alert invalid = new Alert(Alert.AlertType.ERROR);
        invalid.setTitle("Invalid Input");

        final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            Transaction transaction;

            // get values from fields
            String date = tDate.getText();
            String amount = tAmount.getText();
            String description = tDescription.getText();
            String incomingInterest_OrSender = tIncomingInterest_OrSender.getText();
            String outgoingInterest_OrRecipient = tOutgoingInterest_OrRecipient.getText();

            double amountDouble = 0;
            double incomingInterestDouble = 0;
            double outgoingInterestDouble = 0;

            boolean valid = true;

            // --- Validation ---

            // check if any field is empty
            if (date.isEmpty() || amount.isEmpty() || description.isEmpty() ||
                    incomingInterest_OrSender.isEmpty() || outgoingInterest_OrRecipient.isEmpty()) {
                invalid.setHeaderText("Please fill in all fields.");
                valid = false;
            } else {
                // try to parse amount
                try {
                    amountDouble = Double.parseDouble(amount);
                    if (menuItem.getId().equals("transfer") && amountDouble <= 0) {
                        invalid.setHeaderText("Amount must be positive.");
                        valid = false;
                    }
                } catch (NumberFormatException e) {
                    invalid.setHeaderText("Amount must be a number.");
                    valid = false;
                }

                if (menuItem.getId().equals("payment")) {
                    // try to parse interests (only payment)
                    try {
                        incomingInterestDouble = Double.parseDouble(incomingInterest_OrSender);
                        outgoingInterestDouble = Double.parseDouble(outgoingInterest_OrRecipient);
                        if (incomingInterestDouble < 0 || outgoingInterestDouble < 0 ||
                                incomingInterestDouble > 1 || outgoingInterestDouble > 1) {
                            invalid.setHeaderText("Interests must be between 0 and 1.");
                            valid = false;
                        }
                    } catch (NumberFormatException e) {
                        invalid.setHeaderText("Incoming interest must be a number.");
                        valid = false;
                    }
                } else {
                    // check if sender and recipient are different and either of them is the account name
                    if (incomingInterest_OrSender.equals(outgoingInterest_OrRecipient) ||
                            (!incomingInterest_OrSender.equals(this.name) && !outgoingInterest_OrRecipient.equals(this.name))) {
                        invalid.setHeaderText("Sender and recipient must be different and either of them must be the this accounts name.");
                        valid = false;
                    }
                }
            }

            // --- Falls Validation erfolgreich, Objekt anlegen ---

            if (!valid) {
                invalid.showAndWait();
                event.consume();
            } else {
                // all inputs are valid (and parsed if necessary)
                if (menuItem.getId().equals("payment")) {
                    transaction = new Payment(
                            date, amountDouble, description, incomingInterestDouble, outgoingInterestDouble);
                } else {
                    // check if sender or recipient is the account name
                    if (incomingInterest_OrSender.equals(this.name)) {
                        transaction = new OutgoingTransfer(
                                date, amountDouble, description, incomingInterest_OrSender, outgoingInterest_OrRecipient);
                    } else {
                        transaction = new IncomingTransfer(
                                date, amountDouble, description, incomingInterest_OrSender, outgoingInterest_OrRecipient);
                    }
                }
                addTransaction(transaction);
            }
        });
        dialog.show();
    }

    /**
     * Fügt die gegebene Transaction dem Konto hinzu und aktualisiert die Anzeige.
     * @param transaction Transaction, die hinzugefügt werden soll.
     */
    private void addTransaction(Transaction transaction) {
        try {
            bank.addTransaction(name, transaction);
            updateTransactionList(bank.getTransactions(name));
            updateAccountName();
        } catch(TransactionAlreadyExistException | AccountDoesNotExistException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Transaction could not be added. Please try again.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
