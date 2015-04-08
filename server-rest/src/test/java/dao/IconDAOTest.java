package dao;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;

import entity.Icon;
import util.Configuration;

import static org.junit.Assert.assertEquals;

/**
 * Created by jerem on 08/04/15.
 */
public class IconDAOTest {

    private static IconDAO dao = new IconDAO();

    @BeforeClass
    public static void beforeAllTests() {
        HashMap<String, String> configs = new HashMap<String, String>();
        configs.put("COUCHBASE_HOSTNAME", "148.60.11.195");
        configs.put("BUCKET_NAME","test");
        Configuration.loadConfigurations(configs);
        dao.connect();

    }

    @AfterClass
    public static void afterAllTests() {

        dao.disconnect();
    }

    @Test
    public void testInsert(){
        Icon icon = new Icon();
        icon.setFilename("prise_eau_perenne");
        icon.setEntitled("Point d'eau");
        dao.entityToJsonDocument(icon);

        Icon res = dao.create(icon);
        assertEquals(icon, res);
        assertEquals(icon.getId(), res.getId());


    }

    @Test
    public void testUpdate() {

        //insertion
        Icon icon = new Icon();
        icon.setFilename("prise_air");
        icon.setEntitled("Point d'air");
        dao.entityToJsonDocument(icon);

        Icon res = dao.create(icon);
        long idInbase = res.getId();
        assertEquals(icon, res);
        assertEquals(icon.getId(), res.getId());

        // update
        icon = dao.getById(idInbase);
        res = dao.update(icon);
        assertEquals(icon, res);
        assertEquals(icon.getId(), res.getId());
    }

}
