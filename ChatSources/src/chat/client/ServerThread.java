package chat.client;

import java.net.Socket;

public class ServerThread implements Runnable {
    private Socket socket;
    public ServerThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){

    }
}
