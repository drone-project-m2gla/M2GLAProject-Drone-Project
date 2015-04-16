package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by alban on 12/03/15.
 */
public class Configuration {
    private static String BUCKET_NAME;
    private static String COUCHBASE_HOSTNAME;
    private static String SERVER_PYTHON;
    private static String COUCHBASE_BUCKET_PORT;
    private static String COUCHBASE_DESIGNDOC_PORT;


    public static String getCOUCHBASE_BUCKET_PORT() {
        if (COUCHBASE_BUCKET_PORT == null) {loadConfigurations();}
        return COUCHBASE_BUCKET_PORT;
    }

    public static String getCOUCHBASE_DESIGNDOC_PORT() {
        if (COUCHBASE_DESIGNDOC_PORT == null) {loadConfigurations();}
        return COUCHBASE_DESIGNDOC_PORT;
    }

    public static String getBUCKET_NAME() {
        if (BUCKET_NAME == null) {loadConfigurations();}
        return BUCKET_NAME;
    }

    public static String getCOUCHBASE_HOSTNAME() {
        if (COUCHBASE_HOSTNAME == null) {loadConfigurations();}
        return COUCHBASE_HOSTNAME;
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


        Properties prop = new Properties();
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


        BUCKET_NAME = prop.getProperty("BUCKET_NAME");
        COUCHBASE_HOSTNAME = prop.getProperty("COUCHBASE_HOSTNAME");
        SERVER_PYTHON = prop.getProperty("SERVER_PYTHON");
        COUCHBASE_BUCKET_PORT = prop.getProperty("COUCHBASE_BUCKET_PORT");
        COUCHBASE_DESIGNDOC_PORT = prop.getProperty("COUCHBASE_DESIGNDOC_PORT");
    }
}
