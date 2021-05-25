
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HuaweiERPSConfig complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HuaweiERPSConfig">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AendRemotePort" type="{http://www.tcl.com/2014/4/ipsvc/xsd}HuaweiERPSNodePortDetails" minOccurs="0"/>
 *         &lt;element name="ZendRemotePort" type="{http://www.tcl.com/2014/4/ipsvc/xsd}HuaweiERPSNodePortDetails" minOccurs="0"/>
 *         &lt;element name="AendLocalPort" type="{http://www.tcl.com/2014/4/ipsvc/xsd}HuaweiERPSNodePortDetails" minOccurs="0"/>
 *         &lt;element name="ZendLocalPort" type="{http://www.tcl.com/2014/4/ipsvc/xsd}HuaweiERPSNodePortDetails" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HuaweiERPSConfig", namespace = "http://www.tcl.com/2014/4/ipsvc/xsd", propOrder = {
    "aendRemotePort",
    "zendRemotePort",
    "aendLocalPort",
    "zendLocalPort",
    "isObjectInstanceModified"
})
public class HuaweiERPSConfig {

    @XmlElement(name = "AendRemotePort")
    protected HuaweiERPSNodePortDetails aendRemotePort;
    @XmlElement(name = "ZendRemotePort")
    protected HuaweiERPSNodePortDetails zendRemotePort;
    @XmlElement(name = "AendLocalPort")
    protected HuaweiERPSNodePortDetails aendLocalPort;
    @XmlElement(name = "ZendLocalPort")
    protected HuaweiERPSNodePortDetails zendLocalPort;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the aendRemotePort property.
     * 
     * @return
     *     possible object is
     *     {@link HuaweiERPSNodePortDetails }
     *     
     */
    public HuaweiERPSNodePortDetails getAendRemotePort() {
        return aendRemotePort;
    }

    /**
     * Sets the value of the aendRemotePort property.
     * 
     * @param value
     *     allowed object is
     *     {@link HuaweiERPSNodePortDetails }
     *     
     */
    public void setAendRemotePort(HuaweiERPSNodePortDetails value) {
        this.aendRemotePort = value;
    }

    /**
     * Gets the value of the zendRemotePort property.
     * 
     * @return
     *     possible object is
     *     {@link HuaweiERPSNodePortDetails }
     *     
     */
    public HuaweiERPSNodePortDetails getZendRemotePort() {
        return zendRemotePort;
    }

    /**
     * Sets the value of the zendRemotePort property.
     * 
     * @param value
     *     allowed object is
     *     {@link HuaweiERPSNodePortDetails }
     *     
     */
    public void setZendRemotePort(HuaweiERPSNodePortDetails value) {
        this.zendRemotePort = value;
    }

    /**
     * Gets the value of the aendLocalPort property.
     * 
     * @return
     *     possible object is
     *     {@link HuaweiERPSNodePortDetails }
     *     
     */
    public HuaweiERPSNodePortDetails getAendLocalPort() {
        return aendLocalPort;
    }

    /**
     * Sets the value of the aendLocalPort property.
     * 
     * @param value
     *     allowed object is
     *     {@link HuaweiERPSNodePortDetails }
     *     
     */
    public void setAendLocalPort(HuaweiERPSNodePortDetails value) {
        this.aendLocalPort = value;
    }

    /**
     * Gets the value of the zendLocalPort property.
     * 
     * @return
     *     possible object is
     *     {@link HuaweiERPSNodePortDetails }
     *     
     */
    public HuaweiERPSNodePortDetails getZendLocalPort() {
        return zendLocalPort;
    }

    /**
     * Sets the value of the zendLocalPort property.
     * 
     * @param value
     *     allowed object is
     *     {@link HuaweiERPSNodePortDetails }
     *     
     */
    public void setZendLocalPort(HuaweiERPSNodePortDetails value) {
        this.zendLocalPort = value;
    }

    /**
     * Gets the value of the isObjectInstanceModified property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsObjectInstanceModified() {
        return isObjectInstanceModified;
    }

    /**
     * Sets the value of the isObjectInstanceModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsObjectInstanceModified(Boolean value) {
        this.isObjectInstanceModified = value;
    }

}
