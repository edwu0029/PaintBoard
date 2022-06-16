import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

public class ServerChat extends JFrame implements ActionListener {
    private Client client;
    private User user;
    private JTextArea txaDisplay;
    private JScrollPane scrdisplay;
    private JTextField txtInput;
    private JButton button;
    private String message;
    
    ServerChat(Client client, User user) {    
        this.client = client;
        this.user = user;
        setTitle("Chat");
        setLayout(new FlowLayout());
        
        txaDisplay = new JTextArea();
        DefaultCaret caret = (DefaultCaret)txaDisplay.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);//Forces chat to scroll down
        txaDisplay.setEditable(false);
        scrdisplay = new JScrollPane(txaDisplay);
        scrdisplay.setPreferredSize(new Dimension(450, 200));
        getContentPane().add(scrdisplay);
        
        txtInput = new JTextField();
        txtInput.setPreferredSize(new Dimension(450, 30));
        getContentPane().add(txtInput);
        
        button = new JButton("Send message");
        button.addActionListener(this);
        this.add(button);
        
        setSize(500, 320);
        setVisible(false);
        setResizable(false);
        setLocationRelativeTo(null);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(button)) {
            message = txtInput.getText();
            txaDisplay.append(user.getName()+": "+message+"\n");
            try {
                client.sendMessage(user.getName()+": "+message);
            } catch (Exception ex) {}
        } 
    }
    
    public String getMessage() {
        return message;
    }
    
    public void sendMessage(String message) {
        txaDisplay.append(message+"\n");
    }
    
}
