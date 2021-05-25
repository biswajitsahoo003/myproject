package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
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

/**
 * 
 * Entity Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "customer")
@NamedQuery(name = "Customer.findAll", query = "SELECT c FROM Customer c")
public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "customer_code")
	private String customerCode;

	@Column(name = "customer_email_id")
	private String customerEmailId;

	@Column(name = "customer_name")
	private String customerName;

	@Column(name = "erf_cus_customer_id")
	private Integer erfCusCustomerId;

	private Byte status;

	// bi-directional many-to-one association to Partner
	@ManyToOne(fetch = FetchType.LAZY)
	private Partner partner;

	// bi-directional many-to-one association to Engagement
	@OneToMany(mappedBy = "customer")
	private Set<Engagement> engagements;

	// bi-directional many-to-one association to Order
	@OneToMany(mappedBy = "customer")
	private Set<Order> orders;

	// bi-directional many-to-one association to Quote
	@OneToMany(mappedBy = "customer")
	private Set<Quote> quotes;

	// bi-directional many-to-one association to User
	@OneToMany(mappedBy = "customer")
	private Set<User> users;

	public Customer() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCustomerCode() {
		return this.customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerEmailId() {
		return this.customerEmailId;
	}

	public void setCustomerEmailId(String customerEmailId) {
		this.customerEmailId = customerEmailId;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getErfCusCustomerId() {
		return this.erfCusCustomerId;
	}

	public void setErfCusCustomerId(Integer erfCusCustomerId) {
		this.erfCusCustomerId = erfCusCustomerId;
	}

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Partner getPartner() {
		return this.partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public Set<Engagement> getEngagements() {
		return this.engagements;
	}

	public void setEngagements(Set<Engagement> engagements) {
		this.engagements = engagements;
	}

	public Engagement addEngagement(Engagement engagement) {
		getEngagements().add(engagement);
		engagement.setCustomer(this);

		return engagement;
	}

	public Engagement removeEngagement(Engagement engagement) {
		getEngagements().remove(engagement);
		engagement.setCustomer(null);

		return engagement;
	}

	public Set<Order> getOrders() {
		return this.orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	public Order addOrder(Order order) {
		getOrders().add(order);
		order.setCustomer(this);

		return order;
	}

	public Order removeOrder(Order order) {
		getOrders().remove(order);
		order.setCustomer(null);

		return order;
	}

	public Set<Quote> getQuotes() {
		return this.quotes;
	}

	public void setQuotes(Set<Quote> quotes) {
		this.quotes = quotes;
	}

	public Quote addQuote(Quote quote) {
		getQuotes().add(quote);
		quote.setCustomer(this);

		return quote;
	}

	public Quote removeQuote(Quote quote) {
		getQuotes().remove(quote);
		quote.setCustomer(null);

		return quote;
	}

	public Set<User> getUsers() {
		return this.users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public User addUser(User user) {
		getUsers().add(user);
		user.setCustomer(this);

		return user;
	}

	public User removeUser(User user) {
		getUsers().remove(user);
		user.setCustomer(null);

		return user;
	}

}