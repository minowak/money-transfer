package com.minowak;

import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api/v1")
public class ApplicationConfig extends Application {
    private final static Logger LOGGER = LoggerFactory.getLogger(ApplicationConfig.class);

    public void start(Stage primaryStage) throws Exception {
        LOGGER.info("App started");
    }

}
