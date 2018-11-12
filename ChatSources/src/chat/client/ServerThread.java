package chat.client;

import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class ServerThread implements Runnable {
    private final Client client;
    private Socket socket;
    private String userName;
    private final LinkedList<String> messagesToSend;
    private boolean hasMessages = false;

    ServerThread(Socket socket, String userName, Client client){
        this.socket = socket;
        this.userName = userName;
        this.client = client;
        messagesToSend = new LinkedList<>();
        messagesToSend.add("joined the server");
        hasMessages = true;
    }

    void addNextMessage(String message){
        synchronized (messagesToSend){
            hasMessages = true;
            messagesToSend.push(message);
        }
    }

    @Override
    public void run(){
        System.out.println("Welcome: " + userName);
        System.out.println("Local Port:" + socket.getLocalPort());
        System.out.println("Server:" + socket.getRemoteSocketAddress());

        try{

            java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
            System.setOut(new java.io.PrintStream(out));

            PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), false);
            InputStream serverInStream = socket.getInputStream();
            Scanner serverIn = new Scanner(serverInStream);
            // BufferedReader userBr = new BufferedReader(new InputStreamReader(userInStream));
            // Scanner userIn = new Scanner(userInStream);
            String lastRead = "";
            while(!socket.isClosed()){
                if(serverInStream.available() > 0){
                    if(serverIn.hasNextLine()){
                        System.out.println(serverIn.nextLine());
                    }
                }
                if(hasMessages){
                    String nextSend;
                    synchronized(messagesToSend){
                        nextSend = messagesToSend.pop();
                        hasMessages = !messagesToSend.isEmpty();
                    }
                    serverOut.println(userName + " > " + nextSend);
                    serverOut.flush();
                }
                if(!lastRead.equals(out.toString())){
                    String output = out.toString().substring(lastRead.length());
                    Platform.runLater(() -> client.chatLabel.setText(client.chatLabel.getText()+output));
                    lastRead = out.toString();
                }
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }

    }
}