import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        for(String arg: args){
            if(arg.startsWith("--server-port=")){
                int port = Integer.parseInt(arg.replaceAll("--server-port=",""));
                try (Socket socket = new Socket("localhost", port)) {
                    Scanner in = new Scanner(socket.getInputStream());
                    Scanner scanner = new Scanner(System.in);
                    PrintWriter out = new PrintWriter(socket.getOutputStream());
                    ObjectMapper objectMapper = new ObjectMapper();
                    boolean[] exitFlag = {false};
                    new Thread(()->{
                        while (!exitFlag[0]){
                            try {
                                String response = in.nextLine();
                                HashMap<String, Object> jsonMap =objectMapper.readValue(response, HashMap.class);
                                if(jsonMap.get("sender").equals("server")){
                                    System.out.println(jsonMap.get("text"));
                                } else {
                                    System.out.println(jsonMap.get("sender")+ ": " +jsonMap.get("text"));
                                }
                            }catch (NoSuchElementException e){
                                in.close();
                                out.close();
                                exitFlag[0] = true;
                                System.exit(0);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }).start();
                    while (!exitFlag[0]) {
                        out.println("{\"message\":\"" + scanner.nextLine() + "\"}");
                        out.flush();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}