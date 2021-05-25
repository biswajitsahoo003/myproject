
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				Topology info will contain only UNI switch details
 * 			
 * 
 * <p>Java class for TopologyInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TopologyInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="uNISwitch" type="{http://www.tcl.com/2011/11/ipsvc/xsd}Switch" minOccurs="0"/>
 *         &lt;element name="isObjectModifiedInstance" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TopologyInfo", propOrder = {
    "name",
    "uniSwitch",
    "isObjectModifiedInstance"
})
public class TopologyInfo {

    protected String name;
    @XmlElement(name = "uNISwitch")
    protected Switch uniSwitch;
    protected Boolean isObjectModifiedInstance;

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
     * Gets the value of the uniSwitch property.
     * 
     * @return
     *     possible object is
     *     {@link Switch }
     *     
     */
    public Switch getUNISwitch() {
        return uniSwitch;
    }

    /**
     * Sets the value of the uniSwitch property.
     * 
     * @param value
     *     allowed object is
     *     {@link Switch }
     *     
     */
    public void setUNISwitch(Switch value) {
        this.uniSwitch = value;
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
