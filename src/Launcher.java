import java.util.Scanner;
public class Launcher {
    public static void main(String[] args) throws Exception{
        Menu menu = new Menu();
        
        // stalls until a button is pressed
        while(Menu.buttonPressed == false) {
            try { 
                Thread.sleep(200);
            } catch (InterruptedException e) {}
        }
        
        // add server functions later
        if (Menu.single == true) {
            //Temporary
            //Scanner input = new Scanner(System.in);
            //System.out.println("Type in your name: ");
            //String name = input.next();
            new BoardFrame(false);
        } else if (Menu.serverCreate == true) {
        	
        } else if (Menu.serverJoin == true) {
            
            new JoinServerPanel();
            // stalls until the user inputs ID Address
            while (JoinServerPanel.buttonPressed == false) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {}
            }
            
            new BoardFrame(true);
            
        }

    }

}
