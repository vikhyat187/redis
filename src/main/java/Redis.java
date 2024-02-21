import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Redis {
    private int port;
    private static ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();
    private ExecutorService executorService;
    private String role = "master";
    private String masterHost;
    private int masterIp;

    public Redis() {
        this.port = 6379;
        startServer();
    }

    public Redis(String[] args) {
        setArgs(args);
        startServer();
    }

    private void setArgs(String[] args) {
        if (args == null){
            return;
        }

        for (int i = 0; i < args.length; i++){
            if (args[i].equals("--port")){
                try{
                    this.port = Integer.parseInt(args[i + 1]);
                } catch(NumberFormatException e){
                    System.out.println("The port number cannot be parsed");
                    System.exit(1);
                }
            }
            if (args[i].equals("--replicaof") && i + 2 < args.length){
                String masterHost = args[i + 1];
                setMasterHost(masterHost);

                try{
                    int masterIp = Integer.parseInt(args[i + 2]);
                    setMasterIp(masterIp);
                } catch(NumberFormatException e){
                    System.out.println("The master Ip cannot be converted to integer");
                    System.exit(1);
                }
                setRole("slave");
            }
        }
    }

    public void setRole(String role){
        this.role = role;
    }
    public void setMasterHost(String masterHost){
        this.masterHost = masterHost;
    }
    public void setMasterIp(int masterIp){
        this.masterIp = masterIp;
    }

    private void startServer() {
        executorService = Executors.newCachedThreadPool();
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            serverSocket.setReuseAddress(true);

            while (true) {
                final Socket clientSocket = serverSocket.accept();
//                    Thread newThread = new Thread(new ClientHandler(clientSocket, cache));
//                    newThread.start();
                System.out.println("The role is " + this.role + " The port is " + this.port);
                executorService.execute(new ClientHandler(clientSocket, cache, role, port));
            }

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            executorService.shutdown();
        }
    }
}