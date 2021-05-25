
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Leg complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Leg">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SiteID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="legServiceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="instanceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="role" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="HUB"/>
 *               &lt;enumeration value="SPOKE"/>
 *               &lt;enumeration value="FULL-MESH"/>
 *               &lt;enumeration value="MGMT-SPOKE"/>
 *               &lt;enumeration value="TCL-SITE"/>
 *               &lt;enumeration value="NOT-APPLICABLE"/>
 *               &lt;enumeration value="PARTNER-SITE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Interfacename" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UA" type="{http://com/tcl/www/_2011/_11/ipsvc/xsd}UA" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Leg", propOrder = {
    "siteID",
    "legServiceID",
    "instanceID",
    "role",
    "interfacename",
    "ua",
    "isObjectInstanceModified"
})
public class Leg {

    @XmlElement(name = "SiteID")
    protected String siteID;
    protected String legServiceID;
    protected String instanceID;
    protected String role;
    @XmlElement(name = "Interfacename")
    protected String interfacename;
    @XmlElement(name = "UA")
    protected UA ua;
    protected String isObjectInstanceModified;

    /**
     * Gets the value of the siteID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiteID() {
        return siteID;
    }

    /**
     * Sets the value of the siteID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiteID(String value) {
        this.siteID = value;
    }

    /**
     * Gets the value of the legServiceID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLegServiceID() {
        return legServiceID;
    }

    /**
     * Sets the value of the legServiceID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLegServiceID(String value) {
        this.legServiceID = value;
    }

    /**
     * Gets the value of the instanceID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstanceID() {
        return instanceID;
    }

    /**
     * Sets the value of the instanceID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstanceID(String value) {
        this.instanceID = value;
    }

    /**
     * Gets the value of the role property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the value of the role property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRole(String value) {
        this.role = value;
    }

    /**
     * Gets the value of the interfacename property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInterfacename() {
        return interfacename;
    }

    /**
     * Sets the value of the interfacename property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInterfacename(String value) {
        this.interfacename = value;
    }

    /**
     * Gets the value of the ua property.
     * 
     * @return
     *     possible object is
     *     {@link UA }
     *     
     */
    public UA getUA() {
        return ua;
    }

    /**
     * Sets the value of the ua property.
     * 
     * @param value
     *     allowed object is
     *     {@link UA }
     *     
     */
    public void setUA(UA value) {
        this.ua = value;
    }

    /**
     * Gets the value of the isObjectInstanceModified property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsObjectInstanceModified() {
        return isObjectInstanceModified;
    }

    /**
     * Sets the value of the isObjectInstanceModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsObjectInstanceModified(String value) {
        this.isObjectInstanceModified = value;
    }

}
