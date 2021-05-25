
package com.tcl.dias.serviceactivation.cramer.getclrsync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getCLRResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getCLRResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://com.tatacommunications.cramer.ace.service.async.ws.getclr.ServiceDetail_Async}serviceDetailResponseAsync" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getCLRResponse", propOrder = {
    "_return"
})
@XmlRootElement(name = "getCLRResponse")
public class GetCLRResponse {

    @XmlElement(name = "return")
    protected ServiceDetailResponseAsync _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceDetailResponseAsync }
     *     
     */
    public ServiceDetailResponseAsync getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceDetailResponseAsync }
     *     
     */
    public void setReturn(ServiceDetailResponseAsync value) {
        this._return = value;
    }

}
