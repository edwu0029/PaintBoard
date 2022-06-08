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
        this.boardFrame = new BoardFrame(server.getServerIP(), online);
    }
    User(String serverIP, boolean online) throws Exception{
        this.boardFrame = new BoardFrame(serverIP, online);
    }
}
