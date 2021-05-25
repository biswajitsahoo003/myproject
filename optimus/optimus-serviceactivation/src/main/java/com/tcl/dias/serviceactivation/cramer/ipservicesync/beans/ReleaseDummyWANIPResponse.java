
package com.tcl.dias.serviceactivation.cramer.ipservicesync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for releaseDummyWANIPResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="releaseDummyWANIPResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices}ReleaseDummyWANIPResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "releaseDummyWANIPResponse", propOrder = {
    "_return"
})
@XmlRootElement(name ="releaseDummyWANIPResponse") 
public class ReleaseDummyWANIPResponse {

    @XmlElement(name = "return")
    protected ReleaseDummyWANIPResponse2 _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link ReleaseDummyWANIPResponse2 }
     *     
     */
    public ReleaseDummyWANIPResponse2 getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReleaseDummyWANIPResponse2 }
     *     
     */
    public void setReturn(ReleaseDummyWANIPResponse2 value) {
        this._return = value;
    }

}
