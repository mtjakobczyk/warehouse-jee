package test;


import mjk.ems.QueueReceiver;

import org.junit.Test;

public class TestQueueReceiver {
	@Test
	public void testSend() {
		QueueReceiver qs = new QueueReceiver("qr.001");
		qs.run();
	}
}
