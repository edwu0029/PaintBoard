public class Launcher {
    public static void main(String[] args) throws Exception{
        Menu menu = new Menu();
        
        // stalls until a button is pressed
        while(menu.buttonPressed() == false) {
            try { 
                Thread.sleep(200);
            } catch (InterruptedException e) {}
        }
        
        if (menu.offline() == true) {
            new User();
        } else if (menu.createServer() == true) {
        	JoinServerPanel joinServerPanel = new JoinServerPanel(true);
            // stalls until the user inputs nickname
        	while (joinServerPanel.buttonPressed() == false) {
        		try {
        			Thread.sleep(200);
        		} catch (InterruptedException e) {}
        	}
        	new User(true, joinServerPanel.getName(), true);
        } else if (menu.joinServer() == true) {
            JoinServerPanel joinServerPanel = new JoinServerPanel(false);
            // stalls until the user inputs ID Address
            while (joinServerPanel.buttonPressed() == false) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {}
            }
            System.out.println("test");
            new User(joinServerPanel.getServerIPAdress(), joinServerPanel.getName(), true);   
        }
    }
}
