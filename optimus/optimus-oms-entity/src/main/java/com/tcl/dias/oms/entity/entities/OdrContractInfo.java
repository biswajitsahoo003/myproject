package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * This class contains entity of odr_contract_info table
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "odr_contract_info")
@NamedQuery(name = "OdrContractInfo.findAll", query = "SELECT o FROM OdrContractInfo o")
public class OdrContractInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "account_manager")
	private String accountManager;

	@Column(name = "account_manager_email")
	private String accountManagerEmail;

	private BigDecimal arc;

	@Column(name = "billing_address")
	private String billingAddress;

	@Column(name = "billing_frequency")
	private String billingFrequency;

	@Column(name = "billing_method")
	private String billingMethod;

	@Column(name = "contract_end_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date contractEndDate;

	@Column(name = "contract_start_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date contractStartDate;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Column(name = "customer_contact")
	private String customerContact;

	@Column(name = "customer_contact_email")
	private String customerContactEmail;

	@Column(name = "discount_arc")
	private BigDecimal discountArc;

	@Column(name = "discount_mrc")
	private BigDecimal discountMrc;

	@Column(name = "discount_nrc")
	private BigDecimal discountNrc;

	@Column(name = "erf_cust_currency_id")
	private Integer erfCustCurrencyId;

	@Column(name = "po_mandatory_status")
	private String poMandatoryStatus;

	@Column(name = "erf_cust_le_id")
	private Integer erfCustLeId;

	@Column(name = "erf_cust_le_name")
	private String erfCustLeName;

	@Column(name = "erf_cust_sp_le_id")
	private Integer erfCustSpLeId;

	@Column(name = "erf_cust_sp_le_name")
	private String erfCustSpLeName;

	@Column(name = "erf_loc_billing_location_id")
	private String erfLocBillingLocationId;

	@Column(name = "is_active")
	private String isActive;

	@Column(name = "last_macd_date")
	private String lastMacdDate;

	private BigDecimal mrc;

	private BigDecimal nrc;

	@Column(name = "order_term_in_months")
	private Double orderTermInMonths;

	@Column(name = "payment_term")
	private String paymentTerm;

	@Column(name = "tps_sfdc_cuid")
	private String tpsSfdcCuid;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;

	// bi-directional many-to-one association to OdrOrder
	@ManyToOne
	@JoinColumn(name = "odr_order_id")
	private OdrOrder odrOrder;

	@Column(name = "billing_address_line_1")
	private String billingAddressLine1;

	@Column(name = "billing_address_line_2")
	private String billingAddressLine2;

	@Column(name = "billing_address_line_3")
	private String billingAddressLine3;

	@Column(name = "billing_state")
	private String billingState;

	@Column(name = "billing_country")
	private String billingCountry;

	@Column(name = "billing_pincode")
	private String billingPincode;

	@Column(name = "billing_city")
	private String billingCity;

	// bi-directional many-to-one association to OdrServiceDetail
	@OneToMany(mappedBy = "odrContractInfo")
	private Set<OdrServiceDetail> odrServiceDetails;

	@Column(name = "billing_contact_id")
	private Integer billingContactId;

	public OdrContractInfo() {
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

	public OdrOrder getOdrOrder() {
		return this.odrOrder;
	}

	public void setOdrOrder(OdrOrder odrOrder) {
		this.odrOrder = odrOrder;
	}

	public Set<OdrServiceDetail> getOdrServiceDetails() {
		return this.odrServiceDetails;
	}

	public void setOdrServiceDetails(Set<OdrServiceDetail> odrServiceDetails) {
		this.odrServiceDetails = odrServiceDetails;
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

	public OdrServiceDetail addOdrServiceDetail(OdrServiceDetail odrServiceDetail) {
		getOdrServiceDetails().add(odrServiceDetail);
		odrServiceDetail.setOdrContractInfo(this);

		return odrServiceDetail;
	}

	public OdrServiceDetail removeOdrServiceDetail(OdrServiceDetail odrServiceDetail) {
		getOdrServiceDetails().remove(odrServiceDetail);
		odrServiceDetail.setOdrContractInfo(null);

		return odrServiceDetail;
	}

	public Integer getBillingContactId() {
		return billingContactId;
	}

	public void setBillingContactId(Integer billingContactId) {
		this.billingContactId = billingContactId;
	}

	public String getPoMandatoryStatus() {
		return poMandatoryStatus;
	}

	public void setPoMandatoryStatus(String poMandatoryStatus) {
		this.poMandatoryStatus = poMandatoryStatus;
	}

}