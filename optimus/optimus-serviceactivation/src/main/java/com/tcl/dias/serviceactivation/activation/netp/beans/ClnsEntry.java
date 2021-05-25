
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for clnsEntry complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="clnsEntry">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="clnsAddress" type="{http://CiscoImportMap/com/tcl/www/_2011/_11/ipsvc/xsd}CLNSAdd" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="action" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="PERMIT"/>
 *               &lt;enumeration value="DENY"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clnsEntry", namespace = "http://CiscoImportMap/com/tcl/www/_2011/_11/ipsvc/xsd", propOrder = {
    "clnsAddress",
    "isObjectInstanceModified",
    "action"
})
public class ClnsEntry {

    protected CLNSAdd clnsAddress;
    protected Boolean isObjectInstanceModified;
    protected String action;

    /**
     * Gets the value of the clnsAddress property.
     * 
     * @return
     *     possible object is
     *     {@link CLNSAdd }
     *     
     */
    public CLNSAdd getClnsAddress() {
        return clnsAddress;
    }

    /**
     * Sets the value of the clnsAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link CLNSAdd }
     *     
     */
    public void setClnsAddress(CLNSAdd value) {
        this.clnsAddress = value;
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

    /**
     * Gets the value of the action property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the value of the action property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAction(String value) {
        this.action = value;
    }

}
