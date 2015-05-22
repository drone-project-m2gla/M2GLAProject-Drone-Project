package rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import entity.User;
import org.apache.http.client.ClientProtocolException;
import org.junit.BeforeClass;
import org.junit.Test;
import javax.ws.rs.core.Response;

public class UserServicesRestTest {


    public static String address;

    @BeforeClass
    public static void init() {
    }

    @Test
    public void LoginFailed() throws ClientProtocolException, IOException {
        UserServices userServices = new UserServices();
        User user = new User();
        user.setUsername("login");
        user.setPassword("password");
        Response response = userServices.logIn(user);
        assertEquals("401", "" + response.getStatus());
    }

    @Test
    public void LoginOk() throws ClientProtocolException, IOException {
        UserServices userServices = new UserServices();
        Response responseC = userServices.createUser("user_d","user_l");
        assertEquals("200", "" + responseC.getStatus());
        User user = new User();
        user.setUsername("user_d");
        user.setPassword("user_l");
        Response responseL = userServices.logIn(user);
        assertEquals("200", "" + responseL.getStatus());
    }

    @Test
    public void CreateLogin() throws ClientProtocolException, IOException {
        UserServices userServices = new UserServices();
        Response response = userServices.createUser("testlogin", "testpassword");
        assertEquals("200", "" + response.getStatus());
    }
}


