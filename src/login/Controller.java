package login;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Controller implements Initializable {

    @FXML
    private Button btn_login;

    @FXML
    private TextField email_up;

    @FXML
    private AnchorPane pane_login;

    @FXML
    private AnchorPane pane_signup;

    @FXML
    public TextField errorField;

    @FXML
    private PasswordField txt_password;

    @FXML
    private TextField txt_password_up;

    @FXML
    private TextField passwordTextField;

    @FXML
    private CheckBox showPassword;

    @FXML
    private TextField txt_username;

    @FXML
    private TextField txt_username_up;

    @FXML
    private ComboBox type;

    @FXML
    private ComboBox type_up;

    @FXML
    private AnchorPane rootPane;

    ObservableList<String> list = FXCollections.observableArrayList("Admin", "Client");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        type.getItems().addAll("Admin", "Client");
//        type_up.getItems().addAll("Admin", "Client");
        type.setItems(list);
        type_up.setItems(list);
    }

    @FXML
    private void clear() {
        txt_username.clear();
        txt_password.clear();
        txt_username_up.clear();
        txt_password_up.clear();
        passwordTextField.clear();
        email_up.clear();
    }

    @FXML
    private void loginpaneShow() {
        pane_login.setVisible(true);
        pane_signup.setVisible(false);
    }

    @FXML
    private void signuppaneShow() {
        pane_login.setVisible(false);
        pane_signup.setVisible(true);
    }

    Encryptor encryptor = new Encryptor();
    Connection conn = null; // JDBC connection
    ResultSet rs = null; // results after SQL execution
    PreparedStatement pst = null; // SQL statement object

    @FXML
    private void login(ActionEvent event) throws Exception {
        if (!txt_username.getText().isEmpty() && !txt_password.getText().isEmpty() && type.getValue() != null) {

            conn = mysqlconnect.ConnectDb();
            String sql = "Select * from users where username =? and password =? and type = ?";
            try {
                pst = conn.prepareStatement(sql);
                pst.setString(1, txt_username.getText());
                pst.setString(2, encryptor.encryptString(txt_password.getText()));
                pst.setString(3, type.getValue().toString());
                rs = pst.executeQuery();

                if (rs.next()) {
                    btn_login.getScene().getWindow().hide();
                    Parent root
                            = FXMLLoader.load(getClass().getResource("CommonplaceBook.fxml"));
                    Stage secondStage = new Stage();
                    secondStage.setTitle("My Word Book");
                    Scene scene = new Scene(root);
                    secondStage.setScene(scene);
                    secondStage.show();

                } else {
                    errorField.setVisible(true);
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Alert");
                    alert.setContentText("Invalid Username or Password");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warnig Alert");
                alert.setContentText("Try again :" + e);
                alert.showAndWait();
                clear();
            }

        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Alert");
            alert.setContentText("Please fill in all information");
            alert.showAndWait();
        }

    }

    //Click the checkbox then what is happening next
    @FXML
    void changeVisibility(ActionEvent event) {
        if (showPassword.isSelected()) {
            passwordTextField.setText(txt_password_up.getText());
            passwordTextField.setVisible(true);
            txt_password_up.setVisible(false);
            return;
        }
        txt_password_up.setText(passwordTextField.getText());
        txt_password_up.setVisible(true);
        passwordTextField.setVisible(false);
    }

    //Get hidden password to send it toMD5 (to make hashed password and store it in data.csv)
    private String getPassword() {
        if (passwordTextField.isVisible()) {
            return passwordTextField.getText();
        } else {
            return txt_password_up.getText();
        }
    }

    @FXML
    private void add_users(ActionEvent event) throws SQLException, NoSuchAlgorithmException {
        String password = getPassword();
        String encryptedPassword = encryptor.encryptString(password);
        if (!txt_username_up.getText().isEmpty() && !txt_password_up.getText().isEmpty() && type_up.getValue() != null && !email_up.getText().isEmpty()) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Alert");
            alert.setContentText("Are you sure to add a user?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                conn = mysqlconnect.ConnectDb();
                String sql = "insert into users (username,password,type,email) values(?,?,?,?)";
                try {
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, txt_username_up.getText());
                    pst.setString(2, encryptedPassword);
                    pst.setString(3, type_up.getValue().toString());
                    pst.setString(4, email_up.getText());
                    pst.execute();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Confirmation Alert");
                    alert.setContentText("A user created");
                    alert.showAndWait();
                    clear();
                } catch (Exception e) {
                    alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Warnig Alert");
                    alert.setContentText("Try again :" + e);
                    alert.showAndWait();
                    clear();
                }
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Alert");
            alert.setContentText("Please fill in all information");
            alert.showAndWait();
        }

    }
}
