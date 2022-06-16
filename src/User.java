public class User {
    private boolean hasServer;
    private boolean online;
    private Server server;
    private BoardFrame boardFrame;
    private String name;
    
    User(boolean hasServer, String name, boolean online) { //host
        this.hasServer = hasServer;
        this.online = online;
        this.name = name;
        try {
            if (hasServer) {
                this.server = new Server();
            }
            this.boardFrame = new BoardFrame(this, server.getServerIP(), online);
        } catch(Exception e) {
            System.out.println("Failed to create a user");
        }
    }
    
    User(String serverIP, String name, boolean online) { //client
        this.name = name;
        try {
            this.boardFrame = new BoardFrame(this, serverIP, online);
        } catch(Exception e) {
            System.out.println("Failed to create a user");
        }
    }
    
    User() { //offline
        try {
            this.boardFrame = new BoardFrame(this, "", online);
        } catch(Exception e) {
            System.out.println("Failed to create a user");
        }
    }
    
    public void quit() throws Exception{
        if (hasServer) {
            server.quit();
        }
    }
    
    public boolean isHost() {
        return hasServer;
    }
    
    public String getName() {
        return name;
    }
}
