
package com.tcl.dias.serviceactivation.cramer.muxsync;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for handOffDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="handOffDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bandwidthUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bandwidthValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="feasibilityId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="muxIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="muxName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="portType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "handOffDetail", propOrder = {
    "bandwidthUnit",
    "bandwidthValue",
    "feasibilityId",
    "muxIP",
    "muxName",
    "portType"
})
public class HandOffDetail {

    protected String bandwidthUnit;
    protected String bandwidthValue;
    protected String feasibilityId;
    protected String muxIP;
    protected String muxName;
    protected String portType;

    /**
     * Gets the value of the bandwidthUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBandwidthUnit() {
        return bandwidthUnit;
    }

    /**
     * Sets the value of the bandwidthUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBandwidthUnit(String value) {
        this.bandwidthUnit = value;
    }

    /**
     * Gets the value of the bandwidthValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBandwidthValue() {
        return bandwidthValue;
    }

    /**
     * Sets the value of the bandwidthValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBandwidthValue(String value) {
        this.bandwidthValue = value;
    }

    /**
     * Gets the value of the feasibilityId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFeasibilityId() {
        return feasibilityId;
    }

    /**
     * Sets the value of the feasibilityId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFeasibilityId(String value) {
        this.feasibilityId = value;
    }

    /**
     * Gets the value of the muxIP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMuxIP() {
        return muxIP;
    }

    /**
     * Sets the value of the muxIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMuxIP(String value) {
        this.muxIP = value;
    }

    /**
     * Gets the value of the muxName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMuxName() {
        return muxName;
    }

    /**
     * Sets the value of the muxName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMuxName(String value) {
        this.muxName = value;
    }

    /**
     * Gets the value of the portType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPortType() {
        return portType;
    }

    /**
     * Sets the value of the portType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPortType(String value) {
        this.portType = value;
    }

}
