
package com.tcl.dias.serviceactivation.cramer.servicedesign.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for orderDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="orderDetails"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="BANDWIDTH_UNIT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BANDWIDTH_VALUE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="COVERAGE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CUSTOMER_NAME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="OrderType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PRERESERVED" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="RequestType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "orderDetails", propOrder = {
    "bandwidthunit",
    "bandwidthvalue",
    "coverage",
    "customername",
    "orderType",
    "prereserved",
    "requestType"
})
public class OrderDetails {

    @XmlElement(name = "BANDWIDTH_UNIT")
    protected String bandwidthunit;
    @XmlElement(name = "BANDWIDTH_VALUE")
    protected String bandwidthvalue;
    @XmlElement(name = "COVERAGE")
    protected String coverage;
    @XmlElement(name = "CUSTOMER_NAME")
    protected String customername;
    @XmlElement(name = "OrderType")
    protected String orderType;
    @XmlElement(name = "PRERESERVED")
    protected boolean prereserved;
    @XmlElement(name = "RequestType")
    protected String requestType;

    /**
     * Gets the value of the bandwidthunit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBANDWIDTHUNIT() {
        return bandwidthunit;
    }

    /**
     * Sets the value of the bandwidthunit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBANDWIDTHUNIT(String value) {
        this.bandwidthunit = value;
    }

    /**
     * Gets the value of the bandwidthvalue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBANDWIDTHVALUE() {
        return bandwidthvalue;
    }

    /**
     * Sets the value of the bandwidthvalue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBANDWIDTHVALUE(String value) {
        this.bandwidthvalue = value;
    }

    /**
     * Gets the value of the coverage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOVERAGE() {
        return coverage;
    }

    /**
     * Sets the value of the coverage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOVERAGE(String value) {
        this.coverage = value;
    }

    /**
     * Gets the value of the customername property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUSTOMERNAME() {
        return customername;
    }

    /**
     * Sets the value of the customername property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUSTOMERNAME(String value) {
        this.customername = value;
    }

    /**
     * Gets the value of the orderType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderType() {
        return orderType;
    }

    /**
     * Sets the value of the orderType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderType(String value) {
        this.orderType = value;
    }

    /**
     * Gets the value of the prereserved property.
     * 
     */
    public boolean isPRERESERVED() {
        return prereserved;
    }

    /**
     * Sets the value of the prereserved property.
     * 
     */
    public void setPRERESERVED(boolean value) {
        this.prereserved = value;
    }

    /**
     * Gets the value of the requestType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * Sets the value of the requestType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestType(String value) {
        this.requestType = value;
    }

}
