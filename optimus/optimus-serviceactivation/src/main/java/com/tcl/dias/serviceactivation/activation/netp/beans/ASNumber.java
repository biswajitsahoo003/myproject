
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ASNumber complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ASNumber">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="number" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/>
 *         &lt;element name="category" minOccurs="0" form="unqualified">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="PRIVATE"/>
 *               &lt;enumeration value="PUBLIC"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="isASNumberCustomerOwned" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0" form="unqualified"/>
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
@XmlType(name = "ASNumber", namespace = "http://www.tcl.com/2011/11/ace/common/xsd", propOrder = {
    "number",
    "category",
    "isASNumberCustomerOwned",
    "isObjectInstanceModified"
})
public class ASNumber {

    protected String number;
    protected String category;
    protected Boolean isASNumberCustomerOwned;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the number property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the value of the number property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumber(String value) {
        this.number = value;
    }

    /**
     * Gets the value of the category property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the value of the category property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategory(String value) {
        this.category = value;
    }

    /**
     * Gets the value of the isASNumberCustomerOwned property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsASNumberCustomerOwned() {
        return isASNumberCustomerOwned;
    }

    /**
     * Sets the value of the isASNumberCustomerOwned property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsASNumberCustomerOwned(Boolean value) {
        this.isASNumberCustomerOwned = value;
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
