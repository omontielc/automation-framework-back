package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * Writes environment metadata to allure-results/environment.properties
 * so Allure displays it in the ENVIRONMENT section of the report.
 * @author Osiris Montiel Campos
 */
public class EnvironmentWriter {

    private static final Logger logger = LogManager.getLogger(EnvironmentWriter.class);
    private static final String ALLURE_RESULTS_PATH = "target/allure-results/environment.properties";

    public static void write(String baseUrl, String environment) {
        Properties props = new Properties();
        props.setProperty("Environment",  environment);
        props.setProperty("Base.URL",     baseUrl);
        props.setProperty("Framework",    "API Automation Framework");
        props.setProperty("Author",       "Osiris Montiel Campos");
        props.setProperty("Java.Version", System.getProperty("java.version"));
        props.setProperty("OS",           System.getProperty("os.name"));

        try (FileWriter writer = new FileWriter(ALLURE_RESULTS_PATH)) {
            props.store(writer, "Allure Environment");
            logger.info("environment.properties written to {}", ALLURE_RESULTS_PATH);
        } catch (IOException e) {
            logger.error("Could not write environment.properties: {}", e.getMessage());
        }
    }
}