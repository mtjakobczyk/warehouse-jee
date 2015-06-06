package mjk.rv.vc;

import java.util.logging.Logger;

import com.tibco.tibrv.*;

public class VCPeer implements Runnable, TibrvMsgCallback {
	private String id;
	private final Logger log = Logger.getLogger("VCPeer");
	
	TibrvRvdTransport tr = null;
	TibrvListener vcTrLn = null;
	
	public VCPeer(String id) {
		this.id = id;
	}
	
	@Override
	public void run() {
		
		try {
			Tibrv.open(Tibrv.IMPL_NATIVE);
			tr = new TibrvRvdTransport(null,null,null); // using defaults
			
			@SuppressWarnings("unused")
			TibrvListener VCRegistrationListener = new TibrvListener(Tibrv.defaultQueue(), new VCRegistrator(), tr, "VC.CONNECTION.REQUEST", null);
		
			while(true) Tibrv.defaultQueue().dispatch();
			
		} catch (TibrvException | InterruptedException e) {
		} finally {
			tr.destroy();
			try { Tibrv.close(); } catch (TibrvException onCloseE) { }
		}
		
	}

	@Override
	public void onMsg(TibrvListener listener, TibrvMsg msg) {
		try {
			log.info("Received a message over VC on subject: "+msg.getSendSubject()+" with Data: "+msg.getField("Data"));
		} catch (TibrvException e) {
			log.severe("Error processing message over VC "+e.getMessage());
		}
	}
	
	private class VCRegistrator implements TibrvMsgCallback {
		@Override
		public void onMsg(TibrvListener listener, TibrvMsg requestMsg) {
			log.info("Received VC request message: "+requestMsg.toString());
			try {
				// Prepare VC transport
				TibrvVcTransport vcTr = TibrvVcTransport.createAcceptVc(tr); 
				String connectSubject = vcTr.getConnectSubject();
				
				// Send connectSubject to initiator
				TibrvMsg replyMsg = new TibrvMsg();
				replyMsg.setSendSubject(requestMsg.getReplySubject());
				replyMsg.setReplySubject(connectSubject);
				tr.send(replyMsg);
				log.info("Sent VC request response: "+replyMsg.toString());
				
				vcTr.waitForVcConnection(10000);
				log.info("VC created on connectSubject: "+connectSubject);
				
				vcTrLn = new TibrvListener(Tibrv.defaultQueue(), VCPeer.this, vcTr, "VC.SOME.SUBJECT", null);
				
			} catch (TibrvException e) {
				log.severe("Error processing VC request "+e.getMessage());
			}
		}
	}

}
