package utils;

public class ResponseUtils {
    public static String bulk(String response){
        return response != null ? "$" + response.length() + "\r\n" + response + "\r\n" : "$-1\r\n";
    }

}
