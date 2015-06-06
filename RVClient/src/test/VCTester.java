package test;

import mjk.rv.vc.VCInitiator;
import mjk.rv.vc.VCPeer;

import org.junit.Test;

public class VCTester {
	@Test
	public void test() throws InterruptedException {
		VCPeer vp = new VCPeer("01");
		Thread vpT = new Thread(vp);
		vpT.start();
		
		Thread.sleep(1000);
		
		VCInitiator vi = new VCInitiator("01");
		Thread viT = new Thread(vi);
		viT.start();
		
		Thread.sleep(60000);
	}
}
