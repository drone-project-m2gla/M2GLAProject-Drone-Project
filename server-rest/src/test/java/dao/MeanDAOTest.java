package dao;

import entity.Mean;
import entity.Position;
import entity.Vehicle;
import org.apache.log4j.Logger;
import org.junit.*;
import util.Configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by alban on 04/05/15.
 */
public class MeanDAOTest {

    private static MeanDAO dao = new MeanDAO();

    private static final Logger LOGGER = Logger.getLogger(UserDAOTest.class);

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
        Mean mean = new Mean();
        mean.setCoordinates(new Position(34,78,39));
        mean.setInPosition(true);
        mean.setisDeclined(true);
        mean.setVehicle(Vehicle.VSAV);
        Mean meanInBase = dao.create(mean);
        assertEquals(mean, meanInBase);
    }

    @Test
    public void testUpdate() {
        Mean mean = new Mean();
        mean.setCoordinates(new Position(34, 78, 39));
        mean.setInPosition(true);
        mean.setisDeclined(true);
        mean.setVehicle(Vehicle.VSAV);
        Mean meanInBase = dao.create(mean);
        assertEquals(mean, meanInBase);
        meanInBase.setisDeclined(false);
        Mean updated = dao.update(meanInBase);
        assertEquals(meanInBase,updated);
        assertEquals(false, updated.getisDeclined());
    }

    @Test
    public void testDelete() {
        Mean mean = new Mean();
        mean.setCoordinates(new Position(34, 78, 39));
        mean.setInPosition(true);
        mean.setisDeclined(true);
        mean.setVehicle(Vehicle.VSAV);
        Mean meanInBase = dao.create(mean);
        assertEquals(mean, meanInBase);
        dao.delete(meanInBase);
        assertNull(dao.getById(meanInBase.getId()));
    }
}
