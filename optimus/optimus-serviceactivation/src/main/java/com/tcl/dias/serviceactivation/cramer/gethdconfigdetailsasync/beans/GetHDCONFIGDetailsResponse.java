
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
 *         &lt;element name="HDCONFIGDetailsAck" type="{http://ACEIAS_HDCONFIG_Module}HDCONFIGDetailsAcknowledgement"/&gt;
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
    "hdconfigDetailsAck"
})
@XmlRootElement(name = "getHDCONFIGDetailsResponse", namespace = "http://ACEIAS_HDCONFIG_Module/GetHDCONFIGDetails")
public class GetHDCONFIGDetailsResponse {

    @XmlElement(name = "HDCONFIGDetailsAck", required = true, nillable = true)
    protected HDCONFIGDetailsAcknowledgement hdconfigDetailsAck;

    /**
     * Gets the value of the hdconfigDetailsAck property.
     * 
     * @return
     *     possible object is
     *     {@link HDCONFIGDetailsAcknowledgement }
     *     
     */
    public HDCONFIGDetailsAcknowledgement getHDCONFIGDetailsAck() {
        return hdconfigDetailsAck;
    }

    /**
     * Sets the value of the hdconfigDetailsAck property.
     * 
     * @param value
     *     allowed object is
     *     {@link HDCONFIGDetailsAcknowledgement }
     *     
     */
    public void setHDCONFIGDetailsAck(HDCONFIGDetailsAcknowledgement value) {
        this.hdconfigDetailsAck = value;
    }

}
