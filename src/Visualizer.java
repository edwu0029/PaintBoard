import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Visualizer extends JFrame{
    paintBoardPanel panel;
    
    final int MAX_X = (int)getToolkit().getScreenSize().getWidth();
    final int MAX_Y = (int)getToolkit().getScreenSize().getHeight();    

    Visualizer (){
        this.panel = new paintBoardPanel();
        this.panel.setBackground(Color.LIGHT_GRAY);
        this.getContentPane().add(BorderLayout.CENTER, panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(MAX_X/2, MAX_Y/2);
        this.setVisible(true);
    }
    
    private class paintBoardPanel extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            this.repaint();
        }        
    }
}