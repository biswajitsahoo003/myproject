//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.02.06 at 03:13:07 PM IST 
//


package com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BGP complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BGP">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="remoteASNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BGP", propOrder = {
    "remoteASNumber"
})
public class BGP {

    protected String remoteASNumber;

    /**
     * Gets the value of the remoteASNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemoteASNumber() {
        return remoteASNumber;
    }

    /**
     * Sets the value of the remoteASNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemoteASNumber(String value) {
        this.remoteASNumber = value;
    }

	@Override
	public String toString() {
		return "BGP [remoteASNumber=" + remoteASNumber + "]";
	}

    
    
}
