package dao;

import entity.GeoImage;
import entity.Position;
import org.apache.log4j.Logger;
import org.junit.*;
import util.Configuration;

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
        GeoImage geoImageInBase = dao.create(geoImage);
        assertEquals(geoImage, geoImageInBase);
    }

    @Test
    public void testUpdate() {
        GeoImage geoImage = new GeoImage();
        geoImage.setImage("BASE64");
        geoImage.setPosition(new Position(7.0, 6.0, 4.0));
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
        GeoImage geoImageInBase = dao.create(geoImage);
        assertEquals(geoImage, geoImageInBase);

        dao.delete(geoImageInBase);
        assertNull(dao.getById(geoImageInBase.getId()));
    }
}
