package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.tcl.dias.productcatelog.entity.base.BaseEntity;

/**
 * The persistent class for the product_component_discount database table.
 * 
 */
/*@Entity
@Table(name = "product_component_discount")
@NamedQuery(name = "ProductComponentDiscount.findAll", query = "SELECT p FROM ProductComponentDiscount p")*/
public class ProductComponentDiscount extends BaseEntity implements Serializable {/*
	private static final long serialVersionUID = 1L;

	@Column(name = "apply_order")
	private String applyOrder;

	@Column(name = "disc_pct")
	private String discPct;

	@Column(name = "disc_type")
	private String discType;

	@Column(name = "from_range")
	private String fromRange;

	@Column(name = "geography_id")
	private int geographyId;

	@Column(name = "to_range")
	private String toRange;

	// bi-directional many-to-one association to Product
	@ManyToOne
	private Product product;

	// bi-directional many-to-one association to Component
	@ManyToOne
	private Component component;

	public ProductComponentDiscount() {
		//default constructor
	}

	public String getApplyOrder() {
		return this.applyOrder;
	}

	public void setApplyOrder(String applyOrder) {
		this.applyOrder = applyOrder;
	}

	public String getDiscPct() {
		return this.discPct;
	}

	public void setDiscPct(String discPct) {
		this.discPct = discPct;
	}

	public String getDiscType() {
		return this.discType;
	}

	public void setDiscType(String discType) {
		this.discType = discType;
	}

	public String getFromRange() {
		return this.fromRange;
	}

	public void setFromRange(String fromRange) {
		this.fromRange = fromRange;
	}

	public int getGeographyId() {
		return this.geographyId;
	}

	public void setGeographyId(int geographyId) {
		this.geographyId = geographyId;
	}

	public String getToRange() {
		return this.toRange;
	}

	public void setToRange(String toRange) {
		this.toRange = toRange;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Component getComponent() {
		return this.component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

*/}