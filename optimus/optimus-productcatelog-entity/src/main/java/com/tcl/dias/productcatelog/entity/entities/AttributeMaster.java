package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

import com.tcl.dias.productcatelog.entity.base.BaseEntity;

import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the attribute_master database table.
 * 
 */
@Entity
@Table(name="attribute_master")
public class AttributeMaster extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@Column(name="cd")
	private String cd;

	

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="effective_from_dt")
	private Date effectiveFromDt;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="effective_to_dt")
	private Date effectiveToDt;



	@Column(name="is_chargeable_ind")
	private String isChargeableInd;

	@Column(name="long_desc")
	private String longDesc;

	@Column(name="nm")

	private String name;

	@Column(name="reason_txt")
	private String reasonTxt;

	//bi-directional many-to-one association to UomMaster
    @ManyToOne
	@JoinColumn(name="uom_cd")
	private UomMaster uomMaster;


	//bi-directional many-to-one association to AttributeGroupAttrAssoc
	@OneToMany(mappedBy="attributeMaster")
	private Set<AttributeGroupAttrAssoc> attributeGroupAttrAssocs;

	//bi-directional many-to-one association to AttributeCategoryMaster
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="attr_cat_id")
	private AttributeCategoryMaster attributeCategoryMaster;

	//bi-directional many-to-one association to AttributeValue
	@OneToMany(mappedBy="attributeMaster")
	private Set<AttributeValue> attributeValues;
	
	

    public AttributeMaster() {
    	// TO DO
    }


	public String getCd() {
		return this.cd;
	}

	public void setCd(String cd) {
		this.cd = cd;
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

	

	public String getIsChargeableInd() {
		return this.isChargeableInd;
	}

	public void setIsChargeableInd(String isChargeableInd) {
		this.isChargeableInd = isChargeableInd;
	}

	public String getLongDesc() {
		return this.longDesc;
	}

	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}

	
	public String getReasonTxt() {
		return this.reasonTxt;
	}

	public void setReasonTxt(String reasonTxt) {
		this.reasonTxt = reasonTxt;
	}



	public Set<AttributeValue> getAttributeValues() {
		return this.attributeValues;
	}

	public void setAttributeValues(Set<AttributeValue> attributeValues) {
		this.attributeValues = attributeValues;
	}
	
	




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public UomMaster getUomMaster() {
		return uomMaster;
	}




	public void setUomMaster(UomMaster uomMaster) {
		this.uomMaster = uomMaster;
	}


	public Set<AttributeGroupAttrAssoc> getAttributeGroupAttrAssocs() {
		return attributeGroupAttrAssocs;
	}



	public void setAttributeGroupAttrAssocs(Set<AttributeGroupAttrAssoc> attributeGroupAttrAssocs) {
		this.attributeGroupAttrAssocs = attributeGroupAttrAssocs;
	}



	public AttributeCategoryMaster getAttributeCategoryMaster() {
		return attributeCategoryMaster;
	}



	public void setAttributeCategoryMaster(AttributeCategoryMaster attributeCategoryMaster) {
		this.attributeCategoryMaster = attributeCategoryMaster;
	}



	@Override
	public String toString() {
		return "AttributeMaster [cd=" + cd + ", effectiveFromDt=" + effectiveFromDt + ", effectiveToDt=" + effectiveToDt
				+ ", isChargeableInd=" + isChargeableInd + ", longDesc=" + longDesc + ", name=" + name + ", reasonTxt="
				+ reasonTxt + ", uomMaster=" + uomMaster + ", attributeGroupAttrAssocs=" + attributeGroupAttrAssocs
				+ ", attributeCategoryMaster=" + attributeCategoryMaster + ", attributeValues=" + attributeValues + "]";
	}
	
}