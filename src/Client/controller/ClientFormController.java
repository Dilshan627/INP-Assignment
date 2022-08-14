package Client.controller;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientFormController {
    public JFXTextArea txtMsg;
    public JFXTextField txtTyping;
    public Label lblName;
    BufferedReader reader;
    PrintWriter writer;
    Socket socket;


    public void initialize(){
        lblName.setText(LoginFormController.username);
        connectSocket();
    }

    private void connectSocket() {
        try {
            socket = new Socket("localhost", 5555);
            System.out.println("Socket is connected with server!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            try {
                while (true) {
                    String msg = reader.readLine();
                    String[] tokens = msg.split(" ");
                    String cmd = tokens[0];
                    System.out.println(cmd);
                    StringBuilder fulmsg = new StringBuilder();
                    for(int i = 1; i < tokens.length; i++) {
                        fulmsg.append(tokens[i]);
                    }
                    System.out.println(fulmsg);
                    if (cmd.equalsIgnoreCase(LoginFormController.username + ":")) {
                        continue;
                    } else if(fulmsg.toString().equalsIgnoreCase("bye")) {
                        break;
                    }
                    txtMsg.appendText(msg + "\n");
                }
                reader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }).start();
    }

    public void sendOnAction(ActionEvent actionEvent) {
        String msg = txtTyping.getText();
        writer.println(LoginFormController.username + ": " + msg);
        txtMsg.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        txtMsg.appendText("Me: " + msg + "\n");
        txtTyping.setText("");
        if(msg.equalsIgnoreCase("BYE") || (msg.equalsIgnoreCase("logout"))) {
            System.exit(0);
        }
    }
}
