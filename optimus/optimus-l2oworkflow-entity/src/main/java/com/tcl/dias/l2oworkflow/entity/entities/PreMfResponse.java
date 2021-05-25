package com.tcl.dias.l2oworkflow.entity.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "pre_mf_response")
@NamedQuery(name = "PreMfResponse.findAll", query = "SELECT a FROM PreMfResponse a")
public class PreMfResponse {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "task_id")
	private Integer taskId;

	@Column(name = "site_id")
	private Integer siteId;

	@Column(name = "provider")
	private String provider;

	@Column(name = "create_response_json")
	private String createResponseJson;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	private Timestamp createdTime;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_time")
	private Timestamp updatedTime;

	@Column(name = "mf_type")
	private String type;

	@Column(name = "mf_feasibility_mode")
	private String feasibilityMode;

	@Column(name = "mf_rank")
	private Integer rank;

	@Column(name = "is_selected")
	private Integer isSelected;

	@Column(name = "mf_feasibility_status")
	private String feasibilityStatus;

	

	@Column(name = "mf_feasibility_check")
	private String feasibilityCheck;

	@Column(name = "mf_feasibility_type")
	private String feasibilityType;

	@Column(name = "mf_quote_id")
	private Integer quoteId;

	@Column(name = "mf_quote_code")
	private String quoteCode;

	@Column(name = "mf_product")
	private String product;

	@Transient
	private Double otcTotal = 0.0;

	@Transient
	private Double arcTotal = 0.0;

	@Transient
	private Double capexTotal = 0.0;

	@Transient
	private Double totalCharges = 0.0;

	@Transient
	private Double networkCapex = 0.0;

	public Double getNetworkCapex() {
		return networkCapex;
	}

	public void setNetworkCapex(Double networkCapex) {
		this.networkCapex = networkCapex;
	}

	public Double getOtcTotal() {
		return otcTotal;
	}

	public void setOtcTotal(Double otcTotal) {
		this.otcTotal = otcTotal;
	}

	public Double getArcTotal() {
		return arcTotal;
	}

	public void setArcTotal(Double arcTotal) {
		this.arcTotal = arcTotal;
	}

	public Double getCapexTotal() {
		return capexTotal;
	}

	public void setCapexTotal(Double capexTotal) {
		this.capexTotal = capexTotal;
	}

	public Double getTotalCharges() {
		return totalCharges;
	}

	public void setTotalCharges(Double totalCharges) {
		this.totalCharges = totalCharges;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getCreateResponseJson() {
		return createResponseJson;
	}

	public void setCreateResponseJson(String createResponseJson) {
		this.createResponseJson = createResponseJson;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	

	public Integer getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Integer isSelected) {
		this.isSelected = isSelected;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFeasibilityMode() {
		return feasibilityMode;
	}

	public void setFeasibilityMode(String feasibilityMode) {
		this.feasibilityMode = feasibilityMode;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getFeasibilityStatus() {
		return feasibilityStatus;
	}

	public void setFeasibilityStatus(String feasibilityStatus) {
		this.feasibilityStatus = feasibilityStatus;
	}

	public String getFeasibilityCheck() {
		return feasibilityCheck;
	}

	public void setFeasibilityCheck(String feasibilityCheck) {
		this.feasibilityCheck = feasibilityCheck;
	}

	public String getFeasibilityType() {
		return feasibilityType;
	}

	public void setFeasibilityType(String feasibilityType) {
		this.feasibilityType = feasibilityType;
	}

	public String getQuoteCode() {
		return quoteCode;
	}

	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}

	

	
	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

}
