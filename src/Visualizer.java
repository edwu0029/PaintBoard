import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Visualizer extends JFrame implements MouseMotionListener, MouseListener{
    private paintBoardPanel panel;
    private ArrayList<Point> points = new ArrayList<Point>();
    final int MAX_X = (int)getToolkit().getScreenSize().getWidth();
    final int MAX_Y = (int)getToolkit().getScreenSize().getHeight();    
    private BufferedImage board = new BufferedImage(MAX_X, MAX_Y, BufferedImage.TYPE_INT_ARGB);
    private Graphics2D boardGraphics = board.createGraphics();
    Point start;
    Point end;
    Visualizer (){
    	addMouseMotionListener(this);
    	addMouseListener(this);
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
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(board, null, 0, 0);
        }        
    }

	@Override
	public void mouseDragged(MouseEvent e) {
		start = end;
		end = e.getPoint();
		boardGraphics.setColor(Color.BLACK);
    	boardGraphics.setStroke(new BasicStroke(100, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        //boardGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	boardGraphics.drawLine((int)start.getX(), (int)start.getY(), (int)end.getX(), (int)end.getY());
    	repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		start = null;
		end = null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		end = e.getPoint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}