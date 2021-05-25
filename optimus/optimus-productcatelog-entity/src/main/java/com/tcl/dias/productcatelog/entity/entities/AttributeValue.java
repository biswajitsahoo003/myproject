package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

import com.tcl.dias.productcatelog.entity.base.BaseEntity;

import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the attribute_value database table.
 * 
 */
@Entity
@Table(name="attribute_value")
public class AttributeValue extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@Column(name="attr_values")
	private String attrValues;

	

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="effective_from_dt")
	private Date effectiveFromDt;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="effective_to_dt")
	private Date effectiveToDt;


	@Column(name="reason_txt")
	private String reasonTxt;
	
	
	

	//bi-directional many-to-one association to AttributeMaster
    @ManyToOne
	@JoinColumn(name="attr_id")
	private AttributeMaster attributeMaster;

	//bi-directional many-to-one association to ComponentProductOfferPriceFactor
	@OneToMany(mappedBy="attributeValue")
	private Set<ComponentProductOfferPriceFactor> componentProductOfferPriceFactors;

	//bi-directional many-to-one association to UomMaster
    @ManyToOne
	@JoinColumn(name="uom_cd")
	private UomMaster uomMaster;

	//bi-directional many-to-one association to AttributeValueGroupAssoc
	@OneToMany(mappedBy="attributeValue")
	private Set<AttributeValueGroupAssoc> attributeValueGroupAssocs;

	
	//bi-directional many-to-one association to SlaSpecAttrValueGroup
	@OneToMany(mappedBy="attributeValue")
	private Set<SlaSpecAttrValueGroup> slaSpecAttrValueGroups;
	
	//bi-directional one-to-one association to AttributeValuesAlternate
	@OneToOne(mappedBy="attributeValue")
	private AttributeValuesAlternate attributeValuesAlternate;
	
    public AttributeValuesAlternate getAttributeValuesAlternate() {
		return attributeValuesAlternate;
	}

	public void setAttributeValuesAlternate(AttributeValuesAlternate attributeValuesAlternate) {
		this.attributeValuesAlternate = attributeValuesAlternate;
	}


	public AttributeValue() {
    	//TO DO
    }

	
	public String getAttrValues() {
		return this.attrValues;
	}

	public void setAttrValues(String attrValues) {
		this.attrValues = attrValues;
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

	

	public String getReasonTxt() {
		return this.reasonTxt;
	}

	public void setReasonTxt(String reasonTxt) {
		this.reasonTxt = reasonTxt;
	}

	
	public AttributeMaster getAttributeMaster() {
		return this.attributeMaster;
	}

	public void setAttributeMaster(AttributeMaster attributeMaster) {
		this.attributeMaster = attributeMaster;
	}
	
	public Set<ComponentProductOfferPriceFactor> getComponentProductOfferPriceFactors() {
		return this.componentProductOfferPriceFactors;
	}

	public void setComponentProductOfferPriceFactors(Set<ComponentProductOfferPriceFactor> componentProductOfferPriceFactors) {
		this.componentProductOfferPriceFactors = componentProductOfferPriceFactors;
	}
	
	public UomMaster getUomMaster() {
		return this.uomMaster;
	}

	public void setUomMaster(UomMaster uomMaster) {
		this.uomMaster = uomMaster;
	}
	
	public Set<AttributeValueGroupAssoc> getAttributeValueGroupAssocs() {
		return this.attributeValueGroupAssocs;
	}

	public void setAttributeValueGroupAssocs(Set<AttributeValueGroupAssoc> attributeValueGroupAssocs) {
		this.attributeValueGroupAssocs = attributeValueGroupAssocs;
	}
	
	
	
	public Set<SlaSpecAttrValueGroup> getSlaSpecAttrValueGroups() {
		return this.slaSpecAttrValueGroups;
	}

	public void setSlaSpecAttrValueGroups(Set<SlaSpecAttrValueGroup> slaSpecAttrValueGroups) {
		this.slaSpecAttrValueGroups = slaSpecAttrValueGroups;
	}
	
	
}