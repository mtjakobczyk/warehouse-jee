package mjk.ejb;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.tibco.schemas.handlingschema.Shipment;

/**
 * Session Bean implementation class ShipmentsBean
 */
@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class ShipmentsBean implements Shipments {
	private final Logger log = Logger.getLogger("SingletonBean");
	private Map<String,Shipment> shipments = new HashMap<>();
 
    public ShipmentsBean() {
    	log.info("Singleton instance created - constructor.");
    }
    
    @PostConstruct
    private void postConstruct() {
    	Shipment s = new Shipment();
    	s.setAwb("TEST");
    	s.setOrigin("FRA");
    	s.setDestination("WAW");
    	s.setShippingDate(OffsetDateTime.now());
    	shipments.put("TEST", s);
    	log.info("Singleton instance initialized - @PostConstruct ended.");
    }
    
	@Override
	@javax.ejb.Lock(javax.ejb.LockType.READ)
	public Shipment getShipmentByAWB(String awb) {
		log.info("Query for AWB "+awb);
		Shipment result =  shipments.get(awb);
		if(result==null) log.warning("AWB "+awb+" not found");
		return result;
	}
	
	@Override
	@javax.ejb.Lock(javax.ejb.LockType.READ)
	public com.tibco.schemas.handlingschema.Shipments getAllShipments() {
		log.info("Query for all shipments");
		com.tibco.schemas.handlingschema.Shipments ss = new com.tibco.schemas.handlingschema.Shipments();
		List<Shipment> list = ss.getShipment();
		
		shipments.values().stream().sorted(
				new Comparator<Shipment>() {
						@Override
						public int compare(Shipment left, Shipment right) {
							return left.getAwb().compareTo(right.getAwb());
						}		
					}
				).forEachOrdered(s -> list.add(s));
		
		return ss;
	}	
	
	@Override
	@javax.ejb.Lock(javax.ejb.LockType.WRITE)
	public boolean putShipment(Shipment newShipment) {
		if(newShipment==null) {
			log.warning("Request to put an empty Shipment - ignored");
			return false;
		}
		if(newShipment.getAwb()==null) {
			log.warning("Request to put a Shipment with an empty AWB - ignored");
			return false;
		}
		log.info("Request to put new Shipment with AWB "+newShipment.getAwb()+" from "+newShipment.getOrigin()+" to "+newShipment.getDestination());
		if(shipments.containsKey(newShipment.getAwb())) {
			log.warning("Request to put a Shipment with already existing AWB "+newShipment.getAwb()+" - ignored");
			return false;			
		}
		shipments.put(newShipment.getAwb(), newShipment);
		log.info("Request to put new Shipment with AWB "+newShipment.getAwb()+" - succeeded");
		return true;
	}

	@PreDestroy
	public void preDestoy() {
		log.info("Singleton instance destroyed - @PreDestroy ended.");
	}
}
