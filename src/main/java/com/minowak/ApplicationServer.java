package com.minowak;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class ApplicationServer {
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {
        Server server = configureServer();
        server.start();
        server.join();
    }

    private static Server configureServer() {

        ResourceConfig resourceConfig = new ApplicationResourceConfig();
        resourceConfig.packages("com.minowak.api");
        resourceConfig.register(JacksonFeature.class);

        ServletContainer servletContainer = new ServletContainer(resourceConfig);
        ServletHolder servletHolder = new ServletHolder(servletContainer);
        Server server = new Server(DEFAULT_PORT);
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.setContextPath("/");
        servletContextHandler.addServlet(servletHolder, "/*");
        server.setHandler(servletContextHandler);

        return server;
    }
}
