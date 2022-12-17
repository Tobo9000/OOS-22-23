package ui;

import bank.PrivateBank;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Startpunkt der JavaFX Anwendung.
 * Startet das Userinterface und das Banksystem.
 * @author Tobias Schnuerpel
 * @version 5.0
 */
public class FxApplication extends Application {

    private static Stage primaryStage;

    /**
     * Startet die JavaFX Anwendung.
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Mainview.fxml"));
        primaryStage = stage;
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Banksystem");
        stage.show();
    }

    /**
     * Einstiegspunkt der gesamten Anwendung. Startet die JavaFX Anwendung.
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Wechselt die Szene zur AccountView und übergibt die für die
     * Detailansicht benötigten Daten.
     * @param bank das Bankobjekt
     * @param accountName der Name des Kontos
     */
    public static void changeToAccountView(PrivateBank bank, String accountName) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(FxApplication.class.getResource("Accountview.fxml")));
            loader.load();

            AccountController accountController = loader.getController();
            accountController.initData(bank,
                    accountName.replace("[", "").replace("]", ""));

            primaryStage.getScene().setRoot(loader.getRoot());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wechselt die Szene zurück zur Hauptansicht.
     */
    public static void changeToMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(FxApplication.class.getResource("Mainview.fxml")));
            loader.load();

            primaryStage.getScene().setRoot(loader.getRoot());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
