
package com.tcl.dias.serviceactivation.cramer.reverseisc.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for wanIPUseCase4 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="wanIPUseCase4"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="IP_ADDRESS_ON_DISTRIBUTION_LAN_VR_SUB_INT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IP_ADDRESS_ON_LAN_VR_SIDE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="WAN_IP_POOL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wanIPUseCase4", propOrder = {
    "ipaddressondistributionlanvrsubint",
    "ipaddressonlanvrside",
    "wanippool"
})
public class WanIPUseCase4 {

    @XmlElement(name = "IP_ADDRESS_ON_DISTRIBUTION_LAN_VR_SUB_INT")
    protected String ipaddressondistributionlanvrsubint;
    @XmlElement(name = "IP_ADDRESS_ON_LAN_VR_SIDE")
    protected String ipaddressonlanvrside;
    @XmlElement(name = "WAN_IP_POOL")
    protected String wanippool;

    /**
     * Gets the value of the ipaddressondistributionlanvrsubint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPADDRESSONDISTRIBUTIONLANVRSUBINT() {
        return ipaddressondistributionlanvrsubint;
    }

    /**
     * Sets the value of the ipaddressondistributionlanvrsubint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPADDRESSONDISTRIBUTIONLANVRSUBINT(String value) {
        this.ipaddressondistributionlanvrsubint = value;
    }

    /**
     * Gets the value of the ipaddressonlanvrside property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPADDRESSONLANVRSIDE() {
        return ipaddressonlanvrside;
    }

    /**
     * Sets the value of the ipaddressonlanvrside property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPADDRESSONLANVRSIDE(String value) {
        this.ipaddressonlanvrside = value;
    }

    /**
     * Gets the value of the wanippool property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWANIPPOOL() {
        return wanippool;
    }

    /**
     * Sets the value of the wanippool property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWANIPPOOL(String value) {
        this.wanippool = value;
    }

}
