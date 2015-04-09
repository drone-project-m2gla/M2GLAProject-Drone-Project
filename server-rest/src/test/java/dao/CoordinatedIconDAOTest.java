package dao;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;

import entity.CoordinatedIcon;
import entity.Icon;
import entity.Position;
import util.Configuration;

import static org.junit.Assert.assertEquals;

/**
 * Created by jerem on 08/04/15.
 */
public class CoordinatedIconDAOTest {



    private static CoordinatedIconDAO dao = new CoordinatedIconDAO();

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
        CoordinatedIcon coordicon = new CoordinatedIcon();

        Icon icon = new Icon();
        icon.setFilename("prise_eau_perenne");
        icon.setEntitled("Point d'eau");

        coordicon.setIcon(icon);

        coordicon.setPosition(new Position(4.0, 9.0, 19.0));
        dao.entityToJsonDocument(coordicon);

        CoordinatedIcon res = dao.create(coordicon);
        //problème avec le equals de la classe CoordinatedIcon
        assertEquals(coordicon, res);
        assertEquals(coordicon.getId(), res.getId());


    }

    @Ignore
    public void testUpdate() {

        //insertion
        CoordinatedIcon coordicon = new CoordinatedIcon();

        Icon icon = new Icon();
        icon.setFilename("prise_eau_perenne");
        icon.setEntitled("Point d'eau");

        coordicon.setIcon(icon);


        dao.entityToJsonDocument(coordicon);

        CoordinatedIcon res = dao.create(coordicon);
        long idInbase = res.getId();
        assertEquals(coordicon, res);
        assertEquals(coordicon.getId(), res.getId());

        // update
        coordicon = dao.getById(idInbase);
        res = dao.update(coordicon);
        assertEquals(coordicon, res);
        assertEquals(coordicon.getId(), res.getId());
    }

}
