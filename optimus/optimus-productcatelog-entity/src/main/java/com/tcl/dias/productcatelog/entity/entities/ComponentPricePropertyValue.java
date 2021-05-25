package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.tcl.dias.productcatelog.entity.base.BaseEntity;


/**
 * 
 * @author Manojkumar R
 *
 */
@Entity
@Table(name="component_price_property_value")
@NamedQuery(name="ComponentPricePropertyValue.findAll", query="SELECT c FROM ComponentPricePropertyValue c")
public class ComponentPricePropertyValue extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="attr_val")
	private String attrVal;

	@Column(name="comparator")
	private String comparator;

	@Column(name="dim_or_msr_cd")
	private String dimOrMsrCd;


	//bi-directional many-to-one association to AttributeMaster
	@ManyToOne
	@JoinColumn(name="attr_id")
	private AttributeMaster attributeMaster;

	//bi-directional many-to-one association to PriceCategoryMaster
	@ManyToOne
	@JoinColumn(name="pr_cat_id")
	private PriceCategoryMaster priceCategoryMaster;

	public String getAttrVal() {
		return this.attrVal;
	}

	public void setAttrVal(String attrVal) {
		this.attrVal = attrVal;
	}

	public String getComparator() {
		return this.comparator;
	}

	public void setComparator(String comparator) {
		this.comparator = comparator;
	}

	public String getDimOrMsrCd() {
		return this.dimOrMsrCd;
	}

	public void setDimOrMsrCd(String dimOrMsrCd) {
		this.dimOrMsrCd = dimOrMsrCd;
	}

	

	public AttributeMaster getAttributeMaster() {
		return this.attributeMaster;
	}

	public void setAttributeMaster(AttributeMaster attributeMaster) {
		this.attributeMaster = attributeMaster;
	}

	public PriceCategoryMaster getPriceCategoryMaster() {
		return this.priceCategoryMaster;
	}

	public void setPriceCategoryMaster(PriceCategoryMaster priceCategoryMaster) {
		this.priceCategoryMaster = priceCategoryMaster;
	}

	
}