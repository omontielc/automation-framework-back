package config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Central repository of framework-wide configuration constants.
 * @author Osiris Montiel Campos
 * @version 2025-10-08
 */
public class Config {

    private static final Logger logger = LogManager.getLogger(Config.class);
    private static final Properties props = new Properties();
    /** 
     * Absolute path to the root directory of the Maven project. 
     */
    private static final String PROJECT_DIR = System.getProperty("user.dir");

    // Defaults — used when api.properties is not found
    private static final String DEFAULT_BASE_URL    = "https://jsonplaceholder.typicode.com";
    private static final String DEFAULT_BOOKING_BASE_URL    = "https://restful-booker.herokuapp.com/";
    private static final String DEFAULT_TIMEOUT     = "10000";
    private static final String DEFAULT_CONTENT_TYPE = "application/json";

    static {
        try {
            String path = PROJECT_DIR
                    + File.separator + "src"
                    + File.separator + "test"
                    + File.separator + "resources"
                    + File.separator + "api.properties";
            props.load(new FileInputStream(path));
            logger.info("api.properties loaded from: {}", path);
        } catch (Exception e) {
            logger.warn("api.properties not found — using default values");
        }
    }

    private Config() {
        throw new IllegalStateException("Config is a utility class");
    }

    /** 
     * Base URL of the API under test. 
     */
    public static final String BASE_URL = props.getProperty("API.baseUrl", DEFAULT_BASE_URL);
    
    /** 
     * Base URL of the API under test. 
     */
    public static final String BOIKING_BASE_URL = props.getProperty("API.bokking.baseUrl", DEFAULT_BOOKING_BASE_URL);

    /** 
     * Request timeout in milliseconds. 
     */
    public static final int TIMEOUT = Integer.parseInt(props.getProperty("API.timeout", DEFAULT_TIMEOUT));

    /** 
     * Default Content-Type header sent with every request. 
     */
    public static final String CONTENT_TYPE = props.getProperty("API.contentType", DEFAULT_CONTENT_TYPE);
    
    /**
     * Name of the Excel file that holds test input data.
     */
    public static final String TESTDATA_FILENAME = "TestData.xlsx";
    
    /** 
     * Path to the directory containing test data files. 
     */
    public static final String TESTDATA_PATH = PROJECT_DIR + File.separator + "testData" + File.separator;
}
