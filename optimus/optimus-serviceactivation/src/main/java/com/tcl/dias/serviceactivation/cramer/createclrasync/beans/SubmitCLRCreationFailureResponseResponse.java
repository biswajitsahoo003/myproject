
package com.tcl.dias.serviceactivation.cramer.createclrasync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SubmitCLRCreationFailureAck" type="{http://ACE_CLRCreation_Module}CLRCreationDetailsAcknowledgement"/&gt;
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
    "submitCLRCreationFailureAck"
})
@XmlRootElement(name = "submitCLRCreationFailureResponseResponse", namespace = "http://ACE_CLRCreation_Module/GetCLRCreationDetails")
public class SubmitCLRCreationFailureResponseResponse {

    @XmlElement(name = "SubmitCLRCreationFailureAck", required = true, nillable = true)
    protected CLRCreationDetailsAcknowledgement submitCLRCreationFailureAck;

    /**
     * Gets the value of the submitCLRCreationFailureAck property.
     * 
     * @return
     *     possible object is
     *     {@link CLRCreationDetailsAcknowledgement }
     *     
     */
    public CLRCreationDetailsAcknowledgement getSubmitCLRCreationFailureAck() {
        return submitCLRCreationFailureAck;
    }

    /**
     * Sets the value of the submitCLRCreationFailureAck property.
     * 
     * @param value
     *     allowed object is
     *     {@link CLRCreationDetailsAcknowledgement }
     *     
     */
    public void setSubmitCLRCreationFailureAck(CLRCreationDetailsAcknowledgement value) {
        this.submitCLRCreationFailureAck = value;
    }

}
