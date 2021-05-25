
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EOAMConfig complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EOAMConfig">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="shutdown" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="mode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ACTIVE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="acceptRemoteLoopback" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="TxInterval" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="multiplier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="holdTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Tunneling" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
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
@XmlType(name = "EOAMConfig", namespace = "http://www.tcl.com/2014/3/ipsvc/xsd", propOrder = {
    "shutdown",
    "mode",
    "acceptRemoteLoopback",
    "txInterval",
    "multiplier",
    "holdTime",
    "tunneling",
    "isObjectInstanceModified"
})
public class EOAMConfig {

    protected String shutdown;
    protected String mode;
    protected String acceptRemoteLoopback;
    @XmlElement(name = "TxInterval")
    protected String txInterval;
    protected String multiplier;
    protected String holdTime;
    @XmlElement(name = "Tunneling")
    protected Boolean tunneling;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the shutdown property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShutdown() {
        return shutdown;
    }

    /**
     * Sets the value of the shutdown property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShutdown(String value) {
        this.shutdown = value;
    }

    /**
     * Gets the value of the mode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMode() {
        return mode;
    }

    /**
     * Sets the value of the mode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMode(String value) {
        this.mode = value;
    }

    /**
     * Gets the value of the acceptRemoteLoopback property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAcceptRemoteLoopback() {
        return acceptRemoteLoopback;
    }

    /**
     * Sets the value of the acceptRemoteLoopback property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAcceptRemoteLoopback(String value) {
        this.acceptRemoteLoopback = value;
    }

    /**
     * Gets the value of the txInterval property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTxInterval() {
        return txInterval;
    }

    /**
     * Sets the value of the txInterval property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTxInterval(String value) {
        this.txInterval = value;
    }

    /**
     * Gets the value of the multiplier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMultiplier() {
        return multiplier;
    }

    /**
     * Sets the value of the multiplier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMultiplier(String value) {
        this.multiplier = value;
    }

    /**
     * Gets the value of the holdTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHoldTime() {
        return holdTime;
    }

    /**
     * Sets the value of the holdTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHoldTime(String value) {
        this.holdTime = value;
    }

    /**
     * Gets the value of the tunneling property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTunneling() {
        return tunneling;
    }

    /**
     * Sets the value of the tunneling property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTunneling(Boolean value) {
        this.tunneling = value;
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
