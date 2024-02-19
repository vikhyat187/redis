import commands.*;
import utils.Constants;
import utils.ResponseUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private ConcurrentHashMap<String, String> cache;
    private static final String role = "master";

    public ClientHandler(Socket clientSocket,
                         ConcurrentHashMap<String, String> cache){
        this.clientSocket = clientSocket;
        this.cache = cache;
    }
    @Override
    public void run() {
        try(BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)){
            String clientInput;
            while((clientInput = inputReader.readLine()) != null){
                System.out.println("The client input is "+ clientInput);

                if (clientInput.startsWith("*")){
                    int numberOfItems = Integer.parseInt(clientInput.substring(1));
                    System.out.println("The number of items are " + numberOfItems);

                    List<String> commands = new ArrayList<>(numberOfItems * 2); // here we are also considering the size of the command given before
                    // for easy understanding the 0th pos = len of the command
                    // 1st pos is the command - GET, SET, ECHO, PING...
                    // 2nd pos is the length of the first input
                    // 3rd pos is the key
                    // 4th is the length of the value
                    // 5th is value
                    // 6th is command => PX
                    // 7th is PX's value
                    for (int i = 0; i < numberOfItems * 2; i++){
                        commands.add(inputReader.readLine());
                        System.out.println("input of " + i + " item is " + commands.get(i));
                    }

                    switch(commands.get(1).toLowerCase()){
                        case Constants.PING: sendResponse(new Ping().execute(commands, cache));
                                    break;
                        case Constants.ECHO: sendResponse(new Echo().execute(commands, cache)); //*2\r\n$4\r\necho\r\n$3\r\nhey\r\n
                                     break;
                        case Constants.GET: sendResponse(new Get().execute(commands, cache));
                            break;
                        case Constants.SET: sendResponse(new SetKey().execute(commands, cache));
                            break;

                        case Constants.INFO:
                            if (commands.get(3).equalsIgnoreCase("replication")){
                                String response = "role:" + this.role;
                                sendResponse(ResponseUtils.bulk(response));
                            }
                            break;
                        default: sendResponse("Currently you have entered a not supported command, please wait for few days.");
                    }

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void sendResponse(String response){
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.print(response);
            out.flush();
        } catch(IOException ex){
            throw new RuntimeException("Caught error while sending data to client");
        }
    }

}
