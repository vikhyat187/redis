package commands;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Get implements Command{
    @Override
    public String execute(List<String> input, ConcurrentHashMap<String, String> cache) {
        String key = input.get(3);

        String value = cache.getOrDefault(key, null);
        System.out.println("The key is " + key + " value is " + value);

        String outputString;
        if (value != null){
            outputString = "+" + value + "\r\n";
        } else {
            outputString = "$-1\r\n";
        }
        return outputString;
    }
}
