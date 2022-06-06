import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.BorderFactory;
import javax.swing.Box;
import java.awt.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.DimensionUIResource;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TextDialog extends JDialog implements ActionListener{
    private int result;
    private String inputtedText;
    private Font inputtedFont;

    private JTextField text;
    private JComboBox sizes;
    private JComboBox fonts;

    private JButton ok;
    private JButton cancel;

    TextDialog(BoardFrame frame){
        super(frame, "Create Text", true); //True makes the JDialog modal

        this.text = new JTextField("Example");
        text.setColumns(27);
        text.setFont(new Font("Arial", Font.PLAIN, 15));

        String[]fontsList = {"Arial", "TimesRoman", "Calibri", "Roboto"};
        this.fonts = new JComboBox(fontsList);
        String[]sizeLists = {"8", "9", "10", "11", "12", "14", "18", "24", "30", "36", "48"};
        this.sizes = new JComboBox(sizeLists);

        this.ok = new JButton("Ok");
        ok.addActionListener(this);
        this.cancel = new JButton("Cancel");
        cancel.addActionListener(this);

        JPanel p = new JPanel();
        p.add(text);
        p.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Text"));

        JPanel p1 = new JPanel();
        p1.add(fonts);
        p1.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Font Family"));

        JPanel p2 = new JPanel();
        p2.add(sizes);
        p2.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Font Size"));

        JPanel p3 = new JPanel();
        p3.add(ok);
        p3.add(cancel);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(p);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(p1);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(p2);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(p3);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.setContentPane(panel);
        this.pack();
        this.setResizable(false);
        this.setSize(new DimensionUIResource(500, 400));
        this.setVisible(false);
    }
    public int showTextDialog(){
        this.setVisible(true);
        return result;
    }
    public String getInputtedText(){
        return inputtedText;
    }
    public Font getInputtedFont(){
        return inputtedFont;
    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==ok){
            result = Const.SUCCESS;
            inputtedText = text.getText();
            int inputtedSize = Integer.parseInt((String)sizes.getSelectedItem());
            inputtedFont = new Font((String)fonts.getSelectedItem(), Font.PLAIN, inputtedSize);
            this.setVisible(false);
            
        }else if(e.getSource()==cancel){
            result = Const.FAILURE;
            this.setVisible(false);
        }
    }
}
