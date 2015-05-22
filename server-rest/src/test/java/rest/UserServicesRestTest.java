package rest;

import entity.User;
import org.apache.http.client.ClientProtocolException;
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
        UserServices userServices = new UserServices();
        User user = new User();
        user.setUsername("login");
        user.setPassword("password");
        Response response = userServices.logIn(user);
        assertEquals(401, response.getStatus());
    }

    @Test
    public void LoginOk() throws IOException {
        UserServices userServices = new UserServices();
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
        UserServices userServices = new UserServices();
        Response response = userServices.createUser("testlogin", "testpassword");
        assertEquals(200, response.getStatus());
    }
}


