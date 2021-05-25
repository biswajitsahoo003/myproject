package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;



/**
 * The persistent class for the product_service_component_assoc database table.
 * 
 */
@Entity
@Table(name="product_service_component_assoc")
public class ProductServiceComponentAssoc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="created_by")
	private String createdBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="created_dt")
	private Date createdDt;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="effective_from_dt")
	private Date effectiveFromDt;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="effective_to_dt")
	private Date effectiveToDt;

	@Column(name="is_active_ind")
	private String isActiveInd;

	@Column(name="reason_txt")
	private String reasonTxt;

	@Column(name="updated_by")
	private String updatedBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="updated_dt")
	private Date updatedDt;


	//bi-directional many-to-one association to ProductComponentAssoc
    @ManyToOne
	@JoinColumn(name="product_component_assoc_id")
	private ProductComponentAssoc productComponentAssoc;

	//bi-directional many-to-one association to ProductOfferingServiceLevelAssoc
    @ManyToOne
	@JoinColumn(name="product_offering_service_level_id")
	private ProductOfferingServiceLevelAssoc productOfferingServiceLevelAssoc;


    public ProductServiceComponentAssoc() {
    	// TO DO

    }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
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

	public Date getEffectiveFromDt() {
		return this.effectiveFromDt;
	}

	public void setEffectiveFromDt(Date effectiveFromDt) {
		this.effectiveFromDt = effectiveFromDt;
	}

	public Date getEffectiveToDt() {
		return this.effectiveToDt;
	}

	public void setEffectiveToDt(Date effectiveToDt) {
		this.effectiveToDt = effectiveToDt;
	}

	public String getIsActiveInd() {
		return this.isActiveInd;
	}

	public void setIsActiveInd(String isActiveInd) {
		this.isActiveInd = isActiveInd;
	}

	public String getReasonTxt() {
		return this.reasonTxt;
	}

	public void setReasonTxt(String reasonTxt) {
		this.reasonTxt = reasonTxt;
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

	public ProductComponentAssoc getProductComponentAssoc() {
		return this.productComponentAssoc;
	}

	public void setProductComponentAssoc(ProductComponentAssoc productComponentAssoc) {
		this.productComponentAssoc = productComponentAssoc;
	}
	
	public ProductOfferingServiceLevelAssoc getProductOfferingServiceLevelAssoc() {
		return this.productOfferingServiceLevelAssoc;
	}

	public void setProductOfferingServiceLevelAssoc(ProductOfferingServiceLevelAssoc productOfferingServiceLevelAssoc) {
		this.productOfferingServiceLevelAssoc = productOfferingServiceLevelAssoc;
	}

	
	
}