
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="service" type="{http://www.tcl.com/2011/11/transmissionsvc/xsd}TransmissionService"/>
 *         &lt;element name="oldservice" type="{http://www.tcl.com/2011/11/transmissionsvc/xsd}TransmissionService" minOccurs="0"/>
 *         &lt;element name="serviceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="oldServiceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="copfId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerDetails" type="{http://www.tcl.com/2011/11/ace/common/xsd}Customer" minOccurs="0"/>
 *         &lt;element name="scheduleId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderCategory" type="{http://www.tcl.com/2011/11/ace/common/xsd}OrderCategory" minOccurs="0"/>
 *         &lt;element name="orderType" type="{http://www.tcl.com/2011/11/transmissionsvc/xsd}OrderType" minOccurs="0"/>
 *         &lt;element name="orderId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="portBandwidth" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serviceBandwidth" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serviceType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requiredTraceAndSwitchOver" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="ActivityType" type="{http://www.tcl.com/2011/11/transmissionsvc/xsd}ActivityType" minOccurs="0"/>
 *         &lt;element name="PlannedChangeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isSDHConfigurable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isMPLSTPConfigurable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderInfo", namespace = "http://www.tcl.com/2011/11/transmissionsvc/xsd", propOrder = {
    "service",
    "oldservice",
    "serviceId",
    "oldServiceId",
    "copfId",
    "customerDetails",
    "scheduleId",
    "orderCategory",
    "orderType",
    "orderId",
    "portBandwidth",
    "serviceBandwidth",
    "serviceType",
    "requiredTraceAndSwitchOver",
    "activityType",
    "plannedChangeType",
    "isSDHConfigurable",
    "isMPLSTPConfigurable"
})
public class OrderInfo2 {

    @XmlElement(required = true)
    protected TransmissionService service;
    protected TransmissionService oldservice;
    protected String serviceId;
    protected String oldServiceId;
    protected String copfId;
    protected Customer customerDetails;
    protected String scheduleId;
    @XmlSchemaType(name = "string")
    protected OrderCategory orderCategory;
    @XmlSchemaType(name = "string")
    protected OrderType3 orderType;
    protected String orderId;
    protected String portBandwidth;
    protected String serviceBandwidth;
    protected String serviceType;
    protected Boolean requiredTraceAndSwitchOver;
    @XmlElement(name = "ActivityType")
    @XmlSchemaType(name = "string")
    protected ActivityType activityType;
    @XmlElement(name = "PlannedChangeType")
    protected String plannedChangeType;
    protected Boolean isSDHConfigurable;
    protected Boolean isMPLSTPConfigurable;

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
     * Gets the value of the oldservice property.
     * 
     * @return
     *     possible object is
     *     {@link TransmissionService }
     *     
     */
    public TransmissionService getOldservice() {
        return oldservice;
    }

    /**
     * Sets the value of the oldservice property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransmissionService }
     *     
     */
    public void setOldservice(TransmissionService value) {
        this.oldservice = value;
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
     * Gets the value of the oldServiceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOldServiceId() {
        return oldServiceId;
    }

    /**
     * Sets the value of the oldServiceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOldServiceId(String value) {
        this.oldServiceId = value;
    }

    /**
     * Gets the value of the copfId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCopfId() {
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
    public void setCopfId(String value) {
        this.copfId = value;
    }

    /**
     * Gets the value of the customerDetails property.
     * 
     * @return
     *     possible object is
     *     {@link Customer }
     *     
     */
    public Customer getCustomerDetails() {
        return customerDetails;
    }

    /**
     * Sets the value of the customerDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link Customer }
     *     
     */
    public void setCustomerDetails(Customer value) {
        this.customerDetails = value;
    }

    /**
     * Gets the value of the scheduleId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScheduleId() {
        return scheduleId;
    }

    /**
     * Sets the value of the scheduleId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScheduleId(String value) {
        this.scheduleId = value;
    }

    /**
     * Gets the value of the orderCategory property.
     * 
     * @return
     *     possible object is
     *     {@link OrderCategory }
     *     
     */
    public OrderCategory getOrderCategory() {
        return orderCategory;
    }

    /**
     * Sets the value of the orderCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderCategory }
     *     
     */
    public void setOrderCategory(OrderCategory value) {
        this.orderCategory = value;
    }

    /**
     * Gets the value of the orderType property.
     * 
     * @return
     *     possible object is
     *     {@link OrderType3 }
     *     
     */
    public OrderType3 getOrderType() {
        return orderType;
    }

    /**
     * Sets the value of the orderType property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderType3 }
     *     
     */
    public void setOrderType(OrderType3 value) {
        this.orderType = value;
    }

    /**
     * Gets the value of the orderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * Sets the value of the orderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderId(String value) {
        this.orderId = value;
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
     * Gets the value of the serviceBandwidth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceBandwidth() {
        return serviceBandwidth;
    }

    /**
     * Sets the value of the serviceBandwidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceBandwidth(String value) {
        this.serviceBandwidth = value;
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
     * Gets the value of the requiredTraceAndSwitchOver property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRequiredTraceAndSwitchOver() {
        return requiredTraceAndSwitchOver;
    }

    /**
     * Sets the value of the requiredTraceAndSwitchOver property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRequiredTraceAndSwitchOver(Boolean value) {
        this.requiredTraceAndSwitchOver = value;
    }

    /**
     * Gets the value of the activityType property.
     * 
     * @return
     *     possible object is
     *     {@link ActivityType }
     *     
     */
    public ActivityType getActivityType() {
        return activityType;
    }

    /**
     * Sets the value of the activityType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActivityType }
     *     
     */
    public void setActivityType(ActivityType value) {
        this.activityType = value;
    }

    /**
     * Gets the value of the plannedChangeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlannedChangeType() {
        return plannedChangeType;
    }

    /**
     * Sets the value of the plannedChangeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlannedChangeType(String value) {
        this.plannedChangeType = value;
    }

    /**
     * Gets the value of the isSDHConfigurable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsSDHConfigurable() {
        return isSDHConfigurable;
    }

    /**
     * Sets the value of the isSDHConfigurable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsSDHConfigurable(Boolean value) {
        this.isSDHConfigurable = value;
    }

    /**
     * Gets the value of the isMPLSTPConfigurable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsMPLSTPConfigurable() {
        return isMPLSTPConfigurable;
    }

    /**
     * Sets the value of the isMPLSTPConfigurable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsMPLSTPConfigurable(Boolean value) {
        this.isMPLSTPConfigurable = value;
    }

}
