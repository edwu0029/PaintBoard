import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Stack;
import java.io.File;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.FileDialog;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BoardPanel extends JPanel implements MouseMotionListener, MouseListener {
    private Client client;
    private User user;
    private boolean online;

    private BoardFrame frame;
    private ToolBar toolBar;
    private ServerChat serverChat;
    
    private TextDialog textDialog;
    private Stack<Object> undo;
    private Stack<Object> redo;
    private Stroke currentStroke;
    private Color color;
    
    private Point start;
    private Point end;
    private int thickness = 4;
    private int tool = Const.BRUSH;
    
    BufferedImage image;
    private LinkedHashSet<Object> elements;
    
    BoardPanel(User user, String serverIP, BoardFrame frame, boolean online) throws Exception{
        this.user = user;
        this.frame = frame;
        this.online = online;
        
        this.addMouseMotionListener(this);
        this.addMouseListener(this);

        undo = new Stack<Object>();
        redo = new Stack<Object>();
        
        elements = new LinkedHashSet<Object>();

        start = null;
        end = null;

        color = new Color(0, 0, 0);
        textDialog = new TextDialog(frame);

        if(online){
            client = new Client(serverIP, this);
            client.start();
            serverChat = new ServerChat(client, user);
            client.addChatReference(serverChat);
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
            user.quit();
        }
        frame.quit();
    }
    public void addToolBarReference(ToolBar toolBar) { //Method to reference the toolbar panel from this panel
        this.toolBar = toolBar;
    }
    public void openChat() {
        serverChat.setVisible(true);
    }
    
    //Networking methods
    public void syncBoard(LinkedHashSet<Object> elements) {
        this.elements = elements;
        repaint();
    }
    
    public void addElement(Object element) {
        elements.add(element);
        repaint();
        System.out.println("size: "+elements.size());
    }
    
    public void removeElement(Object element) {
        elements.remove(element);
        repaint();
    }
    
    public void undo(){
        if(undo.size()==0){
            return;
        }
        Object previous = undo.pop();
        elements.remove(previous);
        if(online&&!client.getClosed()){
            try{
                client.removeElement(previous);
            }catch(Exception e){}
        }
        redo.push(previous);     
        repaint();
    }
    
    public void redo(){
        if(redo.size()==0){
            return;
        }
        Object future = redo.pop();
        elements.add(future);
        if(online&&!client.getClosed()){
            try{
                client.addElement(future);
            }catch(Exception e){}
        }
        undo.push(future);
        repaint();
    }
    
    public void clear(){
        this.setBackground(Color.WHITE);
        undo.clear();
        redo.clear();
        elements.clear();
        repaint();
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
    
    public void saveBoard() throws Exception{
        BufferedImage temp = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = temp.createGraphics();
        this.paint(g);
        FileDialog dialog = new FileDialog(frame, "Select Directory to Save");
        dialog.setFile("*.png");
        dialog.setMode(FileDialog.SAVE);
        dialog.setVisible(true);
        ImageIO.write(temp, "png", new File(dialog.getDirectory()+dialog.getFile()));
    }
    
    public void openBoard() throws Exception{
        FileDialog dialog = new FileDialog(frame, "Select File to Open");
        dialog.setFile("*.jpg;*.png;*.jpeg;");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        image = ImageIO.read(new File(dialog.getDirectory()+dialog.getFile()));
        clear();
        elements.add(new ImageIcon(image));
        if (online&&!client.getClosed()) {
            client.clear();
            client.addElement(new ImageIcon(image));
        }
        repaint();
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
            } else if (element instanceof ImageIcon) {
                ((ImageIcon)element).paintIcon(this, g2, 0, 0);
            } else if (element instanceof Color) {
                this.setBackground((Color)element);
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
                    client.addElement(currentStroke);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            currentStroke = null;
        } else if (tool==Const.COLOR_PICKER) {
            int xPixel = e.getX();
            int yPixel = e.getY();
            BufferedImage temp = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = temp.createGraphics();
            this.paint(g2);
            this.setColor(new Color(temp.getRGB(xPixel, yPixel)));
            toolBar.updateColorIcon(new Color(temp.getRGB(xPixel, yPixel)));
            g2.dispose();
        } else if (tool==Const.FILL) {
            clear();
            clearServer();
            elements.add(color);
            if (online&&!client.getClosed()) {
                try {
                    client.addElement(color);
                }catch(Exception ex) {}
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if((tool==Const.BRUSH||tool==Const.ERASER)&&currentStroke==null){
            System.out.println("New stroke");
            currentStroke = new Stroke(thickness);
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
                        client.addElement(newText);
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
