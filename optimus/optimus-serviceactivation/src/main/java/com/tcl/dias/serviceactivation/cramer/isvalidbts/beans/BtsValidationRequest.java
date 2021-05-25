
package com.tcl.dias.serviceactivation.cramer.isvalidbts.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for btsValidationRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="btsValidationRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BTSIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COPFId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Provider" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RequestId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ScenarioType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SectorId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ServiceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "btsValidationRequest", propOrder = {
    "btsip",
    "copfId",
    "provider",
    "requestId",
    "scenarioType",
    "sectorId",
    "serviceId"
})
public class BtsValidationRequest {

    @XmlElement(name = "BTSIP")
    protected String btsip;
    @XmlElement(name = "COPFId")
    protected String copfId;
    @XmlElement(name = "Provider")
    protected String provider;
    @XmlElement(name = "RequestId")
    protected String requestId;
    @XmlElement(name = "ScenarioType")
    protected String scenarioType;
    @XmlElement(name = "SectorId")
    protected String sectorId;
    @XmlElement(name = "ServiceId")
    protected String serviceId;

    /**
     * Gets the value of the btsip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBTSIP() {
        return btsip;
    }

    /**
     * Sets the value of the btsip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBTSIP(String value) {
        this.btsip = value;
    }

    /**
     * Gets the value of the copfId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOPFId() {
        return copfId;
    }

    /**
     * Sets the value of the copfId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOPFId(String value) {
        this.copfId = value;
    }

    /**
     * Gets the value of the provider property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvider() {
        return provider;
    }

    /**
     * Sets the value of the provider property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvider(String value) {
        this.provider = value;
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
     * Gets the value of the sectorId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSectorId() {
        return sectorId;
    }

    /**
     * Sets the value of the sectorId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSectorId(String value) {
        this.sectorId = value;
    }

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

}
