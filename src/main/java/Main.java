import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.out.println("Logs from your program will appear here!");

        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        int port = 6379;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);

                while(true) {
                    clientSocket = serverSocket.accept();
                    BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String clientCommand;
                    while((clientCommand = inputReader.readLine()) != null){
                        if (clientCommand.equalsIgnoreCase("PING")){
                        System.out.println("The input is " + clientCommand);
                            sendResponse(clientSocket);
                        }
                    }
                }

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }

    private static void sendResponse(Socket clientSocket){
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.print("+PONG\r\n");
            out.flush();
        } catch(IOException ex){
            throw new RuntimeException("Caught error while sending data to client");
        }
    }
}
