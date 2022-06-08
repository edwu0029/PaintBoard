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
    JButton button;
    JTextField textField;
    
    JoinServerPanel() {
        
        this.setLayout(new FlowLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        textField = new JTextField();
        textField.setPreferredSize(new Dimension(800, 40)); 
        textField.setText("Input Server IP Address");
        button = new JButton("Enter IP Address");
        button.addActionListener(this);
        
        this.add(button);
        this.add(textField);
        this.pack();
        this.setVisible(true);
        
    }
    public String getServerIPAdress(){
        return ipAddress;
    }
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == button) {
            // add exceptions if IP Address is invalid
            ipAddress = textField.getText();
            
            // insert exceptions here
            
            buttonPressed = true; // place after exceptions
            dispose();
        }
        
    }

}
