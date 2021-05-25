
package com.tcl.dias.serviceactivation.cramer.reverseisc.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for wanip complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="wanip"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="WAN_IP_ON_MPLS_PE_1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="WAN_IP_ON_MPLS_PE_2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="WAN_IP_ON_VERSA_1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="WAN_IP_ON_VERSA_2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "wanip", propOrder = {
    "waniponmplspe1",
    "waniponmplspe2",
    "waniponversa1",
    "waniponversa2",
    "wanippool"
})
public class Wanip {

    @XmlElement(name = "WAN_IP_ON_MPLS_PE_1")
    protected String waniponmplspe1;
    @XmlElement(name = "WAN_IP_ON_MPLS_PE_2")
    protected String waniponmplspe2;
    @XmlElement(name = "WAN_IP_ON_VERSA_1")
    protected String waniponversa1;
    @XmlElement(name = "WAN_IP_ON_VERSA_2")
    protected String waniponversa2;
    @XmlElement(name = "WAN_IP_POOL")
    protected String wanippool;

    /**
     * Gets the value of the waniponmplspe1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWANIPONMPLSPE1() {
        return waniponmplspe1;
    }

    /**
     * Sets the value of the waniponmplspe1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWANIPONMPLSPE1(String value) {
        this.waniponmplspe1 = value;
    }

    /**
     * Gets the value of the waniponmplspe2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWANIPONMPLSPE2() {
        return waniponmplspe2;
    }

    /**
     * Sets the value of the waniponmplspe2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWANIPONMPLSPE2(String value) {
        this.waniponmplspe2 = value;
    }

    /**
     * Gets the value of the waniponversa1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWANIPONVERSA1() {
        return waniponversa1;
    }

    /**
     * Sets the value of the waniponversa1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWANIPONVERSA1(String value) {
        this.waniponversa1 = value;
    }

    /**
     * Gets the value of the waniponversa2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWANIPONVERSA2() {
        return waniponversa2;
    }

    /**
     * Sets the value of the waniponversa2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWANIPONVERSA2(String value) {
        this.waniponversa2 = value;
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
