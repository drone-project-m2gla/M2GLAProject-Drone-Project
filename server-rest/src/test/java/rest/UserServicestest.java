package rest;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.ClientProtocolException;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserServicestest {

	
	public static String address;

	@BeforeClass
	public static void init() {

		address = "http://localhost:8080/sitserver/rest/user";
	}

	@Test
	public void LoginFailed() throws ClientProtocolException, IOException{

		try {

	        URL url = new URL(address + "/login");
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setDoOutput(true);
	        conn.setRequestMethod("POST");

	        conn.setRequestProperty("Content-Type", "application/json");
	        String input = "{\"username\": \"d\",\"password\": \"u\"}";
	        
	        OutputStream os = conn.getOutputStream();
	        os.write(input.getBytes());
	        os.flush();

	        assertEquals(401, conn.getResponseCode());
	       
	        conn.disconnect();

	      } catch (MalformedURLException e) {

	        e.printStackTrace();

	      } catch (IOException e) {

	        e.printStackTrace();

	     }	
	}

	@Test
	public void LoginOk() throws ClientProtocolException, IOException{

		try {

	        URL url = new URL(address + "/login");
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setDoOutput(true);
	        conn.setRequestMethod("POST");
	        
	        conn.setRequestProperty("Content-Type", "application/json");

	        String input = "{\"username\": \"d\",\"password\": \"l\"}";
	        
	        OutputStream os = conn.getOutputStream();
	        os.write(input.getBytes());
	        os.flush();

	        assertEquals(200, conn.getResponseCode());
	       
	        /*
	        if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
	            throw new RuntimeException("Failed : HTTP error code : "
	                + conn.getResponseCode());
	        }
	        */

	        BufferedReader br = new BufferedReader(new InputStreamReader(
	                (conn.getInputStream())));

	        String output;
	       // System.out.println("Output from Server .... \n");
	        while ((output = br.readLine()) != null) {
	        	if (output =="username"){
	        		assertEquals("\"username\":\"d\"", output);
	        	}
	        	//System.out.println(output);
	        }

	        conn.disconnect();

	      } catch (MalformedURLException e) {

	        e.printStackTrace();

	      } catch (IOException e) {

	        e.printStackTrace();

	     }
	}
}


