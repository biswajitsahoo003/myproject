//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.02.06 at 03:13:07 PM IST 
//


package com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for APSRouter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="APSRouter">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tcl.com/2012/09/csvc/inf}Device">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.tcl.com/2012/09/csvc/inf}wanInterface" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "APSRouter", propOrder = {
    "wanInterface"
})
public class APSRouter
    extends Device
{

    @XmlElement(namespace = "http://www.tcl.com/2012/09/csvc/inf")
    protected WanInterface wanInterface;

    /**
     * Gets the value of the wanInterface property.
     * 
     * @return
     *     possible object is
     *     {@link WanInterface }
     *     
     */
    public WanInterface getWanInterface() {
        return wanInterface;
    }

    /**
     * Sets the value of the wanInterface property.
     * 
     * @param value
     *     allowed object is
     *     {@link WanInterface }
     *     
     */
    public void setWanInterface(WanInterface value) {
        this.wanInterface = value;
    }

}
