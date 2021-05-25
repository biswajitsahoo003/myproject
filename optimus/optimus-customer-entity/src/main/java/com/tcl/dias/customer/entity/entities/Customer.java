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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



/**
 * 
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@NamedQuery(name = "Customer.findAll", query = "SELECT c FROM Customer c")
public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "account_id_18")
	private String accountId18;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@Column(name = "customer_name")
	private String customerName;

	@Column(name = "erf_cus_reliware_id")
	private String erfCusReliwareId;

	private Byte status;

	@Column(name = "tps_sfdc_account_id")
	private String tpsSfdcAccountId;
	
	@Column(name = "customer_code")
	private String customerCode;

	@Column(name = "is_verified")
	private String isVerified;
	// bi-directional many-to-one association to CustomerGroup
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_group_id")
	private CustomerGroup customerGroup;

	// bi-directional many-to-one association to CustomerAttributeValue
	@OneToMany(mappedBy = "customer")
	private Set<CustomerAttributeValue> customerAttributeValues;

	// bi-directional many-to-one association to CustomerLegalEntity
	@OneToMany(mappedBy = "customer")
	private Set<CustomerLegalEntity> customerLegalEntities;

	public Customer() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccountId18() {
		return this.accountId18;
	}

	public void setAccountId18(String accountId18) {
		this.accountId18 = accountId18;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getErfCusReliwareId() {
		return this.erfCusReliwareId;
	}

	public void setErfCusReliwareId(String erfCusReliwareId) {
		this.erfCusReliwareId = erfCusReliwareId;
	}

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getTpsSfdcAccountId() {
		return this.tpsSfdcAccountId;
	}

	public void setTpsSfdcAccountId(String tpsSfdcAccountId) {
		this.tpsSfdcAccountId = tpsSfdcAccountId;
	}

	public CustomerGroup getCustomerGroup() {
		return this.customerGroup;
	}

	public void setCustomerGroup(CustomerGroup customerGroup) {
		this.customerGroup = customerGroup;
	}

	public Set<CustomerAttributeValue> getCustomerAttributeValues() {
		return this.customerAttributeValues;
	}

	public void setCustomerAttributeValues(Set<CustomerAttributeValue> customerAttributeValues) {
		this.customerAttributeValues = customerAttributeValues;
	}

	public CustomerAttributeValue addCustomerAttributeValue(CustomerAttributeValue customerAttributeValue) {
		getCustomerAttributeValues().add(customerAttributeValue);
		customerAttributeValue.setCustomer(this);

		return customerAttributeValue;
	}

	public CustomerAttributeValue removeCustomerAttributeValue(CustomerAttributeValue customerAttributeValue) {
		getCustomerAttributeValues().remove(customerAttributeValue);
		customerAttributeValue.setCustomer(null);

		return customerAttributeValue;
	}

	public Set<CustomerLegalEntity> getCustomerLegalEntities() {
		return this.customerLegalEntities;
	}

	public void setCustomerLegalEntities(Set<CustomerLegalEntity> customerLegalEntities) {
		this.customerLegalEntities = customerLegalEntities;
	}

	public CustomerLegalEntity addCustomerLegalEntity(CustomerLegalEntity customerLegalEntity) {
		getCustomerLegalEntities().add(customerLegalEntity);
		customerLegalEntity.setCustomer(this);

		return customerLegalEntity;
	}

	public CustomerLegalEntity removeCustomerLegalEntity(CustomerLegalEntity customerLegalEntity) {
		getCustomerLegalEntities().remove(customerLegalEntity);
		customerLegalEntity.setCustomer(null);

		return customerLegalEntity;
	}

	/**
	 * @return the customerCode
	 */
	public String getCustomerCode() {
		return customerCode;
	}

	/**
	 * @param customerCode the customerCode to set
	 */
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}


	public String getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(String isVerified) {
		this.isVerified = isVerified;
	}





}