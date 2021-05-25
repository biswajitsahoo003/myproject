package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name="partner")
@NamedQuery(name = "Partner.findAll", query = "SELECT p FROM Partner p")
public class Partner implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "erf_cus_partner_id")
	private Integer erfCusPartnerId;

	@Column(name = "partner_code")
	private String partnerCode;

	@Column(name = "partner_email_id")
	private String partnerEmailId;

	@Column(name = "partner_name")
	private String partnerName;

	private Byte status;

	// bi-directional many-to-one association to Customer
	@OneToMany(mappedBy = "partner")
	private Set<Customer> customers;

	// bi-directional many-to-one association to Engagement
	@OneToMany(mappedBy = "partner")
	private Set<Engagement> engagements;

	public Partner() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getErfCusPartnerId() {
		return erfCusPartnerId;
	}

	public void setErfCusPartnerId(Integer erfCusPartnerId) {
		this.erfCusPartnerId = erfCusPartnerId;
	}

	public String getPartnerCode() {
		return this.partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getPartnerEmailId() {
		return this.partnerEmailId;
	}

	public void setPartnerEmailId(String partnerEmailId) {
		this.partnerEmailId = partnerEmailId;
	}

	public String getPartnerName() {
		return this.partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Set<Customer> getCustomers() {
		return this.customers;
	}

	public void setCustomers(Set<Customer> customers) {
		this.customers = customers;
	}

	public Customer addCustomer(Customer customer) {
		getCustomers().add(customer);
		customer.setPartner(this);

		return customer;
	}

	public Customer removeCustomer(Customer customer) {
		getCustomers().remove(customer);
		customer.setPartner(null);

		return customer;
	}

	public Set<Engagement> getEngagements() {
		return this.engagements;
	}

	public void setEngagements(Set<Engagement> engagements) {
		this.engagements = engagements;
	}

	public Engagement addEngagement(Engagement engagement) {
		getEngagements().add(engagement);
		engagement.setPartner(this);

		return engagement;
	}

	public Engagement removeEngagement(Engagement engagement) {
		getEngagements().remove(engagement);
		engagement.setPartner(null);

		return engagement;
	}

}
