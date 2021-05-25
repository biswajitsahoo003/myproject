//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.07.24 at 10:33:40 AM IST 
//


package com.tcl.dias.servicehandover.ipc.beans.customerdata;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AccountInformation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AccountInformation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AccountNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AccountBillingAddress" type="{http://WS.CustomerManagement.com}AccountBillingAddress" minOccurs="0"/&gt;
 *         &lt;element name="AccountContactDetails" type="{http://WS.CustomerManagement.com}AccountContactDetails" minOccurs="0"/&gt;
 *         &lt;element name="InvoiceMethod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BillingCycle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BillingMethod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BillingType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AccountType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PaymentTerm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SiteEnd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountInformation", propOrder = {
    "accountNo",
    "accountBillingAddress",
    "accountContactDetails",
    "invoiceMethod",
    "billingCycle",
    "billingMethod",
    "billingType",
    "accountType",
    "paymentTerm",
    "siteEnd"
})
public class AccountInformation {

    @XmlElement(name = "AccountNo")
    protected String accountNo;
    @XmlElement(name = "AccountBillingAddress")
    protected AccountBillingAddress accountBillingAddress;
    @XmlElement(name = "AccountContactDetails")
    protected AccountContactDetails accountContactDetails;
    @XmlElement(name = "InvoiceMethod")
    protected String invoiceMethod;
    @XmlElement(name = "BillingCycle")
    protected String billingCycle;
    @XmlElement(name = "BillingMethod")
    protected String billingMethod;
    @XmlElement(name = "BillingType")
    protected String billingType;
    @XmlElement(name = "AccountType")
    protected String accountType;
    @XmlElement(name = "PaymentTerm")
    protected String paymentTerm;
    @XmlElement(name = "SiteEnd")
    protected String siteEnd;

    /**
     * Gets the value of the accountNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountNo() {
        return accountNo;
    }

    /**
     * Sets the value of the accountNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountNo(String value) {
        this.accountNo = value;
    }

    /**
     * Gets the value of the accountBillingAddress property.
     * 
     * @return
     *     possible object is
     *     {@link AccountBillingAddress }
     *     
     */
    public AccountBillingAddress getAccountBillingAddress() {
        return accountBillingAddress;
    }

    /**
     * Sets the value of the accountBillingAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccountBillingAddress }
     *     
     */
    public void setAccountBillingAddress(AccountBillingAddress value) {
        this.accountBillingAddress = value;
    }

    /**
     * Gets the value of the accountContactDetails property.
     * 
     * @return
     *     possible object is
     *     {@link AccountContactDetails }
     *     
     */
    public AccountContactDetails getAccountContactDetails() {
        return accountContactDetails;
    }

    /**
     * Sets the value of the accountContactDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccountContactDetails }
     *     
     */
    public void setAccountContactDetails(AccountContactDetails value) {
        this.accountContactDetails = value;
    }

    /**
     * Gets the value of the invoiceMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvoiceMethod() {
        return invoiceMethod;
    }

    /**
     * Sets the value of the invoiceMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvoiceMethod(String value) {
        this.invoiceMethod = value;
    }

    /**
     * Gets the value of the billingCycle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingCycle() {
        return billingCycle;
    }

    /**
     * Sets the value of the billingCycle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingCycle(String value) {
        this.billingCycle = value;
    }

    /**
     * Gets the value of the billingMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingMethod() {
        return billingMethod;
    }

    /**
     * Sets the value of the billingMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingMethod(String value) {
        this.billingMethod = value;
    }

    /**
     * Gets the value of the billingType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingType() {
        return billingType;
    }

    /**
     * Sets the value of the billingType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingType(String value) {
        this.billingType = value;
    }

    /**
     * Gets the value of the accountType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * Sets the value of the accountType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountType(String value) {
        this.accountType = value;
    }

    /**
     * Gets the value of the paymentTerm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentTerm() {
        return paymentTerm;
    }

    /**
     * Sets the value of the paymentTerm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentTerm(String value) {
        this.paymentTerm = value;
    }

    /**
     * Gets the value of the siteEnd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiteEnd() {
        return siteEnd;
    }

    /**
     * Sets the value of the siteEnd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiteEnd(String value) {
        this.siteEnd = value;
    }

}
