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
 *         &lt;element name="request" type="{http://www.tcl.com/2012/09/csvc/xsd}CramerTxServiceRequestFailure"/&gt;
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
    "request"
})
@XmlRootElement(name = "submitGetCramerServiceFailureResponse", namespace = "http://ACE_Common_Lib/ACE_SP_GetCLRStatus/CramerGetCLRResponse")
public class SubmitGetCramerServiceFailureResponse {

    @XmlElement(required = true, nillable = true)
    protected CramerServiceHeader header;
    @XmlElement(required = true, nillable = true)
    protected CramerTxServiceRequestFailure request;

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
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link CramerTxServiceRequestFailure }
     *     
     */
    public CramerTxServiceRequestFailure getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link CramerTxServiceRequestFailure }
     *     
     */
    public void setRequest(CramerTxServiceRequestFailure value) {
        this.request = value;
    }

}
