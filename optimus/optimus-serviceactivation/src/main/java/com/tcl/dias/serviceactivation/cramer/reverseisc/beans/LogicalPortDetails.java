
package com.tcl.dias.serviceactivation.cramer.reverseisc.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Logical_Port_Details complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Logical_Port_Details"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="WAN_IP" type="{http://com.tatacommunications.cramer.reverseisc.ws}wanip" minOccurs="0"/&gt;
 *         &lt;element name="LAN_VR_AND_MPLS_PE_DETAILS" type="{http://com.tatacommunications.cramer.reverseisc.ws}lanVRMPLSPEVo" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Logical_Port_Details", propOrder = {
    "wanip",
    "lanvrandmplspedetails"
})
public class LogicalPortDetails {

    @XmlElement(name = "WAN_IP")
    protected Wanip wanip;
    @XmlElement(name = "LAN_VR_AND_MPLS_PE_DETAILS")
    protected LanVRMPLSPEVo lanvrandmplspedetails;

    /**
     * Gets the value of the wanip property.
     * 
     * @return
     *     possible object is
     *     {@link Wanip }
     *     
     */
    public Wanip getWANIP() {
        return wanip;
    }

    /**
     * Sets the value of the wanip property.
     * 
     * @param value
     *     allowed object is
     *     {@link Wanip }
     *     
     */
    public void setWANIP(Wanip value) {
        this.wanip = value;
    }

    /**
     * Gets the value of the lanvrandmplspedetails property.
     * 
     * @return
     *     possible object is
     *     {@link LanVRMPLSPEVo }
     *     
     */
    public LanVRMPLSPEVo getLANVRANDMPLSPEDETAILS() {
        return lanvrandmplspedetails;
    }

    /**
     * Sets the value of the lanvrandmplspedetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link LanVRMPLSPEVo }
     *     
     */
    public void setLANVRANDMPLSPEDETAILS(LanVRMPLSPEVo value) {
        this.lanvrandmplspedetails = value;
    }

}
