package mjk.ws.ws;

import java.time.OffsetDateTime;

import javax.ejb.EJB;
import javax.jws.WebService;
import javax.xml.ws.Holder;

import pl.mtjakobczyk.contracts.freightfrontend.IFreight;
import pl.mtjakobczyk.contracts.freightfrontend.NewShipmentFault;

import com.tibco.schemas.handlingschema.Shipment;
import com.tibco.schemas.handlingschema.Shipments;

@WebService(serviceName="FreightService", targetNamespace="http://www.mtjakobczyk.pl/services/FreightFrontend/")
//@HandlerChain(file = "handler-chain.xml")
public class ServiceFreight implements IFreight {
	@EJB
	private mjk.ejb.Shipments sb;
	
	@Override
	public void getShipment(Holder<String> searchedAWB, Holder<Shipments> shipments) {
		Shipment s = sb.getShipmentByAWB(searchedAWB.value);
		Shipments ss = new Shipments();
		if(s!=null) ss.getShipment().add(s);
		shipments.value = ss;
	}

	@Override
	public void newShipment(Holder<Shipment> shipment) throws NewShipmentFault {
		pl.mtjakobczyk.schemas.freightfrontend.NewShipmentFault nsf = new pl.mtjakobczyk.schemas.freightfrontend.NewShipmentFault();
		nsf.setFaultinfo("newShipment operation fault");
		
		if(shipment==null || shipment.value==null) throw new NewShipmentFault("Invalid request.",nsf);
		if(sb.getShipmentByAWB(shipment.value.getAwb())!=null) throw new NewShipmentFault("Shipment already exists.",nsf);
		
    	shipment.value.setShippingDate(OffsetDateTime.now());
    	
    	sb.putShipment(shipment.value);

	}

}
