package config;

import java.io.File;
import java.util.Properties;

import util.PropertiesHandle;

/**
 * Central repository of framework-wide configuration constants.
 * @author Osiris Montiel Campos
 * @version 2025-10-08
 */
public class Config {

    private static final Properties properties = new PropertiesHandle("API").getProperty();
    /** 	
     * Absolute path to the root directory of the Maven project. 
     */
    private static final String PROJECT_DIR = System.getProperty("user.dir");
    /** 
     * Used when api.properties is not found
     */
    private static final String DEFAULT_URL  = "https://jsonplaceholder.typicode.com";
    private static final String DEFAULT_TIMEOUT = "1000";
    private static final String DEFAULT_CONTENT_TYPE = "application/json";
    private static final String DEFAULT_ACCEPT = "application/json";
    /** 
     * Properties directory files.
     */
    public static final String PROPERTIES_DIRECTORY = PROJECT_DIR
            + File.separator + "src"
            + File.separator + "test"
            + File.separator + "resources"
            + File.separator;
    /** 
     * Base URL of the API under test. 
     */
    public static final String BASE_URL = properties.getProperty("API.baseUrl",DEFAULT_URL);
    
    /** 
     * Base URL of the API under test. 
     */
    public static final String BOIKING_BASE_URL = properties.getProperty("API.bokking.baseUrl",DEFAULT_URL);

    /** 
     * Request timeout in milliseconds. 
     */
    public static final int TIMEOUT = Integer.parseInt(properties.getProperty("API.timeout",DEFAULT_TIMEOUT));

    /** 
     * Default Content-Type header sent with every request. 
     */
    public static final String CONTENT_TYPE = properties.getProperty("API.contentType",DEFAULT_CONTENT_TYPE);
    /** 
     * Default Content-Type header sent with every request. 
     */
    public static final String ACCEPT = properties.getProperty("API.accept",DEFAULT_ACCEPT);
    
    /**
     * Name of the Excel file that holds test input data.
     */
    public static final String TESTDATA_FILENAME = "TestData.xlsx";
    
    /** 
     * Path to the directory containing test data files. 
     */
    public static final String TESTDATA_PATH = PROJECT_DIR + File.separator + "testData" + File.separator;
}
