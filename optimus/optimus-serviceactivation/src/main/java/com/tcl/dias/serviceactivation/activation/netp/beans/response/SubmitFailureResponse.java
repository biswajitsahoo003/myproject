
package com.tcl.dias.serviceactivation.activation.netp.beans.response;

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
 *         &lt;element name="request" type="{http://www.tcl.com/2011/11/netpsvc/xsd}ConfigFailureResponse"/&gt;
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
    "request"
})
@XmlRootElement(name = "submitFailureResponse", namespace = "http://www.tcl.com/v1/netpresponse/inf")
public class SubmitFailureResponse {

    @XmlElement(required = true, nillable = true)
    protected ConfigFailureResponse request;

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link ConfigFailureResponse }
     *     
     */
    public ConfigFailureResponse getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConfigFailureResponse }
     *     
     */
    public void setRequest(ConfigFailureResponse value) {
        this.request = value;
    }

}
