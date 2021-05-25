
package com.tcl.dias.serviceactivation.cramer.reverseisc.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getReverseISCData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getReverseISCData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="reverseISCInput" type="{http://com.tatacommunications.cramer.reverseisc.ws}reverseISCInput" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getReverseISCData", propOrder = {
    "reverseISCInput"
})
@XmlRootElement(name="getReverseISCData")
public class GetReverseISCData {

	//@XmlElement(name = "reverseISCInput")
    protected ReverseISCInput reverseISCInput;

    /**
     * Gets the value of the reverseISCInput property.
     * 
     * @return
     *     possible object is
     *     {@link ReverseISCInput }
     *     
     */
    public ReverseISCInput getReverseISCInput() {
        return reverseISCInput;
    }

    /**
     * Sets the value of the reverseISCInput property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReverseISCInput }
     *     
     */
    public void setReverseISCInput(ReverseISCInput value) {
        this.reverseISCInput = value;
    }

}
