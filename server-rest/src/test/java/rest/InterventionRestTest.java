package rest;

import dao.InterventionDAO;
import entity.*;
import org.junit.*;
import service.impl.RetrieveAddressImpl;
import util.Configuration;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

/**
 * Created by alban on 16/03/15.
 */
public class InterventionRestTest {

    private static InterventionDAO dao = new InterventionDAO();

    @BeforeClass
    public static void beforeAllTests() {
        Configuration.loadConfigurations();
        dao.connect();
    }

    @AfterClass
    public static void afterAllTests() {
        for(Intervention i : dao.getAll()){
            dao.delete(i);
        }
        dao.disconnect();
    }

    @Before
    public void setUp() throws Exception {
     //   RestAssured.basePath = "http://localhost:8088";
    }

    @After
    public void tearDown() {


    }

    @Test
    public void testListAllInterventionsWhenListIsEmpty()
    {
        InterventionRest interventionRest= new InterventionRest();
        List<Intervention> interventionList = interventionRest.getAllIntervention();
        assertEquals(0, interventionList.size());
    }

    @Test
    public void testListAllInterventions()
    {
        Intervention intervention = new Intervention("Test Intervention","36 rue des chataigners","35830","Betton", DisasterCode.AVP);
        RetrieveAddressImpl adresseIntervention = new RetrieveAddressImpl(intervention.getAddress(), intervention.getPostcode(), intervention.getCity());
        Position coordinatesIntervention = adresseIntervention.getCoordinates();
        intervention.setCoordinates(coordinatesIntervention);
        dao.create(intervention);
        InterventionRest interventionRest= new InterventionRest();
        List<Intervention> interventionList = interventionRest.getAllIntervention();
        assertEquals(1, interventionList.size());
        dao.delete(intervention);
    }


    @Test
    public void testCreateIntervention()
    {
        Intervention intervention = new Intervention("Test Intervention","36 rue des chataigners","35830","Betton", DisasterCode.AVP);
        RetrieveAddressImpl adresseIntervention = new RetrieveAddressImpl(intervention.getAddress(), intervention.getPostcode(), intervention.getCity());
        Position coordinatesIntervention = adresseIntervention.getCoordinates();
        intervention.setCoordinates(coordinatesIntervention);
        InterventionRest interventionRest= new InterventionRest();
        interventionRest.setIntervention(intervention);
        assertEquals(intervention, dao.getById(intervention.getId()));
        dao.delete(intervention);
    }

    @Test
    public void testGetIntervention()
    {
        Intervention intervention = new Intervention("Test Intervention","36 rue des chataigners","35830","Betton", DisasterCode.AVP);
        RetrieveAddressImpl adresseIntervention = new RetrieveAddressImpl(intervention.getAddress(), intervention.getPostcode(), intervention.getCity());
        Position coordinatesIntervention = adresseIntervention.getCoordinates();
        intervention.setCoordinates(coordinatesIntervention);
        dao.create(intervention);
        InterventionRest interventionRest= new InterventionRest();
        Intervention interverionWithRest = interventionRest.getIntervention(intervention.getId());
        assertEquals(interverionWithRest, intervention);
        dao.delete(intervention);
    }

    @Test
    public void testUpdateMeanPositionForIntervention()
    {
        // TODO
    }

    @Test
    public void testReleaseMeanForIntervention()
    {
        Intervention intervention = new Intervention("Test Intervention","36 rue des chataigners","35830","Betton", DisasterCode.AVP);
        RetrieveAddressImpl adresseIntervention = new RetrieveAddressImpl(intervention.getAddress(), intervention.getPostcode(), intervention.getCity());
        Position coordinatesIntervention = adresseIntervention.getCoordinates();
        intervention.setCoordinates(coordinatesIntervention);

        List<Mean> means = new ArrayList<Mean>();
        Mean mean1 = new Mean();
        mean1.setVehicle(Vehicle.VSAV);
        mean1.setMeanState(MeanState.ACTIVATED);
        mean1.setInPosition(true);
        Mean mean2 = new Mean();
        mean2.setMeanState(MeanState.REFUSED);
        mean2.setVehicle(Vehicle.VSAV);
        mean1.setInPosition(false);
        means.add(mean1);
        means.add(mean2);
        intervention.setMeansList(means);
        dao.create(intervention);
        InterventionRest interventionRest= new InterventionRest();

        Response response1 = interventionRest.releaseMeanForIntervention(intervention.getId(),mean1);
        Response response2 = interventionRest.releaseMeanForIntervention(intervention.getId(),mean2);
        assertEquals(200,response1.getStatus());
        assertEquals(400,response2.getStatus());

        assertEquals("Mean is already released or not in a state where it can be released",response2.getEntity());
        intervention = dao.getById(intervention.getId());
        assertFalse(intervention.getMeansList().contains(mean1));
        assertTrue(intervention.getMeansList().contains(mean2));
        assertEquals(2, intervention.getMeansList().size());
        intervention.getMeansList().remove(mean2);
        Mean newMean1 = intervention.getMeansList().get(0);
        assertEquals(MeanState.RELEASED, newMean1.getMeanState());
        assertFalse(newMean1.getInPosition());
        assertEquals(newMean1,response1.getEntity());
        dao.delete(intervention);
    }
}
