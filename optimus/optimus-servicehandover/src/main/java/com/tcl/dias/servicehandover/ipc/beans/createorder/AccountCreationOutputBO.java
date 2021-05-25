//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.08.12 at 10:48:57 AM IST 
//


package com.tcl.dias.servicehandover.ipc.beans.createorder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AccountCreationOutputBO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AccountCreationOutputBO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="account_Attributes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="account_no" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="account_response" type="{http://InstaCCLibrary/BSSEBOs}ResponseHeader" minOccurs="0"/&gt;
 *         &lt;element name="success_message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountCreationOutputBO", propOrder = {
    "accountAttributes",
    "accountNo",
    "accountResponse",
    "successMessage"
})
public class AccountCreationOutputBO {

    @XmlElement(name = "account_Attributes")
    protected String accountAttributes;
    @XmlElement(name = "account_no")
    protected String accountNo;
    @XmlElement(name = "account_response")
    protected ResponseHeader accountResponse;
    @XmlElement(name = "success_message")
    protected String successMessage;

    /**
     * Gets the value of the accountAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountAttributes() {
        return accountAttributes;
    }

    /**
     * Sets the value of the accountAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountAttributes(String value) {
        this.accountAttributes = value;
    }

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
     * Gets the value of the accountResponse property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseHeader }
     *     
     */
    public ResponseHeader getAccountResponse() {
        return accountResponse;
    }

    /**
     * Sets the value of the accountResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseHeader }
     *     
     */
    public void setAccountResponse(ResponseHeader value) {
        this.accountResponse = value;
    }

    /**
     * Gets the value of the successMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSuccessMessage() {
        return successMessage;
    }

    /**
     * Sets the value of the successMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSuccessMessage(String value) {
        this.successMessage = value;
    }

}
