package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited The persistent class for the
 *            si_asset database table.
 *
 */

@Entity
@Table(name = "odr_asset")
public class OdrAsset implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_date")
	private Timestamp createdDate;

	private String fqdn;

	private String name;

	@Column(name = "public_ip")
	private String publicIp;

	private String type;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_date")
	private Timestamp updatedDate;

	@Column(name = "is_active")
	private String isActive;

	// bi-directional many-to-one association to SIServiceDetail
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "odr_service_detail_id")
	private OdrServiceDetail odrServiceDetail;

	@Column(name = "origin_ntwrk")
	private String originnetwork;

	@Column(name = "is_shared_ind")
	private String isSharedInd;

	// bi-directional many-to-one association to SiAssetAttribute
	//@OneToMany(mappedBy = "odrAsset", cascade = CascadeType.ALL)
	@OneToMany(mappedBy = "odrAsset")
	private Set<OdrAssetAttribute> odrAssetAttributes;
	
	@Transient
	private List<OdrAssetRelation> odrAssetRelations;

	public OdrAsset() {
	}

	public Integer getId() {
		return this.id;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getFqdn() {
		return this.fqdn;
	}

	public void setFqdn(String fqdn) {
		this.fqdn = fqdn;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPublicIp() {
		return this.publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public OdrServiceDetail getOdrServiceDetail() {
		return odrServiceDetail;
	}

	public void setOdrServiceDetail(OdrServiceDetail odrServiceDetail) {
		this.odrServiceDetail = odrServiceDetail;
	}

	public String getOriginnetwork() {
		return originnetwork;
	}

	public void setOriginnetwork(String originnetwork) {
		this.originnetwork = originnetwork;
	}

	public String getIsSharedInd() {
		return isSharedInd;
	}

	public void setIsSharedInd(String isSharedInd) {
		this.isSharedInd = isSharedInd;
	}

	public Set<OdrAssetAttribute> getOdrAssetAttributes() {
		return odrAssetAttributes;
	}

	public void setOdrAssetAttributes(Set<OdrAssetAttribute> odrAssetAttributes) {
		this.odrAssetAttributes = odrAssetAttributes;
	}
	
	public void addOdrAssetAttributes(OdrAssetAttribute odrAssetAttribute) {
		if(this.odrAssetAttributes == null)
			this.odrAssetAttributes = new HashSet<OdrAssetAttribute>();
		this.odrAssetAttributes.add(odrAssetAttribute);
	}

	public List<OdrAssetRelation> getOdrAssetRelations() {
		return odrAssetRelations;
	}

	public void setOdrAssetRelations(List<OdrAssetRelation> odrAssetRelations) {
		this.odrAssetRelations = odrAssetRelations;
	}
	
	public void addOdrAssetRelations(OdrAssetRelation odrAssetRelation) {
		if(this.odrAssetRelations == null)
			this.odrAssetRelations = new ArrayList<OdrAssetRelation>();
		this.odrAssetRelations.add(odrAssetRelation);
	}

	@Override
	public String toString() {
		return "OdrAsset{" + "id=" + id + ", createdBy='" + createdBy + '\'' + ", createdDate=" + createdDate
				+ ", fqdn='" + fqdn + '\'' + ", name='" + name + '\'' + ", publicIp='" + publicIp + '\'' + ", type='"
				+ type + '\'' + ", updatedBy='" + updatedBy + '\'' + ", updatedDate=" + updatedDate + ", isActive='"
				+ isActive + '\'' + ", odrServiceDetail=" + odrServiceDetail + ", originnetwork='" + originnetwork
				+ '\'' + ", isSharedInd='" + isSharedInd + '\'' + '}';
	}
}