import java.util.LinkedHashSet;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private BoardPanel boardPanel;
    private ServerChat serverChat;
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private ConnectionHandler connectionHandler;
    private boolean closed;

    private final int PORT = 5000;

    Client(String serverIP, BoardPanel boardPanel) throws Exception {
        this.boardPanel = boardPanel;
        this.socket = new Socket(serverIP, PORT);
    }
    public void start() throws Exception {
        //Set up input and output streams
        this.input = new ObjectInputStream(socket.getInputStream());
        this.output = new ObjectOutputStream(socket.getOutputStream());

        //Create and start connection handler thread
        this.connectionHandler = new ConnectionHandler();
        connectionHandler.start();
        this.closed = false;

        System.out.println("Socket started");
    }
    public boolean getClosed() {
        return closed;
    }
    public void addChatReference(ServerChat serverChat) {
        this.serverChat = serverChat;
    }
    public void quit() throws Exception {
        //Stop connection handler thread
        connectionHandler.quit();
        connectionHandler.interrupt();

        //Close input and ouput streams
        input.close();
        output.close();
        
        //Close socket
        socket.close();
        System.out.println("Closed client");
        closed = true;
    }
    public void addElement(Object element) {
        try {
            output.writeInt(Const.ADD_ELEMENT);
            output.writeObject(element);
            output.flush();
        } catch (Exception e) {}
        System.out.println("Sent element");
    }
    public void removeElement(Object element) {
        try{
            output.writeInt(Const.REMOVE_ELEMENT);
            output.writeObject(element);
            output.flush();
        } catch (Exception e) {}
        System.out.println("Sent element");
    }
    public void clear() {
        try{
            output.writeInt(Const.CLEAR);
            output.flush();
        } catch (Exception e) {}
    }
    public void requestElements() {
        try{
            output.writeInt(Const.GET_ELEMENTS);
            output.flush();
        } catch (Exception e) {}
    }
    public void sendMessage(String message) {
        try{
            output.writeInt(Const.SEND_MESSAGE);
            output.writeObject(message);
            output.flush();
        } catch (Exception e) {}
    }

    class ConnectionHandler extends Thread {
        private boolean running = true;

        public void quit() throws Exception {
            running = false;
            closed = true;
        }
        public void run() {
            while (running) {
                try {
                    //Get command
                    int command = input.readInt();
                    if (command==Const.ADD_ELEMENT) { //Add element
                        Object element = input.readObject();
                        boardPanel.addElement(element);
                        System.out.println("Recevied element to be added");
                    } else if(command==Const.REMOVE_ELEMENT) { //Remove element
                        Object element = input.readObject();
                        boardPanel.removeElement(element);
                        System.out.println("Recevied element to be removed");
                    } else if (command==Const.CLEAR) { //Clear all elements
                        boardPanel.clear();
                    } else if (command==Const.SEND_ELEMENTS) { //Sync elements from server with client's board panel
                        LinkedHashSet<Object> elements = (LinkedHashSet<Object>)input.readObject();
                        boardPanel.syncBoard(elements);
                    } else if (command==Const.SEND_MESSAGE) { //Send message to clinet's server chat
                        String message = (String)input.readObject();
                        serverChat.sendMessage(message);
                    }
                } catch(Exception e) {
                    //If there is an error with the connection, stop and close this thread
                    System.out.println("server disconnected");
                    running = false;
                    closed = true;
                }
            }
        }
    }
}
