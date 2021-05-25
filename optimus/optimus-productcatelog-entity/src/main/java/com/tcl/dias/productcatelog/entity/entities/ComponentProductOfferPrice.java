package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the component_product_offer_price database table.
 * 
 */
@Entity
@Table(name="component_product_offer_price")
public class ComponentProductOfferPrice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Column(name="attribute_group_value_combination_id")
	private int attributeGroupValueCombinationId;

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

	private String remarks;

	@Column(name="updated_by")
	private String updatedBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="updated_dt")
	private Date updatedDt;

	//bi-directional many-to-one association to ProductOfferingServiceLevelAssoc
    @ManyToOne
	@JoinColumn(name="product_component_service_assoc_id")
	private ProductOfferingServiceLevelAssoc productOfferingServiceLevelAssoc;

	//bi-directional many-to-one association to ComponentProductOfferPriceFactor
	@OneToMany(mappedBy="componentProductOfferPrice")
	private Set<ComponentProductOfferPriceFactor> componentProductOfferPriceFactors;

	//bi-directional many-to-one association to ComponentProductOfferPriceValue
	@OneToMany(mappedBy="componentProductOfferPrice")
	private Set<ComponentProductOfferPriceValue> componentProductOfferPriceValues;


    
    public ComponentProductOfferPrice() {
    	//TO DO
    }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAttributeGroupValueCombinationId() {
		return this.attributeGroupValueCombinationId;
	}

	public void setAttributeGroupValueCombinationId(int attributeGroupValueCombinationId) {
		this.attributeGroupValueCombinationId = attributeGroupValueCombinationId;
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

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public Set<ComponentProductOfferPriceFactor> getComponentProductOfferPriceFactors() {
		return this.componentProductOfferPriceFactors;
	}

	public void setComponentProductOfferPriceFactors(Set<ComponentProductOfferPriceFactor> componentProductOfferPriceFactors) {
		this.componentProductOfferPriceFactors = componentProductOfferPriceFactors;
	}
	
	public Set<ComponentProductOfferPriceValue> getComponentProductOfferPriceValues() {
		return this.componentProductOfferPriceValues;
	}

	public void setComponentProductOfferPriceValues(Set<ComponentProductOfferPriceValue> componentProductOfferPriceValues) {
		this.componentProductOfferPriceValues = componentProductOfferPriceValues;
	}


	public ProductOfferingServiceLevelAssoc getProductOfferingServiceLevelAssoc() {
		return productOfferingServiceLevelAssoc;
	}

	public void setProductOfferingServiceLevelAssoc(ProductOfferingServiceLevelAssoc productOfferingServiceLevelAssoc) {
		this.productOfferingServiceLevelAssoc = productOfferingServiceLevelAssoc;
	}
	
}