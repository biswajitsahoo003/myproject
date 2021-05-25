package com.tcl.dias.serviceactivation.cramer.getclrasync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrderInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OrderInfo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="customer" type="{http://www.tcl.com/2012/09/csvc/xsd}Customer"/&gt;
 *         &lt;element name="isDowntimeRequired" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="portBandwidth" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="service" type="{http://www.tcl.com/2012/09/csvc/xsd}TransmissionService"/&gt;
 *         &lt;element name="serviceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="serviceType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderInfo", propOrder = {
    "customer",
    "isDowntimeRequired",
    "portBandwidth",
    "service",
    "serviceId",
    "serviceType"
})
public class OrderInfo {

    @XmlElement(required = true)
    protected Customer customer;
    protected Boolean isDowntimeRequired;
    protected String portBandwidth;
    @XmlElement(required = true)
    protected TransmissionService service;
    protected String serviceId;
    protected String serviceType;

    /**
     * Gets the value of the customer property.
     * 
     * @return
     *     possible object is
     *     {@link Customer }
     *     
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Sets the value of the customer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Customer }
     *     
     */
    public void setCustomer(Customer value) {
        this.customer = value;
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
     * Gets the value of the portBandwidth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPortBandwidth() {
        return portBandwidth;
    }

    /**
     * Sets the value of the portBandwidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPortBandwidth(String value) {
        this.portBandwidth = value;
    }

    /**
     * Gets the value of the service property.
     * 
     * @return
     *     possible object is
     *     {@link TransmissionService }
     *     
     */
    public TransmissionService getService() {
        return service;
    }

    /**
     * Sets the value of the service property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransmissionService }
     *     
     */
    public void setService(TransmissionService value) {
        this.service = value;
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

    @Override
    public String toString() {
        return "OrderInfo{" +
                "customer=" + customer +
                ", isDowntimeRequired=" + isDowntimeRequired +
                ", portBandwidth='" + portBandwidth + '\'' +
                ", service=" + service +
                ", serviceId='" + serviceId + '\'' +
                ", serviceType='" + serviceType + '\'' +
                '}';
    }
}
