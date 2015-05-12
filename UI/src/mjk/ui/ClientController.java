package mjk.ui;

import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

@ManagedBean
@RequestScoped
public class ClientController {
	private final Logger log = Logger.getLogger("ClientController");
	
	String payload;

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}
	public String doSendRequest() {
		// Send SOAP Request using Dispatch API
		log.info("Sending SOAP Request: "+ System.getProperty("line.separator")+payload);
		
		QName serviceName = new QName("http://www.mtjakobczyk.pl/contracts/FreightFrontend/","FreightService");
		Service service = Service.create(serviceName);
		QName portName = new QName("http://www.mtjakobczyk.pl/contracts/FreightFrontend/","IFreight");
		String endpointAddress = "http://127.0.0.1:8080/FreightFrontend/FreightService";
		service.addPort(portName, SOAPBinding.SOAP12HTTP_BINDING, endpointAddress);
		Dispatch<SOAPMessage> dispatch = service.createDispatch(portName, SOAPMessage.class, Service.Mode.MESSAGE);
		
		try {
			MessageFactory mf = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
			
			SOAPMessage request = mf.createMessage();
			SOAPPart part = request.getSOAPPart();
			SOAPEnvelope env = part.getEnvelope();
			SOAPHeader header = env.getHeader();
			SOAPElement stationElement = header.addChildElement("station","ss","http://www.mtjakobczyk.pl/Schemas/serviceHeader");
			stationElement.addTextNode("FRA");
			SOAPBody body = env.getBody();
			
			SOAPElement operationElement = body.addChildElement("newShipment", "fre", "http://www.mtjakobczyk.pl/Schemas/FreightFrontend");
			SOAPElement shipmentElement = operationElement.addChildElement("shipment","han","http://www.tibco.com/schemas/HandlingSchema");
			SOAPElement awbElement = shipmentElement.addChildElement("awb","han","http://www.tibco.com/schemas/HandlingSchema");
			awbElement.addTextNode("301");
			SOAPElement originElement = shipmentElement.addChildElement("origin","han","http://www.tibco.com/schemas/HandlingSchema");
			originElement.addTextNode("KRK");
			SOAPElement destinationElement = shipmentElement.addChildElement("destination","han","http://www.tibco.com/schemas/HandlingSchema");
			destinationElement.addTextNode("GDA");
			request.saveChanges();
			
			SOAPMessage response = dispatch.invoke(request);
			
			log.info("Received SOAP Response: "+ System.getProperty("line.separator")+response.toString());
		} catch (SOAPException e) {
			e.printStackTrace();
		}
		
		return "sent.xhtml";
	}
}
