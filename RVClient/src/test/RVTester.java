package test;

import mjk.rv.RVListener;
import mjk.rv.RVSender;

import org.junit.Test;

public class RVTester {
	@Test
	public void test() {
		RVListener l = new RVListener("01");
		l.run();
		
//		RVSender s = new RVSender("01");
//		s.run();
	}
}
