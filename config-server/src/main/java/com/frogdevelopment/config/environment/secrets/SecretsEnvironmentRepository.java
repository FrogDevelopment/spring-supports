package com.frogdevelopment.config.environment.secrets;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.core.Ordered;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.springframework.util.StringUtils.commaDelimitedListToStringArray;

@Slf4j
public class SecretsEnvironmentRepository implements EnvironmentRepository, Ordered {

    private static final String APPLICATION = "application";

    private final int order;
    private final String pathValue;

    public SecretsEnvironmentRepository(SecretsEnvironmentProperties properties) {
        this.order = properties.getOrder();
        this.pathValue = properties.getLocation();
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public Environment findOne(String applicationName, String profileName, String label) {
        log.debug("Looking for applicationName={}, profile={}, label={}", applicationName, profileName, label);

        var profiles = new String[] {profileName};
        if (profileName != null) {
            profiles = commaDelimitedListToStringArray(profileName);
        }

        var environment = new Environment(applicationName, profiles);

        var resources = new ArrayList<String>();
        var applications = getApplications(applicationName);

        // 1st, loading profile resources
        for (String application : applications) {
            for (String profile : profiles) {
                resources.add(format("%s-%s", application, profile));
            }
        }

        // then application resources
        resources.addAll(applications);

        if (log.isDebugEnabled()) {
            log.debug("Property sources to load={}", String.join(", ", resources));
        }

        resources.forEach(fileName -> loadDockerSecrets(environment, fileName));

        return environment;
    }

    private static List<String> getApplications(String applicationName) {
        var applications = Arrays.stream(commaDelimitedListToStringArray(applicationName))
                .filter(app-> !APPLICATION.equals(app))
                .collect(Collectors.toList());

        // so 'application' is always at the last position
        applications.add(APPLICATION);

        return applications;
    }

    private void loadDockerSecrets(Environment environment, String fileName) {
        var path = Paths.get(pathValue, fileName);
        if (Files.exists(path)) {
            log.debug("Adding property source: file={}", path.toFile().getAbsolutePath());
            try {
                var data = Files.readString(path);
                if (data != null) {
                    var yaml = new YamlPropertiesFactoryBean();
                    yaml.setResources(new ByteArrayResource(data.getBytes()));
                    var properties = yaml.getObject();

                    if (properties != null && !properties.isEmpty()) {
                        environment.add(new PropertySource("secrets:" + fileName, properties));
                    }
                }
            } catch (IOException e) {
                log.error(format("Unreadable: %s", path.toFile().getAbsolutePath()), e);
            }
        } else {
            log.debug("{} doesn't exists, skipping it.", path.toFile().getAbsolutePath());
        }
    }

}
