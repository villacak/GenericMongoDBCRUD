package au.com.mongodb.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReadProperties {

    private final String PROPERTIES_FILE_NAME = "basic.properties";

    private Properties properties;
    private String loadPropertiesFrom;

    public ReadProperties() {
        InputStream is = null;
        properties = new Properties();
        if (loadPropertiesFrom == null) {
            try {
                is = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
                properties.load(is);
            } catch (IOException ioe) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ioe.getMessage(), ioe);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                        is = null;
                    } catch (IOException e) {
//                    e.printStackTrace();
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
                    }
                }
            }
        }
    }

    public ReadProperties(final String loadPropertiesFrom) {
        this.loadPropertiesFrom = loadPropertiesFrom;
//        properties = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(loadPropertiesFrom);
            properties.load(fis);
        } catch (IOException ioe) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ioe.getMessage(), ioe);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                    fis = null;
                } catch (IOException e) {
//                    e.printStackTrace();
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }
    }


    /**
     * Get property from the properties file in resources
     *
     * @param propertyKey
     * @return
     */
    public String getProperty(final String propertyKey) {
        final String valueToReturn = properties.getProperty(propertyKey);
        return valueToReturn;
    }

    /**
     * Return all properties in a Map
     *
     * @return
     */
    public Map<String, String> getAllPropertiesMap() {
        Map<String, String> toReturn = null;
        final Set<String> propertiesNames = properties.stringPropertyNames();
        if (propertiesNames.size() > 0) {
            toReturn = new HashMap<>(propertiesNames.size());
            for(final String pKey: propertiesNames) {
                final String pValue = properties.getProperty(pKey);
                toReturn.put(pKey, pValue);
            }
        }
        return toReturn;
    }
}
