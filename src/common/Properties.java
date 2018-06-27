package common;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author andy920262
 * Properties
 */
public class Properties {
	public static java.util.Properties properties = new java.util.Properties();
    public static String configFile = "config.properties";
    private static int port;
    private static String host;
    static {
	    try {
	        properties.load(new FileInputStream(configFile));
	        port = Integer.valueOf(properties.getProperty("PORT"));
	        host = properties.getProperty("HOST");
	    } catch (IOException ex) {
	    	port = 7122;
	    	host = "o.csie.org";
	        ex.printStackTrace();
	    }
    }
    public static int getPort() {
    	return port;
    }
    public static String getHost() {
    	return host;
    }
}
