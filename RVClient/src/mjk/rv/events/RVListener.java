package mjk.rv.events;

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
		try {
			Tibrv.open(Tibrv.IMPL_NATIVE);
			tr = new TibrvRvdTransport("8500",";225.1.1.5",null); // using multicast and custom rvd UPD port
			tr.setDescription("RVListener."+id);
			if(tr.isValid()) {
				log.info("Service: "+tr.getService());
				log.info("Network: "+tr.getNetwork());
				log.info("Daemon: "+tr.getDaemon());
			}
			
			TibrvQueue highPrioEventQueue = new TibrvQueue();
			highPrioEventQueue.setPriority(5);
			TibrvQueue eventQueue = new TibrvQueue();
			highPrioEventQueue.setPriority(2);
			TibrvQueueGroup queueGroup = new TibrvQueueGroup();
			queueGroup.add(highPrioEventQueue);
			queueGroup.add(eventQueue);
			
			TibrvListener highPrioListener = new TibrvListener(highPrioEventQueue, new BasicMsgCallback(), tr, "TEST.PRIORITY.>", null);
			TibrvListener listener = new TibrvListener(eventQueue, new BasicMsgCallback(), tr, "TEST.BASIC.>", null);
			
			TibrvDispatcher dispatcherThread = new TibrvDispatcher("dispatcherThread", queueGroup, 10);
			
			// Wait until dispatcher processes all messages and exits after (idle) timeout
			dispatcherThread.join();
			
			//while(true) Tibrv.defaultQueue().dispatch();
			
				
		} catch (TibrvException | InterruptedException  e ) {
		} finally {
			tr.destroy();
			try { Tibrv.close(); } catch (TibrvException onCloseE) { }
		}
		
	}
	
	private class BasicMsgCallback implements TibrvMsgCallback {

		@Override
		public void onMsg(TibrvListener arg0, TibrvMsg msg) {
			try {
				msg.get("Metrics");
				
				// iteration over root level fields
				int fieldNb = msg.getNumFields(); 
				for(int i=0;i<fieldNb; i++) {
					TibrvMsgField field = msg.getFieldByIndex(i);
					//log.info("Field: ["+field.name+"] Value: ["+field.data+"]");
				}
				// String representation of the message
				log.info("Received message on subject ["+msg.getSendSubject()+"]: "+msg.toString());
				
			} catch (TibrvException e) {
				log.severe("Error on processing a message: "+e.getMessage());
			}
		}
		
	}

}
