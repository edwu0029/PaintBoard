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
	private ToolBar toolBar;
	private TextDialog textDialog;
    private int tool = Const.BRUSH;
	private Stack<Integer>previousChange; //Log of previous changes done
	private Stack<Integer>removedItems;
	private Stack<Object>removedObjects;

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

		previousChange = new Stack<Integer>();
		removedItems = new Stack<Integer>();
		removedObjects = new Stack<Object>();

		texts = new Stack<Text>();
		strokes = new Stack<Stroke>();

        start = null;
        end = null;

		this.color = new Color(0, 0, 0);
		this.textDialog = new TextDialog(frame);
    }
    
    public void addToolBarReference(ToolBar toolBar) { //Method to reference the toolbar panel from this panel
    	this.toolBar = toolBar;
    }
    
	public void undo(){
		if(previousChange.size()==0){
			return;
		}
		int x = previousChange.pop();
		if(x==1||x==2){ //Brush or Eraser
			removedItems.push(1);
			removedObjects.push(strokes.pop());
		}else if(x==3){ //Text
			removedItems.push(3);
			removedObjects.push(texts.pop());
		}
		this.repaint();
	}
	
	public void redo(){
		if(removedItems.size()==0){
			return;
		}
		int x = removedItems.pop();
		if(x==1||x==2){ //Brush or Eraser
			Stroke recoveredStroke = (Stroke)removedObjects.pop();
			previousChange.push(1);
			strokes.push(recoveredStroke);
		}else if(x==3){ //Text
			Text recoveredText = (Text)removedObjects.pop();
			previousChange.push(3);
			texts.push(recoveredText);
		}
		this.repaint();
	}
	public void clear(){
		previousChange.clear();
		removedItems.clear();
		removedObjects.clear();
		texts.clear();
		strokes.clear();
		this.repaint();
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
			g2.setColor(text.getColor());
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
		if (tool==Const.BRUSH || tool==Const.ERASER) {
			currentStroke.addLine((int)(start.getX()), (int)(start.getY()), (int)(end.getX()), (int)(end.getY()));
	    	this.repaint();
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) {}

	//MouseListener
	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("released");
		//Reset
		start = null;
		end = null;
		currentStroke = null;
		if(tool==Const.BRUSH){
			previousChange.push(1);
		}else if (tool==Const.COLOR_PICKER) {
			int xPixel = e.getX();
			int yPixel = e.getY();
			BufferedImage temp = new BufferedImage(1500, 850, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = temp.createGraphics();
			this.paint(g2);
			this.setColor(new Color(temp.getRGB(xPixel, yPixel)));
			toolBar.updateColorIcon(new Color(temp.getRGB(xPixel, yPixel)));
			g2.dispose();
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {
		if((tool==Const.BRUSH||tool==Const.ERASER)&&currentStroke==null){
			System.out.println("New stroke");
			strokes.push(new Stroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)));
			currentStroke = strokes.peek();
			if(tool==Const.BRUSH){ //Brush
				currentStroke.setColor(color);
			}else if(tool==Const.ERASER){ //Eraser
				currentStroke.setColor(Color.WHITE);
			}
		}
		end = e.getPoint();
		if(tool==Const.TEXT){
			int result = textDialog.showTextDialog();
			if(result==1){
				Text newText = new Text(e.getX(), e.getY(), textDialog.getInputtedText(), textDialog.getInputtedFont(), color);
				texts.add(newText);
				previousChange.add(3);
			}
		}
		this.repaint();
	}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
    
}
