package com.minowak;

import org.glassfish.jersey.server.ResourceConfig;

public class ApplicationResourceConfig extends ResourceConfig {
    public ApplicationResourceConfig() {
        register(new ApplicationBinder());
        packages("com.minowak.api");
    }
}
