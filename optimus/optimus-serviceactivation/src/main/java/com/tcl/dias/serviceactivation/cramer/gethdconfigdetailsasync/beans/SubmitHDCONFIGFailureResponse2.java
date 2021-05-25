
package com.tcl.dias.serviceactivation.cramer.gethdconfigdetailsasync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SubmitHDCONFIGFailureResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SubmitHDCONFIGFailureResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="requestID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="serviceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="oldServiceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="scenarioType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="HDCONFIGFailureResponse" type="{http://ACEIAS_HDCONFIG_Module}HDCONFIGFailureResponse" minOccurs="0"/&gt;
 *         &lt;element name="extraAttrs" type="{http://ACEIAS_HDCONFIG_Module}HDCONFIGAttrs" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubmitHDCONFIGFailureResponse", propOrder = {
    "requestID",
    "serviceID",
    "oldServiceID",
    "scenarioType",
    "hdconfigFailureResponse",
    "extraAttrs"
})
public class SubmitHDCONFIGFailureResponse2 {

    protected String requestID;
    protected String serviceID;
    protected String oldServiceID;
    protected String scenarioType;
    @XmlElement(name = "HDCONFIGFailureResponse")
    protected HDCONFIGFailureResponse hdconfigFailureResponse;
    protected HDCONFIGAttrs extraAttrs;

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
     * Gets the value of the oldServiceID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOldServiceID() {
        return oldServiceID;
    }

    /**
     * Sets the value of the oldServiceID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOldServiceID(String value) {
        this.oldServiceID = value;
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
     * Gets the value of the hdconfigFailureResponse property.
     * 
     * @return
     *     possible object is
     *     {@link HDCONFIGFailureResponse }
     *     
     */
    public HDCONFIGFailureResponse getHDCONFIGFailureResponse() {
        return hdconfigFailureResponse;
    }

    /**
     * Sets the value of the hdconfigFailureResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link HDCONFIGFailureResponse }
     *     
     */
    public void setHDCONFIGFailureResponse(HDCONFIGFailureResponse value) {
        this.hdconfigFailureResponse = value;
    }

    /**
     * Gets the value of the extraAttrs property.
     * 
     * @return
     *     possible object is
     *     {@link HDCONFIGAttrs }
     *     
     */
    public HDCONFIGAttrs getExtraAttrs() {
        return extraAttrs;
    }

    /**
     * Sets the value of the extraAttrs property.
     * 
     * @param value
     *     allowed object is
     *     {@link HDCONFIGAttrs }
     *     
     */
    public void setExtraAttrs(HDCONFIGAttrs value) {
        this.extraAttrs = value;
    }

}
