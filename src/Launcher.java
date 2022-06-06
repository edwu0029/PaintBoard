public class Launcher {
    public static void main(String[] args) {
        Menu menu = new Menu();
        
        // stalls until a button is pressed
        while(Menu.buttonPressed == false) {
            try { 
                Thread.sleep(200);
            } catch (InterruptedException e) {}
        }
        
        // add server functions later
        if (Menu.serverCreate == true) {
            new BoardFrame();
        } else if (Menu.serverJoin == true) {
            
            new JoinServerPanel();
            // stalls until the user inputs ID Address
            while (JoinServerPanel.buttonPressed == false) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {}
            }
            
            BoardFrame paintBoard = new BoardFrame();
            
        }

    }

}
