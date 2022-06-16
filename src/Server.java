import java.util.ArrayList;
import java.util.LinkedHashSet;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
 
public class Server {
    private String ip;
    private ArrayList<ConnectionHandler>connections;
    private ServerSocket serverSocket;
    private ServerThread serverThread;
    private boolean running = true;
    private LinkedHashSet<Object> elements = new LinkedHashSet<Object>();
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
                    if(command==Const.ADD_ELEMENT){
                        Object element = input.readObject();
                        elements.add(element);
                        //Update in other clients
                        for(ConnectionHandler i: connections){
                            if(i!=this&&i.getRunning()){
                                i.addElement(element);
                            }
                        }
                    }else if(command==Const.REMOVE_ELEMENT){
                        Object element = input.readObject();
                        elements.remove(element);
                        //Update in other clients
                        for(ConnectionHandler i: connections){
                            if(i!=this&&i.getRunning()){
                                i.removeElement(element);
                            }
                        }
                    }else if(command==Const.CLEAR) {
                        elements.clear();
                        for(ConnectionHandler i: connections){
                            if(i!=this&&i.getRunning()){
                                i.clear();
                            }
                        }
                    }else if(command==Const.GET_ELEMENTS) {
                        for(ConnectionHandler i: connections) {
                            if (i==this&&i.getRunning()) {
                                i.sendElements();
                            }
                        }
                    }else if(command==Const.SEND_MESSAGE) {
                        String message = (String)input.readObject();
                        for(ConnectionHandler i: connections) {
                            if (i!=this&&i.getRunning()) {
                                i.sendMessage(message);
                            }
                        }
                    }
                }catch(Exception e){
                    System.out.println("Error inputing from socket. Socket thread stopped");
                    running = false;
                }
            }
        }
        
        public void sendElements() {
            try {
                output.writeInt(Const.SEND_ELEMENTS);
                output.writeObject(elements);
                output.flush();
            }catch(Exception e) {}
        }
        
        public void addElement(Object element){
            try{
                output.writeInt(Const.ADD_ELEMENT);
                output.writeObject(element);
                output.flush();
            }catch(Exception exp){}
        }
        
        public void removeElement(Object element){
            try{
                output.writeInt(Const.REMOVE_ELEMENT);
                output.writeObject(element);
                output.flush();
            }catch(Exception exp) {}
        }
        
        public void clear() {
            try{
                output.writeInt(Const.CLEAR);
                output.flush();
            }catch(Exception exp){

            }
        }
        
        public void sendMessage(String message) {
            try{
                output.writeInt(Const.SEND_MESSAGE);
                output.writeObject(message);
                output.flush();
            }catch(Exception exp){

            }
        }
    }
    
}
