import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    static ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();
    public static void main(String[] args) {
        System.out.println("Logs from your program will appear here!");

        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        if (args.length >= 1 && args[0].equalsIgnoreCase("--port")) {
            Redis redis = new Redis(args);
        } else {
            Redis redis = new Redis();
        }
    }

}
