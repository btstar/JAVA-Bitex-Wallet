package com.udun_demo.support.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class LocalSettingsEnvironmentPostProcessor implements EnvironmentPostProcessor {
    private static final String LOCATION = System.getProperty("cfgPath");
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment configurableEnvironment, SpringApplication springApplication) {
        if(LOCATION!=null){
            File file = new File(LOCATION);
            if (file.exists()) {
                MutablePropertySources propertySources = configurableEnvironment.getPropertySources();
                Properties properties = loadProperties(file);
                propertySources.addFirst(new PropertiesPropertySource("localConfig", properties));
            }
        }
    }

    private Properties loadProperties(File f) {
        FileSystemResource resource = new FileSystemResource(f);
        try {
            return PropertiesLoaderUtils.loadProperties(resource);
        }
        catch (IOException ex) {
            throw new IllegalStateException("Failed to load local settings from " + f.getAbsolutePath(), ex);
        }
    }
}
