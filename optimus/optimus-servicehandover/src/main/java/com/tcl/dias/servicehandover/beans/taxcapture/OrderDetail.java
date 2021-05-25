//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.10.19 at 11:39:50 AM IST 
//


package com.tcl.dias.servicehandover.beans.taxcapture;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrderDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OrderDetail"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CRNID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ProductName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ProductSequence" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="Status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Error_Msg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="COPFID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ViznetID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderDetail", propOrder = {
    "crnid",
    "accountNumber",
    "productName",
    "productSequence",
    "status",
    "errorMsg",
    "copfid",
    "viznetID"
})
public class OrderDetail {

    @XmlElement(name = "CRNID")
    protected String crnid;
    @XmlElement(name = "AccountNumber")
    protected String accountNumber;
    @XmlElement(name = "ProductName")
    protected String productName;
    @XmlElement(name = "ProductSequence")
    protected Integer productSequence;
    @XmlElement(name = "Status")
    protected String status;
    @XmlElement(name = "Error_Msg")
    protected String errorMsg;
    @XmlElement(name = "COPFID")
    protected String copfid;
    @XmlElement(name = "ViznetID")
    protected String viznetID;

    /**
     * Gets the value of the crnid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCRNID() {
        return crnid;
    }

    /**
     * Sets the value of the crnid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCRNID(String value) {
        this.crnid = value;
    }

    /**
     * Gets the value of the accountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the value of the accountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountNumber(String value) {
        this.accountNumber = value;
    }

    /**
     * Gets the value of the productName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the value of the productName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductName(String value) {
        this.productName = value;
    }

    /**
     * Gets the value of the productSequence property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getProductSequence() {
        return productSequence;
    }

    /**
     * Sets the value of the productSequence property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setProductSequence(Integer value) {
        this.productSequence = value;
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
     * Gets the value of the viznetID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getViznetID() {
        return viznetID;
    }

    /**
     * Sets the value of the viznetID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setViznetID(String value) {
        this.viznetID = value;
    }

}
