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
@Table(name = "user_to_customer_le")
@NamedQuery(name = "UserToCustomerLe.findAll", query = "SELECT u FROM UserToCustomerLe u")
public class UserToCustomerLe implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// bi-directional many-to-one association to User
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	// bi-directional many-to-one association to Customer
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	private Customer customerId;

	@Column(name = "erf_customer_le_id")
	private Integer erfCustomerLeId;

	@Column(name = "erf_customer_le_name")
	private String erfCustomerLeName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Customer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Customer customerId) {
		this.customerId = customerId;
	}

	public Integer getErfCustomerLeId() {
		return erfCustomerLeId;
	}

	public void setErfCustomerLeId(Integer erfCustomerLeId) {
		this.erfCustomerLeId = erfCustomerLeId;
	}

	public String getErfCustomerLeName() {
		return erfCustomerLeName;
	}

	public void setErfCustomerLeName(String erfCustomerLeName) {
		this.erfCustomerLeName = erfCustomerLeName;
	}

	@Override
	public String toString() {
		return "UserToCustomerLe [id=" + id + ", user=" + user + ", customerId=" + customerId + ", erfCustomerLeId="
				+ erfCustomerLeId + ", erfCustomerLeName=" + erfCustomerLeName + "]";
	}

}
