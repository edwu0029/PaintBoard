import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JToolBar;

import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.event.ActionListener;

public class ToolBar extends JToolBar {
    private BoardPanel boardPanel;
    
    private JButton brush;
    private ImageIcon brushIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/brushicon.png"));

    private JButton eraser;
    private ImageIcon eraserIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/erasericon.png"));

    private JButton color;
    private ImageIcon colorIcon;
    
    private JButton colorPicker;
    private ImageIcon colorPickerIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/colorpickericon.png"));

    private JButton text;
    private ImageIcon textIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/texticon.png"));

    private JButton clear;
    private ImageIcon clearIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/clearicon.jpg"));

    private JButton undo;
    private ImageIcon undoIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/undoicon.png"));

    private JButton redo;
    private ImageIcon redoIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/redoicon.png"));
    
    ToolBar(BoardPanel boardPanel){
        this.boardPanel = boardPanel;

        //Set up tool menu variables
        this.setOrientation(JToolBar.VERTICAL); //Set toolbar as vertical
        this.setLayout(new GridLayout(9, 0)); //Make each button in tool bar smaller
        this.setFloatable(false);

        ButtonController buttonController = new ButtonController();

        //Add buttons to tool bar
        //Brush button
        brush = new JButton("Paint");
        brush.setIcon(new ImageIcon(brushIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        brush.addActionListener(buttonController);
        this.add(brush);
        
        //Eraser button
        eraser = new JButton("Eraser");
        eraser.setIcon(new ImageIcon(eraserIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        eraser.addActionListener(buttonController);
        this.add(eraser);

        //Color button
        color = new JButton("Color");
        BufferedImage img = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setPaint(new Color(0, 0, 0)); //Default color is black
        g2d.fillRect(0, 0, img.getWidth(), img.getHeight());
        colorIcon = new ImageIcon(img);
        color.setIcon(colorIcon);
        color.addActionListener(buttonController);
        this.add(color);
        
        //Color picker button
        colorPicker = new JButton("C.Picker");
        colorPicker.setIcon(new ImageIcon(colorPickerIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        colorPicker.addActionListener(buttonController);
        this.add(colorPicker);
        
        //Text button
        text = new JButton("Text");
        text.setIcon(new ImageIcon(textIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        text.addActionListener(buttonController);
        this.add(text);

        //Clear button
        clear = new JButton("Clear");
        clear.setIcon(new ImageIcon(clearIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        clear.addActionListener(buttonController);
        this.add(clear);

        this.addSeparator();
        
        //Undo button
        undo = new JButton("Undo");
        undo.setIcon(new ImageIcon(undoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        undo.addActionListener(buttonController);
        this.add(undo);

        //Redo button
        redo = new JButton("Redo");
        redo.setIcon(new ImageIcon(redoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        redo.addActionListener(buttonController);
        this.add(redo);
    }
    
    //update color icon function
    public void updateColorIcon(Color newColor) {
    	BufferedImage img = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setPaint(newColor);
        g2d.fillRect(0, 0, img.getWidth(), img.getHeight());
        colorIcon = new ImageIcon(img);
        color.setIcon(colorIcon);
    }
    
    private class ButtonController implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(e.getSource()==brush){
                System.out.println("brush");
                boardPanel.switchTool(Const.BRUSH);
            }else if(e.getSource()==eraser){
                System.out.println("eraser");
                boardPanel.switchTool(Const.ERASER);
            }else if(e.getSource()==color){
                System.out.println("color");
                JColorChooser colorChooser = new JColorChooser();
                Color newColor = colorChooser.showDialog(null, "Select a color", Color.BLACK);
                boardPanel.setColor(newColor);

                //Update color icon
                updateColorIcon(newColor);

            }else if (e.getSource()==colorPicker) {
            	System.out.println("color picker");
                boardPanel.switchTool(Const.COLOR_PICKER);
        	}else if(e.getSource()==text){
                System.out.println("text");
                boardPanel.switchTool(Const.TEXT);
            }else if(e.getSource()==undo){
                System.out.println("undo");
                boardPanel.undo();
            }else if(e.getSource()==redo){
                boardPanel.redo();
            }else if(e.getSource()==clear){
                boardPanel.clear();
            }
        }
    }
}
