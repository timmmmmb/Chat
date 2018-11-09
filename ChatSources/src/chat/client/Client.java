package chat.client;

import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args){
        Socket socket = null;
        int portNumber = 4444;
        try{
            socket = new Socket("localhost", portNumber);
            Thread.sleep(1000);
            Thread server = new Thread(new ServerThread(socket));
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
