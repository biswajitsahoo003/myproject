//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.02.06 at 03:13:07 PM IST 
//


package com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VRRP complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VRRP">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="virtualIPv4Address" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="virtualIPv6Address" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VRRP", propOrder = {
    "virtualIPv4Address",
    "virtualIPv6Address"
})
public class VRRP {

    protected String virtualIPv4Address;
    protected String virtualIPv6Address;

    /**
     * Gets the value of the virtualIPv4Address property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVirtualIPv4Address() {
        return virtualIPv4Address;
    }

    /**
     * Sets the value of the virtualIPv4Address property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVirtualIPv4Address(String value) {
        this.virtualIPv4Address = value;
    }

    /**
     * Gets the value of the virtualIPv6Address property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVirtualIPv6Address() {
        return virtualIPv6Address;
    }

    /**
     * Sets the value of the virtualIPv6Address property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVirtualIPv6Address(String value) {
        this.virtualIPv6Address = value;
    }

}
