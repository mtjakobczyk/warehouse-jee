package mjk.rv.cd.simple;

import java.util.logging.Logger;

import com.tibco.tibrv.*;

public class RVListener implements Runnable {
	private String id;
	private final Logger log = Logger.getLogger("RVListener");
	
	public RVListener(String id) {
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
			tr.setDescription("RVListener."+id);
			if(tr.isValid()) {
				log.info("Service: "+tr.getService());
				log.info("Network: "+tr.getNetwork());
				log.info("Daemon: "+tr.getDaemon());
			}
			
			TibrvQueue advisoryQueue = new TibrvQueue();
			TibrvListener advisoryListener = new TibrvListener(advisoryQueue, new AdvisoryMsgCallback(), tr, "_RV.*.RVCM.>", null);
			TibrvDispatcher advisoryDispatcherThread = new TibrvDispatcher("advisoryDispatcherThread.Sender", advisoryQueue, 10);
			
			// Reusable, file-ledger based (sync) - persistner correspondent
			cmTransport  = new TibrvCmTransport(tr,"mjk.rv.simple.cd.rvlistener",true,"mjk.rv.simple.cd.rvlistener.ledger",true,null);
			
			TibrvQueue eventQueue = new TibrvQueue();
			
			CMMsgCallback cmCallback = new CMMsgCallback();
			cmListener = new TibrvCmListener(eventQueue, cmCallback , cmTransport, "TEST.SIMPLE.S", null);
			cmListener.setExplicitConfirm();
			cmCallback.setCmL(cmListener);
			
			TibrvDispatcher dispatcherThread = new TibrvDispatcher("dispatcherThread", eventQueue, 10);
			
			// Wait until dispatcher processes all messages and exits after (idle) timeout
			dispatcherThread.join();
			
		} catch (TibrvException | InterruptedException  e ) {
			log.severe("Exception: "+e.getMessage());
		} finally {
			cmTransport.destroy();
			tr.destroy();
			try { Tibrv.close(); } catch (TibrvException onCloseE) { }
		}
		
	}
	
	private class CMMsgCallback implements TibrvMsgCallback {
		private TibrvCmListener cmL = null;
		
		public void setCmL(TibrvCmListener cmL) {
			this.cmL = cmL;
		}

		@Override
		public void onMsg(TibrvListener arg0, TibrvMsg msg) {
			try {
				log.info("Received message on subject ["+msg.getSendSubject()+"] from sender ["+TibrvCmMsg.getSender(msg)+"]: "+msg.get("SenderSequence"));

				// If it was not CM message or very first message we'll get seqno=0
				long seqno = TibrvCmMsg.getSequence(msg);
				//Thread.sleep(100);
				cmL.confirmMsg(msg);
				
			} catch (TibrvException /*| InterruptedException */ e) {
				log.severe("Error on processing a message: "+e.getMessage());
			}
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
