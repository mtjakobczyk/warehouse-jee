package mjk.rv.cd;

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
			
			// Reusable, file-ledger based (sync) - persistner correspondent
			cmTransport  = new TibrvCmTransport(tr,"mjk.name.rvlistener",true,"mjk.name.rvlistener.ledger",true,null);
			
			TibrvQueue highPrioEventQueue = new TibrvQueue();
			highPrioEventQueue.setPriority(5);
			TibrvQueue eventQueue = new TibrvQueue();
			highPrioEventQueue.setPriority(2);
			TibrvQueueGroup queueGroup = new TibrvQueueGroup();
			queueGroup.add(highPrioEventQueue);
			queueGroup.add(eventQueue);
			
			CMMsgCallback cmCallback = new CMMsgCallback();
			TibrvCmListener highPrioListener = new TibrvCmListener(highPrioEventQueue, cmCallback , cmTransport, "TEST.PRIORITY.>", null);
			highPrioListener.setExplicitConfirm();
			cmCallback.setCmL(highPrioListener);
			
			TibrvListener listener = new TibrvListener(eventQueue, new BasicMsgCallback(), tr, "TEST.BASIC.>", null);
			
			TibrvDispatcher dispatcherThread = new TibrvDispatcher("dispatcherThread", queueGroup, 10);
			
			// Wait until dispatcher processes all messages and exits after (idle) timeout
			dispatcherThread.join();
			
			//while(true) Tibrv.defaultQueue().dispatch();
			
				
		} catch (TibrvException | InterruptedException  e ) {
		} finally {
			cmTransport.destroy();
			tr.destroy();
			try { Tibrv.close(); } catch (TibrvException onCloseE) { }
		}
		
	}
	
	private class BasicMsgCallback implements TibrvMsgCallback {

		@Override
		public void onMsg(TibrvListener arg0, TibrvMsg msg) {
			try {
				TibrvMsg stationMsg = (TibrvMsg)msg.getField("Metrics").data;
				log.info("Received message on subject ["+msg.getSendSubject()+"]: "+stationMsg.getField("Ticks").data);
				
			} catch (TibrvException e) {
				log.severe("Error on processing a message: "+e.getMessage());
			}
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
				TibrvMsg stationMsg = (TibrvMsg)msg.getField("Metrics").data;
				int ticks = (int)stationMsg.getField("Ticks").data;
				log.info("Received message on subject ["+msg.getSendSubject()+"] from sender ["+TibrvCmMsg.getSender(msg)+"]: "+ticks);

				// If it was not CM message or very first message we'll get seqno=0
				long seqno = TibrvCmMsg.getSequence(msg);
				if (seqno > 0) 
					if(ticks!=4) cmL.confirmMsg(msg);
				
			} catch (TibrvException e) {
				log.severe("Error on processing a message: "+e.getMessage());
			}
		}
		
	}

}
