package common;

import java.io.FileInputStream;
import java.io.IOException;

public class Properties {
	public static java.util.Properties properties = new java.util.Properties();
    public static String configFile = "config.properties";
    static {
	    try {
	        properties.load(new FileInputStream(configFile));
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
    }
    public static int getPort() {
    	return Integer.valueOf(properties.getProperty("PORT"));
    }
    public static String getHost() {
    	return properties.getProperty("Host");
    }
}
