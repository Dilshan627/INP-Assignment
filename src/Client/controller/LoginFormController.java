package Client.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {
    public JFXTextField txtUserName;
    public AnchorPane context;
    public static String username;

    public void loginOnAction(ActionEvent actionEvent) {
        username=txtUserName.getText();

        if (true){
            login();
        }
    }

    private void login() {
        try {
            Stage stage = (Stage) txtUserName.getScene().getWindow();
            Parent root = FXMLLoader.load(this.getClass().getResource("../view/client-form.fxml"));
            stage.setScene(new Scene(root, 330, 560));
            stage.setTitle(username + "");
            stage.setOnCloseRequest(event -> {
                System.exit(0);
            });
            stage.setResizable(false);
            stage.show();

        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
