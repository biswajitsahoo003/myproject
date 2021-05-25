
package com.tcl.dias.serviceactivation.cramer.isvalidbts.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for isValidBTS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="isValidBTS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="requestAttr" type="{http://cramerserviceslibrary/csvc/wsdl/v2/ws/bts}btsValidationRequest" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "isValidBTS", propOrder = {
    "requestAttr"
})
@XmlRootElement
public class IsValidBTS {

    protected BtsValidationRequest requestAttr;

    /**
     * Gets the value of the requestAttr property.
     * 
     * @return
     *     possible object is
     *     {@link BtsValidationRequest }
     *     
     */
    public BtsValidationRequest getRequestAttr() {
        return requestAttr;
    }

    /**
     * Sets the value of the requestAttr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BtsValidationRequest }
     *     
     */
    public void setRequestAttr(BtsValidationRequest value) {
        this.requestAttr = value;
    }

}
