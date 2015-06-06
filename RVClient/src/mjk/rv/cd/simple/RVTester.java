package mjk.rv.cd.simple;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import mjk.rv.cd.simple.RVSender;

import org.junit.Before;
import org.junit.Test;


public class RVTester {
	@Before
	public void removeLedgers() throws IOException {
		boolean rvs = Files.deleteIfExists(Paths.get("C:\\git\\github\\RVClient\\mjk.rv.simple.cd.rvsender.ledger"));
		Files.deleteIfExists(Paths.get("C:\\git\\github\\RVClient\\mjk.rv.simple.cd.rvlistener.ledger"));
		System.out.println("mjk.rv.simple.cd.rvsender.ledger - "+rvs);
	}
	
	@Test
	public void test() throws InterruptedException {
		RVListener l = new RVListener("01");
		Thread lT = new Thread(l);
		lT.start();
		
		Thread.sleep(5000);
		
		RVSender s = new RVSender("01");
		Thread sT = new Thread(s);
		sT.start();
		
		sT.join();
		lT.join(); // Wait until listening thread finishes
	}
}
