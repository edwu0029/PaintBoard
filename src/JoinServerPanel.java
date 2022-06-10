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
    JTextField textField;
    JTextField textField1;
    
    JoinServerPanel() {
        
        this.setLayout(new FlowLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        textField = new JTextField();
        textField.setPreferredSize(new Dimension(800, 40)); 
        textField.setText("Input Server IP Address");
        textField1 = new JTextField();
        textField1.setPreferredSize(new Dimension(800, 40)); 
        textField1.setText("Input Nickname");
        button = new JButton("Enter IP Address");
        button.addActionListener(this);
        
        this.add(textField);
        this.add(textField1);
        this.add(button);
        this.setSize(800, 150);
        this.setVisible(true);
        
    }
    public String getServerIPAdress(){
        return ipAddress;
    }
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == button) {
            ipAddress = textField.getText();
            name = textField1.getText();    
            buttonPressed = true;
            dispose();
        }
        
    }

}
