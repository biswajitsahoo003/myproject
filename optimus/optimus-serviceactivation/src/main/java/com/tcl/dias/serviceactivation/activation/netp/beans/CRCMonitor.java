
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CRCMonitor complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CRCMonitor">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="multiplier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="windowSize" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sdThreshold" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "CRCMonitor", namespace = "http://www.tcl.com/2014/2/ipsvc/xsd", propOrder = {
    "multiplier",
    "windowSize",
    "sdThreshold",
    "isObjectInstanceModified"
})
public class CRCMonitor {

    protected String multiplier;
    protected String windowSize;
    protected String sdThreshold;
    protected Boolean isObjectInstanceModified;

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
     * Gets the value of the windowSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWindowSize() {
        return windowSize;
    }

    /**
     * Sets the value of the windowSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWindowSize(String value) {
        this.windowSize = value;
    }

    /**
     * Gets the value of the sdThreshold property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSdThreshold() {
        return sdThreshold;
    }

    /**
     * Sets the value of the sdThreshold property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSdThreshold(String value) {
        this.sdThreshold = value;
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
