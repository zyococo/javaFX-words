package login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Login extends Application {

    @Override
    public void start(Stage firstStage) throws Exception {

//        Parent root = FXMLLoader.load(getClass().getResource("CommonplaceBook.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        firstStage.setTitle("Login");
        firstStage.setScene(new Scene(root));
        firstStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
