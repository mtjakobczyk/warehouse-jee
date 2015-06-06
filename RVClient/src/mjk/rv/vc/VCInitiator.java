package mjk.rv.vc;

import java.util.logging.Logger;

import com.tibco.tibrv.*;

public class VCInitiator implements Runnable {
	private String id;
	private final Logger log = Logger.getLogger("VCInitiator");
	
	public VCInitiator(String id) {
		this.id = id;
	}
	
	@Override
	public void run() {
		TibrvRvdTransport tr = null;
		TibrvVcTransport vcTr = null;
		try {
			Tibrv.open(Tibrv.IMPL_NATIVE);
			tr = new TibrvRvdTransport(null,null,null); // using defaults
			
			// Request for VC connectSubject
			TibrvMsg msg = new TibrvMsg();
			msg.setSendSubject("VC.CONNECTION.REQUEST");
			TibrvMsg replyMsg = tr.sendRequest(msg,5000);
			
			if(replyMsg == null) {
				log.warning("No connectSubject received within time limit.");
				return;
			}
			// create connect VC
			String connectSubject = replyMsg.getReplySubject();
			vcTr = TibrvVcTransport.createConnectVc(connectSubject,tr);
			
			// The connect object in A automatically initiates a protocol to establish the virtual circuit connection between the two terminal objects.
			
			// When the connection is complete (that is, ready to use) both terminals present VC.CONNECTED advisories
			vcTr.waitForVcConnection(10000);
			log.info("VC created on connectSubject: "+connectSubject);
			
			// Communication over VC
			for(int i=0;i<10;i++) {
				TibrvMsg msgOverVC = new TibrvMsg();
				msgOverVC.setSendSubject("VC.SOME.SUBJECT");
				msgOverVC.add("Data", "Message number "+i);
				Thread.sleep(1000);
				vcTr.send(msgOverVC);
			}
			vcTr.destroy();
			
		} catch (InterruptedException ie) {	
			log.info("InterruptedException: "+ie.getMessage());
		} catch (TibrvException te) {
			log.info("TibrvException: "+te.getMessage());
		} finally {
			tr.destroy();
			try { Tibrv.close(); } catch (TibrvException onCloseE) { }
		}
		
	}

}
