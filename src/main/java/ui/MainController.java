package ui;

import bank.PrivateBank;
import bank.exceptions.AccountAlreadyExistsException;
import bank.exceptions.AccountDoesNotExistException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Controller für die Mainview.fxml.
 * Stellt eine Übersicht über alle Konten der Bank dar.
 * @author Tobias Schnuerpel
 * @version 5.0
 */
public class MainController implements Initializable {

    /**
     * Verzeichnis, in dem die Kontodaten gespeichert werden sollen.
     */
    private static final String DIRECTORY = "src/main/resources/data/sparkasse";
    /**
     * Liste mit allen Konten der Bank. Wird im UI angezeigt.
     */
    private final ObservableList<String> accountList = FXCollections.observableArrayList();
    /**
     * Das Bankobjekt.
     */
    private final PrivateBank bank = new PrivateBank("Spardose & Co.", 0.03, 0.01, DIRECTORY);
    /**
     * Referenz auf den derzeit in der Liste ausgewählten Account.
     */
    AtomicReference<String> selectedAccount = new AtomicReference<>();

    /** Text unterhalb der Accountliste. Zeigt Statusmeldungen an. */
    @FXML
    private Text text;
    /** Button um Accounts hinzuzufügen */
    @FXML
    private Button addButton;
    /** ListView, zeigt die AccountListe auf der UI an */
    @FXML
    private ListView<String> accountsListView;

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
        updateAccountList();
        initializeAccountListView();
        addButton.setOnMouseClicked(this::addAccountEvent);
    }

    /**
     * Initialisiert die AccountListView.
     * Setzt die AccountListe als Items und fügt
     * Listener für das ContextMenu hinzu.
     */
    private void initializeAccountListView() {
        accountsListView.setItems(accountList);

        ContextMenu contextMenu = new ContextMenu();
        MenuItem viewAccount = new MenuItem("View Account");
        MenuItem deleteAccount = new MenuItem("Delete Account");
        contextMenu.getItems().addAll(viewAccount, deleteAccount);
        accountsListView.setContextMenu(contextMenu);

        accountsListView.setOnMouseClicked(this::onClickedAccountsListView);
        deleteAccount.setOnAction(this::onClickedDeleteAccount);
        viewAccount.setOnAction(this::onClickedViewAccount);
    }

    /**
     * Aktualisiert die AccountListe mit den aktuellen Konten der Bank.
     */
    private void updateAccountList() {
        accountList.clear();
        accountList.addAll(bank.getAllAccounts());
        accountList.sort(Comparator.naturalOrder());
    }

    /**
     * Eventhandler für das (Doppel-) klicken auf einen Account in der AccountListView.
     * Öffnet die Detailansicht für den Account.
     * @param event Das Event
     */
    private void onClickedAccountsListView(MouseEvent event) {
        if (accountsListView.getSelectionModel().getSelectedItem() == null)
            return;
        selectedAccount.set(String.valueOf(
                accountsListView.getSelectionModel().getSelectedItem()));
        String account = selectedAccount.get()
                .replace("[", "")
                .replace("]", "");
        System.out.println("Selected account: " + account);
        text.setText("Account " + selectedAccount + " is selected.");

        // on double click
        if (event.getClickCount() == 2)
            showAccount();
    }

    /**
     * Eventhandler für das klicken auf den "Delete Account" Eintrag im ContextMenu.
     * Löscht den Account aus der Bank.
     * @param event Das Event
     */
    private void onClickedDeleteAccount(ActionEvent event) {
        if (selectedAccount.get() == null)
            return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Account");
        alert.setContentText("Are you sure you want to delete this account?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    bank.deleteAccount(
                            selectedAccount.get().replace("[", "").replace("]", ""));
                    System.out.println("Account " + selectedAccount + " deleted.");
                    updateAccountList();
                    text.setText("Account " + selectedAccount + " deleted.");
                } catch(AccountDoesNotExistException e) {
                    System.out.println("Account " + selectedAccount + " does not exist.");
                    text.setText("Account " + selectedAccount + " does not exist.");
                }
            }
        });
    }

    /**
     * Eventhandler für das klicken auf den "View Account" Eintrag im ContextMenu.
     * Öffnet die Detailansicht für den Account.
     * @param event Das Event
     */
    private void onClickedViewAccount(ActionEvent event) {
        showAccount();
    }

    /**
     * Öffnet die Detailansicht für den derzeit in der Liste ausgewählten Account.
     */
    private void showAccount() {
        if (selectedAccount.get() != null)
            FxApplication.changeToAccountView(bank, selectedAccount.get());
    }

    /**
     * Eventhandler für das klicken auf den "Add Account" Button.
     * Öffnet ein Dialogfenster, in dem der Accountname eingegeben werden kann.
     * @param event Das Event
     */
    private void addAccountEvent(MouseEvent event) {
        text.setText("");
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Add Account");
        dialog.setHeaderText("Add a new account to " + bank.getName());
        dialog.getDialogPane().setMinWidth(300);

        Label lName = new Label("Name: ");
        TextField tName = new TextField();

        GridPane grid = new GridPane();
        grid.add(lName, 2, 1);
        grid.add(tName, 3, 1);
        dialog.getDialogPane().setContent(grid);
        dialog.setResizable(true);

        ButtonType buttonTypeOk = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                addAccount(tName.getText());
                return tName.getText();
            }
            return null;
        });
        dialog.show();
    }

    /**
     * Fügt einen neuen Account mit dem übergebenen Namen zur Bank hinzu.
     * @param name Der Name des neuen Accounts
     */
    private void addAccount(String name) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (name.isEmpty()) {
            alert.setContentText("Please enter a name.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                text.setText("No account added.");
            }
            return;
        }

        try {
            bank.createAccount(name);
            updateAccountList();
            text.setText("Account " + name + " was added.");
        } catch (AccountAlreadyExistsException e) {
            alert.setContentText("Account already exists.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                text.setText("Account " + name + " already exists.");
            }
            System.out.print(e.getMessage());
        }
    }
}
