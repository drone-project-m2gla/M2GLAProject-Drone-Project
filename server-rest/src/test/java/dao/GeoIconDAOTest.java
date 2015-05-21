package dao;

import entity.GeoIcon;
import entity.Position;
import org.apache.log4j.Logger;
import org.junit.*;
import util.Configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by alban on 21/05/15.
 */
public class GeoIconDAOTest {

    private static GeoIconDAO dao = new GeoIconDAO();

    private static final Logger LOGGER = Logger.getLogger(GeoIconDAOTest.class);

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
        GeoIcon geoIcon = new GeoIcon();
        geoIcon.setColor("BLEU");
        geoIcon.setEntitled("APPARAIT !!");
        geoIcon.setFilename("/HOME/COUCOU/plop.PNG");
        geoIcon.setFirstContent("TYPE DE VEC");
        geoIcon.setPosition(new Position(7.0,6.0,4.0));
        geoIcon.setSecondContent("POMPOM");
        geoIcon.setTiret(true);
        GeoIcon geoIconInBase = dao.create(geoIcon);
        assertEquals(geoIcon, geoIconInBase);
    }

    @Test
    public void testUpdate() {
        GeoIcon geoIcon = new GeoIcon();
        geoIcon.setColor("BLEU");
        geoIcon.setEntitled("APPARAIT !!");
        geoIcon.setFilename("/HOME/COUCOU/plop.PNG");
        geoIcon.setFirstContent("TYPE DE VEC");
        geoIcon.setPosition(new Position(7.0,6.0,4.0));
        geoIcon.setSecondContent("POMPOM");
        geoIcon.setTiret(true);
        GeoIcon geoIconInBase = dao.create(geoIcon);
        assertEquals(geoIcon, geoIconInBase);

        geoIconInBase.setFilename("/PLOP/PLOP/plop.JPG");
        GeoIcon updated = dao.update(geoIconInBase);
        assertEquals(updated, geoIconInBase);
        assertEquals("/PLOP/PLOP/plop.JPG", geoIconInBase.getFilename());
    }

    @Test
    public void testDelete() {
        GeoIcon geoIcon = new GeoIcon();
        geoIcon.setColor("BLEU");
        geoIcon.setEntitled("APPARAIT !!");
        geoIcon.setFilename("/HOME/COUCOU/plop.PNG");
        geoIcon.setFirstContent("TYPE DE VEC");
        geoIcon.setPosition(new Position(7.0,6.0,4.0));
        geoIcon.setSecondContent("POMPOM");
        geoIcon.setTiret(true);
        GeoIcon geoIconInBase = dao.create(geoIcon);
        assertEquals(geoIcon, geoIconInBase);

        dao.delete(geoIconInBase);
        assertNull(dao.getById(geoIconInBase.getId()));
    }
}

