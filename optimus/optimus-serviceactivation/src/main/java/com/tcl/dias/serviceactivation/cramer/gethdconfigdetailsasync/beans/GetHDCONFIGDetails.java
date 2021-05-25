
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
 *         &lt;element name="HDCONFIGDetails" type="{http://ACEIAS_HDCONFIG_Module}HDCONFIGDetails"/&gt;
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
    "hdconfigDetails"
})
@XmlRootElement(name = "getHDCONFIGDetails", namespace = "http://ACEIAS_HDCONFIG_Module/GetHDCONFIGDetails")
public class GetHDCONFIGDetails {

    @XmlElement(name = "HDCONFIGDetails", required = true, nillable = true)
    protected HDCONFIGDetails hdconfigDetails;

    /**
     * Gets the value of the hdconfigDetails property.
     * 
     * @return
     *     possible object is
     *     {@link HDCONFIGDetails }
     *     
     */
    public HDCONFIGDetails getHDCONFIGDetails() {
        return hdconfigDetails;
    }

    /**
     * Sets the value of the hdconfigDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link HDCONFIGDetails }
     *     
     */
    public void setHDCONFIGDetails(HDCONFIGDetails value) {
        this.hdconfigDetails = value;
    }

}
