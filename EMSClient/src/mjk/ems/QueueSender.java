package mjk.ems;

import java.util.logging.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import mjk.util.Methods;

public class QueueSender implements Runnable {
	private String clientID = null;
	private Logger log = null;
	private String jmsProviderURL = "tcp://127.0.0.1:7522";
	private String username = "app.uxu";
	private String password = "app.uxu.pass";
	private String jmsDestination = "mafft.inbound";
	private String queueConnectionFactory = "QueueConnectionFactory";
	
	public QueueSender(String clientID) {
		this.clientID = clientID;
		this.log = Logger.getLogger(this.clientID);
	}
	@Override
	public void run() {
		log.info("run - started");
		Connection connection = null;
		try {
			InitialContext ic = Methods.prepareJNDIContext(jmsProviderURL, username, password, "com.tibco.tibjms.naming.TibjmsInitialContextFactory");
			Queue queue = (Queue)ic.lookup(jmsDestination);
			QueueConnectionFactory queueFactory = (QueueConnectionFactory)ic.lookup(queueConnectionFactory);
			connection = queueFactory.createConnection(username,password);
			connection.setClientID(clientID);
			Session session = connection.createSession(false, javax.jms.Session.CLIENT_ACKNOWLEDGE);
			MessageProducer mp = session.createProducer(queue);
			TextMessage m = session.createTextMessage("Plan text payload");
			m.setStringProperty("action", "updateManufacturerBO");
			mp.send(m);
			log.info("message sent");
			mp.close();
			session.close();
		} catch (NamingException | JMSException e) {
			log.severe("run - failure due to "+e);
		} finally {
			try { connection.close(); } catch (JMSException e) { log.severe("failed to close connection in exception handling"+e); }
			log.info("processing finished");
		}

	}
}
