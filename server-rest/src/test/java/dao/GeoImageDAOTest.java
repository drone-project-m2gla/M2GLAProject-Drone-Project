package dao;

import entity.GeoImage;
import entity.Position;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.Test;
import util.Configuration;

import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by alban on 21/05/15.
 */
public class GeoImageDAOTest {

    private static GeoImageDAO dao = new GeoImageDAO();

    private static final Logger LOGGER = Logger.getLogger(GeoImageDAOTest.class);

    @BeforeClass
    public static void beforeAllTests() {
        Configuration.loadConfigurations();
        dao.connect();
        dao.ensureIndex();
    }

    @AfterClass
    public static void afterAllTests() {
        dao.disconnect();
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() {


    }

    @Test
    public void testInsert()
    {
        GeoImage geoImage = new GeoImage();
        geoImage.setImage("BASE64");
        geoImage.setPosition(new Position(7.0, 6.0, 4.0));
        geoImage.setInterventionId(0);
        GeoImage geoImageInBase = dao.create(geoImage);
        assertEquals(geoImage, geoImageInBase);
    }

    @Test
    public void testUpdate() {
        GeoImage geoImage = new GeoImage();
        geoImage.setImage("BASE64");
        geoImage.setPosition(new Position(7.0, 6.0, 4.0));
        geoImage.setInterventionId(0);
        GeoImage geoImageInBase = dao.create(geoImage);
        assertEquals(geoImage, geoImageInBase);

        geoImageInBase.setImage("PLAP");
        GeoImage updated = dao.update(geoImageInBase);
        assertEquals(updated, geoImageInBase);
        assertEquals("PLAP", geoImageInBase.getImage());
    }

    @Test
    public void testDelete() {
        GeoImage geoImage = new GeoImage();
        geoImage.setImage("BASE64");
        geoImage.setPosition(new Position(7.0, 6.0, 4.0));
        geoImage.setWidth(400);
        geoImage.setHeight(500);
        geoImage.setInterventionId(0);
        GeoImage geoImageInBase = dao.create(geoImage);
        assertEquals(geoImage, geoImageInBase);

        dao.delete(geoImageInBase);
        assertNull(dao.getById(geoImageInBase.getId()));
    }

    @Test
    public void testGetAllImagesNear()
    {
        GeoImage geoImage1 = new GeoImage();
        geoImage1.setImage("BASE64");
        geoImage1.setPosition(new Position(0, 11, 0));
        geoImage1.setWidth(400);
        geoImage1.setHeight(500);
        geoImage1.setInterventionId(0);
        GeoImage geoImage1InBase = dao.create(geoImage1);
        geoImage1.setDate(geoImage1InBase.getDate());
        assertEquals(geoImage1, geoImage1InBase);

        GeoImage geoImage2 = new GeoImage();
        geoImage2.setImage("BASE64");
        geoImage2.setPosition(new Position(0.0000000001, 11, 0));
        geoImage2.setWidth(400);
        geoImage2.setHeight(500);
        geoImage2.setInterventionId(0);
        GeoImage geoImage2InBase = dao.create(geoImage2);
        geoImage2.setDate(geoImage2InBase.getDate());
        assertEquals(geoImage2, geoImage2InBase);

        GeoImage geoImage3 = new GeoImage();
        geoImage3.setImage("BASE64");
        geoImage3.setPosition(new Position(7.0, 6.0, 4.0));
        geoImage3.setWidth(400);
        geoImage3.setHeight(500);
        geoImage3.setInterventionId(0);
        GeoImage geoImage3InBase = dao.create(geoImage3);
        geoImage3.setDate(geoImage3InBase.getDate());
        assertEquals(geoImage3, geoImage3InBase);

        dao.ensureIndex();
        List<GeoImage> concernedImages = dao.getAllImagesNear(11,0,3);
        assertEquals(2, concernedImages.size());
        assertTrue(concernedImages.contains(geoImage1));
        assertTrue(concernedImages.contains(geoImage2));
        assertFalse(concernedImages.contains(geoImage3));
    }


}
