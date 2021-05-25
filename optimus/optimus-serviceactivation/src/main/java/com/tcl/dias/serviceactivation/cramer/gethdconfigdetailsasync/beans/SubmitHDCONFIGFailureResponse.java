
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
 *         &lt;element name="SubmitHDCONFIGFailure" type="{http://ACEIAS_HDCONFIG_Module}SubmitHDCONFIGFailureResponse"/&gt;
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
    "submitHDCONFIGFailure"
})
@XmlRootElement(name = "submitHDCONFIGFailureResponse", namespace = "http://ACEIAS_HDCONFIG_Module/GetHDCONFIGDetails")
public class SubmitHDCONFIGFailureResponse {

    @XmlElement(name = "SubmitHDCONFIGFailure", required = true, nillable = true)
    protected SubmitHDCONFIGFailureResponse2 submitHDCONFIGFailure;

    /**
     * Gets the value of the submitHDCONFIGFailure property.
     * 
     * @return
     *     possible object is
     *     {@link SubmitHDCONFIGFailureResponse2 }
     *     
     */
    public SubmitHDCONFIGFailureResponse2 getSubmitHDCONFIGFailure() {
        return submitHDCONFIGFailure;
    }

    /**
     * Sets the value of the submitHDCONFIGFailure property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubmitHDCONFIGFailureResponse2 }
     *     
     */
    public void setSubmitHDCONFIGFailure(SubmitHDCONFIGFailureResponse2 value) {
        this.submitHDCONFIGFailure = value;
    }

}
