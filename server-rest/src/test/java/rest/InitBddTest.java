package rest;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dao.InterventionDAO;
import entity.DisasterCode;
import entity.Intervention;
import entity.Position;
import org.junit.*;
import service.RetrieveAddress;
import util.Configuration;

import java.util.HashMap;

import static com.jayway.restassured.RestAssured.expect;

/**
 * Created by alban on 16/03/15.
 */
public class InitBddTest {

    private static InterventionDAO dao = new InterventionDAO();

    @BeforeClass
    public static void beforeAllTests() {
        HashMap<String, String> configs = new HashMap<String, String>();
        configs.put("COUCHBASE_HOSTNAME","148.60.11.195");
        configs.put("BUCKET_NAME","test");
        Configuration.loadConfigurations(configs);
        dao.connect();
    }

    @AfterClass
    public static void afterAllTests() {
        //CouchbaseCluster.create(Configuration.COUCHBASE_HOSTNAME).openBucket("e").;
        dao.disconnect();
    }

    @Before
    public void setUp() throws Exception {
     //   RestAssured.basePath = "http://localhost:8088";
    }

    @After
    public void tearDown() {


    }

    @Test
    public void testBDDInitialization()
    {

        expect().statusCode(200).contentType(ContentType.ANY).when()
                .get("http://localhost:8088/sitserver/rest/bdd/init");

    }

}
