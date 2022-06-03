import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Visualizer extends JFrame implements MouseMotionListener, MouseListener, ActionListener{
    private paintBoardPanel panel;
    private ArrayList<Point> points = new ArrayList<Point>();
    final int MAX_X = 1500;
    final int MAX_Y = 850;
    private BufferedImage board = new BufferedImage(1385, MAX_Y, BufferedImage.TYPE_INT_ARGB);
    private Graphics2D boardGraphics = board.createGraphics();
    
    private ToolSelectPanel toolPanel;
    
	private FlowLayout toolLayout = new FlowLayout();
    private JButton brushIcon = new JButton(new ImageIcon("ASSets/brushicon.png"));
    
    Point start;
    Point end;
    
    Visualizer (){
        this.panel = new paintBoardPanel();
        this.panel.setBackground(Color.WHITE);
        this.getContentPane().add(BorderLayout.CENTER, panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(MAX_X, MAX_Y);
        this.setVisible(true);
        this.setResizable(false);
        
    	panel.addMouseMotionListener(this);
    	panel.addMouseListener(this);
    	
    	this.add(toolPanel, BorderLayout.EAST);
    	
    	panel.setLayout(toolLayout);
    	panel.add(brushIcon);
    	brushIcon.setEnabled(true);
    	brushIcon.addActionListener(this);
    }
    
    private class paintBoardPanel extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(board, 0, 0, null);
        }        
    }

	@Override
	public void mouseDragged(MouseEvent e) {
		start = end;
		end = e.getPoint();
		boardGraphics.setColor(Color.BLACK);
    	boardGraphics.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    	boardGraphics.drawLine((int)(start.getX()), (int)(start.getY()), (int)(end.getX()), (int)(end.getY()));
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