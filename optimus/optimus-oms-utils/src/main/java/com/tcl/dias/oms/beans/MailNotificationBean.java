package com.tcl.dias.oms.beans;

import java.util.Map;

/**
 * Mail Notification bean to hold all required field
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class MailNotificationBean {

    private String customerEmail;
    private String accountManagerEmail;
    private String orderId;
    private String quoteLink;
    private String estimatedReadinessDate;
    private String productName;
    private String classification;
    private String endCustomerLegalEntityName;
    private String subjectMsg;
    private String customerName;
    private String customerAccountName;
    private String userName;
    private String userContactNumber;
    private String userEmail;
    private String fileName;
    private Map<String, String> cofObjectMapper;
    private String contactEntityName;
    private String supllierEntityName;
    private String accountManagerContact;
    private String projectManagerEmail;
    private String subOrderId;
    private String effectiveDeliveryDate;
    private String orderType;

    public MailNotificationBean() {
    }

    public MailNotificationBean(String customerEmail, String accountManagerEmail, String orderId, String quoteLink, String productName) {
        this.customerEmail = customerEmail;
        this.accountManagerEmail = accountManagerEmail;
        this.orderId = orderId;
        this.quoteLink = quoteLink;
        this.productName = productName;
    }

    public MailNotificationBean(String customerEmail, String accountManagerEmail, String orderId, String quoteLink,
                                String estimatedReadinessDate, String productName) {
        this.customerEmail = customerEmail;
        this.accountManagerEmail = accountManagerEmail;
        this.orderId = orderId;
        this.quoteLink = quoteLink;
        this.estimatedReadinessDate = estimatedReadinessDate;
        this.productName = productName;
    }

    public MailNotificationBean(String customerEmail, String accountManagerEmail, String orderId, String quoteLink,
                                String estimatedReadinessDate, String productName, String classification, String endCustomerLegalEntityName) {
        this(customerEmail, accountManagerEmail, orderId, quoteLink, estimatedReadinessDate, productName);
        this.classification = classification;
        this.endCustomerLegalEntityName = endCustomerLegalEntityName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getAccountManagerEmail() {
        return accountManagerEmail;
    }

    public void setAccountManagerEmail(String accountManagerEmail) {
        this.accountManagerEmail = accountManagerEmail;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getQuoteLink() {
        return quoteLink;
    }

    public void setQuoteLink(String quoteLink) {
        this.quoteLink = quoteLink;
    }

    public String getEstimatedReadinessDate() {
        return estimatedReadinessDate;
    }

    public void setEstimatedReadinessDate(String estimatedReadinessDate) {
        this.estimatedReadinessDate = estimatedReadinessDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getEndCustomerLegalEntityName() {
        return endCustomerLegalEntityName;
    }

    public void setEndCustomerLegalEntityName(String endCustomerLegalEntityName) {
        this.endCustomerLegalEntityName = endCustomerLegalEntityName;
    }

    public String getSubjectMsg() {
        return subjectMsg;
    }

    public void setSubjectMsg(String subjectMsg) {
        this.subjectMsg = subjectMsg;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAccountName() {
        return customerAccountName;
    }

    public void setCustomerAccountName(String customerAccountName) {
        this.customerAccountName = customerAccountName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserContactNumber() {
        return userContactNumber;
    }

    public void setUserContactNumber(String userContactNumber) {
        this.userContactNumber = userContactNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Map<String, String> getCofObjectMapper() {
        return cofObjectMapper;
    }

    public void setCofObjectMapper(Map<String, String> cofObjectMapper) {
        this.cofObjectMapper = cofObjectMapper;
    }

    public String getContactEntityName() {
        return contactEntityName;
    }

    public void setContactEntityName(String contactEntityName) {
        this.contactEntityName = contactEntityName;
    }

    public String getSupllierEntityName() {
        return supllierEntityName;
    }

    public void setSupplierEntityName(String supllierEntityName) {
        this.supllierEntityName = supllierEntityName;
    }

    public String getAccountManagerContact() {
        return accountManagerContact;
    }

    public void setAccountManagerContact(String accountManagerContact) {
        this.accountManagerContact = accountManagerContact;
    }

    public String getProjectManagerEmail() {
        return projectManagerEmail;
    }

    public void setProjectManagerEmail(String projectManagerEmail) {
        this.projectManagerEmail = projectManagerEmail;
    }

    @Override
    public String toString() {
        return "MailNotificationBean{" +
                "customerEmail='" + customerEmail + '\'' +
                ", accountManagerEmail='" + accountManagerEmail + '\'' +
                ", orderId='" + orderId + '\'' +
                ", quoteLink='" + quoteLink + '\'' +
                ", estimatedReadinessDate='" + estimatedReadinessDate + '\'' +
                ", productName='" + productName + '\'' +
                ", classification='" + classification + '\'' +
                ", endCustomerLegalEntityName='" + endCustomerLegalEntityName + '\'' +
                ", subjectMsg='" + subjectMsg + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerAccountName='" + customerAccountName + '\'' +
                ", userName='" + userName + '\'' +
                ", userContactNumber='" + userContactNumber + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", fileName='" + fileName + '\'' +
                ", cofObjectMapper=" + cofObjectMapper +
                ", contactEntityName='" + contactEntityName + '\'' +
                ", supllierEntityName='" + supllierEntityName + '\'' +
                ", orderType='" + orderType + '\'' +
                ", accountManagerContact='" + accountManagerContact + '\'' +
                '}';
    }

    public String getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId) {
        this.subOrderId = subOrderId;
    }

    public String getEffectiveDeliveryDate() {
        return effectiveDeliveryDate;
    }

    public void setEffectiveDeliveryDate(String effectiveDeliveryDate) {
        this.effectiveDeliveryDate = effectiveDeliveryDate;
    }

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
  
}
