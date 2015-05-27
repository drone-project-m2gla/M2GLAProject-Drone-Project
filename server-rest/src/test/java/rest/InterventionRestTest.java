package rest;

import dao.InterventionDAO;
import entity.*;
import org.junit.*;
import service.impl.RetrieveAddressImpl;
import util.Configuration;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by alban on 16/03/15.
 */
public class InterventionRestTest {

    private static InterventionDAO dao = new InterventionDAO();
    private static Intervention intervention;
    @BeforeClass
    public static void beforeAllTests() {
        Configuration.loadConfigurations();
        dao.connect();
        intervention = new Intervention("Test Intervention","36 rue des chataigners","35830","Betton", DisasterCode.AVP);
        RetrieveAddressImpl adresseIntervention = new RetrieveAddressImpl(intervention.getAddress(), intervention.getPostcode(), intervention.getCity());
        Position coordinatesIntervention = adresseIntervention.getCoordinates();
        intervention.setCoordinates(coordinatesIntervention);
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
        dao.create(intervention);
        InterventionRest interventionRest= new InterventionRest();
        List<Intervention> interventionList = interventionRest.getAllIntervention();
        assertEquals(1, interventionList.size());
        dao.delete(intervention);
    }


    @Test
    public void testCreateIntervention()
    {
        InterventionRest interventionRest= new InterventionRest();
        interventionRest.setIntervention(intervention);
        assertEquals(intervention, dao.getById(intervention.getId()));
        dao.delete(intervention);
    }

    @Test
    public void testGetIntervention()
    {
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

    @Test
    public void testGetMeanListForIntervention()
    {

        Intervention intervention2 = new Intervention("Test Intervention1","36 rue des chataigners","35830","Betton", DisasterCode.AVP);
        intervention2.setCoordinates(intervention.getCoordinates());
        intervention2.setMeansList(new ArrayList<Mean>());

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
        dao.create(intervention2);
        InterventionRest interventionRest= new InterventionRest();

        Response response1 = interventionRest.getMeanListForIntervention(intervention.getId());
        Response response2 = interventionRest.getMeanListForIntervention(intervention2.getId());
        assertEquals(200,response1.getStatus());
        assertEquals(200,response2.getStatus());
        assertNotEquals(intervention.getId(),intervention2.getId());
        intervention = dao.getById(intervention.getId());
        intervention2 = dao.getById(intervention2.getId());

        assertEquals(means, intervention.getMeansList());
        assertEquals(0, intervention2.getMeansList().size());

        dao.delete(intervention);
        dao.delete(intervention2);
    }

    @Test
    public void testGetMeanForIntervention()
    {
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

        Mean mean3 = new Mean();
        mean3.setMeanState(MeanState.REFUSED);
        mean3.setVehicle(Vehicle.VSAV);

        dao.create(intervention);
        InterventionRest interventionRest= new InterventionRest();

        Response response1 = interventionRest.getMeanForIntervention(intervention.getId(), mean1.getId());
        Response response2 = interventionRest.getMeanForIntervention(intervention.getId(), mean3.getId());
        assertEquals(200,response1.getStatus());
        assertEquals(204,response2.getStatus());

        assertEquals(mean1, response1.getEntity());

        dao.delete(intervention);
    }

    @Test
    public void testAddExtraMeanToIntervention()
    {
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

        Mean mean3 = new Mean();
        mean3.setMeanState(MeanState.REFUSED);
        mean3.setVehicle(Vehicle.VSAV);

        dao.create(intervention);
        InterventionRest interventionRest= new InterventionRest();

        Response response1 = interventionRest.addExtraMeanToIntervention(intervention.getId(), mean3);
        assertEquals(200,response1.getStatus());
        assertEquals(mean3, response1.getEntity());
        intervention = dao.getById(intervention.getId());
        assertTrue(intervention.getMeansList().contains(mean1));
        assertTrue(intervention.getMeansList().contains(mean2));
        assertTrue(intervention.getMeansList().contains(mean3));
        assertEquals(3,intervention.getMeansList().size());
        dao.delete(intervention);
    }


    @Test
    public void testConfirmMeanArrivalForIntervention()
    {
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

        Mean mean3 = new Mean();
        mean3.setMeanState(MeanState.REFUSED);
        mean3.setVehicle(Vehicle.VSAV);

        dao.create(intervention);
        InterventionRest interventionRest= new InterventionRest();

        Response response1 = interventionRest.addExtraMeanToIntervention(intervention.getId(), mean3);
        assertEquals(200,response1.getStatus());
        assertEquals(mean3, response1.getEntity());
        intervention = dao.getById(intervention.getId());
        assertTrue(intervention.getMeansList().contains(mean1));
        assertTrue(intervention.getMeansList().contains(mean2));
        assertTrue(intervention.getMeansList().contains(mean3));
        assertEquals(3,intervention.getMeansList().size());
        dao.delete(intervention);
    }

    @Test
    public void testValidateMeanPositionForIntervention()
    {
        dao.create(intervention);
        InterventionRest interventionRest= new InterventionRest();
        Mean mean1 = intervention.getMeansList().get(0);

        Response response1 = interventionRest.confirmMeanArrivalForIntervention(intervention.getId(), mean1);
        assertEquals(200,response1.getStatus());
        intervention = dao.getById(intervention.getId());
        assertEquals(MeanState.ARRIVED,intervention.getMeansList().get(0).getMeanState());
        dao.delete(intervention);
    }

    @Test
    public void testValidateMeanPositionForInterventionCannotBeExecutedTwice()
    {
        dao.create(intervention);
        InterventionRest interventionRest= new InterventionRest();
        Mean mean1 = intervention.getMeansList().get(0);

        Response response1 = interventionRest.confirmMeanArrivalForIntervention(intervention.getId(), mean1);
        assertEquals(200,response1.getStatus());

        Response response2 = interventionRest.confirmMeanArrivalForIntervention(intervention.getId(), mean1);
        assertEquals(400,response2.getStatus());

        intervention = dao.getById(intervention.getId());
        assertEquals(MeanState.ARRIVED,intervention.getMeansList().get(0).getMeanState());
        dao.delete(intervention);
    }


    public void assertAreEqualsWitoutInPosition(Mean expected, Mean real)
    {
        assertEquals(expected.getDateArrived(),real.getDateArrived());
        assertEquals(expected.getCoordinates(),real.getCoordinates());
        assertEquals(expected.getDateActivated(),real.getDateActivated());
        assertEquals(expected.getDateEngaged(),real.getDateEngaged());
        assertEquals(expected.getDateRefused(),real.getDateRefused());
        assertEquals(expected.getDateReleased(),real.getDateReleased());
        assertEquals(expected.getDateRequested(),real.getDateRequested());
        assertEquals(expected.getMeanState(),real.getMeanState());
        assertEquals(expected.getName(),real.getName());
        assertEquals(expected.getVehicle(),real.getVehicle());
    }
}
