package rest;


import dao.InterventionDAO;
import entity.*;
import org.junit.*;
import service.impl.RetrieveAddressImpl;
import util.Configuration;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.isNaN;
import static org.junit.Assert.*;

/**
 * Created by alban on 16/03/15.
 */
public class MeanRestTest {

    private static InterventionDAO dao = new InterventionDAO();
    private static Intervention intervention;
    private static Position coordinatesIntervention;
    private final static String interventionName = "Test Intervention";
    private final static String address = "36 rue des chataigners";
    private final static String postCode = "35830";
    private final static String city = "Betton";
    private final static DisasterCode disasterCode = DisasterCode.AVP;

    @BeforeClass
    public static void beforeAllTests() {
        Configuration.loadConfigurations();
        RetrieveAddressImpl adresseIntervention = new RetrieveAddressImpl(address, postCode, city);
        coordinatesIntervention = adresseIntervention.getCoordinates();
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
        dao.connect();
        intervention = new Intervention(interventionName,address,postCode,city,disasterCode);
        intervention.setCoordinates(coordinatesIntervention);
    }

    @After
    public void tearDown() {
        dao.delete(intervention);
    }

    @Test
    public void testValidateMeanXtra(){
        Mean mean = new Mean(Vehicle.VSAV,false);
        dao.create(intervention);
        InterventionRest interventionRest= new InterventionRest();
        interventionRest.addExtraMeanToIntervention(intervention.getId(),mean);
        intervention = dao.getById(intervention.getId());
        assertEquals(MeanState.REQUESTED,intervention.getMeansList().get(intervention.getMeansList().size()-1).getMeanState());
        MeanRest meanRest = new MeanRest();
        meanRest.validateMeanXtra(intervention.getId(),mean);
        intervention = dao.getById(intervention.getId());
        assertEquals(MeanState.ACTIVATED,intervention.getMeansList().get(intervention.getMeansList().size()-1).getMeanState());
    }

    @Test
    public void testValidateMeanXtraWhenNotInStateRequested(){
        Mean mean = new Mean(Vehicle.VSAV,false);
        dao.create(intervention);
        InterventionRest interventionRest= new InterventionRest();
        interventionRest.addExtraMeanToIntervention(intervention.getId(),mean);
        intervention = dao.getById(intervention.getId());
        MeanRest meanRest = new MeanRest();
        Response response1 = meanRest.validateMeanXtra(intervention.getId(),mean);
        assertEquals(200,response1.getStatus());
        Response response2 = meanRest.validateMeanXtra(intervention.getId(),mean);
        assertEquals(400,response2.getStatus());
    }

    @Test
    public void testDeclineMeanXtra(){
        Mean mean = new Mean(Vehicle.VSAV,false);
        dao.create(intervention);
        InterventionRest interventionRest= new InterventionRest();
        interventionRest.addExtraMeanToIntervention(intervention.getId(),mean);
        intervention = dao.getById(intervention.getId());
        assertEquals(MeanState.REQUESTED,intervention.getMeansList().get(intervention.getMeansList().size()-1).getMeanState());
        MeanRest meanRest = new MeanRest();
        meanRest.declineMeanXtra(intervention.getId(),mean);
        intervention = dao.getById(intervention.getId());
        assertEquals(MeanState.REFUSED,intervention.getMeansList().get(intervention.getMeansList().size()-1).getMeanState());
    }

    @Test
    public void testDeclineMeanXtraWhenNotInStateRequested(){
        Mean mean = new Mean(Vehicle.VSAV,false);
        dao.create(intervention);
        InterventionRest interventionRest= new InterventionRest();
        interventionRest.addExtraMeanToIntervention(intervention.getId(),mean);
        intervention = dao.getById(intervention.getId());
        MeanRest meanRest = new MeanRest();
        Response response1 = meanRest.declineMeanXtra(intervention.getId(),mean);
        assertEquals(200,response1.getStatus());
        Response response2 = meanRest.declineMeanXtra(intervention.getId(),mean);
        assertEquals(400,response2.getStatus());
    }


}
