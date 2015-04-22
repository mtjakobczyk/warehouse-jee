package mjk.client.freight;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;
import java.util.logging.Logger;

public class FreightClient {
	private final Logger log = Logger.getLogger("FreightClient");
	
	public void fillData() {
		log.info("FillData - started.");
		Client c = Client.create();
		c.setFollowRedirects(true);
		WebResource res;
		
		res = c.resource("http://127.0.0.1:8080/FreightFrontend/ra/shipment/new");
		log.info("Response received: "+postNewShipment(res,"112","FRA","WAW"));
		log.info("Response received: "+postNewShipment(res,"318","FRA","MLT"));
		log.info("Response received: "+postNewShipment(res,"229","KRK","WAW"));
		log.info("Response received: "+postNewShipment(res,"041","FRA","WAW"));
		log.info("FillData - ended.");
	}

	private String postNewShipment(WebResource res, String awb, String o, String d) {
		Form f = new Form();
		f.add("awb", awb);
		f.add("origin", o);
		f.add("destination", d);
		return res.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).accept(MediaType.TEXT_PLAIN_TYPE).post(String.class,f);
	}
}
