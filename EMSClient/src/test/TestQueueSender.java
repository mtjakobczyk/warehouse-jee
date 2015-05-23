package test;

import mjk.ems.QueueSender;

import org.junit.Test;

public class TestQueueSender {
	@Test
	public void testSend() {
		QueueSender qs = new QueueSender("qs.001");
		qs.run();
	}
}
