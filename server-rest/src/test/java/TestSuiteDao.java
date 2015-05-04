/**
 * Created by arno on 14/04/15.
 */

import dao.InterventionDAOTest;
import dao.UserDAOTest;
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
        InterventionDAOTest.class,
        UserDAOTest.class
})
public class TestSuiteDao {
    @BeforeClass
    public static void setUp() {

    }

    @AfterClass
    public static void tearDown() {
        System.out.println("tearing down");
    }


}
