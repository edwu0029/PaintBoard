import java.util.HashSet;
import java.util.ArrayList;
import java.net.*;
import java.io.*;
 
public class Server {
    private String ip;
    private ArrayList<ConnectionHandler>connections;
    private ServerSocket serverSocket;
    private ServerThread serverThread;
    private boolean running = true;

    final int PORT = 5000;
    Server() throws Exception{
        String localHost = InetAddress.getLocalHost().toString();
        ip = localHost.substring(localHost.indexOf('/')+1);
        System.out.println(ip);

        this.serverSocket = new ServerSocket(PORT);
        this.connections = new ArrayList<ConnectionHandler>();
        this.serverThread = new ServerThread();
        serverThread.start();
    }
    public String getServerIP(){
        return ip;
    }
    public void quit() throws Exception{
        System.out.println("Server quit");
        running = false;
        serverThread.interrupt();
        for(ConnectionHandler connectionHandler: connections){
            connectionHandler.quit();
            connectionHandler.interrupt();
        }
        serverSocket.close();
    }
    class ServerThread extends Thread{
        public void run(){
            while(running){
                try{
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New Connection");
                    ConnectionHandler t = new ConnectionHandler(clientSocket);
                    Thread connectionThread = new Thread(t);
                    connections.add(t);
                    connectionThread.start();
                }catch(Exception e){
                    running = false;
                }
            }
        }
    }

    class ConnectionHandler extends Thread{
        private Socket socket;
        private ObjectInputStream input;
        private ObjectOutputStream output;
        private boolean running = true;
 
        ConnectionHandler(Socket socket) throws Exception{
            this.socket = socket;
            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.input = new ObjectInputStream(socket.getInputStream());
        }
        public boolean getRunning(){
            return running;
        }
        public void quit() throws Exception{
            System.out.println("intiated server connection quit");
            running = false;
            input.close();
            output.close();
            socket.close();
        }
        public void run(){
            while(running){
                try{
                    int command = input.readInt();
                    if(command==1){
                        Stroke stroke = (Stroke)input.readObject();

                        //Update in other clients
                        for(ConnectionHandler i: connections){
                            if(i!=this&&i.getRunning()){
                                i.addStroke(stroke);
                            }
                        }
                    }else if(command==-1){
                        Stroke stroke = (Stroke)input.readObject();

                        //Update in other clients
                        for(ConnectionHandler i: connections){
                            if(i!=this&&i.getRunning()){
                                i.removeStroke(stroke);
                            }
                        }
                    }else if(command==3){
                        Text text = (Text)input.readObject();

                        //Update in other clients
                        for(ConnectionHandler i: connections){
                            if(i!=this&&i.getRunning()){
                                i.addText(text);
                            }
                        }
                    }else if(command==-3){
                        Text text = (Text)input.readObject();

                        //Update in other clients
                        for(ConnectionHandler i: connections){
                            if(i!=this&&i.getRunning()){
                                i.removeText(text);
                            }
                        }
                    } else if(command==4) {
                    	for(ConnectionHandler i: connections){
                            if(i!=this&&i.getRunning()){
                                i.clear();
                            }
                        }
                    }
                }catch(Exception e){
                    System.out.println("Error inputing from socket. Socket thread stopped");
                    running = false;
                }
            }
        }
        public void addStroke(Stroke stroke){
            try{
                output.writeInt(1);
                output.writeObject(stroke);
                output.flush();
            }catch(Exception exp){

            }
        }
        public void addText(Text text){
            try{
                output.writeInt(3);
                output.writeObject(text);
                output.flush();
            }catch(Exception exp){}
        }
        public void removeStroke(Stroke stroke){
            try{
                output.writeInt(-1);
                output.writeObject(stroke);
                output.flush();
            }catch(Exception exp){

            }
        }
        public void removeText(Text text){
            try{
                output.writeInt(-3);
                output.writeObject(text);
                output.flush();
            }catch(Exception exp){

            }
        }
        public void clear() {
            try{
                output.writeInt(4);
                output.flush();
            }catch(Exception exp){

            }
        }
    }
}
