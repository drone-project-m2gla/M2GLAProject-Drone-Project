package util;

import java.util.Map;

/**
 * Created by alban on 12/03/15.
 */
public class Configuration {
    public static String BUCKET_NAME = "prod";
    public static String COUCHBASE_HOSTNAME = "148.60.11.195";
    public static String SERVER_PYTHON = "http://projm2gla2.istic.univ-rennes1.fr:5000/robot";

    /**
     * Load configurations
     *
     * @param configs
     */
    public static void loadConfigurations(Map<String, String> configs) {
        BUCKET_NAME = configs.get("BUCKET_NAME");
        COUCHBASE_HOSTNAME = configs.get("COUCHBASE_HOSTNAME");
    }
}
