
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HSRPProtocol complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HSRPProtocol">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="helloValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="holdValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="timerUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="virtualIPAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="virtualIPv6Address" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="role" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ACTIVE"/>
 *               &lt;enumeration value="STANDBY"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="priority" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
@XmlType(name = "HSRPProtocol", propOrder = {
    "helloValue",
    "holdValue",
    "timerUnit",
    "virtualIPAddress",
    "virtualIPv6Address",
    "role",
    "priority",
    "isObjectInstanceModified"
})
public class HSRPProtocol {

    protected String helloValue;
    protected String holdValue;
    protected String timerUnit;
    protected String virtualIPAddress;
    protected String virtualIPv6Address;
    protected String role;
    protected Integer priority;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the helloValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHelloValue() {
        return helloValue;
    }

    /**
     * Sets the value of the helloValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHelloValue(String value) {
        this.helloValue = value;
    }

    /**
     * Gets the value of the holdValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHoldValue() {
        return holdValue;
    }

    /**
     * Sets the value of the holdValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHoldValue(String value) {
        this.holdValue = value;
    }

    /**
     * Gets the value of the timerUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimerUnit() {
        return timerUnit;
    }

    /**
     * Sets the value of the timerUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimerUnit(String value) {
        this.timerUnit = value;
    }

    /**
     * Gets the value of the virtualIPAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVirtualIPAddress() {
        return virtualIPAddress;
    }

    /**
     * Sets the value of the virtualIPAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVirtualIPAddress(String value) {
        this.virtualIPAddress = value;
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
     * Gets the value of the priority property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPriority(Integer value) {
        this.priority = value;
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
