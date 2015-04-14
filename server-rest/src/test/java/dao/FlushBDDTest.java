package dao;

import entity.DisasterCode;
import entity.Intervention;
import entity.Position;

import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.*;

import service.impl.RetrieveAddressImpl;
import util.Configuration;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by alban on 16/03/15.
 */
public class FlushBDDTest {

    private static InterventionDAO dao = new InterventionDAO();
    private static HashMap<String, String> configs;

    @BeforeClass
    public static void beforeAllTests() {
        configs = new HashMap<String, String>();
        configs.put("COUCHBASE_HOSTNAME","148.60.11.195");
        configs.put("BUCKET_NAME","test");
        configs.put("COUCHBASE_PORT","8091");
        Configuration.loadConfigurations(configs);
        dao.connect();
    }

    @AfterClass
    public static void afterAllTests() {
        //CouchbaseCluster.create(Configuration.COUCHBASE_HOSTNAME).openBucket("e").;
        dao.disconnect();
    }

    @Test
    public void flushBucket()
    {

        HttpClient client = new HttpClient();
        client.getParams().setParameter(
                HttpMethodParams.USER_AGENT,
                "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2"
        );
        client.getState().setCredentials(
                new AuthScope(configs.get("COUCHBASE_HOSTNAME")+":"+configs.get("COUCHBASE_PORT"), 443, AuthScope.ANY_REALM),
                new UsernamePasswordCredentials("admin", "password")
        );

        PostMethod flushBDDTest = new PostMethod("http://"+configs.get("COUCHBASE_HOSTNAME")+":"+configs.get("COUCHBASE_PORT")+"/pools/default/buckets/test/controller/doFlush");
        flushBDDTest.setDoAuthentication(true);

        int status = 0;
//        try {
//            status = client.executeMethod( flushBDDTest );
//            System.out.println(status + "\n" + flushBDDTest.getResponseBodyAsString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            flushBDDTest.releaseConnection();
//        }

        // DELETE DESIGN DOC
        PostMethod removeDesignDoc = new PostMethod("http://"+configs.get("COUCHBASE_HOSTNAME")+":"+configs.get("COUCHBASE_PORT")+"/test/_design/designDoc");
        removeDesignDoc.setDoAuthentication(true);

        status = 0;
        try {
            status = client.executeMethod( removeDesignDoc );
            System.out.println(status + "\n" + removeDesignDoc.getResponseBodyAsString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            removeDesignDoc.releaseConnection();
        }


    }
}
