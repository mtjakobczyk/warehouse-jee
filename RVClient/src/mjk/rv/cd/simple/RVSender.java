package mjk.rv.cd.simple;

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
		TibrvCmTransport  cmTransport  = null;
	    TibrvCmListener   cmListener   = null;
	    
		try {
			Tibrv.open(Tibrv.IMPL_NATIVE);
			tr = new TibrvRvdTransport("8500",";225.1.1.5",null); // using multicast and custom rvd UPD port
			tr.setDescription("RVSender."+id);
			if(tr.isValid()) {
				log.info("Service: "+tr.getService());
				log.info("Network: "+tr.getNetwork());
				log.info("Daemon: "+tr.getDaemon());
			}
			// Reusable, file-ledger based (sync) - persistent correspondent
			cmTransport  = new TibrvCmTransport(tr,"mjk.rv.simple.cd.rvsender",true,"mjk.rv.simple.cd.rvsender.ledger",true,null);
			//cmTransport.addListener("mjk.rv.simple.cd.rvlistener.ledger", "TEST.SIMPLE.S"); // pre-registration of the listener

			TibrvQueue advisoryQueue = new TibrvQueue();
			TibrvListener advisoryListener = new TibrvListener(advisoryQueue, new AdvisoryMsgCallback(), tr, "_RV.*.RVCM.>", null);
			TibrvDispatcher advisoryDispatcherThread = new TibrvDispatcher("advisoryDispatcherThread.Sender", advisoryQueue, 10);
			
			for(int i=0; i<3; i++) {
				Thread.sleep(1000);
				TibrvMsg msg = new TibrvMsg();
				String targetSubject = "TEST.SIMPLE.S";
				msg.setSendSubject(targetSubject);
				msg.add("SenderSequence",0+i);
				cmTransport.send(msg);
				log.info("Sent SenderSequence="+0+i);
			}
			Thread.sleep(5000);
		} catch (TibrvException | InterruptedException e) {
			log.severe("Exception: "+e.getMessage());
		} finally {
			cmTransport.destroy();
			tr.destroy();
			try { Tibrv.close(); } catch (TibrvException onCloseE) { }
		}
		
	}
	private class AdvisoryMsgCallback implements TibrvMsgCallback {
		@Override
		public void onMsg(TibrvListener arg0, TibrvMsg msg) {
				try {
					log.info("Advisory from ["+msg.getSendSubject()+"]: seqno="+msg.get("seqno"));
				} catch (TibrvException e) {
					log.severe("Error on processing a message: "+e.getMessage());
				}
		}
	}
	
	
}
