package ui;

import bank.PrivateBank;
import bank.exceptions.AccountAlreadyExistsException;
import bank.exceptions.AccountDoesNotExistException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class MainController implements Initializable {

    private static final String DIRECTORY = "src/main/resources/data/sparkasse";

    private Stage stage;
    private Scene scene;
    private final ObservableList<String> accountList = FXCollections.observableArrayList();
    private final PrivateBank bank = new PrivateBank("Spardose & Co.", 0.03, 0.01, DIRECTORY);
    AtomicReference<String> selectedAccount = new AtomicReference<>();

    @FXML
    private Text text;
    @FXML
    private Button addButton;
    @FXML
    private ListView<String> accountsListView;
    @FXML
    private Parent root;

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
        //stage = (Stage) root.getScene().getWindow();
        scene = root.getScene();

        updateAccountList();
        initializeAccountListView();
        addButton.setOnMouseClicked(this::addAccountEvent);
    }

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

    private void updateAccountList() {
        accountList.clear();
        accountList.addAll(bank.getAllAccounts());
        accountList.sort(Comparator.naturalOrder());
    }

    private void onClickedAccountsListView(MouseEvent event) {
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

    private void onClickedDeleteAccount(ActionEvent event) {
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

    private void onClickedViewAccount(ActionEvent event) {
        showAccount();
    }

    private void showAccount() {
        FxApplication.changeToAccountView(bank, selectedAccount.get());
    }

    private void addAccountEvent(MouseEvent evet) {
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
