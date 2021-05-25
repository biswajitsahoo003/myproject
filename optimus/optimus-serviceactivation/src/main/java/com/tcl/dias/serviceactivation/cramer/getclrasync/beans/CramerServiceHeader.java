package com.tcl.dias.serviceactivation.cramer.getclrasync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CramerServiceHeader complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CramerServiceHeader"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="RequestID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="originatingSystem" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="originationTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="authUser" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ClientSystemIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CramerServiceHeader", propOrder = {
    "requestID",
    "originatingSystem",
    "originationTime",
    "authUser",
    "clientSystemIP"
})
public class CramerServiceHeader {

    @XmlElement(name = "RequestID")
    protected String requestID;
    protected String originatingSystem;
    protected String originationTime;
    protected String authUser;
    @XmlElement(name = "ClientSystemIP")
    protected String clientSystemIP;

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
     * Gets the value of the originatingSystem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginatingSystem() {
        return originatingSystem;
    }

    /**
     * Sets the value of the originatingSystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginatingSystem(String value) {
        this.originatingSystem = value;
    }

    /**
     * Gets the value of the originationTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginationTime() {
        return originationTime;
    }

    /**
     * Sets the value of the originationTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginationTime(String value) {
        this.originationTime = value;
    }

    /**
     * Gets the value of the authUser property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthUser() {
        return authUser;
    }

    /**
     * Sets the value of the authUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthUser(String value) {
        this.authUser = value;
    }

    /**
     * Gets the value of the clientSystemIP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientSystemIP() {
        return clientSystemIP;
    }

    /**
     * Sets the value of the clientSystemIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientSystemIP(String value) {
        this.clientSystemIP = value;
    }


    @Override
    public String toString() {
        return "CramerServiceHeader{" +
                "requestID='" + requestID + '\'' +
                ", originatingSystem='" + originatingSystem + '\'' +
                ", originationTime='" + originationTime + '\'' +
                ", authUser='" + authUser + '\'' +
                ", clientSystemIP='" + clientSystemIP + '\'' +
                '}';
    }
}
