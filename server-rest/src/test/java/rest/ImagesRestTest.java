package rest;

import dao.GeoImageDAO;
import entity.*;
import org.junit.*;
import org.junit.Test;
import util.Configuration;

import javax.ws.rs.core.Response;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by alban on 22/05/15.
 */
public class ImagesRestTest {

    private static GeoImageDAO dao = new GeoImageDAO();

    @BeforeClass
    public static void beforeAllTests() {
        Configuration.loadConfigurations();
        dao.connect();
    }

    @AfterClass
    public static void afterAllTests() {
        for(GeoImage g : dao.getAll()){
            dao.delete(g);
        }
        dao.disconnect();
    }

    @Test
    public void testGetAllImages()
    {
        GeoImage geoImage1 = new GeoImage();
        geoImage1.setImage("BASE64");
        geoImage1.setPosition(new Position(0, 1, 0));
        geoImage1.setWidth(400);
        geoImage1.setHeight(500);
        geoImage1.setInterventionId(0);
        GeoImage geoImage1InBase = dao.create(geoImage1);
        assertEquals(geoImage1, geoImage1InBase);

        GeoImage geoImage2 = new GeoImage();
        geoImage2.setImage("BASE64");
        geoImage2.setPosition(new Position(0.0000000001, 1, 0));
        geoImage2.setWidth(400);
        geoImage2.setHeight(500);
        geoImage2.setInterventionId(0);
        GeoImage geoImage2InBase = dao.create(geoImage2);
        assertEquals(geoImage2, geoImage2InBase);

        GeoImage geoImage3 = new GeoImage();
        geoImage3.setImage("BASE64");
        geoImage3.setPosition(new Position(7.0, 6.0, 4.0));
        geoImage3.setWidth(400);
        geoImage3.setHeight(500);
        geoImage3.setInterventionId(0);
        GeoImage geoImage3InBase = dao.create(geoImage3);
        assertEquals(geoImage3, geoImage3InBase);

        dao.ensureIndex();
        ImageRest imageRest = new ImageRest();
        Response response = imageRest.getAllImages();
        List<GeoImage> concernedImages = (List<GeoImage>) response.getEntity();
        assertEquals(200, response.getStatus());
        assertEquals(3, concernedImages.size());
        assertTrue(concernedImages.contains(geoImage1));
        assertTrue(concernedImages.contains(geoImage2));
        assertTrue(concernedImages.contains(geoImage3));

        for(GeoImage g : dao.getAll())
        {
            dao.delete(g);
        }
    }

    @Test
    public void testGetAllImagesNear()
    {
        GeoImage geoImage1 = new GeoImage();
        geoImage1.setImage("BASE64");
        geoImage1.setPosition(new Position(0, 1, 0));
        geoImage1.setWidth(400);
        geoImage1.setHeight(500);
        geoImage1.setInterventionId(0);
        GeoImage geoImage1InBase = dao.create(geoImage1);
        assertEquals(geoImage1, geoImage1InBase);

        GeoImage geoImage2 = new GeoImage();
        geoImage2.setImage("BASE64");
        geoImage2.setPosition(new Position(0.0000000001, 1, 0));
        geoImage2.setWidth(400);
        geoImage2.setHeight(500);
        geoImage2.setInterventionId(0);
        GeoImage geoImage2InBase = dao.create(geoImage2);
        assertEquals(geoImage2, geoImage2InBase);

        GeoImage geoImage3 = new GeoImage();
        geoImage3.setImage("BASE64");
        geoImage3.setPosition(new Position(7.0, 6.0, 4.0));
        geoImage3.setWidth(400);
        geoImage3.setHeight(500);
        geoImage3.setInterventionId(0);
        GeoImage geoImage3InBase = dao.create(geoImage3);
        assertEquals(geoImage3, geoImage3InBase);

        dao.ensureIndex();
        ImageRest imageRest = new ImageRest();
        Response response = imageRest.getAllImagesNear(1, 0,5);
        List<GeoImage> concernedImages = (List<GeoImage>) response.getEntity();
        assertEquals(200, response.getStatus());
        assertEquals(2, concernedImages.size());
        assertTrue(concernedImages.contains(geoImage1));
        assertTrue(concernedImages.contains(geoImage2));
        assertFalse(concernedImages.contains(geoImage3));

        for(GeoImage g : dao.getAll())
        {
            dao.delete(g);
        }
    }
}
