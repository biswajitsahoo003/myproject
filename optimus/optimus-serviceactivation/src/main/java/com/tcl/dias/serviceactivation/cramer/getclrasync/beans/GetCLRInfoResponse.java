package com.tcl.dias.serviceactivation.cramer.getclrasync.beans;

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
 *         &lt;element name="header" type="{http://www.tcl.com/2012/09/csvc/xsd}CramerServiceHeader"/&gt;
 *         &lt;element name="response" type="{http://www.tcl.com/2012/09/csvc/xsd}OrderInfo"/&gt;
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
    "header",
    "response"
})
@XmlRootElement(name = "getCLRInfoResponse", namespace = "http://ACE_Common_Lib/ACE_SP_GetCLRStatus/CramerGetCLRResponse")
public class GetCLRInfoResponse {

    @XmlElement(required = true, nillable = true)
    protected CramerServiceHeader header;
    @XmlElement(required = true, nillable = true)
    protected OrderInfo response;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link CramerServiceHeader }
     *     
     */
    public CramerServiceHeader getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link CramerServiceHeader }
     *     
     */
    public void setHeader(CramerServiceHeader value) {
        this.header = value;
    }

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


    @Override
    public String toString() {
        return "GetCLRInfoResponse{" +
                "header=" + header +
                ", response=" + response +
                '}';
    }
}
