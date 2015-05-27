package rest;

import entity.User;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class UserServicesRestTest {

    @BeforeClass
    public static void init() {
    }

    @Test
    public void LoginFailed() throws IOException {
        UserServicesRest userServices = new UserServicesRest();
        User user = new User();
        user.setUsername("login");
        user.setPassword("password");
        Response response = userServices.logIn(user);
        assertEquals(401, response.getStatus());
    }

    @Test
    public void LoginOk() throws IOException {
        UserServicesRest userServices = new UserServicesRest();
        Response responseC = userServices.createUser("user_d","user_l");
        assertEquals("200", "" + responseC.getStatus());
        User user = new User();
        user.setUsername("user_d");
        user.setPassword("user_l");
        Response responseL = userServices.logIn(user);
        assertEquals(200, responseL.getStatus());
    }

    @Test
    public void CreateLogin() throws IOException {
        UserServicesRest userServices = new UserServicesRest();
        Response response = userServices.createUser("testlogin", "testpassword");
        assertEquals(200, response.getStatus());
    }
}


