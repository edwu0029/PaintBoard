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

    final int MAX_X = 1500;
    final int MAX_Y = 850;

    BoardFrame(){
        this.boardPanel = new BoardPanel(this);
        this.toolBar = new ToolBar(boardPanel);
        
        this.boardPanel.addToolBarReference(toolBar);
        
        this.boardPanel.setBackground(Color.WHITE);
        this.getContentPane().add(BorderLayout.CENTER, boardPanel);
        this.add(BorderLayout.WEST, toolBar);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(MAX_X, MAX_Y);
        this.setVisible(true);
        this.setResizable(false);
    }
}
