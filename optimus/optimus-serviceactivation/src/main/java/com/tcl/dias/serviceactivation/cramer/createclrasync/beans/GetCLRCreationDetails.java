
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
 *         &lt;element name="CLRCreationDetails" type="{http://ACE_CLRCreation_Module}CLRCreationDetails"/&gt;
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
    "clrCreationDetails"
})
@XmlRootElement(name = "getCLRCreationDetails", namespace = "http://ACE_CLRCreation_Module/GetCLRCreationDetails")
public class GetCLRCreationDetails {

    @XmlElement(name = "CLRCreationDetails", required = true, nillable = true)
    protected CLRCreationDetails clrCreationDetails;

    /**
     * Gets the value of the clrCreationDetails property.
     * 
     * @return
     *     possible object is
     *     {@link CLRCreationDetails }
     *     
     */
    public CLRCreationDetails getCLRCreationDetails() {
        return clrCreationDetails;
    }

    /**
     * Sets the value of the clrCreationDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link CLRCreationDetails }
     *     
     */
    public void setCLRCreationDetails(CLRCreationDetails value) {
        this.clrCreationDetails = value;
    }

}
