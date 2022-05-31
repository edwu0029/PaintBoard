import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Visualizer extends JFrame implements MouseMotionListener{
    paintBoardPanel panel;
    
    final int MAX_X = (int)getToolkit().getScreenSize().getWidth();
    final int MAX_Y = (int)getToolkit().getScreenSize().getHeight();    

    Visualizer (){
    	addMouseMotionListener(this);
    	setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        this.panel = new paintBoardPanel();
        this.panel.setBackground(Color.WHITE);
        this.getContentPane().add(BorderLayout.CENTER, panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(MAX_X/2, MAX_Y/2);
        this.setVisible(true);
    }
    
    private class paintBoardPanel extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
        }        
    }

	@Override
	public void mouseDragged(MouseEvent e) {
		Graphics g = getGraphics();
		g.setColor(Color.BLACK);
		g.fillOval(e.getX(), e.getY(), 20, 20);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}