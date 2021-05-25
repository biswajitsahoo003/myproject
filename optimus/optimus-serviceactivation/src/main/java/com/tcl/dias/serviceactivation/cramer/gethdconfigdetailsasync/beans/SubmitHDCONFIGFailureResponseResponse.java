
package com.tcl.dias.serviceactivation.cramer.gethdconfigdetailsasync.beans;

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
 *         &lt;element name="SubmitHDCONFIGFailureAck" type="{http://ACEIAS_HDCONFIG_Module}HDCONFIGDetailsAcknowledgement"/&gt;
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
    "submitHDCONFIGFailureAck"
})
@XmlRootElement(name = "submitHDCONFIGFailureResponseResponse", namespace = "http://ACEIAS_HDCONFIG_Module/GetHDCONFIGDetails")
public class SubmitHDCONFIGFailureResponseResponse {

    @XmlElement(name = "SubmitHDCONFIGFailureAck", required = true, nillable = true)
    protected HDCONFIGDetailsAcknowledgement submitHDCONFIGFailureAck;

    /**
     * Gets the value of the submitHDCONFIGFailureAck property.
     * 
     * @return
     *     possible object is
     *     {@link HDCONFIGDetailsAcknowledgement }
     *     
     */
    public HDCONFIGDetailsAcknowledgement getSubmitHDCONFIGFailureAck() {
        return submitHDCONFIGFailureAck;
    }

    /**
     * Sets the value of the submitHDCONFIGFailureAck property.
     * 
     * @param value
     *     allowed object is
     *     {@link HDCONFIGDetailsAcknowledgement }
     *     
     */
    public void setSubmitHDCONFIGFailureAck(HDCONFIGDetailsAcknowledgement value) {
        this.submitHDCONFIGFailureAck = value;
    }

}
