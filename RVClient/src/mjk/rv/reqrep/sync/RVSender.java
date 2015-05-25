package mjk.rv.reqrep.sync;

import java.util.logging.Logger;

import com.tibco.tibrv.*;

public class RVSender implements Runnable {
	private String id;
	private final Logger log = Logger.getLogger("RVSender");
	
	public RVSender(String id) {
		this.id = id;
	}
	
	@Override
	public void run() {
		
		TibrvRvdTransport tr = null;
		try {
			Tibrv.open(Tibrv.IMPL_NATIVE);
			tr = new TibrvRvdTransport(null,null,null); // using defaults
			if(tr.isValid()) {
				log.info("Service: "+tr.getService());
				log.info("Network: "+tr.getNetwork());
				log.info("Daemon: "+tr.getDaemon());
			}
						
			TibrvMsg msg = new TibrvMsg();
			msg.setSendSubject("TEST.HOLZ.REGISTRATION");
			//msg.setReplySubject(tr.createInbox()); // not needed - sendRequest() overwrites any existing reply subject of message with the _INBOX name.
			msg.add("Gebiet", "Hesse");
			msg.add("Stoff", 354.65);
			TibrvMsg replyMsg = tr.sendRequest(msg, 10000); // block and wait for reply for 10 seconds
			log.info("Received response message: "+replyMsg.toString());
			
		} catch (TibrvException e) {
		} finally {
			tr.destroy();
			try { Tibrv.close(); } catch (TibrvException onCloseE) { }
		}
		
	}

}
