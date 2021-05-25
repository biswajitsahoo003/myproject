package com.tcl.dias.oms.teamsdr.beans;

/**
 * Bean for handling legal entity values.
 * @author Syed Ali
 */
public class TeamsDRDocumentBean {
    private Integer quoteId;
    private Integer quoteLeId;
    private Integer customerLegalEntityId;
    private Integer supplierLegalEntityId;
    private Integer customerId;
    private String productName;
    private String billingFrequency;
    private String billingMethod;
    private String paymentTerm;

    public TeamsDRDocumentBean() {
    }

    public Integer getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Integer quoteId) {
        this.quoteId = quoteId;
    }

    public Integer getQuoteLeId() {
        return quoteLeId;
    }

    public void setQuoteLeId(Integer quoteLeId) {
        this.quoteLeId = quoteLeId;
    }

    public Integer getCustomerLegalEntityId() {
        return customerLegalEntityId;
    }

    public void setCustomerLegalEntityId(Integer customerLegalEntityId) {
        this.customerLegalEntityId = customerLegalEntityId;
    }

    public Integer getSupplierLegalEntityId() {
        return supplierLegalEntityId;
    }

    public void setSupplierLegalEntityId(Integer supplierLegalEntityId) {
        this.supplierLegalEntityId = supplierLegalEntityId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBillingFrequency() {
        return billingFrequency;
    }

    public void setBillingFrequency(String billingFrequency) {
        this.billingFrequency = billingFrequency;
    }

    public String getBillingMethod() {
        return billingMethod;
    }

    public void setBillingMethod(String billingMethod) {
        this.billingMethod = billingMethod;
    }

    public String getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(String paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

	@Override
	public String toString() {
		return "TeamsDRDocumentBean{" +
				"quoteId=" + quoteId +
				", quoteLeId=" + quoteLeId +
				", customerLegalEntityId=" + customerLegalEntityId +
				", supplierLegalEntityId=" + supplierLegalEntityId +
				", customerId=" + customerId +
				", productName='" + productName + '\'' +
				", billingFrequency='" + billingFrequency + '\'' +
				", billingMethod='" + billingMethod + '\'' +
				", paymentTerm='" + paymentTerm + '\'' +
				'}';
	}
}
