import java.util.HashSet;
import java.util.ArrayList;
import java.net.*;
import java.io.*;
 
public class Server {
    private String ip;
    private ArrayList<ConnectionHandler>connections;
    private ServerSocket serverSocket;
    final int PORT = 5000;
    public static void main(String[]args) throws Exception{
        Server server = new Server();
        server.start();
    }
    Server() throws Exception{
        String localHost = InetAddress.getLocalHost().toString();
        ip = localHost.substring(localHost.indexOf('/')+1);
        System.out.println(ip);

        this.connections = new ArrayList<ConnectionHandler>();
    }
    public void start() throws Exception{
        this.serverSocket = new ServerSocket(PORT);
        System.out.println("Waiting for client...");
        while(true){
            Socket clientSocket = serverSocket.accept();
            System.out.println("New Connection");
            ConnectionHandler t = new ConnectionHandler(clientSocket);
            Thread connectionThread = new Thread(t);
            connections.add(t);
            connectionThread.start();
        }
    }
 
 
    class ConnectionHandler extends Thread{
        private Socket socket;
        private ObjectInputStream input;
        private ObjectOutputStream output;
 
        ConnectionHandler(Socket socket) throws Exception{
            this.socket = socket;
            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.input = new ObjectInputStream(socket.getInputStream());
        }
        public void run(){
            while(true){
                try{
                    int command = input.readInt();
                    if(command==1){
                        Stroke stroke = (Stroke)input.readObject();

                        //Update in other clients
                        for(ConnectionHandler i: connections){
                            if(i!=this){
                                i.addStroke(stroke);
                            }
                        }
                    }else if(command==-1){
                        Stroke stroke = (Stroke)input.readObject();

                        //Update in other clients
                        for(ConnectionHandler i: connections){
                            if(i!=this){
                                i.removeStroke(stroke);
                            }
                        }
                    }else if(command==3){
                        Text text = (Text)input.readObject();

                        //Update in other clients
                        for(ConnectionHandler i: connections){
                            if(i!=this){
                                i.addText(text);
                            }
                        }
                    }else if(command==-3){
                        Text text = (Text)input.readObject();

                        //Update in other clients
                        for(ConnectionHandler i: connections){
                            if(i!=this){
                                i.removeText(text);
                            }
                        }
                    }
                }catch(Exception e){
                    System.out.println("Error");
                    e.printStackTrace();
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
    }
}
