package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * @author Manojkumar R
 *
 */
@Entity
@Table(name = "product_attribute_value")
@NamedQuery(name = "ProductAttributeValue.findAll", query = "SELECT p FROM ProductAttributeValue p")
public class ProductAttributeValue implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ProductAttributeValuePK id;

	@Column(name = "created_by")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_dt")
	private Date createdDt;

	@Column(name = "is_active_ind")
	private String isActiveInd;

	@Column(name = "updated_by")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_dt")
	private Date updatedDt;

	// bi-directional many-to-one association to Product
	@ManyToOne
	@JoinColumn(insertable = false, updatable = false)
	private Product product;

	// bi-directional many-to-one association to AttributeMaster
	@ManyToOne
	@JoinColumn(name = "attr_id", insertable = false, updatable = false)
	private AttributeMaster attributeMaster;

	public ProductAttributeValuePK getId() {
		return this.id;
	}

	public void setId(ProductAttributeValuePK id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getIsActiveInd() {
		return this.isActiveInd;
	}

	public void setIsActiveInd(String isActiveInd) {
		this.isActiveInd = isActiveInd;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public AttributeMaster getAttributeMaster() {
		return this.attributeMaster;
	}

	public void setAttributeMaster(AttributeMaster attributeMaster) {
		this.attributeMaster = attributeMaster;
	}

}