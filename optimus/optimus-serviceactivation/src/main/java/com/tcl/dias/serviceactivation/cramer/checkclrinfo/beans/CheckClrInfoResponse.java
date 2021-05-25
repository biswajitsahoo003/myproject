
package com.tcl.dias.serviceactivation.cramer.checkclrinfo.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for checkClrInfoResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="checkClrInfoResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="response" type="{http://com.tatacommunications.cramer.ace.service.ws.checkclrstatus}OrderInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "checkClrInfoResponse", propOrder = {
    "response"
})
@XmlRootElement
public class CheckClrInfoResponse {

    protected OrderInfo response;

    /**
     * Gets the value of the response property.
     * 
     * @return
     *     possible object is
     *     {@link OrderInfo }
     *     
     */
    public OrderInfo getResponse() {
        return response;
    }

    /**
     * Sets the value of the response property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderInfo }
     *     
     */
    public void setResponse(OrderInfo value) {
        this.response = value;
    }

}
