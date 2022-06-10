import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JFrame;

public class BoardFrame extends JFrame {
    private BoardPanel boardPanel;
    private ToolBar toolBar;
    private ServerChat serverChat;

    final int MAX_X = 1500;
    final int MAX_Y = 850;

    BoardFrame(User user, String serverIP, boolean online) throws Exception{
        this.boardPanel = new BoardPanel(user, serverIP, this, online);
        toolBar = new ToolBar(boardPanel);
        boardPanel.addToolBarReference(toolBar);
        
        boardPanel.setBackground(Color.WHITE);
        getContentPane().add(BorderLayout.CENTER, boardPanel);
        add(BorderLayout.WEST, toolBar);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(MAX_X, MAX_Y);
        setVisible(true);
    }
    
    public void quit(){
        this.removeAll();
        this.dispose();
    }
    
}
