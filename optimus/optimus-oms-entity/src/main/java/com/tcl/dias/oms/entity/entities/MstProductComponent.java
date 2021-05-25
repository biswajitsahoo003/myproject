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
@Table(name = "mst_product_component")
@NamedQuery(name = "MstProductComponent.findAll", query = "SELECT m FROM MstProductComponent m")
public class MstProductComponent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	@Temporal(TemporalType.DATE)
	private Date createdTime;

	private String description;

	private String name;

	private Byte status;

	// bi-directional many-to-one association to OrderProductComponent
	@OneToMany(mappedBy = "mstProductComponent")
	private Set<OrderProductComponent> orderProductComponents;

	// bi-directional many-to-one association to QuoteProductComponent
	@OneToMany(mappedBy = "mstProductComponent")
	private Set<QuoteProductComponent> quoteProductComponents;

	public MstProductComponent() {
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

	public Set<OrderProductComponent> getOrderProductComponents() {
		return this.orderProductComponents;
	}

	public void setOrderProductComponents(Set<OrderProductComponent> orderProductComponents) {
		this.orderProductComponents = orderProductComponents;
	}

	public OrderProductComponent addOrderProductComponent(OrderProductComponent orderProductComponent) {
		getOrderProductComponents().add(orderProductComponent);
		orderProductComponent.setMstProductComponent(this);

		return orderProductComponent;
	}

	public OrderProductComponent removeOrderProductComponent(OrderProductComponent orderProductComponent) {
		getOrderProductComponents().remove(orderProductComponent);
		orderProductComponent.setMstProductComponent(null);

		return orderProductComponent;
	}

	public Set<QuoteProductComponent> getQuoteProductComponents() {
		return this.quoteProductComponents;
	}

	public void setQuoteProductComponents(Set<QuoteProductComponent> quoteProductComponents) {
		this.quoteProductComponents = quoteProductComponents;
	}

	public QuoteProductComponent addQuoteProductComponent(QuoteProductComponent quoteProductComponent) {
		getQuoteProductComponents().add(quoteProductComponent);
		quoteProductComponent.setMstProductComponent(this);

		return quoteProductComponent;
	}

	public QuoteProductComponent removeQuoteProductComponent(QuoteProductComponent quoteProductComponent) {
		getQuoteProductComponents().remove(quoteProductComponent);
		quoteProductComponent.setMstProductComponent(null);

		return quoteProductComponent;
	}

}