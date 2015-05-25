package mjk.rv.complex;

import java.util.Date;
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
			
			TibrvMsg metricsMsg = new TibrvMsg();
			metricsMsg.add("Ticks",231.26);
			metricsMsg.add("PeriodInSecs",60.00);
			metricsMsg.add("MetricStart", new Date());
			
			TibrvMsg stationMsg = new TibrvMsg();
			stationMsg.add("StationCode","WAW");
			stationMsg.add("StationName","Warszawa");
			
			TibrvMsg msg = new TibrvMsg();
			msg.setSendSubject("TEST.HOLZ.REGISTER");
			msg.add("Station", stationMsg);
			msg.add("Metrics", metricsMsg);
			tr.send(msg);
			
		} catch (TibrvException e) {
		} finally {
			tr.destroy();
			try { Tibrv.close(); } catch (TibrvException onCloseE) { }
		}
		
	}

}
