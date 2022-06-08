import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.BufferOverflowException;

public class User {
    private String name;
    private BoardFrame frame;
    private BoardPanel boardPanel;
    private Client client;

    User(String name) throws Exception{
        this.name = name;

        //User's board
        this.frame = new BoardFrame();
    }
}
