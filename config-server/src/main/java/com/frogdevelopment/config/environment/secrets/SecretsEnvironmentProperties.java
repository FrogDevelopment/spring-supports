package com.frogdevelopment.config.environment.secrets;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.config.server.support.EnvironmentRepositoryProperties;

@Data
@ConfigurationProperties("spring.cloud.config.server.secrets")
public class SecretsEnvironmentProperties implements EnvironmentRepositoryProperties {

    private int order;
    private String location = "/run/secrets/repository";
}
