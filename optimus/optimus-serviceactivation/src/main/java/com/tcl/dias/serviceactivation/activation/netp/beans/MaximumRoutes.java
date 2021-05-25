
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MaximumRoutes complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MaximumRoutes">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="WarnOn" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="THRESHOLD"/>
 *               &lt;enumeration value="MAXROUTES"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="thresholdValue" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MaximumRoutes", propOrder = {
    "value",
    "warnOn",
    "thresholdValue"
})
public class MaximumRoutes {

    @XmlElement(name = "Value")
    protected Integer value;
    @XmlElement(name = "WarnOn")
    protected String warnOn;
    protected Integer thresholdValue;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setValue(Integer value) {
        this.value = value;
    }

    /**
     * Gets the value of the warnOn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarnOn() {
        return warnOn;
    }

    /**
     * Sets the value of the warnOn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarnOn(String value) {
        this.warnOn = value;
    }

    /**
     * Gets the value of the thresholdValue property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getThresholdValue() {
        return thresholdValue;
    }

    /**
     * Sets the value of the thresholdValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setThresholdValue(Integer value) {
        this.thresholdValue = value;
    }

}
