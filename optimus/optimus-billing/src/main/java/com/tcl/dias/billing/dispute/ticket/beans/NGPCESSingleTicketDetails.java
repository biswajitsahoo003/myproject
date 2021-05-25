
package com.tcl.dias.billing.dispute.ticket.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for NGP_CES_Single_Ticket_Details complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NGP_CES_Single_Ticket_Details"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="source" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="customerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="custReferenceNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="circuitID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="serviceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="invoiceNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="issueCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="issueType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="issueCategory" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="oldTTID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="invoiceDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="disputeStartDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="disputeEndDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="amountStuck" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="issueDetails" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="contactPerson" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="custEmail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="primaryContactNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="alternateContactNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="createContactFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="updateContactFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="currencyType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="docAvailability" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="contactType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="userName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="issueAttr1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="issueAttr2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="issueAttr3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="issueAttr4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="issueAttr5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="issueAttr6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="issueAttr7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="issueAttr8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="issueAttr9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="issueAttr10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="attachmentContent" type="{http://CESV/CES_NGP_Single_Ticket.tws}ArrayOf_string" minOccurs="0"/&gt;
 *         &lt;element name="attachmentName" type="{http://CESV/CES_NGP_Single_Ticket.tws}ArrayOf_string" minOccurs="0"/&gt;
 *         &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="city" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="region" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="department" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="alternateEmail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="desgn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NGP_CES_Single_Ticket_Details", namespace = "http://CESV/CES_NGP_Single_Ticket.tws", propOrder = {
    "source",
    "customerName",
    "custReferenceNo",
    "circuitID",
    "serviceID",
    "invoiceNo",
    "issueCode",
    "issueType",
    "issueCategory",
    "oldTTID",
    "invoiceDate",
    "disputeStartDate",
    "disputeEndDate",
    "amountStuck",
    "issueDetails",
    "contactPerson",
    "custEmail",
    "primaryContactNo",
    "alternateContactNo",
    "createContactFlag",
    "updateContactFlag",
    "currencyType",
    "docAvailability",
    "contactType",
    "userName",
    "issueAttr1",
    "issueAttr2",
    "issueAttr3",
    "issueAttr4",
    "issueAttr5",
    "issueAttr6",
    "issueAttr7",
    "issueAttr8",
    "issueAttr9",
    "issueAttr10",
    "attachmentContent",
    "attachmentName",
    "address",
    "city",
    "state",
    "region",
    "department",
    "alternateEmail",
    "desgn"
})
public class NGPCESSingleTicketDetails {

