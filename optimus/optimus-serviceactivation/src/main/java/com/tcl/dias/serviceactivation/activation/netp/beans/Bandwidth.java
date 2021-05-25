
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Bandwidth complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Bandwidth">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="speed" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0" form="unqualified"/>
 *         &lt;element name="unit" type="{http://www.tcl.com/2011/11/ace/common/xsd}BandwidthUnit" minOccurs="0" form="unqualified"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0" form="unqualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Bandwidth", namespace = "http://www.tcl.com/2011/11/ace/common/xsd", propOrder = {
    "speed",
    "unit",
    "isObjectInstanceModified"
})
public class Bandwidth {

    protected Float speed;
    @XmlSchemaType(name = "string")
    protected BandwidthUnit unit;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the speed property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getSpeed() {
        return speed;
    }

    /**
     * Sets the value of the speed property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setSpeed(Float value) {
        this.speed = value;
    }

    /**
     * Gets the value of the unit property.
     * 
     * @return
     *     possible object is
     *     {@link BandwidthUnit }
     *     
     */
    public BandwidthUnit getUnit() {
        return unit;
    }

    /**
     * Sets the value of the unit property.
     * 
     * @param value
     *     allowed object is
     *     {@link BandwidthUnit }
     *     
     */
    public void setUnit(BandwidthUnit value) {
        this.unit = value;
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
