package com.tcl.dias.l2oworkflow.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * Entity Class for Customer Crn table
 * 
 *
 * @author Yogesh 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "customer_crn")
@NamedQuery(name = "CustomerCrn.findAll", query = "SELECT c FROM CustomerCrn c")
public class CustomerCrn {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "customer_le_id")
	private Integer customerLegalId;

	@Column(name = "customer_le_name")
	private String customerLegalName;

	@Column(name = "customer_ref")
	private String customerRef; 

	@Column(name = "account_number")
	private String accountNumber;
	
	@Column(name = "service_type")
	private String serviceType;
	
	@Column(name = "state")
	private String state;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCustomerLegalId() {
		return customerLegalId;
	}

	public void setCustomerLegalId(Integer customerLegalId) {
		this.customerLegalId = customerLegalId;
	}

	public String getCustomerLegalName() {
		return customerLegalName;
	}

	public void setCustomerLegalName(String customerLegalName) {
		this.customerLegalName = customerLegalName;
	}

	public String getCustomerRef() {
		return customerRef;
	}

	public void setCustomerRef(String customerRef) {
		this.customerRef = customerRef;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "CustomerCrn [id=" + id + ", customerLegalId=" + customerLegalId + ", customerLegalName="
				+ customerLegalName + ", customerRef=" + customerRef + ", accountNumber=" + accountNumber
				+ ", serviceType=" + serviceType + ", state=" + state + "]";
	}

	
}
