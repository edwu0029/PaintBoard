
public class PaintBoard {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

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
