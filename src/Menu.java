import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Menu extends JFrame implements ActionListener {
	
	static boolean buttonPressed = false;
	static boolean serverCreate = false;
	static boolean serverJoin = false;
	static boolean exit = false;
	
	public final int WIDTH = 1400;
	public final int HEIGHT = 800;

	private JLabel title;
	private JButton createServer, joinServer, quit;

	static JFrame frame = new JFrame();

	public Menu() {

		frame.setSize(WIDTH, HEIGHT);

		Container mainP = frame.getContentPane();
		mainP.setLayout(null);

		title = new JLabel("PaintBoard");
		createServer = new JButton("Create Server");
		joinServer = new JButton("Join Server");
		quit = new JButton("Quit");

		mainP.add(title);
		title.setFont(new Font("Chiller", Font.BOLD, 50));
		title.setBounds(WIDTH/2 - 125, HEIGHT/2 - 300, 500, 50);

		mainP.add(createServer);
		createServer.setFont(new Font("Chiller", Font.BOLD, 25));
		createServer.setBounds(WIDTH/2 - 390, HEIGHT/2 - 210, 800, 150);

		mainP.add(joinServer);
		joinServer.setFont(new Font("Chiller", Font.BOLD, 25));
		joinServer.setBounds(WIDTH/2 - 390, HEIGHT/2 - 60, 800, 150);

		mainP.add(quit);
		quit.setFont(new Font("Chiller", Font.BOLD, 25));
		quit.setBounds(WIDTH/2 - 390, HEIGHT/2 + 90, 800, 150);

		createServer.addActionListener(this);
		joinServer.addActionListener(this);
		quit.addActionListener(this);

		frame.setVisible(true);
		frame.setResizable(false);

	}

	public void actionPerformed(ActionEvent e) {
		String key = e.getActionCommand();

		if (key == "Create Server") {
			buttonPressed = true;
			serverCreate = true;
			frame.dispose();
		}

		else if (key == "Join Server") {
			buttonPressed = true;
			serverJoin = true;
			frame.dispose();
		}

		else if (key == "Quit") {
			buttonPressed = true;
			exit = true;
			frame.dispose();
		}

	}

}
