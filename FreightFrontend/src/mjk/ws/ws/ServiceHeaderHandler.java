package mjk.ws.ws;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.ws.soap.SOAPFaultException;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mjk.util.xml.nscontext.SimpleNamespaceContext;


public class ServiceHeaderHandler implements SOAPHandler<SOAPMessageContext> {
	private final Logger log = Logger.getLogger("ServiceHeaderHandler");
	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		Boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if(outbound) return true; // do nothing for Outbound messages
		// Inbound
		try {
			SOAPMessage msg = context.getMessage();
			SOAPHeader header = msg.getSOAPHeader();
			if(header==null) throwFault(msg, "missing soap header");
			if(!header.hasChildNodes()) throwFault(msg, "empty soap header");
			
			// Verify {http://www.mtjakobczyk.pl/Schemas/serviceHeader}station has a value
			XPath xPath = null;
			xPath = XPathFactory.newInstance().newXPath();
			Map<String, String> prefMap = new HashMap<String, String>();
	    	prefMap.put("ss", "http://www.mtjakobczyk.pl/Schemas/serviceHeader");
			xPath.setNamespaceContext(new SimpleNamespaceContext(prefMap));
			String station = (String)xPath.evaluate("//ss:station", header.getOwnerDocument(), XPathConstants.STRING);
			if(station == null || station.trim().isEmpty()) throwFault(msg, "missing or empty {http://www.mtjakobczyk.pl/Schemas/serviceHeader}station element in SOAP header"); 
			log.info("Message from station: "+station.trim()+" arrived.");
			
		} catch(SOAPException se) {
			throw new RuntimeException(se);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
		
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		log.warning("Error in SOAP Header processing.");
		return true;
	}

	@Override
	public void close(MessageContext context) { }

	@Override
	public Set<QName> getHeaders() {
		QName stationHeader = new QName("http://www.mtjakobczyk.pl/Schemas/serviceHeader", "station");
		Set<QName> headers = new HashSet<QName>(); 
		headers.add(stationHeader);
		return headers;
	}
	
	private void throwFault(SOAPMessage msg, String reason) {
		try {
			SOAPBody body = msg.getSOAPBody();
			SOAPFault fault = body.addFault();
			fault.setFaultString(reason);
			throw new SOAPFaultException(fault);
		} catch (SOAPException e) {
			throw new RuntimeException(reason);
		}
	}

}
