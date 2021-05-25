
package com.tcl.dias.serviceactivation.cramer.reverseisc.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getReverseISCDataResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getReverseISCDataResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="reverseISCOutput" type="{http://com.tatacommunications.cramer.reverseisc.ws}reverseISCOutput" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "reverseISCOutput"
})
@XmlRootElement(name="getReverseISCDataResponse")
public class GetReverseISCDataResponse {

    protected ReverseISCOutput reverseISCOutput;

    /**
     * Gets the value of the reverseISCOutput property.
     * 
     * @return
     *     possible object is
     *     {@link ReverseISCOutput }
     *     
     */
    public ReverseISCOutput getReverseISCOutput() {
        return reverseISCOutput;
    }

    /**
     * Sets the value of the reverseISCOutput property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReverseISCOutput }
     *     
     */
    public void setReverseISCOutput(ReverseISCOutput value) {
        this.reverseISCOutput = value;
    }

}
