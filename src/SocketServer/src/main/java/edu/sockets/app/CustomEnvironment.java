package edu.sockets.app;

import org.springframework.core.env.AbstractEnvironment;

public class CustomEnvironment extends AbstractEnvironment {
    private final String port;

    public CustomEnvironment(String port) {
        this.port = port;
    }

    public String getPort() {
        return port;
    }
}
