package mjk.ejb;

import javax.ejb.Local;

import com.tibco.schemas.handlingschema.Shipment;

@Local
public interface Shipments {
	public Shipment getShipmentByAWB(String awb);
	public com.tibco.schemas.handlingschema.Shipments getAllShipments();
	public boolean putShipment(Shipment newShipment);
}
