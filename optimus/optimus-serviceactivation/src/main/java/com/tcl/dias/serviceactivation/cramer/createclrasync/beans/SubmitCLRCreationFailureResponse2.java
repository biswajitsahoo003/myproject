
package com.tcl.dias.serviceactivation.cramer.createclrasync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SubmitCLRCreationFailureResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SubmitCLRCreationFailureResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="serviceId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="requestId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="scenarioType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="applicationName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cLRCreationFailureResponse" type="{http://ACE_CLRCreation_Module}CLRcreationFailureResponse" minOccurs="0"/&gt;
 *         &lt;element name="extraAttrs" type="{http://ACE_CLRCreation_Module}RespAttr" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubmitCLRCreationFailureResponse", propOrder = {
    "serviceId",
    "requestId",
    "scenarioType",
    "applicationName",
    "clrCreationFailureResponse",
    "extraAttrs"
})
public class SubmitCLRCreationFailureResponse2 {

    @XmlElement(required = true)
    protected String serviceId;
    @XmlElement(required = true)
    protected String requestId;
    protected String scenarioType;
    protected String applicationName;
    @XmlElement(name = "cLRCreationFailureResponse")
    protected CLRcreationFailureResponse clrCreationFailureResponse;
    protected RespAttr extraAttrs;

    /**
     * Gets the value of the serviceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * Sets the value of the serviceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceId(String value) {
        this.serviceId = value;
    }

    /**
     * Gets the value of the requestId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Sets the value of the requestId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestId(String value) {
        this.requestId = value;
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
     * Gets the value of the clrCreationFailureResponse property.
     * 
     * @return
     *     possible object is
     *     {@link CLRcreationFailureResponse }
     *     
     */
    public CLRcreationFailureResponse getCLRCreationFailureResponse() {
        return clrCreationFailureResponse;
    }

    /**
     * Sets the value of the clrCreationFailureResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link CLRcreationFailureResponse }
     *     
     */
    public void setCLRCreationFailureResponse(CLRcreationFailureResponse value) {
        this.clrCreationFailureResponse = value;
    }

    /**
     * Gets the value of the extraAttrs property.
     * 
     * @return
     *     possible object is
     *     {@link RespAttr }
     *     
     */
    public RespAttr getExtraAttrs() {
        return extraAttrs;
    }

    /**
     * Sets the value of the extraAttrs property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespAttr }
     *     
     */
    public void setExtraAttrs(RespAttr value) {
        this.extraAttrs = value;
    }

}
