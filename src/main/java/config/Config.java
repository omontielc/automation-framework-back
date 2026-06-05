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


    /**
     * Absolute path to the root directory of the Maven project.
     */
    private static final String PROJECT_DIR = System.getProperty("user.dir");
    /**
     * Used when api.properties is not found
     */
    private static final String APIONE_URL_BASE_DEFAULT = "https://jsonplaceholder.typicode.com";
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
    private static final Properties properties = new PropertiesHandle("API").getProperty();

    /**
     * Base URL for the APIONE.
     */
    public static final String APIONE_URL_BASE = properties.getProperty("API.apiOne.baseUrl",APIONE_URL_BASE_DEFAULT);
    /**
     * Base URL for the BOIKING.
     */
    public static final String BOOKING_URL_BASE = properties.getProperty("API.booking.baseUrl",APIONE_URL_BASE_DEFAULT);
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
