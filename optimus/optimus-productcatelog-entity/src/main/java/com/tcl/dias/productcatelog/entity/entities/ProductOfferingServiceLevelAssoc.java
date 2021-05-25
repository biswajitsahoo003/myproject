package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the product_offering_service_level_assoc database table.
 * 
 */
@Entity
@Table(name="product_offering_service_level_assoc")
public class ProductOfferingServiceLevelAssoc implements Serializable {
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
 
	//bi-directional many-to-one association to ComponentProductOfferPrice
	@OneToMany(mappedBy="productOfferingServiceLevelAssoc")
	private List<ComponentProductOfferPrice> componentProductOfferPrices;

	//bi-directional many-to-one association to ProductOffering
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="product_offering_id")
	private Product productOffering;

	//bi-directional many-to-one association to ServiceVarientMaster
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="service_level_spec_id")
	private ServiceVarientMaster serviceVarientMaster ;
	
	
	//bi-directional many-to-one association to ProductServiceComponentAssoc
	@OneToMany(mappedBy="productOfferingServiceLevelAssoc")
	private List<ProductServiceComponentAssoc> productServiceComponentAssocs;

	//bi-directional many-to-one association to ProductSlaComponentAssoc
	@OneToMany(mappedBy="productOfferingServiceLevelAssoc")
	private List<ProductSlaComponentAssoc> productSlaComponentAssocs;

    public ProductOfferingServiceLevelAssoc() {
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

	public List<ComponentProductOfferPrice> getComponentProductOfferPrices() {
		return this.componentProductOfferPrices;
	}

	public void setComponentProductOfferPrices(List<ComponentProductOfferPrice> componentProductOfferPrices) {
		this.componentProductOfferPrices = componentProductOfferPrices;
	}
	
	public Product getProductOffering() {
		return this.productOffering;
	}

	public void setProductOffering(Product productOffering) {
		this.productOffering = productOffering;
	}
	
	public ServiceVarientMaster getServiceVarientMaster() {
		return this.serviceVarientMaster;
	}

	public void setServiceVarientMaster(ServiceVarientMaster serviceVarientMaster) {
		this.serviceVarientMaster = serviceVarientMaster;
	}
	
	public List<ProductServiceComponentAssoc> getProductServiceComponentAssocs() {
		return this.productServiceComponentAssocs;
	}

	public void setProductServiceComponentAssocs(List<ProductServiceComponentAssoc> productServiceComponentAssocs) {
		this.productServiceComponentAssocs = productServiceComponentAssocs;
	}
	
	public List<ProductSlaComponentAssoc> getProductSlaComponentAssocs() {
		return this.productSlaComponentAssocs;
	}

	public void setProductSlaComponentAssocs(List<ProductSlaComponentAssoc> productSlaComponentAssocs) {
		this.productSlaComponentAssocs = productSlaComponentAssocs;
	}


	
}