
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ALUDefaultOriginateConfig complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ALUDefaultOriginateConfig">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="defaultOriginatePrefixList" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}PrefixList" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ALUDefaultOriginateConfig", namespace = "http://www.tcl.com/2014/3/ipsvc/xsd", propOrder = {
    "defaultOriginatePrefixList"
})
public class ALUDefaultOriginateConfig {

    protected PrefixList defaultOriginatePrefixList;

    /**
     * Gets the value of the defaultOriginatePrefixList property.
     * 
     * @return
     *     possible object is
     *     {@link PrefixList }
     *     
     */
    public PrefixList getDefaultOriginatePrefixList() {
        return defaultOriginatePrefixList;
    }

    /**
     * Sets the value of the defaultOriginatePrefixList property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrefixList }
     *     
     */
    public void setDefaultOriginatePrefixList(PrefixList value) {
        this.defaultOriginatePrefixList = value;
    }

}
