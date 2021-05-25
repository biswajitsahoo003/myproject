package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
@Table(name = "sc_asset")
public class ScAsset implements Serializable {
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
	
	private String status;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_date")
	private Timestamp updatedDate;

	@Column(name = "is_active")
	private String isActive;

	// bi-directional many-to-one association to SIServiceDetail
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sc_service_detail_id")
	private ScServiceDetail scServiceDetail;

	@Column(name = "origin_ntwrk")
	private String originnetwork;

	@Column(name = "is_shared_ind")
	private String isSharedInd;

	// bi-directional many-to-one association to SiAssetAttribute
	// @OneToMany(mappedBy = "odrAsset", cascade = CascadeType.ALL)
	@OneToMany(mappedBy = "scAsset")
	private Set<ScAssetAttribute> scAssetAttributes;
	
	@Transient
	private List<ScAssetRelation> scAssetRelations;
	
	@Transient
	private Integer odrAssetId;

	public ScAsset() {
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public ScServiceDetail getOdrServiceDetail() {
		return scServiceDetail;
	}

	public void setScServiceDetail(ScServiceDetail odrServiceDetail) {
		this.scServiceDetail = odrServiceDetail;
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

	public Set<ScAssetAttribute> getScAssetAttributes() {
		return scAssetAttributes;
	}

	public void setScAssetAttributes(Set<ScAssetAttribute> scAssetAttributes) {
		this.scAssetAttributes = scAssetAttributes;
	}
	
	public void addScAssetAttributes(ScAssetAttribute scAssetAttribute) {
		if(this.scAssetAttributes == null)
			this.scAssetAttributes = new HashSet<ScAssetAttribute>();
		this.scAssetAttributes.add(scAssetAttribute);
	}

	public List<ScAssetRelation> getScAssetRelations() {
		return scAssetRelations;
	}

	public void setScAssetRelations(List<ScAssetRelation> scAssetRelations) {
		this.scAssetRelations = scAssetRelations;
	}
	
	public void addScAssetRelations(ScAssetRelation scAssetRelation) {
		if(this.scAssetRelations == null)
			this.scAssetRelations = new ArrayList<ScAssetRelation>();;
		this.scAssetRelations.add(scAssetRelation);
	}

	public Integer getOdrAssetId() {
		return odrAssetId;
	}

	public void setOdrAssetId(Integer odrAssetId) {
		this.odrAssetId = odrAssetId;
	}

	@Override
	public String toString() {
		return "ScAsset{" + "id=" + id + ", createdBy='" + createdBy + '\'' + ", createdDate=" + createdDate
				+ ", fqdn='" + fqdn + '\'' + ", name='" + name + '\'' + ", publicIp='" + publicIp + '\'' + ", type='"
				+ type + '\'' + ", updatedBy='" + updatedBy + '\'' + ", updatedDate=" + updatedDate + ", isActive='"
				+ isActive + '\'' + ", scServiceDetail=" + scServiceDetail + ", originnetwork='" + originnetwork + '\''
				+ ", isSharedInd='" + isSharedInd + '\'' + '}';
	}
}
