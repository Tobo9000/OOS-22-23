package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FxApplication extends Application {

    private Parent createContent() {
        return new StackPane(new Text("Hello World!"));
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Mainview.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Banksystem");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
