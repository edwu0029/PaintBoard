import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ToolSelectPanel extends JPanel {
	
    private ImageIcon brushIcon = new ImageIcon(getClass().getClassLoader().getResource("ASSets/brushicon.png"));
    private JButton brushButton = new JButton();

    
    private buttonsPanel buttons;
    
	public ToolSelectPanel() {
		brushButton.setIcon(new ImageIcon(brushIcon.getImage().getScaledInstance(90, 110, Image.SCALE_SMOOTH)));

		this.buttons = new buttonsPanel();
		this.setBackground(Color.GRAY);
        this.setPreferredSize(new Dimension(192,0));
        this.setLayout(new BorderLayout(8,8));
        this.add(buttons);
	}
	
	private class buttonsPanel extends JPanel {
		public buttonsPanel() {
			this.setLayout(new GridLayout(7,2));

			this.add(brushButton);
			System.out.println(brushButton.getWidth());

			this.add(new JButton());
			this.add(new JButton());
			this.add(new JButton());
			this.add(new JButton());
			this.add(new JButton());
			this.add(new JButton());
			this.add(new JButton());

		}
	}
}
