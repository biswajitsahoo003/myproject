package com.tcl.dias.serviceactivation.cramer.createservice.beans;



import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="Service_Id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Service_Type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Customer_Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ListOfFeasibilityId" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="COPF_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ServiceBandwidth_Value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ServiceBandwidth_Unit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LMBandwidth_Value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LMBandwidth_Unit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AdditionalIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="scopeOfManagement" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ServiceOption" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RequestID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "", propOrder = {
    "serviceId",
    "serviceType",
    "customerName",
    "listOfFeasibilityId",
    "copfid",
    "serviceBandwidthValue",
    "serviceBandwidthUnit",
    "lmBandwidthValue",
    "lmBandwidthUnit",
    "additionalIP",
    "scopeOfManagement",
    "serviceOption",
    "requestID",
    "requestingSystem"
})
@XmlRootElement(name = "CreateService")
public class CreateService {

    @XmlElement(name = "Service_Id")
    protected String serviceId;
    @XmlElement(name = "Service_Type")
    protected String serviceType;
    @XmlElement(name = "Customer_Name")
    protected String customerName;
    @XmlElement(name = "ListOfFeasibilityId")
    protected List<String> listOfFeasibilityId;
    @XmlElement(name = "COPF_ID")
    protected String copfid;
    @XmlElement(name = "ServiceBandwidth_Value")
    protected String serviceBandwidthValue;
    @XmlElement(name = "ServiceBandwidth_Unit")
    protected String serviceBandwidthUnit;
    @XmlElement(name = "LMBandwidth_Value")
    protected String lmBandwidthValue;
    @XmlElement(name = "LMBandwidth_Unit")
    protected String lmBandwidthUnit;
    @XmlElement(name = "AdditionalIP")
    protected String additionalIP;
    protected String scopeOfManagement;
    @XmlElement(name = "ServiceOption")
    protected String serviceOption;
    @XmlElement(name = "RequestID")
    protected String requestID;
    @XmlElement(name = "RequestingSystem")
    protected String requestingSystem;

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
     * Gets the value of the serviceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Sets the value of the serviceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceType(String value) {
        this.serviceType = value;
    }

    /**
     * Gets the value of the customerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the value of the customerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerName(String value) {
        this.customerName = value;
    }

    /**
     * Gets the value of the listOfFeasibilityId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listOfFeasibilityId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListOfFeasibilityId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getListOfFeasibilityId() {
        if (listOfFeasibilityId == null) {
            listOfFeasibilityId = new ArrayList<String>();
        }
        return this.listOfFeasibilityId;
    }

    /**
     * Gets the value of the copfid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOPFID() {
        return copfid;
    }

    /**
     * Sets the value of the copfid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOPFID(String value) {
        this.copfid = value;
    }

    /**
     * Gets the value of the serviceBandwidthValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceBandwidthValue() {
        return serviceBandwidthValue;
    }

    /**
     * Sets the value of the serviceBandwidthValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceBandwidthValue(String value) {
        this.serviceBandwidthValue = value;
    }

    /**
     * Gets the value of the serviceBandwidthUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceBandwidthUnit() {
        return serviceBandwidthUnit;
    }

    /**
     * Sets the value of the serviceBandwidthUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceBandwidthUnit(String value) {
        this.serviceBandwidthUnit = value;
    }

    /**
     * Gets the value of the lmBandwidthValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLMBandwidthValue() {
        return lmBandwidthValue;
    }

    /**
     * Sets the value of the lmBandwidthValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLMBandwidthValue(String value) {
        this.lmBandwidthValue = value;
    }

    /**
     * Gets the value of the lmBandwidthUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLMBandwidthUnit() {
        return lmBandwidthUnit;
    }

    /**
     * Sets the value of the lmBandwidthUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLMBandwidthUnit(String value) {
        this.lmBandwidthUnit = value;
    }

    /**
     * Gets the value of the additionalIP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalIP() {
        return additionalIP;
    }

    /**
     * Sets the value of the additionalIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalIP(String value) {
        this.additionalIP = value;
    }

    /**
     * Gets the value of the scopeOfManagement property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScopeOfManagement() {
        return scopeOfManagement;
    }

    /**
     * Sets the value of the scopeOfManagement property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScopeOfManagement(String value) {
        this.scopeOfManagement = value;
    }

    /**
     * Gets the value of the serviceOption property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceOption() {
        return serviceOption;
    }

    /**
     * Sets the value of the serviceOption property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceOption(String value) {
        this.serviceOption = value;
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
