
package com.tcl.dias.serviceactivation.cramer.eoriordetails.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cramerRequestHeader complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cramerRequestHeader"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ObjectName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ObjectType" type="{http://cramerserviceslibrary/csvc/wsdl/v3/eoriordetails}objectType"/&gt;
 *         &lt;element name="RequestID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="RequestTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RequestingSystem" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cramerRequestHeader", propOrder = {
    "objectName",
    "objectType",
    "requestID",
    "requestTime",
    "requestingSystem"
})
public class CramerRequestHeader {

    @XmlElement(name = "ObjectName", required = true)
    protected String objectName;
    @XmlElement(name = "ObjectType", required = true)
    @XmlSchemaType(name = "string")
    protected ObjectType objectType;
    @XmlElement(name = "RequestID", required = true)
    protected String requestID;
    @XmlElement(name = "RequestTime")
    protected String requestTime;
    @XmlElement(name = "RequestingSystem")
    protected String requestingSystem;

    /**
     * Gets the value of the objectName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * Sets the value of the objectName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjectName(String value) {
        this.objectName = value;
    }

    /**
     * Gets the value of the objectType property.
     * 
     * @return
     *     possible object is
     *     {@link ObjectType }
     *     
     */
    public ObjectType getObjectType() {
        return objectType;
    }

    /**
     * Sets the value of the objectType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectType }
     *     
     */
    public void setObjectType(ObjectType value) {
        this.objectType = value;
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
     * Gets the value of the requestTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestTime() {
        return requestTime;
    }

    /**
     * Sets the value of the requestTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestTime(String value) {
        this.requestTime = value;
    }

    /**
     * Gets the value of the requestingSystem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestingSystem() {
        return requestingSystem;
    }

    /**
     * Sets the value of the requestingSystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestingSystem(String value) {
        this.requestingSystem = value;
    }

}
