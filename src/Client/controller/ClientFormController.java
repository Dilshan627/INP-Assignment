package Client.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class ClientFormController extends Thread {
    public JFXTextField txtTyping;
    public Label lblName;
    public VBox vboxChat;
    BufferedReader reader;
    PrintWriter writer;
    Socket socket;
    public FileChooser chooser;
    public File path;


    public void initialize() {
        lblName.setText(LoginFormController.username);

        try {
            socket = new Socket("localhost", 5555);
            System.out.println("Socket is connected with server!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        try {
            while (true) {
                String massage = reader.readLine();
                String[] tokens = massage.split(" ");
                String command = tokens[0];

                StringBuilder clientMassage = new StringBuilder();
                for (int i = 1; i < tokens.length; i++) {
                    clientMassage.append(tokens[i] + " ");
                }

                String[] massageAr = massage.split(" ");
                String string = "";
                for (int i = 0; i < massageAr.length - 1; i++) {
                    string += massageAr[i + 1] + " ";
                }

                Text text = new Text(string);
                String fChar = "";

                if (string.length() > 3) {
                    fChar = string.substring(0, 3);
                }

                if (fChar.equalsIgnoreCase("img")) {
                    string = string.substring(3, string.length() - 1);

                    File file = new File(string);
                    Image image = new Image(file.toURI().toString());

                    ImageView imageView = new ImageView(image);

                    imageView.setFitWidth(150);
                    imageView.setFitHeight(200);

                    HBox hBox = new HBox(10);
                    hBox.setAlignment(Pos.BOTTOM_RIGHT);

                    if (!command.equalsIgnoreCase(LoginFormController.username)) {
                        vboxChat.setAlignment(Pos.TOP_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);

                        Text text1 = new Text("  " + command + " :");
                        hBox.getChildren().add(text1);
                        hBox.getChildren().add(imageView);
                    } else {
                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        hBox.getChildren().add(imageView);
                        Text text1 = new Text(": Me ");
                        hBox.getChildren().add(text1);
                    }

                    Platform.runLater(() -> vboxChat.getChildren().addAll(hBox));

                } else {
                    TextFlow tempTextFlow = new TextFlow();

                    if (!command.equalsIgnoreCase(LoginFormController.username + ":")) {
                        Text name = new Text(command + " ");
                        name.getStyleClass().add("name");
                        tempTextFlow.getChildren().add(name);
                    }

                    tempTextFlow.getChildren().add(text);
                    tempTextFlow.setMaxWidth(200);

                    TextFlow textFlow = new TextFlow(tempTextFlow);
                    HBox hBox = new HBox(12);

                    if (!command.equalsIgnoreCase(LoginFormController.username + ":")) {
                        vboxChat.setAlignment(Pos.TOP_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        hBox.getChildren().add(textFlow);
                    } else {
                        Text text1 = new Text(clientMassage + ": Me");
                        TextFlow textFlow1 = new TextFlow(text1);
                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        hBox.getChildren().add(textFlow1);
                    }
                    Platform.runLater(() -> vboxChat.getChildren().addAll(hBox));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendOnAction(ActionEvent actionEvent) {
        String massage = txtTyping.getText();
        writer.println(lblName.getText() + ": " + massage);
        txtTyping.clear();
        writer.flush();
        if (massage.equalsIgnoreCase("exit")) {
            Stage stage = (Stage) txtTyping.getScene().getWindow();
            stage.close();
        }
    }


    public void photoSendOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        chooser = new FileChooser();
        chooser.setTitle("Open Image");
        this.path = chooser.showOpenDialog(stage);
        writer.println(LoginFormController.username + " " + "img" + path.getPath());
        writer.flush();
    }
}
