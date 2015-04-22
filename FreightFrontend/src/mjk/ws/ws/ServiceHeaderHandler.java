package mjk.ws.ws;

import java.util.Set;

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

public class ServiceHeaderHandler implements SOAPHandler<SOAPMessageContext> {

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		Boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if(outbound) return true; // Outbound
		// Inbound
		try {
			SOAPMessage msg = context.getMessage();
			SOAPHeader header = msg.getSOAPHeader();
			if(header==null) throwFault(msg, "missing soap header");
		} catch(SOAPException se) {
			throw new RuntimeException(se);
		}
		
		
		return false;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close(MessageContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<QName> getHeaders() {
		// TODO Auto-generated method stub
		return null;
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
