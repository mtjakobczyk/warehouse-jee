
package pl.mtjakobczyk.schemas.dictionaryfrontend;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the pl.mtjakobczyk.schemas.dictionaryfrontend package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: pl.mtjakobczyk.schemas.dictionaryfrontend
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetStationFault }
     * 
     */
    public GetStationFault createGetStationFault() {
        return new GetStationFault();
    }

    /**
     * Create an instance of {@link GetStationResponse }
     * 
     */
    public GetStationResponse createGetStationResponse() {
        return new GetStationResponse();
    }

    /**
     * Create an instance of {@link GetStation }
     * 
     */
    public GetStation createGetStation() {
        return new GetStation();
    }

}
