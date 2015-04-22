package mjk.ws.rs;

import java.net.URI;
import java.time.OffsetDateTime;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.tibco.schemas.handlingschema.Shipment;

import mjk.ejb.Shipments;

@ManagedBean
@Path(value = "/shipment")
public class ShipmentResource {
	@EJB
	private Shipments sb;
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("all")
	public Response getShipments() {
		com.tibco.schemas.handlingschema.Shipments ss = sb.getAllShipments();
		return Response.ok(ss ,MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("{awb}")
	public Response getShipmentByAWB(@PathParam("awb") String awb) {
		Shipment s = sb.getShipmentByAWB(awb);
		return Response.ok(s ,MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Produces({MediaType.TEXT_PLAIN})
	@Path("new")
	public Response postNewShipment(
			@Context UriInfo uriInfo,
			@FormParam("awb") String awb, 
			@FormParam("origin") String origin,
			@FormParam("destination") String destination,
			@FormParam("shippingDate") String shippingDate) {
		
		if(awb == null || origin == null || destination == null) 
			return Response.status(Response.Status.BAD_REQUEST).entity("awb, origin and destination are mandatory"+System.lineSeparator()).type(MediaType.TEXT_PLAIN).build();
		
		Shipment s = new Shipment();
    	s.setAwb(awb);
    	s.setOrigin(origin);
    	s.setDestination(destination);
    	s.setShippingDate(OffsetDateTime.now());
    	
    	sb.putShipment(s);

    	URI uri = uriInfo.getBaseUriBuilder().path("shipment").path(awb).build();
    	return Response.seeOther(uri).build();
    	//return Response.ok("Shipment with AWB "+awb+" created."+System.lineSeparator(),MediaType.TEXT_PLAIN).build();
	}
}
