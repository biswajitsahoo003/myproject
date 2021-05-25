
package com.tcl.dias.serviceactivation.cramer.ipservicesync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SetClrResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SetClrResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ack" type="{http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices}cramerIPServiceRequestAck" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SetClrResponse", propOrder = {
    "ack"
})
public class SetClrResponse {

    protected CramerIPServiceRequestAck ack;

    /**
     * Gets the value of the ack property.
     * 
     * @return
     *     possible object is
     *     {@link CramerIPServiceRequestAck }
     *     
     */
    public CramerIPServiceRequestAck getAck() {
        return ack;
    }

    /**
     * Sets the value of the ack property.
     * 
     * @param value
     *     allowed object is
     *     {@link CramerIPServiceRequestAck }
     *     
     */
    public void setAck(CramerIPServiceRequestAck value) {
        this.ack = value;
    }

}
