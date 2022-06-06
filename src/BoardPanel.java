import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import java.util.Stack;
import java.util.ArrayList;

import javax.swing.JColorChooser;
import javax.swing.JPanel;

public class BoardPanel extends JPanel implements MouseMotionListener, MouseListener {
	private BoardFrame frame;
	private TextDialog textDialog;
    private int tool = 1;

	private Stroke currentStroke;
	private Color color;

	private JColorChooser colorChooser;

	private Stack<Text>texts;
	private Stack<Stroke>strokes;

    private BufferedImage board = new BufferedImage(1385, 850, BufferedImage.TYPE_INT_ARGB);
    private Graphics2D boardGraphics = board.createGraphics();

    Point start;
    Point end;

    BoardPanel(BoardFrame frame){
		this.frame = frame;
		this.colorChooser = new JColorChooser();
        this.addMouseMotionListener(this);
        this.addMouseListener(this);

		texts = new Stack<Text>();
		strokes = new Stack<Stroke>();

        start = null;
        end = null;

		this.color = new Color(0, 0, 0);
		this.textDialog = new TextDialog(frame);
    }

    public void switchTool(int newTool){
        tool = newTool;
    }
	public void setColor(Color newColor){
		color = newColor;
	}
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //Anti-aliasing
		for(Text text: texts){
			g2.setColor(color);
			g2.setFont(text.getFont());
			g2.drawString(text.getTextString(), text.getX(), text.getY());
		}
		for(Stroke stroke: strokes){
			g2.setColor(stroke.getColor());
			g2.setStroke(stroke.getStrokeType());
			ArrayList<int[]>lines = stroke.getLines();
			for(int[]x: lines){
				g2.drawLine(x[0], x[1], x[2], x[3]);
			}
		}
        //g2.drawImage(board, 0, 0, null); Old drawing using BufferedImage
    }
	@Override
	public void mouseDragged(MouseEvent e) { //drawing
		start = end;
		end = e.getPoint();

		/* Old Drawing using BufferedImage
        if(tool==1){ //Brush
            boardGraphics.setColor(color);
        }else if(tool==2){ //Eraser
            boardGraphics.setColor(Color.WHITE);
        }
    	boardGraphics.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    	boardGraphics.drawLine((int)(start.getX()), (int)(start.getY()), (int)(end.getX()), (int)(end.getY()));
		*/

		currentStroke.addLine((int)(start.getX()), (int)(start.getY()), (int)(end.getX()), (int)(end.getY()));
    	this.repaint();
	}
	@Override
	public void mouseMoved(MouseEvent e) {}

	//MouseListener
	@Override
	public void mouseReleased(MouseEvent e) {
		//Reset
		start = null;
		end = null;
		currentStroke = null;
	}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {
		if(currentStroke==null){
			strokes.push(new Stroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)));
			currentStroke = strokes.peek();
			if(tool==1){ //Brush
				currentStroke.setColor(color);
			}else if(tool==2){ //Eraser
				currentStroke.setColor(Color.WHITE);
			}
		}
		end = e.getPoint();
		if(tool==4){
			int result = textDialog.showTextDialog();
			if(result==1){
				Text newText = new Text(e.getX(), e.getY(), textDialog.getInputtedText(), textDialog.getInputtedFont());
				texts.add(newText);
			}
		}
		this.repaint();
	}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
    
}
