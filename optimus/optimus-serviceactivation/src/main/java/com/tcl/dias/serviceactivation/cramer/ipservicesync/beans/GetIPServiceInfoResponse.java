
package com.tcl.dias.serviceactivation.cramer.ipservicesync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getIPServiceInfoResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getIPServiceInfoResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices}IPServiceInfoResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getIPServiceInfoResponse", propOrder = {
    "_return"
})
@XmlRootElement(name = "getIPServiceInfoResponse")
public class GetIPServiceInfoResponse {

    @XmlElement(name = "return")
    protected IPServiceInfoResponse _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link IPServiceInfoResponse }
     *     
     */
    public IPServiceInfoResponse getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPServiceInfoResponse }
     *     
     */
    public void setReturn(IPServiceInfoResponse value) {
        this._return = value;
    }

	@Override
	public String toString() {
		return "GetIPServiceInfoResponse [_return=" + _return + "]";
	}
    
    

}
