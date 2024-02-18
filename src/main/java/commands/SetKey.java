package commands;

import utils.Constants;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SetKey implements Command{
    //used for removing the expired elements
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public String execute(List<String> input, ConcurrentHashMap<String, String> cache) {
        String key = input.get(3);
        String value = input.get(5);

        if (input.size() > 6){
            // pos - 7 PX
            // pos - 9 value

            String option = input.get(7);
            switch(option.toLowerCase()){
                case Constants.PX:
                    Duration duration = Duration.ofMillis(Long.parseLong(input.get(9)));
                    executor.execute(() -> removeKey(key, cache, duration));
            }

        }

        cache.put(key, value);
        String outputString = "+" + Constants.OUTPUT_OK + "\r\n";
        return outputString;
    }

    private void removeKey(String key, ConcurrentHashMap<String, String> cache, Duration duration){
        try {
            Long millisec = duration.toMillis();
            Thread.sleep(millisec);
            cache.remove(key);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
