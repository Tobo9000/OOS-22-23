package ui;

import bank.PrivateBank;
import bank.Transaction;
import bank.exceptions.AccountDoesNotExistException;
import bank.exceptions.TransactionDoesNotExistException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class AccountController implements Initializable {

    private final ObservableList<Transaction> transactionList = FXCollections.observableArrayList();
    private PrivateBank bank;
    private String name;
    AtomicReference<Transaction> selectedTransaction = new AtomicReference<>();

    @FXML
    public Text text;
    @FXML
    public MenuButton addButton;
    @FXML
    public MenuItem payment;
    @FXML
    public MenuItem incoming;
    @FXML
    public MenuItem outgoing;
    @FXML
    public Parent root;
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
    @FXML
    public ListView<Transaction> transactionsListView;
    @FXML
    public Text accountName;
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

        payment.setOnAction(event -> openDialogAddTransaction(payment, name));
        incoming.setOnAction(event -> openDialogAddTransaction(incoming, name));
        outgoing.setOnAction(event -> openDialogAddTransaction(outgoing, name));
    }

    private void initializeTransactionListView() {
        transactionsListView.setItems(transactionList);

        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteTransaction = new MenuItem("Delete Transaction");
        contextMenu.getItems().add(deleteTransaction);
        transactionsListView.setContextMenu(contextMenu);

        transactionsListView.setOnMouseClicked(event -> {
            selectedTransaction.set(transactionsListView.getSelectionModel().getSelectedItem());
            System.out.println("Selected " + selectedTransaction.get().toString().replace("\n", " "));
        });
        deleteTransaction.setOnAction(event -> {
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
        });
    }

    private void updateTransactionList(List<Transaction> transactions) {
        transactionList.clear();
        transactionList.addAll(transactions);
    }

    private void updateAccountName() {
        accountName.setText(name + " (" + bank.getAccountBalance(name) + "€)");
    }

    public void initData(PrivateBank bank, String account) {
        this.bank = bank;
        this.name = account;
        updateAccountName();
        updateTransactionList(bank.getTransactionsSorted(account, false));
    }

    private void openDialogAddTransaction(MenuItem menuItem, String account) {
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
        Label lIncomingInterest = new Label();
        TextField tIncomingInterest = new TextField();
        Label lOutgoingInterest = new Label();
        TextField tOutgoingInterest = new TextField();

        GridPane grid = new GridPane();
        grid.add(lDate, 1, 1);
        grid.add(tDate, 2, 1);
        grid.add(lAmount, 1, 2);
        grid.add(tAmount, 2, 2);
        grid.add(lDescription, 1, 3);
        grid.add(tDescription, 2, 3);
        grid.add(lIncomingInterest, 1, 4);
        grid.add(tIncomingInterest, 2, 4);
        grid.add(lOutgoingInterest, 1, 5);
        grid.add(tOutgoingInterest, 2, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.setResizable(true);

    }
}
