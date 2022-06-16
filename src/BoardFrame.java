import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class BoardFrame extends JFrame implements ActionListener {
    private BoardPanel boardPanel;
    private ToolBar toolBar;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem save;
    private JMenuItem open;
    private JMenuItem exit;
    private JMenuItem getIP;
    private String serverIP;
    final int MAX_X = 1500;
    final int MAX_Y = 850;

    BoardFrame(User user, String serverIP, boolean online) throws Exception{
        this.serverIP = serverIP;
    	this.boardPanel = new BoardPanel(user, serverIP, this, online);
        toolBar = new ToolBar(boardPanel, online);
        boardPanel.addToolBarReference(toolBar);

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        if (online) {
            menuBar.add(Box.createHorizontalGlue());
	        menuBar.add(new JLabel("Server IP:  "+serverIP+" "));
        }
        save = new JMenuItem("Save");
        save.addActionListener(this);
        exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        open = new JMenuItem("Open");
        open.addActionListener(this);
        fileMenu.add(save);
        fileMenu.add(open);
        fileMenu.add(exit);
        this.setJMenuBar(menuBar);
        
        boardPanel.setBackground(Color.WHITE);
        getContentPane().add(BorderLayout.CENTER, boardPanel);
        add(BorderLayout.WEST, toolBar);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(MAX_X, MAX_Y);
        setTitle(user.getName());
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
    	if (e.getSource() == exit) {
    		try {
				boardPanel.quit();
                System.exit(0);
			} catch (Exception e1) {}
    	} else if (e.getSource() == save) {
    		try {
				boardPanel.saveBoard();
			} catch (Exception e1) {}
    	} else if (e.getSource() == open) {
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
