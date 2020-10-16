package com.frogdevelopment.config;

import com.frogdevelopment.config.environment.secrets.SecretsEnvironmentProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
@EnableConfigurationProperties(SecretsEnvironmentProperties.class)
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).run();
    }
}
