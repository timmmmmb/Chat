package chat.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String host = "10.0.1.22";
    private static final int portNumber = 4444;

    private String userName;
    private String serverHost;
    private int serverPort;

    public static void main(String[] args){
        String readName = null;
        Scanner scan = new Scanner(System.in);
        //TODO: add a gui where you can enter your name
        System.out.println("Please input username:");
        while(readName == null || readName.trim().equals("")){
            // null, empty, whitespace(s) not allowed.
            readName = scan.nextLine();
            if(readName.trim().equals("")){
                System.out.println("Invalid. Please enter again:");
            }
        }

        Client client = new Client(readName, host, portNumber);
        client.startClient(scan);
    }

    private Client(String userName, String host, int portNumber){
        this.userName = userName;
        this.serverHost = host;
        this.serverPort = portNumber;
    }

    private void startClient(Scanner scan){
        try{
            Socket socket = new Socket(serverHost, serverPort);
            System.out.println("Trying to connect to: "+serverHost+":"+serverPort);
            Thread.sleep(1000); // waiting for network communicating.

            //TODO: switch the panel to the chat layout
            ServerThread serverThread = new ServerThread(socket, userName);
            Thread serverAccessThread = new Thread(serverThread);
            serverAccessThread.start();
            while(serverAccessThread.isAlive()){
                if(scan.hasNextLine()){
                    serverThread.addNextMessage(scan.nextLine());
                }
            }
        }catch(IOException ex){
            System.err.println("Fatal Connection error!");
            ex.printStackTrace();
        }catch(InterruptedException ex){
            System.out.println("Interrupted");
        }
    }
}