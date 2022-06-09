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
import java.util.LinkedHashSet;

import javax.swing.JColorChooser;
import javax.swing.JPanel;

public class BoardPanel extends JPanel implements MouseMotionListener, MouseListener {
    private Client client;
    private User user;

    private BoardFrame frame;
    private ToolBar toolBar;
    private TextDialog textDialog;
    private int tool = Const.BRUSH;
    private Stack<Integer>previousChange; //Log of previous changes done
    private Stack<Integer>removedItems;
    private Stack<Object>removedObjects;
    private Stack<Object> undo;
    private Stack<Object> redo;
    private int thickness = 4;
    private Stroke currentStroke;
    private Color color;
    
    private JColorChooser colorChooser;

    //TODO change names for hashsets
    private Stack<Text>ownTexts;
    private Stack<Stroke>ownStrokes;

    Point start;
    Point end;
    private boolean online;
    
    private LinkedHashSet<Object> elements;
    
    BoardPanel(User user, String serverIP, BoardFrame frame, boolean online) throws Exception{
        this.user = user;
        this.frame = frame;
        this.online = online;
        this.colorChooser = new JColorChooser();
        this.addMouseMotionListener(this);
        this.addMouseListener(this);

        previousChange = new Stack<Integer>();
        removedItems = new Stack<Integer>();
        removedObjects = new Stack<Object>();
        undo = new Stack<Object>();
        redo = new Stack<Object>();
        
        ownTexts = new Stack<Text>();
        ownStrokes = new Stack<Stroke>();
        elements = new LinkedHashSet<Object>();

        start = null;
        end = null;

        this.color = new Color(0, 0, 0);
        this.textDialog = new TextDialog(frame);

        //TODO move client out of BoardPanel
        //TODO hardcode ip adress of server
        if(online){
            client = new Client(serverIP, this);
            client.start();
            if (user.isHost()) {
        		elements = new LinkedHashSet<Object>();
        	} else {
        		client.requestElements();
        	}
        }
    }
    public void quit() throws Exception{
        if(online){
            client.quit();
        }
        user.quit();
        frame.quit();
    }
    public void addToolBarReference(ToolBar toolBar) { //Method to reference the toolbar panel from this panel
        this.toolBar = toolBar;
    }
    //Networking methods
    public void syncBoard(LinkedHashSet<Object> elements) {
    	this.elements = elements;
    	this.repaint();
    }
    public void addStroke(Stroke stroke){
        elements.add(stroke);
        this.repaint();
    }
    public void removeStroke(Stroke stroke){
        elements.remove(stroke);
        this.repaint();
    }
    public void addText(Text text){
        elements.add(text);
        this.repaint();
    }
    public void removeText(Text text){
        elements.remove(text);
        this.repaint();
    }
    public void undo(){
        if(undo.size()==0){
            return;
        }
        Object previous = undo.pop();
        if(previous instanceof Stroke){ //Brush or Eraser
            elements.remove(previous);
            if(online&&!client.getClosed()){
                try{
                    client.removeStroke((Stroke)previous);
                }catch(Exception e){}
            }
            redo.push(previous);
            
        }else if(previous instanceof Text){ //Text
            elements.remove(previous);
            if(online&&!client.getClosed()){
                try{
                    client.removeText((Text)previous);
                }catch(Exception e){}
            }
            redo.push(previous);
        }
        this.repaint();
    }
    
    public void redo(){
        if(redo.size()==0){
            return;
        }
        Object future = redo.pop();
        if(future instanceof Stroke){ //Brush or Eraser
            elements.add(future);
            if(online&&!client.getClosed()){
                try{
                    client.addStroke((Stroke)future);
                }catch(Exception e){}
            }
            undo.push(future);
        }else if(future instanceof Text){ //Text
            elements.add(future);
            if(online&&!client.getClosed()){
                try{
                    client.addText((Text)future);
                }catch(Exception e){}
            }
            undo.push(future);
        }
        this.repaint();
    }
    
    public void clear(){
        undo.clear();
        redo.clear();
        elements.clear();
        this.repaint();
    }
    
    public void clearServer() { //Tell other clients to clear their board
        if(online&&!client.getClosed()){
            try{
                client.clear();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
    
    public void switchTool(int newTool){
        tool = newTool;
    }
    
    public void setColor(Color newColor){
        color = newColor;
    }
    
    public void setThickness(int newThickness){
        thickness = newThickness;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //Anti-aliasing
        for (Object element: elements) {
        	if (element instanceof Stroke) {
        		Stroke stroke = (Stroke)element;
        		g2.setColor(stroke.getColor());
                g2.setStroke(new BasicStroke(stroke.getThickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                ArrayList<int[]>lines = stroke.getLines();
                for(int[]x: lines){
                    g2.drawLine(x[0], x[1], x[2], x[3]);
                }
        	} else if (element instanceof Text) {
        		Text text = (Text)element;
                g2.setColor(text.getColor());
                g2.setFont(text.getFont());
                g2.drawString(text.getTextString(), text.getX(), text.getY());
        	}
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent e) { //drawing
        start = end;
        end = e.getPoint();
        
        if (tool==Const.BRUSH || tool==Const.ERASER) {
            currentStroke.addLine((int)(start.getX()), (int)(start.getY()), (int)(end.getX()), (int)(end.getY()));
            this.repaint();
        }
    }

    //MouseListener
    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("released");
        //Reset
        start = null;
        end = null;
        if(tool==Const.BRUSH||tool==Const.ERASER){
        	redo.clear();
        	undo.push(currentStroke);
            if(online&&!client.getClosed()){
                try{
                    client.addStroke(currentStroke);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            currentStroke = null;
        } else if (tool==Const.COLOR_PICKER) {
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
    public void mousePressed(MouseEvent e) {
        if((tool==Const.BRUSH||tool==Const.ERASER)&&currentStroke==null){
            System.out.println("New stroke");
            ownStrokes.push(new Stroke(thickness));
            currentStroke = ownStrokes.peek();
            elements.add(currentStroke);
            if(tool==Const.BRUSH){ //Brush
                currentStroke.setColor(color);
            }else if(tool==Const.ERASER){ //Eraser
                currentStroke.setColor(Color.WHITE);
            }
        }
        end = e.getPoint();
        if(tool==Const.TEXT){
            int result = textDialog.showTextDialog();
            if(result==Const.SUCCESS){
                Text newText = new Text(e.getX(), e.getY(), textDialog.getInputtedText(), textDialog.getInputtedFont(), color);
                elements.add(newText);
                redo.clear();
                undo.push(newText);
                if(online&&!client.getClosed()){
                    try{
                        client.addText(newText);
                    }catch(Exception ex){}
                }
            }
        }
        this.repaint();
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {}
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

}
