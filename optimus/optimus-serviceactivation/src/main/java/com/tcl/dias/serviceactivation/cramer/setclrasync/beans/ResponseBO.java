
package com.tcl.dias.serviceactivation.cramer.setclrasync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for responseBO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="responseBO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="authUser" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ClientSystemIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="originatingSystem" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="originationTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="requestID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="scenarioType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="taskType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="errorCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="errorMsg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="extraAttribute1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="extraAttribute2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="extraAttribute3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="extraAttribute4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="extraAttribute5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "responseBO", propOrder = {
    "authUser",
    "clientSystemIP",
    "originatingSystem",
    "originationTime",
    "requestID",
    "scenarioType",
    "taskType",
    "status",
    "errorCode",
    "errorMsg",
    "extraAttribute1",
    "extraAttribute2",
    "extraAttribute3",
    "extraAttribute4",
    "extraAttribute5"
})
public class ResponseBO {

    protected String authUser;
    @XmlElement(name = "ClientSystemIP")
    protected String clientSystemIP;
    protected String originatingSystem;
    protected String originationTime;
    protected String requestID;
    protected String scenarioType;
    protected String taskType;
    protected String status;
    protected String errorCode;
    protected String errorMsg;
    protected String extraAttribute1;
    protected String extraAttribute2;
    protected String extraAttribute3;
    protected String extraAttribute4;
    protected String extraAttribute5;

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
     * Gets the value of the taskType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaskType() {
        return taskType;
    }

    /**
     * Sets the value of the taskType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaskType(String value) {
        this.taskType = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the errorCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the value of the errorCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorCode(String value) {
        this.errorCode = value;
    }

    /**
     * Gets the value of the errorMsg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * Sets the value of the errorMsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorMsg(String value) {
        this.errorMsg = value;
    }

    /**
     * Gets the value of the extraAttribute1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtraAttribute1() {
        return extraAttribute1;
    }

    /**
     * Sets the value of the extraAttribute1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtraAttribute1(String value) {
        this.extraAttribute1 = value;
    }

    /**
     * Gets the value of the extraAttribute2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtraAttribute2() {
        return extraAttribute2;
    }

    /**
     * Sets the value of the extraAttribute2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtraAttribute2(String value) {
        this.extraAttribute2 = value;
    }

    /**
     * Gets the value of the extraAttribute3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtraAttribute3() {
        return extraAttribute3;
    }

    /**
     * Sets the value of the extraAttribute3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtraAttribute3(String value) {
        this.extraAttribute3 = value;
    }

    /**
     * Gets the value of the extraAttribute4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtraAttribute4() {
        return extraAttribute4;
    }

    /**
     * Sets the value of the extraAttribute4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtraAttribute4(String value) {
        this.extraAttribute4 = value;
    }

    /**
     * Gets the value of the extraAttribute5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtraAttribute5() {
        return extraAttribute5;
    }

    /**
     * Sets the value of the extraAttribute5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtraAttribute5(String value) {
        this.extraAttribute5 = value;
    }

}
