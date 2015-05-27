/**
 * Created by arno on 14/04/15.
 */

import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import rest.*;
import service.impl.PushServiceImpl;
import util.Configuration;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import dao.GeoIconDAOTest;
import dao.GeoImageDAOTest;
import dao.InterventionDAOTest;
import dao.MeanDAOTest;
import dao.UserDAOTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        InterventionDAOTest.class,
        UserDAOTest.class,
        MeanDAOTest.class,
        GeoIconDAOTest.class,
        GeoImageDAOTest.class,
        UserServicesRestTest.class,
        InterventionRestTest.class,
        ImagesRestTest.class,
        MeanRestTest.class

})
public class TestSuiteSitServer {

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
        mongoClient.close();
        PushServiceImpl.getInstance().setIsTestMode(true);
    }

    @AfterClass
    public static void tearDown() {
        System.out.println("tearing down");
    }


}
