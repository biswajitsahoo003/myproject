package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

import com.tcl.dias.productcatelog.entity.base.BaseEntity;

import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the sla_metric_master database table.
 * 
 */
@Entity
@Table(name="sla_metric_master")
public class SlaMetricMaster  extends BaseEntity implements Serializable{
	private static final long serialVersionUID = 1L;


	@Column(name="long_desc")
	private String longDesc;

	private String nm;

	@Column(name="uom_cd")
	private String uomCd;

	//bi-directional many-to-one association to ProductSlaCosSpec
	@OneToMany(mappedBy="slaMetricMaster")
	private Set<ProductSlaCosSpec> productSlaCosSpecs;

    public SlaMetricMaster() {
    	// do nothing
    }


	public String getLongDesc() {
		return this.longDesc;
	}

	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}

	public String getNm() {
		return this.nm;
	}

	public void setNm(String nm) {
		this.nm = nm;
	}

	public String getUomCd() {
		return this.uomCd;
	}

	public void setUomCd(String uomCd) {
		this.uomCd = uomCd;
	}


	public Set<ProductSlaCosSpec> getProductSlaCosSpecs() {
		return this.productSlaCosSpecs;
	}

	public void setProductSlaCosSpecs(Set<ProductSlaCosSpec> productSlaCosSpecs) {
		this.productSlaCosSpecs = productSlaCosSpecs;
	}
	
}