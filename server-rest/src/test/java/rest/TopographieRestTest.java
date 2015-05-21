package rest;


import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.BeforeClass;
import org.junit.Test;


public class TopographieRestTest {
	private static String address;

	@BeforeClass
	public static void init() {
		address = "http://localhost:8080/sitserver/rest/topographie";
	}


	@Test
	public void returnStatus() throws ClientProtocolException, IOException{

		// Given
		HttpUriRequest request = new HttpGet(address + "/1/1/1");

		// When
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

		// Then
		assertEquals(HttpStatus.SC_OK , httpResponse.getStatusLine().getStatusCode());
	}


	@Test
	public void givenRequestWithNoAcceptHeader_whenRequestIsExecuted_thenDefaultResponseContentTypeIsJson()
			throws ClientProtocolException, IOException {
		// Given
		String jsonMimeType = "application/json";
		HttpUriRequest request = new HttpGet(address + "/1/1/1");

		// When
		HttpResponse response = HttpClientBuilder.create().build()
				.execute(request);

		// Then
		String mimeType = ContentType.getOrDefault(response.getEntity())
				.getMimeType();
		assertEquals(jsonMimeType, mimeType);

	}
}
