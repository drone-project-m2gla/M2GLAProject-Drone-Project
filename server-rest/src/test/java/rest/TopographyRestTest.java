package rest;


import dao.GeoIconDAO;
import entity.GeoIcon;
import entity.Position;
import org.junit.*;
import util.Configuration;

import static org.junit.Assert.*;
import javax.ws.rs.core.Response;
import java.util.List;


public class TopographyRestTest {

    private static GeoIconDAO dao = new GeoIconDAO();

	@BeforeClass
	public static void init() {
        Configuration.loadConfigurations();
    }

    @AfterClass
    public static void afterAllTests() {

    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void testLogIn(){
        GeoIcon icon = new GeoIcon();
        Position position = new Position(-1.63847,48.117121);
        icon.setEntitled("danger");
        icon.setFilename("danger");
        icon.setPosition(position);
        icon.setColor("OOOOff");
        icon.setTiret(true);
        icon.setFirstContent("");
        icon.setSecondContent("");

        TopographyRest topographyRest = new TopographyRest();
        Response response = topographyRest.logIn(icon);

        assertEquals(201, response.getStatus());

        dao.connect();
        dao.delete(icon);
        dao.disconnect();

    }

    @Test
    public void TestLogInMalformedIconFails(){
        GeoIcon icon = new GeoIcon();
        Position position = new Position(-1.63847,48.117121);
        icon.setFilename("danger");
        icon.setPosition(position);

        TopographyRest topographyRest = new TopographyRest();
        Response response = topographyRest.logIn(icon);
        assertEquals(406,response.getStatus());

        dao.connect();
        dao.delete(icon);
        dao.disconnect();
    }

    /**
     *
     * This method is not used and should be improved in the future when used.
     *
     */
    @Test
    public void TestGetCoordinatedIcons() {
        dao.connect();
        List<GeoIcon> geoIconListFromDao = dao.getAll();
        TopographyRest topographyRest = new TopographyRest();
        List<GeoIcon> geoIconList= topographyRest.getCoordinatedIcons(1.0,1.0,2);
        assertEquals(geoIconListFromDao,geoIconList);
    }


}
