
package com.tcl.dias.serviceactivation.cramer.downtime.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for downTimeRequiredResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="downTimeRequiredResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Ack" type="{http://cramerserviceslibrary/csvc/wsdl/v1/downtime}acknowledgement" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "downTimeRequiredResponse", propOrder = {
    "ack"
})
public class DownTimeRequiredResponse {

    @XmlElement(name = "Ack")
    protected Acknowledgement ack;

    /**
     * Gets the value of the ack property.
     * 
     * @return
     *     possible object is
     *     {@link Acknowledgement }
     *     
     */
    public Acknowledgement getAck() {
        return ack;
    }

    /**
     * Sets the value of the ack property.
     * 
     * @param value
     *     allowed object is
     *     {@link Acknowledgement }
     *     
     */
    public void setAck(Acknowledgement value) {
        this.ack = value;
    }

}
