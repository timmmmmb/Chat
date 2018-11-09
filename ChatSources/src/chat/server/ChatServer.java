package chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    private static ServerSocket serverSocket = null;
    private static int portNumber = 4444;
    public static void main(String[] args){
        try{
            serverSocket = new ServerSocket(portNumber);
            acceptClients();
        }catch(IOException e){
            System.err.println("Could not listen on Port: "+portNumber);
            System.exit(0);
        }
    }

    private static void acceptClients() {
        while(true){
            try{
                Socket socket = serverSocket.accept();
            }catch(IOException e){
                System.err.println("Accept failed on port: "+portNumber);
            }
        }
    }
}
