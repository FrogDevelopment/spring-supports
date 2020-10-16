package com.frogdevelopment.config.environment.secrets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration(proxyBeanMethods = false)
@Profile("secrets")
public class SecretsRepositoryConfiguration {

    @Bean
    SecretsEnvironmentRepositoryFactory dockerSecretsEnvironmentRepositoryFactory() {
        return new SecretsEnvironmentRepositoryFactory();
    }

    @Bean
    SecretsEnvironmentRepository dockerSecretsEnvironmentRepository(SecretsEnvironmentRepositoryFactory factory,
                                                                    SecretsEnvironmentProperties environmentProperties) {
        return factory.build(environmentProperties);
    }
}
