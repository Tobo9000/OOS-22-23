package ui;

import bank.PrivateBank;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class FxApplication extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Mainview.fxml"));
        primaryStage = stage;
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Banksystem");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void changeToAccountView(PrivateBank bank, String accountName) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(FxApplication.class.getResource("Accountview.fxml")));
            loader.load();

            AccountController accountController = loader.getController();
            accountController.initData(bank,
                    accountName.replace("[", "").replace("]", ""));

            FxApplication.changeScene("Accountview.fxml");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void changeScene(String fxml) {
        try {
            primaryStage.getScene().setRoot(
                    FXMLLoader.load(Objects.requireNonNull(FxApplication.class.getResource(fxml))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
