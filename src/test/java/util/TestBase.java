package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeSuite;

/**
 * Base class for all API test classes.
 * Thread-safe — each instance holds its own baseUrl.
 * @author Osiris Montiel Campos
 * @version 2025-11-12
 */
public class TestBase {

    protected final Logger logger = LogManager.getLogger(getClass());

    /** Test data reader bound to the current test case name. */
    protected TestData testData;

    /** Name of the test case, used to look up rows in the test data file. */
    protected String ATC_Name;

    /**
     * Base URL for this test class — instance variable, not static.
     * Each parallel class has its own copy.
     */
    protected String baseUrl;

    /**
     * Creates a new TestBase and registers the test case name.
     */
    public TestBase() {
        this.ATC_Name = this.getClass().getSimpleName();
    }

    /**
     * Initializes suite settings once before any test in the suite runs.
     * No longer sets RestAssured.baseURI globally — thread-safe.
     *
     * @param urlBase the base URL for the API under test
     */
    @BeforeSuite
    public void setupSuite(String urlBase) {
        logger.info("Initializing suite: {} with URL: {}", ATC_Name, urlBase);
        this.baseUrl = urlBase; 
        testData = new TestData("Test", ATC_Name);
        logger.info("Suite initialized successfully");
    }
}
