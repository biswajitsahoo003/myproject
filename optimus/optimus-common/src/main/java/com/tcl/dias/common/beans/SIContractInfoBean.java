package com.tcl.dias.common.beans;

import java.math.BigDecimal;
import java.sql.Timestamp;


public class SIContractInfoBean {
	
	private Integer id;
	private String accountManager;
	private String accountManagerEmail;
	private BigDecimal arc;
	private String billingAddress;
	private String billingFrequency;
	private String billingMethod;
	private Timestamp contractEndDate;
	private Timestamp contractStartDate;
	private String createdBy;
	private Timestamp createdDate;
	private String customerContact;
	private String customerContactEmail;
	private BigDecimal discountArc;
	private BigDecimal discountMrc;
	private BigDecimal discountNrc;
	private Integer erfCustCurrencyId;
	private Integer erfCustLeId;
	private String erfCustLeName;
	private Integer erfCustSpLeId;
	private String erfCustSpLeName;
	private String erfLocBillingLocationId;
	private String isActive;
	private String lastMacdDate;
	private BigDecimal mrc;
	private BigDecimal nrc;
	private Double orderTermInMonths;
	private String paymentTerm;
	private String tpsSfdcCuid;
	private String updatedBy;
	private Timestamp updatedDate;
	private Integer billingContactId;
	private String billingCurrency;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAccountManager() {
		return accountManager;
	}
	public void setAccountManager(String accountManager) {
		this.accountManager = accountManager;
	}
	public String getAccountManagerEmail() {
		return accountManagerEmail;
	}
	public void setAccountManagerEmail(String accountManagerEmail) {
		this.accountManagerEmail = accountManagerEmail;
	}
	public BigDecimal getArc() {
		return arc;
	}
	public void setArc(BigDecimal arc) {
		this.arc = arc;
	}
	public String getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
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
	public Timestamp getContractEndDate() {
		return contractEndDate;
	}
	public void setContractEndDate(Timestamp contractEndDate) {
		this.contractEndDate = contractEndDate;
	}
	public Timestamp getContractStartDate() {
		return contractStartDate;
	}
	public void setContractStartDate(Timestamp contractStartDate) {
		this.contractStartDate = contractStartDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public String getCustomerContact() {
		return customerContact;
	}
	public void setCustomerContact(String customerContact) {
		this.customerContact = customerContact;
	}
	public String getCustomerContactEmail() {
		return customerContactEmail;
	}
	public void setCustomerContactEmail(String customerContactEmail) {
		this.customerContactEmail = customerContactEmail;
	}
	public BigDecimal getDiscountArc() {
		return discountArc;
	}
	public void setDiscountArc(BigDecimal discountArc) {
		this.discountArc = discountArc;
	}
	public BigDecimal getDiscountMrc() {
		return discountMrc;
	}
	public void setDiscountMrc(BigDecimal discountMrc) {
		this.discountMrc = discountMrc;
	}
	public BigDecimal getDiscountNrc() {
		return discountNrc;
	}
	public void setDiscountNrc(BigDecimal discountNrc) {
		this.discountNrc = discountNrc;
	}
	public Integer getErfCustCurrencyId() {
		return erfCustCurrencyId;
	}
	public void setErfCustCurrencyId(Integer erfCustCurrencyId) {
		this.erfCustCurrencyId = erfCustCurrencyId;
	}
	public Integer getErfCustLeId() {
		return erfCustLeId;
	}
	public void setErfCustLeId(Integer erfCustLeId) {
		this.erfCustLeId = erfCustLeId;
	}
	public String getErfCustLeName() {
		return erfCustLeName;
	}
	public void setErfCustLeName(String erfCustLeName) {
		this.erfCustLeName = erfCustLeName;
	}
	public Integer getErfCustSpLeId() {
		return erfCustSpLeId;
	}
	public void setErfCustSpLeId(Integer erfCustSpLeId) {
		this.erfCustSpLeId = erfCustSpLeId;
	}
	public String getErfCustSpLeName() {
		return erfCustSpLeName;
	}
	public void setErfCustSpLeName(String erfCustSpLeName) {
		this.erfCustSpLeName = erfCustSpLeName;
	}
	public String getErfLocBillingLocationId() {
		return erfLocBillingLocationId;
	}
	public void setErfLocBillingLocationId(String erfLocBillingLocationId) {
		this.erfLocBillingLocationId = erfLocBillingLocationId;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getLastMacdDate() {
		return lastMacdDate;
	}
	public void setLastMacdDate(String lastMacdDate) {
		this.lastMacdDate = lastMacdDate;
	}
	public BigDecimal getMrc() {
		return mrc;
	}
	public void setMrc(BigDecimal mrc) {
		this.mrc = mrc;
	}
	public BigDecimal getNrc() {
		return nrc;
	}
	public void setNrc(BigDecimal nrc) {
		this.nrc = nrc;
	}
	public Double getOrderTermInMonths() {
		return orderTermInMonths;
	}
	public void setOrderTermInMonths(Double orderTermInMonths) {
		this.orderTermInMonths = orderTermInMonths;
	}
	public String getPaymentTerm() {
		return paymentTerm;
	}
	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}
	public String getTpsSfdcCuid() {
		return tpsSfdcCuid;
	}
	public void setTpsSfdcCuid(String tpsSfdcCuid) {
		this.tpsSfdcCuid = tpsSfdcCuid;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Timestamp getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Integer getBillingContactId() {
		return billingContactId;
	}
	public void setBillingContactId(Integer billingContactId) {
		this.billingContactId = billingContactId;
	}
	public String getBillingCurrency() {
		return billingCurrency;
	}
	public void setBillingCurrency(String billingCurrency) {
		this.billingCurrency = billingCurrency;
	}

	@Override
	public String toString() {
		return "SIContractInfoBean{" +
				"id=" + id +
				", accountManager='" + accountManager + '\'' +
				", accountManagerEmail='" + accountManagerEmail + '\'' +
				", arc=" + arc +
				", billingAddress='" + billingAddress + '\'' +
				", billingFrequency='" + billingFrequency + '\'' +
				", billingMethod='" + billingMethod + '\'' +
				", contractEndDate=" + contractEndDate +
				", contractStartDate=" + contractStartDate +
				", createdBy='" + createdBy + '\'' +
				", createdDate=" + createdDate +
				", customerContact='" + customerContact + '\'' +
				", customerContactEmail='" + customerContactEmail + '\'' +
				", discountArc=" + discountArc +
				", discountMrc=" + discountMrc +
				", discountNrc=" + discountNrc +
				", erfCustCurrencyId=" + erfCustCurrencyId +
				", erfCustLeId=" + erfCustLeId +
				", erfCustLeName='" + erfCustLeName + '\'' +
				", erfCustSpLeId=" + erfCustSpLeId +
				", erfCustSpLeName='" + erfCustSpLeName + '\'' +
				", erfLocBillingLocationId='" + erfLocBillingLocationId + '\'' +
				", isActive='" + isActive + '\'' +
				", lastMacdDate='" + lastMacdDate + '\'' +
				", mrc=" + mrc +
				", nrc=" + nrc +
				", orderTermInMonths=" + orderTermInMonths +
				", paymentTerm='" + paymentTerm + '\'' +
				", tpsSfdcCuid='" + tpsSfdcCuid + '\'' +
				", updatedBy='" + updatedBy + '\'' +
				", updatedDate=" + updatedDate +
				", billingContactId=" + billingContactId +
				", billingCurrency='" + billingCurrency + '\'' +
				'}';
	}
}
