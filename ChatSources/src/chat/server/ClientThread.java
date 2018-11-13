package chat.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread implements Runnable {
    private Socket socket;
    private PrintWriter clientOut;
    private ChatServer server;
    private String username = "";

    ClientThread(ChatServer server, Socket socket){
        this.server = server;
        this.socket = socket;
    }

    private PrintWriter getWriter(){
        return clientOut;
    }

    @Override
    public void run() {
        try{
            // setup
            this.clientOut = new PrintWriter(socket.getOutputStream(), false);
            Scanner in = new Scanner(socket.getInputStream());

            // start communicating
            while(!socket.isClosed()){
                if(in.hasNextLine()){
                    String input = in.nextLine();
                    if("".equals(username)&&input.contains("joined the server")){
                        username = input.substring(0,input.indexOf(">")-1);
                        StringBuilder output = new StringBuilder("All connected Users > ");
                        for(ClientThread thatClient : server.getClients()){
                            output.append(thatClient.username).append(",");
                        }
                        output = new StringBuilder(output.substring(0, output.length() - 1));
                        clientOut.write(output.toString());
                        clientOut.flush();

                    }else if(!"".equals(username)&&input.contains("left the server")){
                        String username = input.substring(0,input.indexOf(">")-1);
                        if(username.equals(this.username)){
                            try {
                                server.removeClient(this);
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    for(ClientThread thatClient : server.getClients()){
                        PrintWriter thatClientOut = thatClient.getWriter();
                        if(thatClientOut != null){
                            thatClientOut.write(input + "\r\n");
                            thatClientOut.flush();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
