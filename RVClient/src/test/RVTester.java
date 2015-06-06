package test;

import mjk.rv.cd.RVListener;
import mjk.rv.cd.RVSender;

import org.junit.Test;

public class RVTester {
	@Test
	public void test() throws InterruptedException {
		RVListener l = new RVListener("01");
		Thread lT = new Thread(l);
		lT.start();
		
		Thread.sleep(1000);
		
		RVSender s = new RVSender("01");
		Thread sT = new Thread(s);
		sT.start();
		
		lT.join(); // Wait until listening thread finishes
	}
}
