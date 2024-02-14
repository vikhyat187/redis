import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket){
        this.clientSocket = clientSocket;
    }
    @Override
    public void run() {
        try(BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)){
            String clientInput;
            while((clientInput = inputReader.readLine()) != null){
                if (clientInput.equalsIgnoreCase("PING")){
                    sendResponse(clientSocket);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void sendResponse(Socket clientSocket){
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.print("+PONG\r\n");
            out.flush();
        } catch(IOException ex){
            throw new RuntimeException("Caught error while sending data to client");
        }
    }

}
