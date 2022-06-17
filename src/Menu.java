/** 
 * ServerChat.Java
 * @version 1.0
 * @author Kyle
 * June 2022
 * Frame that displays the menu screen
 */

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;


public class Menu extends JFrame implements ActionListener {
    
    private static boolean buttonPressed = false;
    private static boolean single = false;
    private static boolean serverCreate = false;
    private static boolean serverJoin = false;
    private static boolean exit = false;
    
    private final int WIDTH = 1500;
    private final int HEIGHT = 850;

    private JLabel title;
    private JButton offline, createServer, joinServer, quit;

    static JFrame frame = new JFrame();

    public Menu() {
        //Set up Menu Frame properties
        Container mainP = frame.getContentPane();
        mainP.setLayout(null);
        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);
        frame.setResizable(false);

        title = new JLabel("PaintBoard");

        //Set up Button options
        offline = new JButton("Offline");
        createServer = new JButton("Create Server");
        joinServer = new JButton("Join Server");
        quit = new JButton("Quit");

        mainP.add(title);
        title.setFont(new Font("Arial", Font.BOLD, 50));
        title.setBounds(WIDTH/2 - 111, HEIGHT/2 - 375, 500, 50);

        mainP.add(offline);
        offline.setFont(new Font("Arial", Font.BOLD, 25));
        offline.setBounds(WIDTH/2 - 390, HEIGHT/2 - 285, 800, 150);
        
        mainP.add(createServer);
        createServer.setFont(new Font("Arial", Font.BOLD, 25));
        createServer.setBounds(WIDTH/2 - 390, HEIGHT/2 - 135, 800, 150);

        mainP.add(joinServer);
        joinServer.setFont(new Font("Arial", Font.BOLD, 25));
        joinServer.setBounds(WIDTH/2 - 390, HEIGHT/2 + 15, 800, 150);

        mainP.add(quit);
        quit.setFont(new Font("Arial", Font.BOLD, 25));
        quit.setBounds(WIDTH/2 - 390, HEIGHT/2 + 165, 800, 150);

        offline.addActionListener(this);
        createServer.addActionListener(this);
        joinServer.addActionListener(this);
        quit.addActionListener(this);
    }
    
    public boolean buttonPressed() {
        return buttonPressed;
    }
    
    public boolean offline() {
        return single;
    }
    
    public boolean createServer() {
        return serverCreate;
    }
    
    public boolean joinServer() {
        return serverJoin;
    }
    
    public boolean exit() {
        return exit;
    }

    /*----- Overriden methods from ActionListener -----*/
    @Override
    public void actionPerformed(ActionEvent e) {
        String key = e.getActionCommand();
        if (key.equals("Offline")) { //Offline button
            buttonPressed = true;
            single = true;
            frame.dispose();
        }
        else if (key.equals("Create Server")) { //Create Server button
            buttonPressed = true;
            serverCreate = true;
            frame.dispose();
        } else if (key.equals("Join Server")) { //Join Server button
            buttonPressed = true;
            serverJoin = true;
            frame.dispose();
        } else if (key.equals("Quit")) { //Quit button
            buttonPressed = true;
            exit = true;
            frame.dispose();
            System.exit(0);
        }
    }

}
