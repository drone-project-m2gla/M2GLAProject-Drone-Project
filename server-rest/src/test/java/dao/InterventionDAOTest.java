package dao;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import service.impl.RetrieveAddressImpl;
import util.Configuration;
import entity.DisasterCode;
import entity.Intervention;
import entity.Position;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by alban on 16/03/15.
 */
public class InterventionDAOTest {

    private static InterventionDAO dao = new InterventionDAO();

    private static final Logger LOGGER = Logger.getLogger(InterventionDAOTest.class);

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
        Intervention intervention = new Intervention( "Intervention 1", "263 Avenue Général Leclerc","35000","Rennes", DisasterCode.FHA);
        RetrieveAddressImpl adresseIntervention = new RetrieveAddressImpl(intervention.getAddress(), intervention.getPostcode(), intervention.getCity());
        Position coordinatesIntervention = adresseIntervention.getCoordinates();
        intervention.setCoordinates(coordinatesIntervention);
        Intervention interventionInBase = dao.create(intervention);
        assertEquals(intervention, interventionInBase);
    }

    @Test
    public void testUpdate() {
        Intervention intervention = new Intervention( "Intervention 1", "263 Avenue Général Leclerc","35000","Rennes", DisasterCode.FHA);
        RetrieveAddressImpl adresseIntervention = new RetrieveAddressImpl(intervention.getAddress(), intervention.getPostcode(), intervention.getCity());
        Position coordinatesIntervention = adresseIntervention.getCoordinates();
        intervention.setCoordinates(coordinatesIntervention);
        LOGGER.info(intervention.getId());
        Intervention created = dao.create(intervention);
        assertEquals(intervention, created);
        created.setAddress("576 rue des Globours");
        Intervention updated = dao.update(created);
        assertEquals(updated, created);
        assertEquals("576 rue des Globours", updated.getAddress());
    }

    @Test
    public void testDelete() {
        Intervention intervention = new Intervention("Intervention 1", "263 Avenue Général Leclerc", "35000", "Rennes", DisasterCode.FHA);
        RetrieveAddressImpl adresseIntervention = new RetrieveAddressImpl(intervention.getAddress(), intervention.getPostcode(), intervention.getCity());
        Position coordinatesIntervention = adresseIntervention.getCoordinates();
        intervention.setCoordinates(coordinatesIntervention);
        LOGGER.info(intervention.getId());
        Intervention created = dao.create(intervention);
        assertEquals(intervention, created);
        dao.delete(created);
        assertNull(dao.getById(created.getId()));
    }
}
