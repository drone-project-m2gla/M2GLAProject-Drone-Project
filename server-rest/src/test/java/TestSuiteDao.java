/**
 * Created by arno on 14/04/15.
 */

import dao.FlushBDDTest;
import dao.InterventionDAOTest;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
        import org.junit.runners.Suite;
import util.Configuration;

import java.io.IOException;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        InterventionDAOTest.class
})
public class TestSuiteDao {
    @BeforeClass
    public static void setUp() {
        System.out.println("Database Initialization");
        Configuration.loadConfigurations();
        int status = 0;

        HttpClient client = new HttpClient();
        client.getParams().setParameter(
                HttpMethodParams.USER_AGENT,
                "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2"
        );
        client.getState().setCredentials(
                new AuthScope(Configuration.getCOUCHBASE_HOSTNAME()+":"+Configuration.getCOUCHBASE_BUCKET_PORT(), 443, AuthScope.ANY_REALM),
                new UsernamePasswordCredentials("admin", "password")
        );

        PostMethod flushBDDTest = new PostMethod("http://"+Configuration.getCOUCHBASE_HOSTNAME()+":"+Configuration.getCOUCHBASE_BUCKET_PORT()+"/pools/default/buckets/test/controller/doFlush");
        flushBDDTest.setDoAuthentication(true);
        try {
            status = client.executeMethod( flushBDDTest );
            System.out.println(status + "\n" + flushBDDTest.getResponseBodyAsString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            flushBDDTest.releaseConnection();
        }

        status = 0;

        // DELETE DESIGN DOC
        HttpClient client1 = new HttpClient();
        client1.getParams().setParameter(
                HttpMethodParams.USER_AGENT,
                "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2"
        );
        client1.getState().setCredentials(
                new AuthScope(Configuration.getCOUCHBASE_HOSTNAME()+":"+Configuration.getCOUCHBASE_DESIGNDOC_PORT(), 443, AuthScope.ANY_REALM),
                new UsernamePasswordCredentials("admin", "password")
        );

        DeleteMethod removeDesignDoc = new DeleteMethod("http://"+Configuration.getCOUCHBASE_HOSTNAME()+":"+Configuration.getCOUCHBASE_DESIGNDOC_PORT()+"/test/_design/designDoc");
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


    @AfterClass
    public static void tearDown() {
        System.out.println("tearing down");
    }


}
