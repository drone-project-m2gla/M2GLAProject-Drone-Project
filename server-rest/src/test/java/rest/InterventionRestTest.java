package rest;

import dao.InterventionDAO;
import entity.DisasterCode;
import entity.Intervention;
import entity.Position;
import org.junit.*;
import service.impl.RetrieveAddressImpl;
import util.Configuration;

import javax.ws.rs.core.Response;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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



}
