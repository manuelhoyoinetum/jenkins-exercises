package com.inetum.course.prueba.jenkins.infrastructure.rest.spring;

import java.util.Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Spring Application Loader.
 */
@SpringBootApplication(scanBasePackages = "com.inetum.course.prueba.jenkins.infrastructure.rest.spring")
@ServletComponentScan
public class Application extends SpringBootServletInitializer
{
    /**
     * Main program.
     *
     * @param args Program arguments
     */
    public static void main(String[] args)
    {
        Properties pomProperties = PropertiesReader.readPropertiesFromClassPathIgnoringErrors("pom.properties");
        SystemProperties.setProperties(pomProperties);
        SpringApplication.run(Application.class, args);
    }
}
