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
 * <p>Java class for EMailAddressCommunicationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EMailAddressCommunicationType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.openapplications.org/oagis/9}EMailAddressID" minOccurs="0"/&gt;
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
@XmlType(name = "EMailAddressCommunicationType", propOrder = {
    "eMailAddressID",
    "userArea"
})
public class EMailAddressCommunicationType {

    @XmlElement(name = "EMailAddressID")
    protected IdentifierType2 eMailAddressID;
    @XmlElement(name = "UserArea")
    protected UserAreaType userArea;

    /**
     * Gets the value of the eMailAddressID property.
     * 
     * @return
     *     possible object is
     *     {@link IdentifierType2 }
     *     
     */
    public IdentifierType2 getEMailAddressID() {
        return eMailAddressID;
    }

    /**
     * Sets the value of the eMailAddressID property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentifierType2 }
     *     
     */
    public void setEMailAddressID(IdentifierType2 value) {
        this.eMailAddressID = value;
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
