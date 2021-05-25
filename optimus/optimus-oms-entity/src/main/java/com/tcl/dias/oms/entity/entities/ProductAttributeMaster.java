package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name = "product_attribute_master")
@NamedQuery(name = "ProductAttributeMaster.findAll", query = "SELECT p FROM ProductAttributeMaster p")
public class ProductAttributeMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@Lob
	private String description;

	private String name;

	private Byte status;

	
	private String category;
	
	// bi-directional many-to-one association to
	// OrderProductComponentsAttributeValue
	@OneToMany(mappedBy = "productAttributeMaster")
	private Set<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues;

	// bi-directional many-to-one association to
	// QuoteProductComponentsAttributeValue
	@OneToMany(mappedBy = "productAttributeMaster")
	private Set<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues;

	public ProductAttributeMaster() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Set<OrderProductComponentsAttributeValue> getOrderProductComponentsAttributeValues() {
		return this.orderProductComponentsAttributeValues;
	}

	public void setOrderProductComponentsAttributeValues(
			Set<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues) {
		this.orderProductComponentsAttributeValues = orderProductComponentsAttributeValues;
	}

	public OrderProductComponentsAttributeValue addOrderProductComponentsAttributeValue(
			OrderProductComponentsAttributeValue orderProductComponentsAttributeValue) {
		getOrderProductComponentsAttributeValues().add(orderProductComponentsAttributeValue);
		orderProductComponentsAttributeValue.setProductAttributeMaster(this);

		return orderProductComponentsAttributeValue;
	}

	public OrderProductComponentsAttributeValue removeOrderProductComponentsAttributeValue(
			OrderProductComponentsAttributeValue orderProductComponentsAttributeValue) {
		getOrderProductComponentsAttributeValues().remove(orderProductComponentsAttributeValue);
		orderProductComponentsAttributeValue.setProductAttributeMaster(null);

		return orderProductComponentsAttributeValue;
	}

	public Set<QuoteProductComponentsAttributeValue> getQuoteProductComponentsAttributeValues() {
		return this.quoteProductComponentsAttributeValues;
	}

	public void setQuoteProductComponentsAttributeValues(
			Set<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues) {
		this.quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValues;
	}

	public QuoteProductComponentsAttributeValue addQuoteProductComponentsAttributeValue(
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue) {
		getQuoteProductComponentsAttributeValues().add(quoteProductComponentsAttributeValue);
		quoteProductComponentsAttributeValue.setProductAttributeMaster(this);

		return quoteProductComponentsAttributeValue;
	}

	public QuoteProductComponentsAttributeValue removeQuoteProductComponentsAttributeValue(
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue) {
		getQuoteProductComponentsAttributeValues().remove(quoteProductComponentsAttributeValue);
		quoteProductComponentsAttributeValue.setProductAttributeMaster(null);

		return quoteProductComponentsAttributeValue;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "ProductAttributeMaster{" +
				"id=" + id +
				", createdBy='" + createdBy + '\'' +
				", createdTime=" + createdTime +
				", description='" + description + '\'' +
				", name='" + name + '\'' +
				", status=" + status +
				", category='" + category + '\'' +
				", orderProductComponentsAttributeValues=" + orderProductComponentsAttributeValues +
				", quoteProductComponentsAttributeValues=" + quoteProductComponentsAttributeValues +
				'}';
	}
}