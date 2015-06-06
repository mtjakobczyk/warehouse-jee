package mjk.rv.cd;

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
		TibrvCmTransport  cmTransport  = null;
	    TibrvCmListener   cmListener   = null;
	    
		try {
			Tibrv.open(Tibrv.IMPL_NATIVE);
			tr = new TibrvRvdTransport("8500",";225.1.1.5",null); // using multicast and custom rvd UPD port
			if(tr.isValid()) {
				log.info("Service: "+tr.getService());
				log.info("Network: "+tr.getNetwork());
				log.info("Daemon: "+tr.getDaemon());
			}
			// Reusable, file-ledger based (sync) - persistner correspondent
			cmTransport  = new TibrvCmTransport(tr,"mjk.name.rvsender",true,"mjk.name.rvsender.ledger",true,null);
			
			for(int i=0; i<10; i++) {
				if(i==0) Thread.sleep(2000);
				TibrvMsg metricsMsg = new TibrvMsg();
				metricsMsg.add("Ticks",0+i);
				metricsMsg.add("PeriodInSecs",1);
				metricsMsg.add("MetricStart", new Date());
				
				TibrvMsg stationMsg = new TibrvMsg();
				stationMsg.add("StationCode","WAW");
				stationMsg.add("StationName","Warszawa");
				
				TibrvMsg msg = new TibrvMsg();
				String targetSubject = (i%2==1)?"TEST.BASIC.A":"TEST.PRIORITY.B";
				msg.setSendSubject(targetSubject);
				msg.add("Station", stationMsg);
				msg.add("Metrics", metricsMsg);
//				tr.send(msg);
				cmTransport.send(msg);
			}
			Thread.sleep(5000);
		} catch (TibrvException | InterruptedException e) {
		} finally {
			cmTransport.destroy();
			tr.destroy();
			try { Tibrv.close(); } catch (TibrvException onCloseE) { }
		}
		
	}

}
