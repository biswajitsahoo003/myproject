
package com.tcl.dias.serviceactivation.cramer.checkclrinfo.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrderInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OrderInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="customerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errorDetails" type="{http://com.tatacommunications.cramer.ace.service.ws.checkclrstatus}ErrorDetails" minOccurs="0"/>
 *         &lt;element name="isACEActionRequired" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isCienaActionRequired" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isDowntimeRequired" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="serviceContents" type="{http://com.tatacommunications.cramer.ace.service.ws.checkclrstatus}serviceContents" minOccurs="0"/>
 *         &lt;element name="serviceType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderInfo", propOrder = {
    "customerName",
    "customerType",
    "errorDetails",
    "isACEActionRequired",
    "isCienaActionRequired",
    "isDowntimeRequired",
    "serviceContents",
    "serviceType"
})
public class OrderInfo {

    protected String customerName;
    protected String customerType;
    protected ErrorDetails errorDetails;
    protected Boolean isACEActionRequired;
    protected Boolean isCienaActionRequired;
    protected Boolean isDowntimeRequired;
    @XmlSchemaType(name = "string")
    protected ServiceContents serviceContents;
    protected String serviceType;

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
     * Gets the value of the customerType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerType() {
        return customerType;
    }

    /**
     * Sets the value of the customerType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerType(String value) {
        this.customerType = value;
    }

    /**
     * Gets the value of the errorDetails property.
     * 
     * @return
     *     possible object is
     *     {@link ErrorDetails }
     *     
     */
    public ErrorDetails getErrorDetails() {
        return errorDetails;
    }

    /**
     * Sets the value of the errorDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link ErrorDetails }
     *     
     */
    public void setErrorDetails(ErrorDetails value) {
        this.errorDetails = value;
    }

    /**
     * Gets the value of the isACEActionRequired property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsACEActionRequired() {
        return isACEActionRequired;
    }

    /**
     * Sets the value of the isACEActionRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsACEActionRequired(Boolean value) {
        this.isACEActionRequired = value;
    }

    /**
     * Gets the value of the isCienaActionRequired property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCienaActionRequired() {
        return isCienaActionRequired;
    }

    /**
     * Sets the value of the isCienaActionRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCienaActionRequired(Boolean value) {
        this.isCienaActionRequired = value;
    }

    /**
     * Gets the value of the isDowntimeRequired property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsDowntimeRequired() {
        return isDowntimeRequired;
    }

    /**
     * Sets the value of the isDowntimeRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsDowntimeRequired(Boolean value) {
        this.isDowntimeRequired = value;
    }

    /**
     * Gets the value of the serviceContents property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceContents }
     *     
     */
    public ServiceContents getServiceContents() {
        return serviceContents;
    }

    /**
     * Sets the value of the serviceContents property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceContents }
     *     
     */
    public void setServiceContents(ServiceContents value) {
        this.serviceContents = value;
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

}
