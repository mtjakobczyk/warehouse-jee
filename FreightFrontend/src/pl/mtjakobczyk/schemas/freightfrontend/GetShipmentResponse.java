
package pl.mtjakobczyk.schemas.freightfrontend;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.tibco.schemas.handlingschema.Shipments;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="searchedAWB" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://www.tibco.com/schemas/HandlingSchema}shipments"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "searchedAWB",
    "shipments"
})
@XmlRootElement(name = "getShipmentResponse")
public class GetShipmentResponse {

    @XmlElement(required = true)
    protected String searchedAWB;
    @XmlElement(namespace = "http://www.tibco.com/schemas/HandlingSchema", required = true)
    protected Shipments shipments;

    /**
     * Gets the value of the searchedAWB property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSearchedAWB() {
        return searchedAWB;
    }

    /**
     * Sets the value of the searchedAWB property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSearchedAWB(String value) {
        this.searchedAWB = value;
    }

    /**
     * Gets the value of the shipments property.
     * 
     * @return
     *     possible object is
     *     {@link Shipments }
     *     
     */
    public Shipments getShipments() {
        return shipments;
    }

    /**
     * Sets the value of the shipments property.
     * 
     * @param value
     *     allowed object is
     *     {@link Shipments }
     *     
     */
    public void setShipments(Shipments value) {
        this.shipments = value;
    }

}
