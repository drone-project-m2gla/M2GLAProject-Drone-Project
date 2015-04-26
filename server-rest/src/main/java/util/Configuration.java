package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by alban on 12/03/15.
 */
public class Configuration {
    private static String DATABASE_NAME;
    private static String MONGODB_HOSTNAME;
    private static String SERVER_PYTHON;
    private static String MONGODB_PORT;

    public static String getMONGODB_PORT() {
        if (MONGODB_PORT == null) {loadConfigurations();}
        return MONGODB_PORT;
    }

    public static String getDATABASE_NAME() {
        if (DATABASE_NAME == null) {loadConfigurations();}
        return DATABASE_NAME;
    }

    public static String getMONGODB_HOSTNAME() {
        if (MONGODB_HOSTNAME == null) {loadConfigurations();}
        return MONGODB_HOSTNAME;
    }

    public static String getSERVER_PYTHON() {
        if (SERVER_PYTHON == null) {loadConfigurations();}
        return SERVER_PYTHON;
    }

    /**
     * Load configurations
     *
     */
    public static void loadConfigurations() {


        /*Properties prop = new Properties();
        InputStream in = Configuration.class.getResourceAsStream("/maven.properties");
        try {

            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        DATABASE_NAME = prop.getProperty("DATABASE_NAME");
        MONGODB_HOSTNAME = prop.getProperty("MONGODB_HOSTNAME");
        SERVER_PYTHON = prop.getProperty("SERVER_PYTHON");
        MONGODB_PORT = prop.getProperty("MONGODB_PORT");
        */
        DATABASE_NAME = "test";
        MONGODB_HOSTNAME = "192.168.1.14";
        SERVER_PYTHON = "http://projm2gla2.istic.univ-rennes1.fr:5000/robot";
        MONGODB_PORT = "27017";
    }
}
