package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * 
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * The persistent class for the si_asset_to_service database table.
 * 
 */
@Entity
@Table(name="si_asset_to_service")
public class SIAssetToService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private Double arc;

	@Column(name="billing_account_no")
	private String billingAccountNo;

	@Column(name="circuit_status")
	private String circuitStatus;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="created_date")
	private Timestamp createdDate;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="erf_cust_currency_id")
	private Integer erfCustCurrencyId;

	@Column(name="is_active")
	private String isActive;

	private Double mrc;

	private Double nrc;

	private String SI_service_detail_tps_service_id;

	@Column(name="start_date")
	private Timestamp startDate;

	@Column(name="updated_by")
	private String updatedBy;

	@Column(name="updated_time")
	private Timestamp updatedTime;

	//bi-directional many-to-one association to SiAsset
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SI_Asset_ID")
	private SIAsset siAsset;

	//bi-directional many-to-one association to SiServiceDetail
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SI_service_detail_id")
	private SIServiceDetail siServiceDetail;

    public SIAssetToService() {
    }

	public Integer getId() {
		return this.id;
	}



	public Double getArc() {
		return this.arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public String getBillingAccountNo() {
		return this.billingAccountNo;
	}

	public void setBillingAccountNo(String billingAccountNo) {
		this.billingAccountNo = billingAccountNo;
	}

	public String getCircuitStatus() {
		return this.circuitStatus;
	}

	public void setCircuitStatus(String circuitStatus) {
		this.circuitStatus = circuitStatus;
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

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Integer getErfCustCurrencyId() {
		return this.erfCustCurrencyId;
	}

	public void setErfCustCurrencyId(Integer erfCustCurrencyId) {
		this.erfCustCurrencyId = erfCustCurrencyId;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Double getMrc() {
		return this.mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return this.nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public String getSI_service_detail_tps_service_id() {
		return this.SI_service_detail_tps_service_id;
	}

	public void setSI_service_detail_tps_service_id(String SI_service_detail_tps_service_id) {
		this.SI_service_detail_tps_service_id = SI_service_detail_tps_service_id;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	public SIAsset getSiAsset() {
		return this.siAsset;
	}

	public void setSiAsset(SIAsset siAsset) {
		this.siAsset = siAsset;
	}
	
	public SIServiceDetail getSiServiceDetail() {
		return this.siServiceDetail;
	}

	public void setSiServiceDetail(SIServiceDetail siServiceDetail) {
		this.siServiceDetail = siServiceDetail;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}