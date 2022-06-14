import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class BoardFrame extends JFrame implements ActionListener {
    private BoardPanel boardPanel;
    private ToolBar toolBar;
    private ServerChat serverChat;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem Save;
    private JMenuItem Open;
    private JMenuItem Exit;
    final int MAX_X = 1500;
    final int MAX_Y = 850;

    BoardFrame(User user, String serverIP, boolean online) throws Exception{
        this.boardPanel = new BoardPanel(user, serverIP, this, online);
        toolBar = new ToolBar(boardPanel, online);
        boardPanel.addToolBarReference(toolBar);

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        Save = new JMenuItem("Save");
        Save.addActionListener(this);
        Exit = new JMenuItem("Exit");
        Exit.addActionListener(this);
        Open = new JMenuItem("Open");
        Open.addActionListener(this);
        fileMenu.add(Save);
        fileMenu.add(Open);
        fileMenu.add(Exit);
        this.setJMenuBar(menuBar);
        
        boardPanel.setBackground(Color.WHITE);
        getContentPane().add(BorderLayout.CENTER, boardPanel);
        add(BorderLayout.WEST, toolBar);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(MAX_X, MAX_Y);
        setTitle(serverIP);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
    	if (e.getSource() == Exit) {
    		try {
				boardPanel.quit();
                System.exit(0);
			} catch (Exception e1) {}
    	} else if (e.getSource() == Save) {
    		try {
				boardPanel.saveBoard();
			} catch (Exception e1) {}
    	} else if (e.getSource() == Open) {
    		try {
    			boardPanel.openBoard();
    		} catch (Exception e1) {}
    	}
    }
    
    public void quit(){
        this.removeAll();
        this.dispose();
    }
    
}
