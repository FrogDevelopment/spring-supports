package com.frogdevelopment.config.environment.secrets;

import org.springframework.cloud.config.server.environment.EnvironmentRepositoryFactory;

public class SecretsEnvironmentRepositoryFactory implements
        EnvironmentRepositoryFactory<SecretsEnvironmentRepository, SecretsEnvironmentProperties> {

    @Override
    public SecretsEnvironmentRepository build(SecretsEnvironmentProperties environmentProperties) {
        return new SecretsEnvironmentRepository(environmentProperties);
    }

}
