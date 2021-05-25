package com.tcl.dias.common.serviceinventory.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ScContractInfoBean {

	private Integer id;
	private String accountManager;
	private String accountManagerEmail;
	private BigDecimal arc;
	private String billingAddress;
	private String billingFrequency;
	private String billingMethod;
	private Date contractEndDate;
	private Date contractStartDate;
	private String createdBy;
	private Date createdDate;
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
	private Date updatedDate;
	private ScOrderBean scOrder;
	private Set<ScServiceDetailBean> scServiceDetails;
	private String billingAddressLine1;
	private String billingAddressLine2;
	private String billingAddressLine3;
	private String billingState;
	private String billingCountry;
	private String billingPincode;
	private String billingCity;
	private Integer billingContactId;

	public ScContractInfoBean() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccountManager() {
		return this.accountManager;
	}

	public void setAccountManager(String accountManager) {
		this.accountManager = accountManager;
	}

	public String getAccountManagerEmail() {
		return this.accountManagerEmail;
	}

	public void setAccountManagerEmail(String accountManagerEmail) {
		this.accountManagerEmail = accountManagerEmail;
	}

	public BigDecimal getArc() {
		return this.arc;
	}

	public void setArc(BigDecimal arc) {
		this.arc = arc;
	}

	public String getBillingAddress() {
		return this.billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getBillingFrequency() {
		return this.billingFrequency;
	}

	public void setBillingFrequency(String billingFrequency) {
		this.billingFrequency = billingFrequency;
	}

	public String getBillingMethod() {
		return this.billingMethod;
	}

	public void setBillingMethod(String billingMethod) {
		this.billingMethod = billingMethod;
	}

	public Date getContractEndDate() {
		return this.contractEndDate;
	}

	public void setContractEndDate(Date contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public Date getContractStartDate() {
		return this.contractStartDate;
	}

	public void setContractStartDate(Date contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCustomerContact() {
		return this.customerContact;
	}

	public void setCustomerContact(String customerContact) {
		this.customerContact = customerContact;
	}

	public String getCustomerContactEmail() {
		return this.customerContactEmail;
	}

	public void setCustomerContactEmail(String customerContactEmail) {
		this.customerContactEmail = customerContactEmail;
	}

	public BigDecimal getDiscountArc() {
		return this.discountArc;
	}

	public void setDiscountArc(BigDecimal discountArc) {
		this.discountArc = discountArc;
	}

	public BigDecimal getDiscountMrc() {
		return this.discountMrc;
	}

	public void setDiscountMrc(BigDecimal discountMrc) {
		this.discountMrc = discountMrc;
	}

	public BigDecimal getDiscountNrc() {
		return this.discountNrc;
	}

	public void setDiscountNrc(BigDecimal discountNrc) {
		this.discountNrc = discountNrc;
	}

	public Integer getErfCustCurrencyId() {
		return this.erfCustCurrencyId;
	}

	public void setErfCustCurrencyId(Integer erfCustCurrencyId) {
		this.erfCustCurrencyId = erfCustCurrencyId;
	}

	public Integer getErfCustLeId() {
		return this.erfCustLeId;
	}

	public void setErfCustLeId(Integer erfCustLeId) {
		this.erfCustLeId = erfCustLeId;
	}

	public String getErfCustLeName() {
		return this.erfCustLeName;
	}

	public void setErfCustLeName(String erfCustLeName) {
		this.erfCustLeName = erfCustLeName;
	}

	public Integer getErfCustSpLeId() {
		return this.erfCustSpLeId;
	}

	public void setErfCustSpLeId(Integer erfCustSpLeId) {
		this.erfCustSpLeId = erfCustSpLeId;
	}

	public String getErfCustSpLeName() {
		return this.erfCustSpLeName;
	}

	public void setErfCustSpLeName(String erfCustSpLeName) {
		this.erfCustSpLeName = erfCustSpLeName;
	}

	public String getErfLocBillingLocationId() {
		return this.erfLocBillingLocationId;
	}

	public void setErfLocBillingLocationId(String erfLocBillingLocationId) {
		this.erfLocBillingLocationId = erfLocBillingLocationId;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getLastMacdDate() {
		return this.lastMacdDate;
	}

	public void setLastMacdDate(String lastMacdDate) {
		this.lastMacdDate = lastMacdDate;
	}

	public BigDecimal getMrc() {
		return this.mrc;
	}

	public void setMrc(BigDecimal mrc) {
		this.mrc = mrc;
	}

	public BigDecimal getNrc() {
		return this.nrc;
	}

	public void setNrc(BigDecimal nrc) {
		this.nrc = nrc;
	}

	public Double getOrderTermInMonths() {
		return this.orderTermInMonths;
	}

	public void setOrderTermInMonths(Double orderTermInMonths) {
		this.orderTermInMonths = orderTermInMonths;
	}

	public String getPaymentTerm() {
		return this.paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public String getTpsSfdcCuid() {
		return this.tpsSfdcCuid;
	}

	public void setTpsSfdcCuid(String tpsSfdcCuid) {
		this.tpsSfdcCuid = tpsSfdcCuid;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public ScOrderBean getScOrder() {
		return scOrder;
	}

	public void setScOrder(ScOrderBean scOrder) {
		this.scOrder = scOrder;
	}

	public Set<ScServiceDetailBean> getScServiceDetails() {
		return scServiceDetails;
	}

	public String getBillingAddressLine1() {
		return billingAddressLine1;
	}

	public void setBillingAddressLine1(String billingAddressLine1) {
		this.billingAddressLine1 = billingAddressLine1;
	}

	public String getBillingAddressLine2() {
		return billingAddressLine2;
	}

	public void setBillingAddressLine2(String billingAddressLine2) {
		this.billingAddressLine2 = billingAddressLine2;
	}

	public String getBillingAddressLine3() {
		return billingAddressLine3;
	}

	public void setBillingAddressLine3(String billingAddressLine3) {
		this.billingAddressLine3 = billingAddressLine3;
	}

	public String getBillingState() {
		return billingState;
	}

	public void setBillingState(String billingState) {
		this.billingState = billingState;
	}

	public String getBillingCountry() {
		return billingCountry;
	}

	public void setBillingCountry(String billingCountry) {
		this.billingCountry = billingCountry;
	}

	public String getBillingPincode() {
		return billingPincode;
	}

	public void setBillingPincode(String billingPincode) {
		this.billingPincode = billingPincode;
	}

	public String getBillingCity() {
		return billingCity;
	}

	public void setBillingCity(String billingCity) {
		this.billingCity = billingCity;
	}

	public void setScServiceDetails(Set<ScServiceDetailBean> scServiceDetails) {
		this.scServiceDetails = scServiceDetails;
	}

	public ScServiceDetailBean addScServiceDetail(ScServiceDetailBean scServiceDetail) {
		getScServiceDetails().add(scServiceDetail);
		scServiceDetail.setScContractInfo(this);
		return scServiceDetail;
	}

	public ScServiceDetailBean removeScServiceDetail(ScServiceDetailBean scServiceDetail) {
		getScServiceDetails().remove(scServiceDetail);
		scServiceDetail.setScContractInfo(null);
		return scServiceDetail;
	}

	public Integer getBillingContactId() {
		return billingContactId;
	}

	public void setBillingContactId(Integer billingContactId) {
		this.billingContactId = billingContactId;
	}

	@Override
	public String toString() {
		return "ScContractInfoBean [id=" + id + ", accountManager=" + accountManager + ", accountManagerEmail="
				+ accountManagerEmail + ", arc=" + arc + ", billingAddress=" + billingAddress + ", billingFrequency="
				+ billingFrequency + ", billingMethod=" + billingMethod + ", contractEndDate=" + contractEndDate
				+ ", contractStartDate=" + contractStartDate + ", createdBy=" + createdBy + ", createdDate="
				+ createdDate + ", customerContact=" + customerContact + ", customerContactEmail="
				+ customerContactEmail + ", discountArc=" + discountArc + ", discountMrc=" + discountMrc
				+ ", discountNrc=" + discountNrc + ", erfCustCurrencyId=" + erfCustCurrencyId + ", erfCustLeId="
				+ erfCustLeId + ", erfCustLeName=" + erfCustLeName + ", erfCustSpLeId=" + erfCustSpLeId
				+ ", erfCustSpLeName=" + erfCustSpLeName + ", erfLocBillingLocationId=" + erfLocBillingLocationId
				+ ", isActive=" + isActive + ", lastMacdDate=" + lastMacdDate + ", mrc=" + mrc + ", nrc=" + nrc
				+ ", orderTermInMonths=" + orderTermInMonths + ", paymentTerm=" + paymentTerm + ", tpsSfdcCuid="
				+ tpsSfdcCuid + ", updatedBy=" + updatedBy + ", updatedDate=" + updatedDate + ", scOrder=" + scOrder
				+ ", scServiceDetails=" + scServiceDetails + ", billingAddressLine1=" + billingAddressLine1
				+ ", billingAddressLine2=" + billingAddressLine2 + ", billingAddressLine3=" + billingAddressLine3
				+ ", billingState=" + billingState + ", billingCountry=" + billingCountry + ", billingPincode="
				+ billingPincode + ", billingCity=" + billingCity + ", billingContactId=" + billingContactId + "]";
	}

}