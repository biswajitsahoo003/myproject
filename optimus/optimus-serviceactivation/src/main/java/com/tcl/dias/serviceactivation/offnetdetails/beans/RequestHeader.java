
package com.tcl.dias.serviceactivation.offnetdetails.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for requestHeader complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="requestHeader"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="END" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FeasibilityId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PoPName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ProviderName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RequestID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ServiceID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "requestHeader", propOrder = {
    "end",
    "feasibilityId",
    "poPName",
    "providerName",
    "requestID",
    "serviceID"
})
public class RequestHeader {

    @XmlElement(name = "END")
    protected String end;
    @XmlElement(name = "FeasibilityId")
    protected String feasibilityId;
    @XmlElement(name = "PoPName")
    protected String poPName;
    @XmlElement(name = "ProviderName")
    protected String providerName;
    @XmlElement(name = "RequestID", required = true)
    protected String requestID;
    @XmlElement(name = "ServiceID", required = true)
    protected String serviceID;

    /**
     * Gets the value of the end property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEND() {
        return end;
    }

    /**
     * Sets the value of the end property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEND(String value) {
        this.end = value;
    }

    /**
     * Gets the value of the feasibilityId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFeasibilityId() {
        return feasibilityId;
    }

    /**
     * Sets the value of the feasibilityId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFeasibilityId(String value) {
        this.feasibilityId = value;
    }

    /**
     * Gets the value of the poPName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoPName() {
        return poPName;
    }

    /**
     * Sets the value of the poPName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoPName(String value) {
        this.poPName = value;
    }

    /**
     * Gets the value of the providerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProviderName() {
        return providerName;
    }

    /**
     * Sets the value of the providerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProviderName(String value) {
        this.providerName = value;
    }

    /**
     * Gets the value of the requestID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestID() {
        return requestID;
    }

    /**
     * Sets the value of the requestID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestID(String value) {
        this.requestID = value;
    }

    /**
     * Gets the value of the serviceID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceID() {
        return serviceID;
    }

    /**
     * Sets the value of the serviceID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceID(String value) {
        this.serviceID = value;
    }

}
