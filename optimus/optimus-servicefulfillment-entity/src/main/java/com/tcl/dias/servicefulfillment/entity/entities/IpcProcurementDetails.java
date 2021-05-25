package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author Savanya
 *
 */
@Entity
@Table(name = "ipc_procurement_details")
@NamedQuery(name = "IpcProcurementDetails.findAll", query = "SELECT ipcprocdtl FROM IpcProcurementDetails ipcprocdtl")
public class IpcProcurementDetails implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "sc_order_id")
	private Integer scOrderId;
	
	@Column(name = "solution_name")
	private String solutionName;
	
	@Column(name = "cloud_code")
	private String cloudCode;
	
	@Column(name = "wbs_number")
	private String wbsNumber;
	
	@Column(name = "wbs_value")
	private String wbsValue;
	
	@Column(name = "glcc_number")
	private String glccNumber;
	
	@Column(name = "glcc_value")
	private String glccValue;
	
	@Column(name = "po_number")
	private String poNumber;
	
	@Column(name = "po_value")
	private String poValue;
	
	@Column(name = "po_release_date")
	private Timestamp poReleaseDate;
	
	@Column(name = "procurement_type")
	private String procurementType;
	
	@Column(name = "receipt_date")
	private Timestamp receiptDate;
	
	@Column(name = "contract_start_date")
	private Timestamp contractStartDate;
	
	@Column(name = "contract_end_date")
	private Timestamp contractEndDate;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "created_date")
	private Timestamp createdDate;
	
	@Column(name = "updated_by")
	private String updatedBy;
	
	@Column(name = "updated_date")
	private Timestamp updatedDate;
	
	public IpcProcurementDetails() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getScOrderId() {
		return scOrderId;
	}

	public void setScOrderId(Integer scOrderId) {
		this.scOrderId = scOrderId;
	}

	public String getSolutionName() {
		return solutionName;
	}

	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	public String getCloudCode() {
		return cloudCode;
	}

	public void setCloudCode(String cloudCode) {
		this.cloudCode = cloudCode;
	}

	public String getWbsNumber() {
		return wbsNumber;
	}

	public void setWbsNumber(String wbsNumber) {
		this.wbsNumber = wbsNumber;
	}

	public String getWbsValue() {
		return wbsValue;
	}

	public void setWbsValue(String wbsValue) {
		this.wbsValue = wbsValue;
	}

	public String getGlccNumber() {
		return glccNumber;
	}

	public void setGlccNumber(String glccNumber) {
		this.glccNumber = glccNumber;
	}

	public String getGlccValue() {
		return glccValue;
	}

	public void setGlccValue(String glccValue) {
		this.glccValue = glccValue;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getPoValue() {
		return poValue;
	}

	public void setPoValue(String poValue) {
		this.poValue = poValue;
	}

	public Timestamp getPoReleaseDate() {
		return poReleaseDate;
	}

	public void setPoReleaseDate(Timestamp poReleaseDate) {
		this.poReleaseDate = poReleaseDate;
	}

	public String getProcurementType() {
		return procurementType;
	}

	public void setProcurementType(String procurementType) {
		this.procurementType = procurementType;
	}

	public Timestamp getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Timestamp receiptDate) {
		this.receiptDate = receiptDate;
	}

	public Timestamp getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(Timestamp contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public Timestamp getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(Timestamp contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Override
	public String toString() {
		return "IpcProcurementDetails [id=" + id + ", scOrderId=" + scOrderId + ", solutionName=" + solutionName
				+ ", cloudCode=" + cloudCode + ", wbsNumber=" + wbsNumber + ", wbsValue=" + wbsValue + ", glccNumber="
				+ glccNumber + ", glccValue=" + glccValue + ", poNumber=" + poNumber + ", poValue=" + poValue
				+ ", poReleaseDate=" + poReleaseDate + ", procurementType=" + procurementType + ", receiptDate="
				+ receiptDate + ", contractStartDate=" + contractStartDate + ", contractEndDate=" + contractEndDate
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", updatedBy=" + updatedBy
				+ ", updatedDate=" + updatedDate + "]";
	}

}
