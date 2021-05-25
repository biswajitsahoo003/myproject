
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ALUSchedulerPolicy complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ALUSchedulerPolicy">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="totalPIRbandwidth" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="totalCIRbandwidth" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isPreprovisioned" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
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
@XmlType(name = "ALUSchedulerPolicy", namespace = "http://www.tcl.com/2014/3/ipsvc/xsd", propOrder = {
    "name",
    "totalPIRbandwidth",
    "totalCIRbandwidth",
    "isPreprovisioned",
    "isObjectInstanceModified"
})
public class ALUSchedulerPolicy {

    protected String name;
    protected String totalPIRbandwidth;
    protected String totalCIRbandwidth;
    protected Boolean isPreprovisioned;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the totalPIRbandwidth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalPIRbandwidth() {
        return totalPIRbandwidth;
    }

    /**
     * Sets the value of the totalPIRbandwidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalPIRbandwidth(String value) {
        this.totalPIRbandwidth = value;
    }

    /**
     * Gets the value of the totalCIRbandwidth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalCIRbandwidth() {
        return totalCIRbandwidth;
    }

    /**
     * Sets the value of the totalCIRbandwidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalCIRbandwidth(String value) {
        this.totalCIRbandwidth = value;
    }

    /**
     * Gets the value of the isPreprovisioned property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsPreprovisioned() {
        return isPreprovisioned;
    }

    /**
     * Sets the value of the isPreprovisioned property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsPreprovisioned(Boolean value) {
        this.isPreprovisioned = value;
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
