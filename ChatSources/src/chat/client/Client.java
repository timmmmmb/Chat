package chat.client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class Client extends Application {

    private String userName;
    private String serverHost;
    private int serverPort;
    private Stage stage;

    public static void main(String[] args){
        launch(args);
    }

    private void startClient(){
        try{
            Socket socket = new Socket(serverHost, serverPort);
            System.out.println("Trying to connect to: "+serverHost+":"+serverPort);
            Thread.sleep(1000); // waiting for network communicating.
            ServerThread serverThread = new ServerThread(socket, userName);

            //create the gui for the chat
            Label userLabel = new Label();
            userLabel.setMinWidth(100);
            ScrollPane userPane = new ScrollPane(userLabel);
            userPane.setMinSize(100,200);
            Label chatLabel = new Label();
            chatLabel.setMinWidth(200);
            ScrollPane chatPane = new ScrollPane(chatLabel);
            chatPane.setMinSize(200,200);
            HBox chatBox = new HBox(chatPane,userPane);
            TextField input = new TextField();
            input.setMinWidth(200);
            Button sendInput = new Button("Send");
            sendInput.setOnAction(event -> {
                chatLabel.setText(chatLabel.getText()+"\n"+userName+" > "+input.getText());
                serverThread.addNextMessage(input.getText());
                input.setText("");
            });
            HBox inputBox = new HBox(input,sendInput);
            VBox chatVBox = new VBox(chatBox,inputBox);
            chatVBox.setSpacing(5);
            chatVBox.setPadding(new Insets(10, 50, 50, 50));
            Scene chatScene = new Scene(chatVBox, 400,400);
            stage.setScene(chatScene);

            Thread serverAccessThread = new Thread(serverThread);
            serverAccessThread.start();
        }catch(IOException ex){
            System.err.println("Fatal Connection error!");
            ex.printStackTrace();
        }catch(InterruptedException ex){
            System.out.println("Interrupted");
        }
    }

    @Override
    public void start(Stage primaryStage){
        //create the gui to connect to the server
        stage = primaryStage;
        VBox loginVBox = new VBox();
        loginVBox.setSpacing(5);
        loginVBox.setPadding(new Insets(10, 50, 50, 50));
        TextField nameField = new TextField("");
        Label nameLabel = new Label("Name: ");
        nameLabel.setMinWidth(100);
        HBox nameBox = new HBox(nameLabel,nameField);
        TextField ipField = new TextField("");
        Label ipLabel = new Label("IP-Adress: ");
        ipLabel.setMinWidth(100);
        HBox ipBox = new HBox(ipLabel,ipField);
        TextField portField = new TextField("");
        Label portLabel = new Label("Port: ");
        portLabel.setMinWidth(100);
        HBox portBox = new HBox(portLabel,portField);
        Button connect = new Button("Connect");
        Label error = new Label();
        error.setTextFill(Color.web("#FF0000"));
        connect.setOnAction(event -> {
            if("".equals(nameField.getText())||"".equals(ipField.getText())||"".equals(portField.getText())){
                error.setText("Please fill out all textfields");
            }else{
                this.userName = nameField.getText();
                this.serverHost = ipField.getText();
                this.serverPort = Integer.parseInt(portField.getText());
                startClient();
            }
        });
        loginVBox.getChildren().addAll(nameBox,ipBox,portBox,connect,error);
        Scene connectScene = new Scene(loginVBox,400,400);
        stage.setTitle("Chat Client");
        stage.setScene(connectScene);
        stage.show();
    }
}