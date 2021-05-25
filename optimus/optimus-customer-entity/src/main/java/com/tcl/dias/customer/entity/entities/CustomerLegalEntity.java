package com.tcl.dias.customer.entity.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the customer_legal_entity database table.
 * 
 */
@Entity
@Table(name = "customer_legal_entity")
@NamedQuery(name = "CustomerLegalEntity.findAll", query = "SELECT c FROM CustomerLegalEntity c")
public class CustomerLegalEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "agreement_id")
	private String agreementId;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@Column(name = "entity_name")
	private String entityName;

	private Byte status;

	@Column(name = "tps_sfdc_cuid")
	private String tpsSfdcCuid;
	
	@Column(name = "customer_le_code")
	private String customerLeCode;
	
	@Column(name="preapproved_mrc")
	private Double preApprovedMrc;
	
	@Column(name="preapproved_nrc")
	private Double preApprovedNrc;
	
	@Column(name="preapproved_payment_term")
	private String preApprovedPaymentTerm;
	
	@Column(name="preapproved_billing_method")
	private String preApprovedBillingMethod;
	
	@Column(name="credit_check_account_type")
	private String creditCheckAccountType;
	
	@Column(name="blacklist_status")
	private String blacklistStatus;
	
	@Column(name="credit_preapproved_flag")
	private String creditPreapprovedFlag;

	// bi-directional many-to-one association to CustomerLeAttributeValue
	@OneToMany(mappedBy = "customerLegalEntity")
	private Set<CustomerLeAttributeValue> customerLeAttributeValues;

	// bi-directional many-to-one association to CustomerLeCountry
	@OneToMany(mappedBy = "customerLegalEntity")
	private Set<CustomerLeCountry> customerLeCountries;

	// bi-directional many-to-one association to CustomerLeCurrency
	@OneToMany(mappedBy = "customerLegalEntity")
	private Set<CustomerLeCurrency> customerLeCurrencies;

	// bi-directional many-to-one association to Customer
	@ManyToOne(fetch = FetchType.LAZY)
	private Customer customer;

	//bi-directional many-to-one association to PartnerEngagement
	@OneToMany(mappedBy="customerLegalEntity")
	private Set<PartnerEngagement> partnerEngagements;

	public CustomerLegalEntity() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAgreementId() {
		return this.agreementId;
	}

	public void setAgreementId(String agreementId) {
		this.agreementId = agreementId;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getEntityName() {
		return this.entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getTpsSfdcCuid() {
		return this.tpsSfdcCuid;
	}

	public void setTpsSfdcCuid(String tpsSfdcCuid) {
		this.tpsSfdcCuid = tpsSfdcCuid;
	}

	public Set<CustomerLeAttributeValue> getCustomerLeAttributeValues() {
		return this.customerLeAttributeValues;
	}

	public void setCustomerLeAttributeValues(Set<CustomerLeAttributeValue> customerLeAttributeValues) {
		this.customerLeAttributeValues = customerLeAttributeValues;
	}

	public CustomerLeAttributeValue addCustomerLeAttributeValue(CustomerLeAttributeValue customerLeAttributeValue) {
		getCustomerLeAttributeValues().add(customerLeAttributeValue);
		customerLeAttributeValue.setCustomerLegalEntity(this);

		return customerLeAttributeValue;
	}

	public CustomerLeAttributeValue removeCustomerLeAttributeValue(CustomerLeAttributeValue customerLeAttributeValue) {
		getCustomerLeAttributeValues().remove(customerLeAttributeValue);
		customerLeAttributeValue.setCustomerLegalEntity(null);

		return customerLeAttributeValue;
	}

	public Set<CustomerLeCountry> getCustomerLeCountries() {
		return this.customerLeCountries;
	}

	public void setCustomerLeCountries(Set<CustomerLeCountry> customerLeCountries) {
		this.customerLeCountries = customerLeCountries;
	}

	public CustomerLeCountry addCustomerLeCountry(CustomerLeCountry customerLeCountry) {
		getCustomerLeCountries().add(customerLeCountry);
		customerLeCountry.setCustomerLegalEntity(this);

		return customerLeCountry;
	}

	public CustomerLeCountry removeCustomerLeCountry(CustomerLeCountry customerLeCountry) {
		getCustomerLeCountries().remove(customerLeCountry);
		customerLeCountry.setCustomerLegalEntity(null);

		return customerLeCountry;
	}

	public Set<CustomerLeCurrency> getCustomerLeCurrencies() {
		return this.customerLeCurrencies;
	}

	public void setCustomerLeCurrencies(Set<CustomerLeCurrency> customerLeCurrencies) {
		this.customerLeCurrencies = customerLeCurrencies;
	}

	public CustomerLeCurrency addCustomerLeCurrency(CustomerLeCurrency customerLeCurrency) {
		getCustomerLeCurrencies().add(customerLeCurrency);
		customerLeCurrency.setCustomerLegalEntity(this);

		return customerLeCurrency;
	}

	public CustomerLeCurrency removeCustomerLeCurrency(CustomerLeCurrency customerLeCurrency) {
		getCustomerLeCurrencies().remove(customerLeCurrency);
		customerLeCurrency.setCustomerLegalEntity(null);

		return customerLeCurrency;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return the customerLeCode
	 */
	public String getCustomerLeCode() {
		return customerLeCode;
	}

	/**
	 * @param customerLeCode the customerLeCode to set
	 */
	public void setCustomerLeCode(String customerLeCode) {
		this.customerLeCode = customerLeCode;
	}

	public Set<PartnerEngagement> getPartnerEngagements() {
		return this.partnerEngagements;
	}

	public void setPartnerEngagements(Set<PartnerEngagement> partnerEngagements) {
		this.partnerEngagements = partnerEngagements;
	}

	public PartnerEngagement addPartnerEngagement(PartnerEngagement partnerEngagement) {
		getPartnerEngagements().add(partnerEngagement);
		partnerEngagement.setCustomerLegalEntity(this);

		return partnerEngagement;
	}

	public PartnerEngagement removePartnerEngagement(PartnerEngagement partnerEngagement) {
		getPartnerEngagements().remove(partnerEngagement);
		partnerEngagement.setCustomerLegalEntity(null);

		return partnerEngagement;
	}

	public Double getPreApprovedMrc() {
		return preApprovedMrc;
	}

	public void setPreApprovedMrc(Double preApprovedMrc) {
		this.preApprovedMrc = preApprovedMrc;
	}

	public Double getPreApprovedNrc() {
		return preApprovedNrc;
	}

	public void setPreApprovedNrc(Double preApprovedNrc) {
		this.preApprovedNrc = preApprovedNrc;
	}

	public String getPreApprovedPaymentTerm() {
		return preApprovedPaymentTerm;
	}

	public void setPreApprovedPaymentTerm(String preApprovedPaymentTerm) {
		this.preApprovedPaymentTerm = preApprovedPaymentTerm;
	}

	public String getPreApprovedBillingMethod() {
		return preApprovedBillingMethod;
	}

	public void setPreApprovedBillingMethod(String preApprovedBillingMethod) {
		this.preApprovedBillingMethod = preApprovedBillingMethod;
	}

	public String getCreditCheckAccountType() {
		return creditCheckAccountType;
	}

	public void setCreditCheckAccountType(String creditCheckAccountType) {
		this.creditCheckAccountType = creditCheckAccountType;
	}

	public String getBlacklistStatus() {
		return blacklistStatus;
	}

	public void setBlacklistStatus(String blacklistStatus) {
		this.blacklistStatus = blacklistStatus;
	}

	public String getCreditPreapprovedFlag() {
		return creditPreapprovedFlag;
	}

	public void setCreditPreapprovedFlag(String creditPreapprovedFlag) {
		this.creditPreapprovedFlag = creditPreapprovedFlag;
	}
	
	

}
