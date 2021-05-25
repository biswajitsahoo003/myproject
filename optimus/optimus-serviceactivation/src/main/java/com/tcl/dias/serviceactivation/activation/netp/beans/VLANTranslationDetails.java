
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VLANTranslationDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VLANTranslationDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SwitchHostname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TranslationPort" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="S1VLAN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="S2VLAN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RemoteS2VLAN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isObjectModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VLANTranslationDetails", namespace = "http://www.tcl.com/2014/4/ipsvc/xsd", propOrder = {
    "switchHostname",
    "translationPort",
    "s1VLAN",
    "s2VLAN",
    "remoteS2VLAN",
    "isObjectModified"
})
public class VLANTranslationDetails {

    @XmlElement(name = "SwitchHostname")
    protected String switchHostname;
    @XmlElement(name = "TranslationPort")
    protected String translationPort;
    @XmlElement(name = "S1VLAN")
    protected String s1VLAN;
    @XmlElement(name = "S2VLAN")
    protected String s2VLAN;
    @XmlElement(name = "RemoteS2VLAN")
    protected String remoteS2VLAN;
    protected Boolean isObjectModified;

    /**
     * Gets the value of the switchHostname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSwitchHostname() {
        return switchHostname;
    }

    /**
     * Sets the value of the switchHostname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSwitchHostname(String value) {
        this.switchHostname = value;
    }

    /**
     * Gets the value of the translationPort property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTranslationPort() {
        return translationPort;
    }

    /**
     * Sets the value of the translationPort property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTranslationPort(String value) {
        this.translationPort = value;
    }

    /**
     * Gets the value of the s1VLAN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getS1VLAN() {
        return s1VLAN;
    }

    /**
     * Sets the value of the s1VLAN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setS1VLAN(String value) {
        this.s1VLAN = value;
    }

    /**
     * Gets the value of the s2VLAN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getS2VLAN() {
        return s2VLAN;
    }

    /**
     * Sets the value of the s2VLAN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setS2VLAN(String value) {
        this.s2VLAN = value;
    }

    /**
     * Gets the value of the remoteS2VLAN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemoteS2VLAN() {
        return remoteS2VLAN;
    }

    /**
     * Sets the value of the remoteS2VLAN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemoteS2VLAN(String value) {
        this.remoteS2VLAN = value;
    }

    /**
     * Gets the value of the isObjectModified property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsObjectModified() {
        return isObjectModified;
    }

    /**
     * Sets the value of the isObjectModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsObjectModified(Boolean value) {
        this.isObjectModified = value;
    }

}
