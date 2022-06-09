public class User {
    private boolean hasServer;
    private boolean online;
    private Server server;
    private BoardFrame boardFrame;
    User(boolean hasServer, boolean online) throws Exception{
        this.hasServer = hasServer;
        if(hasServer){
            this.server = new Server();
        }
        this.online = online;
        this.boardFrame = new BoardFrame(this, server.getServerIP(), online);
    }
    User(String serverIP, boolean online) throws Exception{
        this.boardFrame = new BoardFrame(this, serverIP, online);
    }
    User() throws Exception{
        this.boardFrame = new BoardFrame(this, "", online);
    }
    public void quit() throws Exception{
        if(hasServer){
            server.quit();
        }
    }
    public boolean isHost() {
    	return hasServer;
    }
}
