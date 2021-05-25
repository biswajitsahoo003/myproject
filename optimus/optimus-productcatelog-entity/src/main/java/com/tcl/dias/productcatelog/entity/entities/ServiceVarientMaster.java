package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the service_varient_master database table.
 * 
 */
@Entity
@Table(name="service_varient_master")
public class ServiceVarientMaster implements Serializable {
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

	@Column(name="long_desc")
	private String longDesc;

	@Column(name="nm")
	private String name;

	@Column(name="reason_txt")
	private String reasonTxt;

	@Column(name="updated_by")
	private String updatedBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="updated_dt")
	private Date updatedDt;

	//bi-directional many-to-one association to ProductOfferingServiceLevelAssoc
	@OneToMany(mappedBy="serviceVarientMaster")
	private List<ProductOfferingServiceLevelAssoc> productOfferingServiceLevelAssocs;

	//bi-directional many-to-one association to ProductCatalog
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="product_catalog_id")
	private ProductCatalog productCatalog;

    public ServiceVarientMaster() {
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

	public String getLongDesc() {
		return this.longDesc;
	}

	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	public List<ProductOfferingServiceLevelAssoc> getProductOfferingServiceLevelAssocs() {
		return this.productOfferingServiceLevelAssocs;
	}

	public void setProductOfferingServiceLevelAssocs(List<ProductOfferingServiceLevelAssoc> productOfferingServiceLevelAssocs) {
		this.productOfferingServiceLevelAssocs = productOfferingServiceLevelAssocs;
	}
	
	public ProductCatalog getProductCatalog() {
		return this.productCatalog;
	}

	public void setProductCatalog(ProductCatalog productCatalog) {
		this.productCatalog = productCatalog;
	}
	
}