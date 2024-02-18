package commands;

public class Ping implements Command{
    @Override
    public String execute(String input){
        System.out.println("Ping: " + input);

        return "+PONG\r\n";
    }
}
