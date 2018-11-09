package chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
    private static ServerSocket serverSocket = null;
    private static int portNumber = 4444;
    protected static ArrayList<ClientThread> clients;

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
        clients = new ArrayList<ClientThread>();
        while(true){
            try{
                Socket socket = serverSocket.accept();
                ClientThread client = new ClientThread(socket);
                Thread thread = new Thread(client);
                thread.start();
                clients.add(client);
            }catch(IOException e){
                System.err.println("Accept failed on port: "+portNumber);
            }
        }
    }
}
