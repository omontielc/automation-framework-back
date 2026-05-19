package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeSuite;

import api.base.ApiOneBaseApi;

/**
 * Base class for all API test classes.
 * @author Osiris Montiel Campos
 * @version 2025-10-08
 */
public class TestBase {

    protected final Logger logger = LogManager.getLogger(getClass());
    /** 
     * Test data reader bound to the current test case name. 
     */
    protected TestData testData;
    /** 
     * Name of the test case, used to look up rows in the test data file. 
     */
    protected String ATC_Name;

    /**
     * Creates a new TestBase and registers the test case name.
     */
    public TestBase() {
    	this.ATC_Name = this.getClass().getSimpleName();
    }
    
    /**
     * Initializes RestAssured global settings once before any test in the suite runs.S
     */
    @BeforeSuite
    public void setupSuite() {
        logger.info("Initializing API test suite ");
        testData  = new TestData("Test", ATC_Name);
        ApiOneBaseApi.setup();
        logger.info("API test suite initialized successfully");
    }
}
