
package pl.mtjakobczyk.schemas.freightfrontend;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the pl.mtjakobczyk.schemas.freightfrontend package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: pl.mtjakobczyk.schemas.freightfrontend
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link NewShipment }
     * 
     */
    public NewShipment createNewShipment() {
        return new NewShipment();
    }

    /**
     * Create an instance of {@link GetShipment }
     * 
     */
    public GetShipment createGetShipment() {
        return new GetShipment();
    }

    /**
     * Create an instance of {@link GetShipmentResponse }
     * 
     */
    public GetShipmentResponse createGetShipmentResponse() {
        return new GetShipmentResponse();
    }

    /**
     * Create an instance of {@link NewShipmentResponse }
     * 
     */
    public NewShipmentResponse createNewShipmentResponse() {
        return new NewShipmentResponse();
    }

    /**
     * Create an instance of {@link NewShipmentFault }
     * 
     */
    public NewShipmentFault createNewShipmentFault() {
        return new NewShipmentFault();
    }

}
