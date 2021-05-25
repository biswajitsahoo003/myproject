package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * Entity Class
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "user_group_to_le")
@NamedQuery(name = "UserGroupToLe.findAll", query = "SELECT m FROM UserGroupToLe m")
public class UserGroupToLe implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_group_id")
	private MstUserGroups mstUserGroups;

	@Column(name = "erf_cus_customer_le_id")
	private Integer erfCusCustomerLeId;

	@Column(name = "erf_customer_le_name")
	private String erfCustomerLeName;

	@ManyToOne(fetch = FetchType.LAZY)
	private Customer customer;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public MstUserGroups getMstUserGroups() {
		return mstUserGroups;
	}

	public void setMstUserGroups(MstUserGroups mstUserGroups) {
		this.mstUserGroups = mstUserGroups;
	}

	public Integer getErfCusCustomerLeId() {
		return erfCusCustomerLeId;
	}

	public void setErfCusCustomerLeId(Integer erfCusCustomerLeId) {
		this.erfCusCustomerLeId = erfCusCustomerLeId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getErfCustomerLeName() {
		return erfCustomerLeName;
	}

	public void setErfCustomerLeName(String erfCustomerLeName) {
		this.erfCustomerLeName = erfCustomerLeName;
	}

}
