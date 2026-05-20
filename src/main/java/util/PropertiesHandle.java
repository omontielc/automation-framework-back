package util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import config.Config;


/**
* This class contains the methods responsible for properties
* @author Osiris Montiel Campos
* @version 2025-06-26
*/
public class PropertiesHandle {
	/**
	* objRepository variable used as an instance of the object repository
	*/
	Properties objProperties;
	// Logger variable used to generate logs
	org.apache.logging.log4j.Logger logger=LogManager.getLogger(PropertiesHandle.class);
	/**
	* Class constructor responsible for initializing the object repository
	* @param file Variable containing the name of the api being worked on
	*/
		public PropertiesHandle (String file) {
			try {
				// Create the file for object creation
				File src = new File(Config.PROPERTIES_DIRECTORY  + file +".properties");
				FileInputStream objFile = new FileInputStream(src);
				// Generate the object that will contain the object repository
				objProperties = new Properties();
				// Load the object repository
				objProperties.load(objFile);
				logger.info("Repository file loaded successfully");
			}catch(Exception e) {
				objProperties = new Properties();
				logger.info("Could not load the repository file");
			}
		}
		/**
		 * Method responsible for returning the PropertiesHandle instance
		 * @return objProperties Object containing the loaded properties
		 */ 
		public Properties getProperty(){	
			return objProperties;
		}
		
	}
