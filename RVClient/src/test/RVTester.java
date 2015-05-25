package test;

import mjk.rv.reqrep.async.RVListener;
import mjk.rv.reqrep.async.RVSender;

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
		
		Thread.sleep(60000);
	}
}
