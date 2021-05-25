
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
 *         &lt;element name="submitFault" type="{http://ACE_ServiceDowntime_Module}SubmitServiceDowntimeFault"/&gt;
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
    "submitFault"
})
@XmlRootElement(name = "submitServiceDowntimeFault", namespace = "http://ACE_ServiceDowntime_Module/GetServiceDowntimeDetails")
public class SubmitServiceDowntimeFault {

    @XmlElement(required = true, nillable = true)
    protected SubmitServiceDowntimeFault2 submitFault;

    /**
     * Gets the value of the submitFault property.
     * 
     * @return
     *     possible object is
     *     {@link SubmitServiceDowntimeFault2 }
     *     
     */
    public SubmitServiceDowntimeFault2 getSubmitFault() {
        return submitFault;
    }

    /**
     * Sets the value of the submitFault property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubmitServiceDowntimeFault2 }
     *     
     */
    public void setSubmitFault(SubmitServiceDowntimeFault2 value) {
        this.submitFault = value;
    }

}
