
package com.tcl.dias.serviceactivation.cramer.downtimeasync.beans;

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
 *         &lt;element name="submitFaultResponse" type="{http://ACE_ServiceDowntime_Module}SubmitServiceDowntimeFaultResponse"/&gt;
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
    "submitFaultResponse"
})
@XmlRootElement(name = "submitServiceDowntimeFaultResponse", namespace = "http://ACE_ServiceDowntime_Module/GetServiceDowntimeDetails")
public class SubmitServiceDowntimeFaultResponse {

    @XmlElement(required = true, nillable = true)
    protected SubmitServiceDowntimeFaultResponse2 submitFaultResponse;

    /**
     * Gets the value of the submitFaultResponse property.
     * 
     * @return
     *     possible object is
     *     {@link SubmitServiceDowntimeFaultResponse2 }
     *     
     */
    public SubmitServiceDowntimeFaultResponse2 getSubmitFaultResponse() {
        return submitFaultResponse;
    }

    /**
     * Sets the value of the submitFaultResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubmitServiceDowntimeFaultResponse2 }
     *     
     */
    public void setSubmitFaultResponse(SubmitServiceDowntimeFaultResponse2 value) {
        this.submitFaultResponse = value;
    }

}
