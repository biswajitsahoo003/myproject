
package com.tcl.dias.serviceactivation.cramer.reverseisc.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for wanIPUseCase3 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="wanIPUseCase3"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="IP_ADDRESS_ON_LAN_VR_SUB_INT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IP_ADDRES_ON_INTERNET_WAN_VR_SUB_INT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "wanIPUseCase3", propOrder = {
    "ipaddressonlanvrsubint",
    "ipaddresoninternetwanvrsubint",
    "wanippool"
})
public class WanIPUseCase3 {

    @XmlElement(name = "IP_ADDRESS_ON_LAN_VR_SUB_INT")
    protected String ipaddressonlanvrsubint;
    @XmlElement(name = "IP_ADDRES_ON_INTERNET_WAN_VR_SUB_INT")
    protected String ipaddresoninternetwanvrsubint;
    @XmlElement(name = "WAN_IP_POOL")
    protected String wanippool;

    /**
     * Gets the value of the ipaddressonlanvrsubint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPADDRESSONLANVRSUBINT() {
        return ipaddressonlanvrsubint;
    }

    /**
     * Sets the value of the ipaddressonlanvrsubint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPADDRESSONLANVRSUBINT(String value) {
        this.ipaddressonlanvrsubint = value;
    }

    /**
     * Gets the value of the ipaddresoninternetwanvrsubint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPADDRESONINTERNETWANVRSUBINT() {
        return ipaddresoninternetwanvrsubint;
    }

    /**
     * Sets the value of the ipaddresoninternetwanvrsubint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPADDRESONINTERNETWANVRSUBINT(String value) {
        this.ipaddresoninternetwanvrsubint = value;
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
