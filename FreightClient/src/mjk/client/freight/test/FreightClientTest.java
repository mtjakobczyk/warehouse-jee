package mjk.client.freight.test;

import java.io.IOException;

import mjk.client.freight.FreightClient;

import org.junit.Test;

public class FreightClientTest {

	@Test
	public void testJersey() throws IOException {
		FreightClient fc = new FreightClient();
		fc.fillData();
	}
	
	

}
