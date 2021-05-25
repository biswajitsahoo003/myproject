
package com.tcl.dias.serviceactivation.cramer.createclrasync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CLRCreationDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CLRCreationDetails"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="requestID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="serviceID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="scenarioType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="applicationName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="isCLRchanged" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="Desciption" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="extraAttrs" type="{http://ACE_CLRCreation_Module}ClrAttrs" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CLRCreationDetails", propOrder = {
    "requestID",
    "serviceID",
    "status",
    "scenarioType",
    "applicationName",
    "isCLRchanged",
    "desciption",
    "extraAttrs"
})
public class CLRCreationDetails {

    @XmlElement(required = true)
    protected String requestID;
    @XmlElement(required = true)
    protected String serviceID;
    protected Boolean status;
    protected String scenarioType;
    protected String applicationName;
    protected Boolean isCLRchanged;
    @XmlElement(name = "Desciption")
    protected String desciption;
    protected ClrAttrs extraAttrs;

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

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setStatus(Boolean value) {
        this.status = value;
    }

    /**
     * Gets the value of the scenarioType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScenarioType() {
        return scenarioType;
    }

    /**
     * Sets the value of the scenarioType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScenarioType(String value) {
        this.scenarioType = value;
    }

    /**
     * Gets the value of the applicationName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * Sets the value of the applicationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationName(String value) {
        this.applicationName = value;
    }

    /**
     * Gets the value of the isCLRchanged property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCLRchanged() {
        return isCLRchanged;
    }

    /**
     * Sets the value of the isCLRchanged property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCLRchanged(Boolean value) {
        this.isCLRchanged = value;
    }

    /**
     * Gets the value of the desciption property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesciption() {
        return desciption;
    }

    /**
     * Sets the value of the desciption property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesciption(String value) {
        this.desciption = value;
    }

    /**
     * Gets the value of the extraAttrs property.
     * 
     * @return
     *     possible object is
     *     {@link ClrAttrs }
     *     
     */
    public ClrAttrs getExtraAttrs() {
        return extraAttrs;
    }

    /**
     * Sets the value of the extraAttrs property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClrAttrs }
     *     
     */
    public void setExtraAttrs(ClrAttrs value) {
        this.extraAttrs = value;
    }

}
