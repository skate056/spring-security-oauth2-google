package com.rst.oauth2.google.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
@ComponentScan("com.rst.oauth2.google")
@Configuration
public class SpringBootRestApplication extends SpringBootServletInitializer {
    private static Logger LOGGER = LoggerFactory.getLogger(SpringBootRestApplication.class);

    public static void main(String[] args) {
        LOGGER.info("Booting with " + SpringBootRestApplication.class.getSimpleName());
        SpringApplication application = new SpringApplication(SpringBootRestApplication.class);
        application.setShowBanner(false);
        application.run(args);
        LOGGER.info("Successfully started application: " + SpringBootRestApplication.class.getSimpleName());
    }
}
