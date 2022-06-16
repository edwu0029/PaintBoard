import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class JoinServerPanel extends JFrame implements ActionListener {
    
    private boolean buttonPressed = false;
    private boolean host;
    private String ipAddress;
    private String name;
    private JButton button;
    private JTextField ipField;
    private JTextField nameField;
    
    JoinServerPanel(boolean host) {
        this.host = host;
        this.setLayout(new FlowLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(800, 40)); 
        nameField.setText("Input Nickname");
        this.add(nameField);
        button = new JButton("Create Server");
        button.addActionListener(this);  
        this.setSize(850, 120);

        if (!host) {
            button.setText("Enter Server");
              ipField = new JTextField();
            ipField.setPreferredSize(new Dimension(800, 40)); 
            ipField.setText("Input Server IP Address");
            this.add(ipField);
            this.setSize(850, 170);
        }
        
        this.add(button);
        this.setVisible(true);  
    }
    
    
    public String getServerIPAdress(){
        return ipAddress;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean buttonPressed() {
        return buttonPressed;
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            if (!host) {
                ipAddress = ipField.getText();
            }
            name = nameField.getText();    
            buttonPressed = true;
            dispose();
        }
    }

}
