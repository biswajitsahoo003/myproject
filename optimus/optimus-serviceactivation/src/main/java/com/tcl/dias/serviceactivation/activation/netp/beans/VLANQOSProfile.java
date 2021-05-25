
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VLAN-QOSProfile complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VLAN-QOSProfile">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vlanID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="UpstreamQoSProfile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DownstreamQoSProfile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VLAN-QOSProfile", namespace = "http://IPServicesLibrary/ipsvc/bo/_2011/_11", propOrder = {
    "vlanID",
    "upstreamQoSProfile",
    "downstreamQoSProfile"
})
public class VLANQOSProfile {

    protected Integer vlanID;
    @XmlElement(name = "UpstreamQoSProfile")
    protected String upstreamQoSProfile;
    @XmlElement(name = "DownstreamQoSProfile")
    protected String downstreamQoSProfile;

    /**
     * Gets the value of the vlanID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getVlanID() {
        return vlanID;
    }

    /**
     * Sets the value of the vlanID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setVlanID(Integer value) {
        this.vlanID = value;
    }

    /**
     * Gets the value of the upstreamQoSProfile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUpstreamQoSProfile() {
        return upstreamQoSProfile;
    }

    /**
     * Sets the value of the upstreamQoSProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUpstreamQoSProfile(String value) {
        this.upstreamQoSProfile = value;
    }

    /**
     * Gets the value of the downstreamQoSProfile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDownstreamQoSProfile() {
        return downstreamQoSProfile;
    }

    /**
     * Sets the value of the downstreamQoSProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDownstreamQoSProfile(String value) {
        this.downstreamQoSProfile = value;
    }

}
