package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * 
 * @author Manojkumar R
 *
 */
@Entity
@Table(name="product_location_assoc")
@NamedQuery(name="ProductLocationAssoc.findAll", query="SELECT p FROM ProductLocationAssoc p")
public class ProductLocationAssoc implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ProductLocationAssocPK id;

	@Column(name="created_by")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_dt")
	private Date createdDt;

	@Column(name="is_active_ind")
	private String isActiveInd;

	@Column(name="long_desc")
	private String longDesc;

	@Column(name="updated_by")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_dt")
	private Date updatedDt;

	//bi-directional many-to-one association to Location
	@ManyToOne
	@JoinColumn(name="loc_id", insertable = false, updatable = false)
	private Location location;

	//bi-directional many-to-one association to Product
	@ManyToOne
	@JoinColumn(name="prd_id", insertable = false, updatable = false)
	private Product product;

	public ProductLocationAssoc() {
		//default constructor
	}

	public ProductLocationAssocPK getId() {
		return this.id;
	}

	public void setId(ProductLocationAssocPK id) {
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

	public String getLongDesc() {
		return this.longDesc;
	}

	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
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

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}