
package com.tcl.dias.serviceactivation.cramer.checkipclr.beans;

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
 * &lt;complexType name="requestHeader">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RequestId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RequestTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RequestingSystem" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ScenarioType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "requestHeader", propOrder = {
    "requestId",
    "requestTime",
    "requestingSystem",
    "scenarioType"
})
public class RequestHeader {

    @XmlElement(name = "RequestId")
    protected String requestId;
    @XmlElement(name = "RequestTime")
    protected String requestTime;
    @XmlElement(name = "RequestingSystem")
    protected String requestingSystem;
    @XmlElement(name = "ScenarioType")
    protected String scenarioType;

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

}
