public class User {
    private boolean hasServer;
    private boolean online;
    private Server server;
    private BoardFrame boardFrame;
    private String name;
    User(boolean hasServer, String name, boolean online) throws Exception{
        this.hasServer = hasServer;
        if(hasServer){
            this.server = new Server();
        }
        this.online = online;
        this.name = name;
        this.boardFrame = new BoardFrame(this, server.getServerIP(), online);
    }
    User(String serverIP, String name, boolean online) throws Exception{
    	this.name = name;
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
    public String getName() {
    	return name;
    }
}
