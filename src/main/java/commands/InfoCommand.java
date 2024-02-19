package commands;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Info implements Command{
    @Override
    public String execute(List<String> input, ConcurrentHashMap<String, String> cache){
        return "$11\r\nrole:master\r\n";
    }
}
