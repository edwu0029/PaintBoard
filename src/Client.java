import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedHashSet;

public class Client {
    private BoardPanel boardPanel;
    private Socket socket;
    private String serverIP;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private ConnectionHandler connectionHandler;
    private boolean closed;

    private final int PORT = 5000;

    Client(String serverIP, BoardPanel boardPanel) throws Exception{
        this.serverIP = serverIP;
        this.boardPanel = boardPanel;
        this.socket = new Socket(serverIP, PORT);
    }
    public void start() throws Exception{
        this.input = new ObjectInputStream(socket.getInputStream());
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.connectionHandler = new ConnectionHandler();
        connectionHandler.start();
        this.closed = false;
        System.out.println("Socket started");
    }
    public boolean getClosed(){
        return closed;
    }
    public void quit() throws Exception{
        connectionHandler.quit();
        connectionHandler.interrupt();
        input.close();
        output.close();
        socket.close();
        System.out.println("Closed client");
        closed = true;
    }
    public void addStroke(Stroke stroke) throws Exception{
        output.writeInt(Const.ADD_STROKE);
        output.writeObject(stroke);
        output.flush();
        System.out.println("Sent stroke");
    }
    public void addText(Text text) throws Exception{
        output.writeInt(Const.ADD_TEXT);
        output.writeObject(text);
        output.flush();
        System.out.println("Sent text");
    }
    public void removeStroke(Stroke stroke) throws Exception{
        output.writeInt(Const.REMOVE_STROKE);
        output.writeObject(stroke);
        output.flush();
        System.out.println("Sent stroke");
    }
    public void removeText(Text text) throws Exception{
        output.writeInt(Const.REMOVE_TEXT);
        output.writeObject(text);
        output.flush();
        System.out.println("Sent text");
    }
    public void clear() throws Exception{
    	output.writeInt(Const.CLEAR);
    	output.flush();
    }
    public void requestElements() throws Exception{
    	output.writeInt(Const.GET_ELEMENTS);
    	output.flush();
    }

    class ConnectionHandler extends Thread{
        private boolean running = true;
        ConnectionHandler() throws Exception{
            
        }
        public void quit() throws Exception{
            running = false;
            closed = true;
        }
        public void run(){
            while(running){
                try{
                    int command = input.readInt();
                    System.out.println(command);
                    if(command==Const.ADD_STROKE){
                        Stroke stroke = (Stroke)input.readObject();
                        boardPanel.addStroke(stroke);
                        System.out.println("Recevied stroke to be added");
                    }else if(command==Const.REMOVE_STROKE){
                        Stroke stroke = (Stroke)input.readObject();
                        boardPanel.removeStroke(stroke);
                        System.out.println("Recevied stroke to be removed");
                    }else if(command==Const.ADD_TEXT){
                        Text text = (Text)input.readObject();
                        boardPanel.addText(text);
                        System.out.println("Recevied text to be added");
                    }else if(command==Const.REMOVE_TEXT){
                        Text text = (Text)input.readObject();
                        boardPanel.removeText(text);
                        System.out.println("Recieved text to be removed");
                    }else if (command==Const.CLEAR) {
                    	boardPanel.clear();
                    }else if (command==Const.SEND_ELEMENTS) {
                    	LinkedHashSet<Object> elements = (LinkedHashSet<Object>)input.readObject();
                    	boardPanel.syncBoard(elements);
                    }
                }catch(Exception e){
                    running = false;
                    closed = true;
                }
            }
        }
    }
}
