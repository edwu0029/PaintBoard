import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class JoinServerPanel extends JFrame implements ActionListener {
    
    public boolean buttonPressed = false;
    public String ipAddress;
    String name;
    JButton button;
    JTextField ipField;
    JTextField nameField;
    
    JoinServerPanel() {
        
        this.setLayout(new FlowLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        ipField = new JTextField();
        ipField.setPreferredSize(new Dimension(800, 40)); 
        ipField.setText("Input Server IP Address");
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(800, 40)); 
        nameField.setText("Input Nickname");
        button = new JButton("Enter Server");
        button.addActionListener(this);
        
        this.add(ipField);
        this.add(nameField);
        this.add(button);
        this.setSize(850, 170);
        this.setVisible(true);
        
    }
    public String getServerIPAdress(){
        return ipAddress;
    }
    public String getName() {
    	return name;
    }
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == button) {
            ipAddress = ipField.getText();
            name = nameField.getText();    
            buttonPressed = true;
            dispose();
        }
        
    }

}
