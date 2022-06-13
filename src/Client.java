import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedHashSet;

public class Client {
    private BoardPanel boardPanel;
    private ServerChat serverChat;
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private ConnectionHandler connectionHandler;
    private boolean closed;

    private final int PORT = 5000;

    Client(String serverIP, BoardPanel boardPanel) throws Exception{
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
    public void addChatReference(ServerChat serverChat) {
    	this.serverChat = serverChat;
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
    public void addElement(Object element) {
    	try {
	        output.writeInt(Const.ADD_ELEMENT);
	        output.writeObject(element);
	        output.flush();
    	} catch (Exception e) {e.printStackTrace();}
        System.out.println("Sent element");
    }
    public void removeElement(Object element) throws Exception{
        output.writeInt(Const.REMOVE_ELEMENT);
        output.writeObject(element);
        output.flush();
        System.out.println("Sent element");
    }
    public void clear() throws Exception{
    	output.writeInt(Const.CLEAR);
    	output.flush();
    }
    public void requestElements() throws Exception{
    	output.writeInt(Const.GET_ELEMENTS);
    	output.flush();
    }
    public void sendMessage(String message) throws Exception{
    	output.writeInt(Const.SEND_MESSAGE);
    	output.writeObject(message);
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
                    if(command==Const.ADD_ELEMENT){
                        Object element = input.readObject();
                        boardPanel.addElement(element);
                        System.out.println("Recevied element to be added");
                    }else if(command==Const.REMOVE_ELEMENT){
                        Object element = input.readObject();
                        boardPanel.removeElement(element);
                        System.out.println("Recevied element to be removed");
                    }else if (command==Const.CLEAR) {
                    	boardPanel.clear();
                    }else if (command==Const.SEND_ELEMENTS) {
                    	LinkedHashSet<Object> elements = (LinkedHashSet<Object>)input.readObject();
                    	boardPanel.syncBoard(elements);
                    }else if (command==Const.SEND_MESSAGE) {
                    	String message = (String)input.readObject();
                    	serverChat.sendMessage(message);
                    }
                }catch(Exception e){
                    running = false;
                    closed = true;
                }
            }
        }
    }
}
