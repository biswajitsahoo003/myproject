
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ICMPConfig complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ICMPConfig">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="unreachableRateLimit" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="unreachableBurstSize" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="TTLMaxExpiredMsgs" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="TTLTimeFrame" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
@XmlType(name = "ICMPConfig", namespace = "http://www.tcl.com/2014/3/ipsvc/xsd", propOrder = {
    "unreachableRateLimit",
    "unreachableBurstSize",
    "ttlMaxExpiredMsgs",
    "ttlTimeFrame",
    "isObjectInstanceModified"
})
public class ICMPConfig {

    protected Integer unreachableRateLimit;
    protected Integer unreachableBurstSize;
    @XmlElement(name = "TTLMaxExpiredMsgs")
    protected Integer ttlMaxExpiredMsgs;
    @XmlElement(name = "TTLTimeFrame")
    protected Integer ttlTimeFrame;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the unreachableRateLimit property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getUnreachableRateLimit() {
        return unreachableRateLimit;
    }

    /**
     * Sets the value of the unreachableRateLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setUnreachableRateLimit(Integer value) {
        this.unreachableRateLimit = value;
    }

    /**
     * Gets the value of the unreachableBurstSize property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getUnreachableBurstSize() {
        return unreachableBurstSize;
    }

    /**
     * Sets the value of the unreachableBurstSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setUnreachableBurstSize(Integer value) {
        this.unreachableBurstSize = value;
    }

    /**
     * Gets the value of the ttlMaxExpiredMsgs property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTTLMaxExpiredMsgs() {
        return ttlMaxExpiredMsgs;
    }

    /**
     * Sets the value of the ttlMaxExpiredMsgs property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTTLMaxExpiredMsgs(Integer value) {
        this.ttlMaxExpiredMsgs = value;
    }

    /**
     * Gets the value of the ttlTimeFrame property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTTLTimeFrame() {
        return ttlTimeFrame;
    }

    /**
     * Sets the value of the ttlTimeFrame property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTTLTimeFrame(Integer value) {
        this.ttlTimeFrame = value;
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
