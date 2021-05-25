package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the product_sla_cos_spec database table.
 * 
 */
@Entity
@Table(name="product_sla_cos_spec")
public class ProductSlaCosSpec implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="cos_schema_nm")
	private String cosSchemaNm;

	@Column(name="cos1_value")
	private String cos1Value;

	@Column(name="cos2_value")
	private String cos2Value;

	@Column(name="cos3_value")
	private String cos3Value;

	@Column(name="cos4_value")
	private String cos4Value;

	@Column(name="cos5_value")
	private String cos5Value;

	@Column(name="cos6_value")
	private String cos6Value;

	@Column(name="created_by")
	private String createdBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="created_dt")
	private Date createdDt;

	@Column(name="pdt_catalog_id")
	private Integer pdtCatalogId;

	@Column(name="updated_by")
	private String updatedBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="updated_dt")
	private Date updatedDt;

	//bi-directional many-to-one association to SlaMetricMaster
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="sla_cmp_id")
	private SlaMetricMaster slaMetricMaster;

    public ProductSlaCosSpec() {
    	// do nothing
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCosSchemaNm() {
		return this.cosSchemaNm;
	}

	public void setCosSchemaNm(String cosSchemaNm) {
		this.cosSchemaNm = cosSchemaNm;
	}

	public String getCos1Value() {
		return this.cos1Value;
	}

	public void setCos1Value(String cos1Value) {
		this.cos1Value = cos1Value;
	}

	public String getCos2Value() {
		return this.cos2Value;
	}

	public void setCos2Value(String cos2Value) {
		this.cos2Value = cos2Value;
	}

	public String getCos3Value() {
		return this.cos3Value;
	}

	public void setCos3Value(String cos3Value) {
		this.cos3Value = cos3Value;
	}

	public String getCos4Value() {
		return this.cos4Value;
	}

	public void setCos4Value(String cos4Value) {
		this.cos4Value = cos4Value;
	}

	public String getCos5Value() {
		return this.cos5Value;
	}

	public void setCos5Value(String cos5Value) {
		this.cos5Value = cos5Value;
	}

	public String getCos6Value() {
		return this.cos6Value;
	}

	public void setCos6Value(String cos6Value) {
		this.cos6Value = cos6Value;
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

	public Integer getPdtCatalogId() {
		return this.pdtCatalogId;
	}

	public void setPdtCatalogId(Integer pdtCatalogId) {
		this.pdtCatalogId = pdtCatalogId;
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

	public SlaMetricMaster getSlaMetricMaster() {
		return this.slaMetricMaster;
	}

	public void setSlaMetricMaster(SlaMetricMaster slaMetricMaster) {
		this.slaMetricMaster = slaMetricMaster;
	}
	
}