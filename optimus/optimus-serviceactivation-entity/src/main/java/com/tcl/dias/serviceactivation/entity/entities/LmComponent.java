package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * 
 * LmComponent Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="lm_component")
@NamedQuery(name="LmComponent.findAll", query="SELECT l FROM LmComponent l")
public class LmComponent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="lm_component_id")
	private Integer lmComponentId;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="lm_onwl_provider")
	private String lmOnwlProvider;

	@Column(name="modified_by")
	private String modifiedBy;

	private String remarks;
	
	@Column(name="external_reference_id")
	private String externalReferenceId;
	
	@Column(name="status")
	private String status;

	@Column(name="start_date")
	private Timestamp startDate;

	private Integer version;

	//bi-directional many-to-one association to CambiumLastmile
	@OneToMany(mappedBy="lmComponent")
	private Set<CambiumLastmile> cambiumLastmiles;

	//bi-directional many-to-one association to ServiceDetail
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="service_details_id")
	private ServiceDetail serviceDetail;

	//bi-directional many-to-one association to RadwinLastmile
	@OneToMany(mappedBy="lmComponent")
	private Set<RadwinLastmile> radwinLastmiles;

	//bi-directional many-to-one association to WimaxLastmile
	@OneToMany(mappedBy="lmComponent")
	private Set<WimaxLastmile> wimaxLastmiles;

	public LmComponent() {
	}

	public Integer getLmComponentId() {
		return this.lmComponentId;
	}

	public void setLmComponentId(Integer lmComponentId) {
		this.lmComponentId = lmComponentId;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLmOnwlProvider() {
		return this.lmOnwlProvider;
	}

	public void setLmOnwlProvider(String lmOnwlProvider) {
		this.lmOnwlProvider = lmOnwlProvider;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Set<CambiumLastmile> getCambiumLastmiles() {
		return this.cambiumLastmiles;
	}

	public void setCambiumLastmiles(Set<CambiumLastmile> cambiumLastmiles) {
		this.cambiumLastmiles = cambiumLastmiles;
	}

	public CambiumLastmile addCambiumLastmile(CambiumLastmile cambiumLastmile) {
		getCambiumLastmiles().add(cambiumLastmile);
		cambiumLastmile.setLmComponent(this);

		return cambiumLastmile;
	}

	public CambiumLastmile removeCambiumLastmile(CambiumLastmile cambiumLastmile) {
		getCambiumLastmiles().remove(cambiumLastmile);
		cambiumLastmile.setLmComponent(null);

		return cambiumLastmile;
	}

	public ServiceDetail getServiceDetail() {
		return this.serviceDetail;
	}

	public void setServiceDetail(ServiceDetail serviceDetail) {
		this.serviceDetail = serviceDetail;
	}

	public Set<RadwinLastmile> getRadwinLastmiles() {
		return this.radwinLastmiles;
	}

	public void setRadwinLastmiles(Set<RadwinLastmile> radwinLastmiles) {
		this.radwinLastmiles = radwinLastmiles;
	}

	public RadwinLastmile addRadwinLastmile(RadwinLastmile radwinLastmile) {
		getRadwinLastmiles().add(radwinLastmile);
		radwinLastmile.setLmComponent(this);

		return radwinLastmile;
	}

	public RadwinLastmile removeRadwinLastmile(RadwinLastmile radwinLastmile) {
		getRadwinLastmiles().remove(radwinLastmile);
		radwinLastmile.setLmComponent(null);

		return radwinLastmile;
	}

	public Set<WimaxLastmile> getWimaxLastmiles() {
		return wimaxLastmiles;
	}

	public void setWimaxLastmiles(Set<WimaxLastmile> wimaxLastmiles) {
		this.wimaxLastmiles = wimaxLastmiles;
	}

	public WimaxLastmile addWimaxLastmile(WimaxLastmile wimaxLastmile) {
		getWimaxLastmiles().add(wimaxLastmile);
		wimaxLastmile.setLmComponent(this);

		return wimaxLastmile;
	}

	public WimaxLastmile removeWimaxLastmile(WimaxLastmile wimaxLastmile) {
		getWimaxLastmiles().remove(wimaxLastmile);
		wimaxLastmile.setLmComponent(null);

		return wimaxLastmile;
	}

	public String getExternalReferenceId() {
		return externalReferenceId;
	}

	public void setExternalReferenceId(String externalReferenceId) {
		this.externalReferenceId = externalReferenceId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}