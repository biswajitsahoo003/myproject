package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
@Table(name = "mst_oms_attributes")
@NamedQuery(name = "MstOmsAttribute.findAll", query = "SELECT m FROM MstOmsAttribute m")
public class MstOmsAttribute implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String category;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	private String description;

	@Column(name = "is_active")
	private Byte isActive;

	private String name;

	// bi-directional many-to-one association to OrdersLeAttributeValue
	@OneToMany(mappedBy = "mstOmsAttribute")
	private Set<OrdersLeAttributeValue> ordersLeAttributeValues;

	// bi-directional many-to-one association to QuoteLeAttributeValue
	@OneToMany(mappedBy = "mstOmsAttribute")
	private Set<QuoteLeAttributeValue> quoteLeAttributeValues;

	public MstOmsAttribute() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<OrdersLeAttributeValue> getOrdersLeAttributeValues() {
		return this.ordersLeAttributeValues;
	}

	public void setOrdersLeAttributeValues(Set<OrdersLeAttributeValue> ordersLeAttributeValues) {
		this.ordersLeAttributeValues = ordersLeAttributeValues;
	}

	/**
	 * @return the isActive
	 */
	public Byte getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setIsActive(Byte isActive) {
		this.isActive = isActive;
	}

	public OrdersLeAttributeValue addOrdersLeAttributeValue(OrdersLeAttributeValue ordersLeAttributeValue) {
		getOrdersLeAttributeValues().add(ordersLeAttributeValue);
		ordersLeAttributeValue.setMstOmsAttribute(this);

		return ordersLeAttributeValue;
	}

	public OrdersLeAttributeValue removeOrdersLeAttributeValue(OrdersLeAttributeValue ordersLeAttributeValue) {
		getOrdersLeAttributeValues().remove(ordersLeAttributeValue);
		ordersLeAttributeValue.setMstOmsAttribute(null);

		return ordersLeAttributeValue;
	}

	public Set<QuoteLeAttributeValue> getQuoteLeAttributeValues() {
		return this.quoteLeAttributeValues;
	}

	public void setQuoteLeAttributeValues(Set<QuoteLeAttributeValue> quoteLeAttributeValues) {
		this.quoteLeAttributeValues = quoteLeAttributeValues;
	}

	public QuoteLeAttributeValue addQuoteLeAttributeValue(QuoteLeAttributeValue quoteLeAttributeValue) {
		getQuoteLeAttributeValues().add(quoteLeAttributeValue);
		quoteLeAttributeValue.setMstOmsAttribute(this);

		return quoteLeAttributeValue;
	}

	public QuoteLeAttributeValue removeQuoteLeAttributeValue(QuoteLeAttributeValue quoteLeAttributeValue) {
		getQuoteLeAttributeValues().remove(quoteLeAttributeValue);
		quoteLeAttributeValue.setMstOmsAttribute(null);

		return quoteLeAttributeValue;
	}

}