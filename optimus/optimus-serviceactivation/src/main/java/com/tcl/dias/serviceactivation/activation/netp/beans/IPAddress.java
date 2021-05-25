
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IPAddress complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IPAddress">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/>
 *         &lt;element name="isObjectModifiedInstance" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0" form="unqualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IPAddress", namespace = "http://www.tcl.com/2011/11/ace/common/xsd", propOrder = {
    "address",
    "isObjectModifiedInstance"
})
@XmlSeeAlso({
    IPV6Address.class,
    IPV4Address.class
})
public class IPAddress {

    protected String address;
    protected Boolean isObjectModifiedInstance;

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * Gets the value of the isObjectModifiedInstance property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsObjectModifiedInstance() {
        return isObjectModifiedInstance;
    }

    /**
     * Sets the value of the isObjectModifiedInstance property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsObjectModifiedInstance(Boolean value) {
        this.isObjectModifiedInstance = value;
    }

}
