/**
 * Created by arno on 14/04/15.
 */

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dao.InterventionDAOTest;
import dao.MeanDAOTest;
import dao.UserDAOTest;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.bson.Document;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
        import org.junit.runners.Suite;
import util.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        InterventionDAOTest.class,
        UserDAOTest.class,
        MeanDAOTest.class
})
public class TestSuiteDao {
    @BeforeClass
    public static void setUp() {
        MongoClient mongoClient;
        final MongoDatabase db;
            MongoCredential credential = MongoCredential.createCredential(Configuration.getMONGODB_USER(), Configuration.getDATABASE_NAME(), Configuration.getMONGODB_PWD().toCharArray());
            mongoClient = new MongoClient(new ServerAddress(Configuration.getMONGODB_HOSTNAME(), Integer.parseInt(Configuration.getMONGODB_PORT())), Arrays.asList(credential));
            db = mongoClient.getDatabase(Configuration.getDATABASE_NAME());

            db.listCollectionNames().forEach(new Block<String>() {
                @Override
                public void apply(final String coll) {
                    if (!"system.indexes".equals(coll)) {
                        db.getCollection(coll).drop();
                    }
                }
            });
        if(mongoClient != null) {
            mongoClient.close();
        }

    }

    @AfterClass
    public static void tearDown() {
        System.out.println("tearing down");
    }


}
