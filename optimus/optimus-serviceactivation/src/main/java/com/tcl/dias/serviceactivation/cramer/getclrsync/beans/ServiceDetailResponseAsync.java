
package com.tcl.dias.serviceactivation.cramer.getclrsync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for serviceDetailResponseAsync complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="serviceDetailResponseAsync">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ack" type="{http://com.tatacommunications.cramer.ace.service.async.ws.getclr.ServiceDetail_Async}cramerServiceRequestAck" minOccurs="0"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceDetailResponseAsync", propOrder = {
    "ack",
    "status"
})
public class ServiceDetailResponseAsync {

    protected CramerServiceRequestAck ack;
    protected String status;

    /**
     * Gets the value of the ack property.
     * 
     * @return
     *     possible object is
     *     {@link CramerServiceRequestAck }
     *     
     */
    public CramerServiceRequestAck getAck() {
        return ack;
    }

    /**
     * Sets the value of the ack property.
     * 
     * @param value
     *     allowed object is
     *     {@link CramerServiceRequestAck }
     *     
     */
    public void setAck(CramerServiceRequestAck value) {
        this.ack = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

}
