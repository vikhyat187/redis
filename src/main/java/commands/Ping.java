package commands;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Ping implements Command{
    @Override
    public String execute(List<String> input, ConcurrentHashMap<String, String> cache){
        System.out.println("Ping: " + input.get(1));

        return "+PONG\r\n";
    }
}
