package chat.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args){
        Socket socket;
        System.out.println("Please input username");
        Scanner scan = new Scanner(System.in);
        String name = scan.nextLine();
        scan.close();
        int portNumber = 4444;
        try{
            socket = new Socket("localhost", portNumber);
            Thread.sleep(1000);
            Thread server = new Thread(new ServerThread(socket, name));
            server.start();
        }catch(IOException e){
            System.err.println("Fatal Connection Error!");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Fatal Connection error!");
            e.printStackTrace();
        }
    }
}
