package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.security.Signer;
import java.sql.Timestamp;
import java.util.Set;


/**
 * 
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * The persistent class for the si_contract_info database table.
 * 
 */
@Entity
@Table(name="si_contract_info")
public class SIContractInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="account_manager")
	private String accountManager;

	@Column(name="account_manager_email")
	private String accountManagerEmail;

	private BigDecimal arc;

	@Column(name="billing_address")
	private String billingAddress;

	@Column(name="billing_frequency")
	private String billingFrequency;

	@Column(name="billing_method")
	private String billingMethod;

	@Column(name="contract_end_date")
	private Timestamp contractEndDate;

	@Column(name="contract_start_date")
	private Timestamp contractStartDate;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="created_date")
	private Timestamp createdDate;

	@Column(name="customer_contact")
	private String customerContact;

	@Column(name="customer_contact_email")
	private String customerContactEmail;

	@Column(name="discount_arc")
	private BigDecimal discountArc;

	@Column(name="discount_mrc")
	private BigDecimal discountMrc;

	@Column(name="discount_nrc")
	private BigDecimal discountNrc;

	@Column(name="erf_cust_currency_id")
	private Integer erfCustCurrencyId;

	@Column(name="erf_cust_le_id")
	private Integer erfCustLeId;

	@Column(name="erf_cust_le_name")
	private String erfCustLeName;

	@Column(name="erf_cust_sp_le_id")
	private Integer erfCustSpLeId;

	@Column(name="erf_cust_sp_le_name")
	private String erfCustSpLeName;

	@Column(name="erf_loc_billing_location_id")
	private String erfLocBillingLocationId;

	@Column(name="is_active")
	private String isActive;

	@Column(name="last_macd_date")
	private String lastMacdDate;

	private BigDecimal mrc;

	private BigDecimal nrc;

	@Column(name="order_term_in_months")
	private Double orderTermInMonths;

	@Column(name="payment_term")
	private String paymentTerm;

	@Column(name="tps_sfdc_cuid")
	private String tpsSfdcCuid;

	@Column(name="updated_by")
	private String updatedBy;

	@Column(name="updated_date")
	private Timestamp updatedDate;

	//bi-directional many-to-one association to SiOrder
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="SI_order_id")
	private SIOrder siOrder;

	//bi-directional many-to-one association to SiServiceDetail
	@OneToMany(mappedBy="siContractInfo")
	private Set<SIServiceDetail> siServiceDetails;
	
	@Column(name = "billing_contact_id")
	private Integer billingContactId;

	@Column(name = "billing_currency")
	private String billingCurrency;

	@Column(name="payment_currency")
	private String paymentCurrency;
	
	@Column(name = "der_price_rev_date")
	private Timestamp devPriceRevDate;
	
	@Column(name = "billing_type")
	private String billingType;


	public SIContractInfo() {

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

	public Timestamp getContractEndDate() {
		return this.contractEndDate;
	}

	public void setContractEndDate(Timestamp contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public Timestamp getContractStartDate() {
		return this.contractStartDate;
	}

	public void setContractStartDate(Timestamp contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
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

	public Timestamp getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public SIOrder getSiOrder() {
		return this.siOrder;
	}

	public void setSiOrder(SIOrder siOrder) {
		this.siOrder = siOrder;
	}
	
	public Set<SIServiceDetail> getSiServiceDetails() {
		return this.siServiceDetails;
	}

	public void setSiServiceDetails(Set<SIServiceDetail> siServiceDetails) {
		this.siServiceDetails = siServiceDetails;
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

	public String getPaymentCurrency() {
		return paymentCurrency;
	}

	public void setPaymentCurrency(String paymentCurrency) {
		this.paymentCurrency = paymentCurrency;
	}
	
	public Timestamp getDevPriceRevDate() {
		return devPriceRevDate;
	}

	public void setDevPriceRevDate(Timestamp devPriceRevDate) {
		this.devPriceRevDate = devPriceRevDate;
	}

	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}
}