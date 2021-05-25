
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NATMode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NATMode">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StaticCustomerWANIPGateway" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StaticCustomerWANIPMask" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StaticCustomerWANIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="natQoSProfile" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}VLAN-QOSProfile" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NATMode", namespace = "http://IPServicesLibrary/ipsvc/bo/_2011/_11", propOrder = {
    "staticCustomerWANIPGateway",
    "staticCustomerWANIPMask",
    "staticCustomerWANIP",
    "natQoSProfile"
})
public class NATMode {

    @XmlElement(name = "StaticCustomerWANIPGateway")
    protected String staticCustomerWANIPGateway;
    @XmlElement(name = "StaticCustomerWANIPMask")
    protected String staticCustomerWANIPMask;
    @XmlElement(name = "StaticCustomerWANIP")
    protected String staticCustomerWANIP;
    protected VLANQOSProfile natQoSProfile;

    /**
     * Gets the value of the staticCustomerWANIPGateway property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStaticCustomerWANIPGateway() {
        return staticCustomerWANIPGateway;
    }

    /**
     * Sets the value of the staticCustomerWANIPGateway property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStaticCustomerWANIPGateway(String value) {
        this.staticCustomerWANIPGateway = value;
    }

    /**
     * Gets the value of the staticCustomerWANIPMask property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStaticCustomerWANIPMask() {
        return staticCustomerWANIPMask;
    }

    /**
     * Sets the value of the staticCustomerWANIPMask property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStaticCustomerWANIPMask(String value) {
        this.staticCustomerWANIPMask = value;
    }

    /**
     * Gets the value of the staticCustomerWANIP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStaticCustomerWANIP() {
        return staticCustomerWANIP;
    }

    /**
     * Sets the value of the staticCustomerWANIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStaticCustomerWANIP(String value) {
        this.staticCustomerWANIP = value;
    }

    /**
     * Gets the value of the natQoSProfile property.
     * 
     * @return
     *     possible object is
     *     {@link VLANQOSProfile }
     *     
     */
    public VLANQOSProfile getNatQoSProfile() {
        return natQoSProfile;
    }

    /**
     * Sets the value of the natQoSProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link VLANQOSProfile }
     *     
     */
    public void setNatQoSProfile(VLANQOSProfile value) {
        this.natQoSProfile = value;
    }

}
