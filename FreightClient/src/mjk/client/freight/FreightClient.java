package mjk.client.freight;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.logging.Logger;

public class FreightClient {
	private final Logger log = Logger.getLogger("FreightClient");
	
	
	public void postNewShipmentJavaNet(String urlString) throws IOException {
		// Standard java.net HTTP POST call
		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection(); // URLConnection instance does not establish the actual network 
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); // default anyway
		String encodedPayload = "awb=903&origin=FRA&destination=GDA";
		byte[] encodedPayloadInBytes = encodedPayload.getBytes("UTF-8");
		
		con.setDoOutput(true); // enabling upload (output) to URL
		
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(encodedPayloadInBytes);
		wr.flush();
		wr.close();
		
		log.info("Response Code:"+con.getResponseCode()); // response data needed first time - in background executes HttpURLConnection::connect()

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String line; while( (line = in.readLine()) != null) log.info(line);	
		in.close();
	}
	
	public void postNewShipmentJersey(String urlString) {
		// Jersey custom REST client code
		log.info("POST newShipment (Jersey) - started.");
		Client c = Client.create();
		c.setFollowRedirects(true);
		WebResource res;
		res = c.resource(urlString);
		Form f = new Form();
		f.add("awb", "112");
		f.add("origin", "FRA");
		f.add("destination", "WAW");
		log.info("Response received: "+res.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).accept(MediaType.TEXT_PLAIN_TYPE).post(String.class,f));
		log.info("POST newShipment (Jersey) - ended.");
	}
	
	public void fillData() throws IOException {
		String urlStr = "http://127.0.0.1:8080/FreightFrontend/ra/shipment/new";
//		postNewShipmentJersey(urlStr);
		postNewShipmentJavaNet(urlStr);
	}

}
