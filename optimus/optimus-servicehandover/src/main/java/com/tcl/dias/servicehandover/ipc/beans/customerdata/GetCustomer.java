//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.07.24 at 10:33:40 AM IST 
//


package com.tcl.dias.servicehandover.ipc.beans.customerdata;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="GetCustomerInput" type="{http://WS.CustomerManagement.com}GetCustomerInput"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getCustomerInput"
})
@XmlRootElement(name = "getCustomer", namespace = "http://WS.CustomerManagement.com/GetPOSCustomerWS")
public class GetCustomer {

    @XmlElement(name = "GetCustomerInput", required = true, nillable = true)
    protected GetCustomerInput getCustomerInput;

    /**
     * Gets the value of the getCustomerInput property.
     * 
     * @return
     *     possible object is
     *     {@link GetCustomerInput }
     *     
     */
    public GetCustomerInput getGetCustomerInput() {
        return getCustomerInput;
    }

    /**
     * Sets the value of the getCustomerInput property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetCustomerInput }
     *     
     */
    public void setGetCustomerInput(GetCustomerInput value) {
        this.getCustomerInput = value;
    }

}