    protected String source;
    protected String customerName;
    protected String custReferenceNo;
    protected String circuitID;
    protected String serviceID;
    protected String invoiceNo;
    protected String issueCode;
    protected String issueType;
    protected String issueCategory;
    protected String oldTTID;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar invoiceDate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar disputeStartDate;
    protected String disputeEndDate;
    protected Double amountStuck;
    protected String issueDetails;
    protected String contactPerson;
    protected String custEmail;
    protected String primaryContactNo;
    protected String alternateContactNo;
    protected Boolean createContactFlag;
    protected Boolean updateContactFlag;
    protected String currencyType;
    protected String docAvailability;
    protected String contactType;
    protected String userName;
    protected String issueAttr1;
    protected String issueAttr2;
    protected String issueAttr3;
    protected String issueAttr4;
    protected String issueAttr5;
    protected String issueAttr6;
    protected String issueAttr7;
    protected String issueAttr8;
    protected String issueAttr9;
    protected String issueAttr10;
    protected ArrayOfString attachmentContent;
    protected ArrayOfString attachmentName;
    protected String address;
    protected String city;
    protected String state;
    protected String region;
    protected String department;
    protected String alternateEmail;
    protected String desgn;

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSource(String value) {
        this.source = value;
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
     * Gets the value of the custReferenceNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustReferenceNo() {
        return custReferenceNo;
    }

    /**
     * Sets the value of the custReferenceNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustReferenceNo(String value) {
        this.custReferenceNo = value;
    }

    /**
     * Gets the value of the circuitID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCircuitID() {
        return circuitID;
    }

    /**
     * Sets the value of the circuitID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCircuitID(String value) {
        this.circuitID = value;
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
     * Gets the value of the invoiceNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvoiceNo() {
        return invoiceNo;
    }

    /**
     * Sets the value of the invoiceNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvoiceNo(String value) {
        this.invoiceNo = value;
    }

    /**
     * Gets the value of the issueCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueCode() {
        return issueCode;
    }

    /**
     * Sets the value of the issueCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueCode(String value) {
        this.issueCode = value;
    }

    /**
     * Gets the value of the issueType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueType() {
        return issueType;
    }

    /**
     * Sets the value of the issueType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueType(String value) {
        this.issueType = value;
    }

    /**
     * Gets the value of the issueCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueCategory() {
        return issueCategory;
    }

    /**
     * Sets the value of the issueCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueCategory(String value) {
        this.issueCategory = value;
    }

    /**
     * Gets the value of the oldTTID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOldTTID() {
        return oldTTID;
    }

    /**
     * Sets the value of the oldTTID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOldTTID(String value) {
        this.oldTTID = value;
    }

    /**
     * Gets the value of the invoiceDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getInvoiceDate() {
        return invoiceDate;
    }

    /**
     * Sets the value of the invoiceDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setInvoiceDate(XMLGregorianCalendar value) {
        this.invoiceDate = value;
    }

    /**
     * Gets the value of the disputeStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDisputeStartDate() {
        return disputeStartDate;
    }

    /**
     * Sets the value of the disputeStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDisputeStartDate(XMLGregorianCalendar value) {
        this.disputeStartDate = value;
    }

    /**
     * Gets the value of the disputeEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisputeEndDate() {
        return disputeEndDate;
    }

    /**
     * Sets the value of the disputeEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisputeEndDate(String value) {
        this.disputeEndDate = value;
    }

    /**
     * Gets the value of the amountStuck property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getAmountStuck() {
        return amountStuck;
    }

    /**
     * Sets the value of the amountStuck property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setAmountStuck(Double value) {
        this.amountStuck = value;
    }

    /**
     * Gets the value of the issueDetails property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueDetails() {
        return issueDetails;
    }

    /**
     * Sets the value of the issueDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueDetails(String value) {
        this.issueDetails = value;
    }

    /**
     * Gets the value of the contactPerson property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContactPerson() {
        return contactPerson;
    }

    /**
     * Sets the value of the contactPerson property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContactPerson(String value) {
        this.contactPerson = value;
    }

    /**
     * Gets the value of the custEmail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustEmail() {
        return custEmail;
    }

    /**
     * Sets the value of the custEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustEmail(String value) {
        this.custEmail = value;
    }

    /**
     * Gets the value of the primaryContactNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimaryContactNo() {
        return primaryContactNo;
    }

    /**
     * Sets the value of the primaryContactNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimaryContactNo(String value) {
        this.primaryContactNo = value;
    }

    /**
     * Gets the value of the alternateContactNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlternateContactNo() {
        return alternateContactNo;
    }

    /**
     * Sets the value of the alternateContactNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlternateContactNo(String value) {
        this.alternateContactNo = value;
    }

    /**
     * Gets the value of the createContactFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCreateContactFlag() {
        return createContactFlag;
    }

    /**
     * Sets the value of the createContactFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCreateContactFlag(Boolean value) {
        this.createContactFlag = value;
    }

    /**
     * Gets the value of the updateContactFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isUpdateContactFlag() {
        return updateContactFlag;
    }

    /**
     * Sets the value of the updateContactFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUpdateContactFlag(Boolean value) {
        this.updateContactFlag = value;
    }

    /**
     * Gets the value of the currencyType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyType() {
        return currencyType;
    }

    /**
     * Sets the value of the currencyType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyType(String value) {
        this.currencyType = value;
    }

    /**
     * Gets the value of the docAvailability property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocAvailability() {
        return docAvailability;
    }

    /**
     * Sets the value of the docAvailability property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocAvailability(String value) {
        this.docAvailability = value;
    }

    /**
     * Gets the value of the contactType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContactType() {
        return contactType;
    }

    /**
     * Sets the value of the contactType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContactType(String value) {
        this.contactType = value;
    }

    /**
     * Gets the value of the userName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Gets the value of the issueAttr1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueAttr1() {
        return issueAttr1;
    }

    /**
     * Sets the value of the issueAttr1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueAttr1(String value) {
        this.issueAttr1 = value;
    }

    /**
     * Gets the value of the issueAttr2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueAttr2() {
        return issueAttr2;
    }

    /**
     * Sets the value of the issueAttr2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueAttr2(String value) {
        this.issueAttr2 = value;
    }

    /**
     * Gets the value of the issueAttr3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueAttr3() {
        return issueAttr3;
    }

    /**
     * Sets the value of the issueAttr3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueAttr3(String value) {
        this.issueAttr3 = value;
    }

    /**
     * Gets the value of the issueAttr4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueAttr4() {
        return issueAttr4;
    }

    /**
     * Sets the value of the issueAttr4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueAttr4(String value) {
        this.issueAttr4 = value;
    }

    /**
     * Gets the value of the issueAttr5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueAttr5() {
        return issueAttr5;
    }

    /**
     * Sets the value of the issueAttr5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueAttr5(String value) {
        this.issueAttr5 = value;
    }

    /**
     * Gets the value of the issueAttr6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueAttr6() {
        return issueAttr6;
    }

    /**
     * Sets the value of the issueAttr6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueAttr6(String value) {
        this.issueAttr6 = value;
    }

    /**
     * Gets the value of the issueAttr7 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueAttr7() {
        return issueAttr7;
    }

    /**
     * Sets the value of the issueAttr7 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueAttr7(String value) {
        this.issueAttr7 = value;
    }

    /**
     * Gets the value of the issueAttr8 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueAttr8() {
        return issueAttr8;
    }

    /**
     * Sets the value of the issueAttr8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueAttr8(String value) {
        this.issueAttr8 = value;
    }

    /**
     * Gets the value of the issueAttr9 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueAttr9() {
        return issueAttr9;
    }

    /**
     * Sets the value of the issueAttr9 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueAttr9(String value) {
        this.issueAttr9 = value;
    }

    /**
     * Gets the value of the issueAttr10 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueAttr10() {
        return issueAttr10;
    }

    /**
     * Sets the value of the issueAttr10 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueAttr10(String value) {
        this.issueAttr10 = value;
    }

    /**
     * Gets the value of the attachmentContent property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getAttachmentContent() {
        return attachmentContent;
    }

    /**
     * Sets the value of the attachmentContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setAttachmentContent(ArrayOfString value) {
        this.attachmentContent = value;
    }

    /**
     * Gets the value of the attachmentName property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getAttachmentName() {
        return attachmentName;
    }

    /**
     * Sets the value of the attachmentName property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setAttachmentName(ArrayOfString value) {
        this.attachmentName = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setState(String value) {
        this.state = value;
    }

    /**
     * Gets the value of the region property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegion() {
        return region;
    }

    /**
     * Sets the value of the region property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegion(String value) {
        this.region = value;
    }

    /**
     * Gets the value of the department property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the value of the department property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepartment(String value) {
        this.department = value;
    }

    /**
     * Gets the value of the alternateEmail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlternateEmail() {
        return alternateEmail;
    }

    /**
     * Sets the value of the alternateEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlternateEmail(String value) {
        this.alternateEmail = value;
    }

    /**
     * Gets the value of the desgn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesgn() {
        return desgn;
    }

    /**
     * Sets the value of the desgn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesgn(String value) {
        this.desgn = value;
    }

}
