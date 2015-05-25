package mjk.rv.complex;

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
			tr = new TibrvRvdTransport(null,null,null); // using defaults
			tr.setDescription("RVListener."+id);
			if(tr.isValid()) {
				log.info("Service: "+tr.getService());
				log.info("Network: "+tr.getNetwork());
				log.info("Daemon: "+tr.getDaemon());
			}
			
			TibrvListener listener = new TibrvListener(Tibrv.defaultQueue(), new TibrvMsgCallback() {
				@Override
				public void onMsg(TibrvListener arg0, TibrvMsg msg) {
					try {
						msg.get("Metrics");
						
						// iteration over root level fields
						int fieldNb = msg.getNumFields(); 
						for(int i=0;i<fieldNb; i++) {
							TibrvMsgField field = msg.getFieldByIndex(i);
							log.info("Field: ["+field.name+"] Value: ["+field.data+"]");
						}
						// String representation of the message
						log.info("Received message: "+msg.toString());
						
					} catch (TibrvException e) {
						log.severe("Error on processing a message: "+e.getMessage());
					}
				}
			}, tr, "TEST.HOLZ.>", null);
			
			while(true) Tibrv.defaultQueue().dispatch();
			
				
		} catch (TibrvException | InterruptedException e) {
		} finally {
			tr.destroy();
			try { Tibrv.close(); } catch (TibrvException onCloseE) { }
		}
		
	}

}
