package edu.sockets.app;

import edu.sockets.config.SocketsApplicationConfig;
import edu.sockets.server.Server;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        if(args.length > 0 && args[0].startsWith("--port=")){
            String port = args[0].replaceAll("--port=", "");
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
            context.setEnvironment(new CustomEnvironment(port));
            context.register(SocketsApplicationConfig.class);
            context.refresh();
            Server server = context.getBean("server", Server.class);
            server.start();
        } else {
            System.out.println("Usage: \"java -jar target/socket-server.jar --port=8081\"");
        }
    }
}
