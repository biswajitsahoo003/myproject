//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.10.30 at 06:34:20 PM IST 
//


package com.tcl.dias.oms.cisco.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TelephoneCommunicationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TelephoneCommunicationType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.openapplications.org/oagis/9}FormattedNumber" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.openapplications.org/oagis/9}UserArea" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TelephoneCommunicationType", propOrder = {
    "formattedNumber",
    "userArea"
})
public class TelephoneCommunicationType {

    @XmlElement(name = "FormattedNumber")
    protected TextType2 formattedNumber;
    @XmlElement(name = "UserArea")
    protected UserAreaType userArea;

    /**
     * Gets the value of the formattedNumber property.
     * 
     * @return
     *     possible object is
     *     {@link TextType2 }
     *     
     */
    public TextType2 getFormattedNumber() {
        return formattedNumber;
    }

    /**
     * Sets the value of the formattedNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextType2 }
     *     
     */
    public void setFormattedNumber(TextType2 value) {
        this.formattedNumber = value;
    }

    /**
     * Gets the value of the userArea property.
     * 
     * @return
     *     possible object is
     *     {@link UserAreaType }
     *     
     */
    public UserAreaType getUserArea() {
        return userArea;
    }

    /**
     * Sets the value of the userArea property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserAreaType }
     *     
     */
    public void setUserArea(UserAreaType value) {
        this.userArea = value;
    }

}
