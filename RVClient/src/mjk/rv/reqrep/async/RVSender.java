package mjk.rv.reqrep.async;

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

			// Create listener on _INBOX subject for Reply
			String inboxSubject = tr.createInbox();
			
			TibrvListener replyListener = new TibrvListener(Tibrv.defaultQueue(), new TibrvMsgCallback() {
				@Override
				public void onMsg(TibrvListener listener, TibrvMsg msg) {
					log.info("Received response message: "+msg.toString());
				}
			}, tr, inboxSubject, null);
			
			// Send Request
			TibrvMsg msg = new TibrvMsg();
			msg.setSendSubject("TEST.HOLZ.REGISTRATION");
			msg.setReplySubject(inboxSubject); // set _INBOX subject
			msg.add("Gebiet", "Hesse");
			msg.add("Stoff", 354.65);
			tr.send(msg); // send request - do not block
			
			while(true) Tibrv.defaultQueue().dispatch();
			
		} catch (TibrvException | InterruptedException e) {
		} finally {
			tr.destroy();
			try { Tibrv.close(); } catch (TibrvException onCloseE) { }
		}
		
	}

}
