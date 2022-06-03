import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class PaintBoard {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		// TODO Auto-generated method stub
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		new Menu();
		
		// stalls until a button is pressed
		while (Menu.buttonPressed == false) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {}
		}
		
		// add server functions later
		if (Menu.serverCreate == true) {
			new Visualizer();
		} else if (Menu.serverJoin == true) {
			new Visualizer();
		}

	}

}
