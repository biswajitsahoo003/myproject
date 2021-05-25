package com.tcl.dias.oms.entity.entities;

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
 * 
 * Bean class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "order_lr_job")
@NamedQuery(name = "OrderLrJob.findAll", query = "SELECT o FROM OrderLrJob o")
public class OrderLrJob implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "job_id")
	private String jobId;

	@Column(name = "order_ref_id")
	private String orderRefId;

	@Column(name = "stage")
	private String stage;

	@Column(name = "retry_count")
	private Integer retryCount;

	@Column(name = "lr_service_id")
	private String lrServiceId;

	@Column(name = "product_name")
	private String productName;

	@Column(name = "error_response")
	private String errorResponse;

	@Column(name = "lr_download_count")
	private Integer lrDownloadCount;

	@Column(name = "lr_download_url")
	private String lrDownloadUrl;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	private Timestamp createdTime;

	@Column(name = "updated_time")
	private Timestamp updatedTime;

	@Column(name = "updated_by")
	private String updatedBy;

	public OrderLrJob() {
		// DO NOTHING
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderRefId() {
		return orderRefId;
	}

	public void setOrderRefId(String orderRefId) {
		this.orderRefId = orderRefId;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

	public String getLrServiceId() {
		return lrServiceId;
	}

	public void setLrServiceId(String lrServiceId) {
		this.lrServiceId = lrServiceId;
	}

	public String getLrDownloadUrl() {
		return lrDownloadUrl;
	}

	public void setLrDownloadUrl(String lrDownloadUrl) {
		this.lrDownloadUrl = lrDownloadUrl;
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

	public Timestamp getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getErrorResponse() {
		return errorResponse;
	}

	public void setErrorResponse(String errorResponse) {
		this.errorResponse = errorResponse;
	}

	public Integer getLrDownloadCount() {
		return lrDownloadCount;
	}

	public void setLrDownloadCount(Integer lrDownloadCount) {
		this.lrDownloadCount = lrDownloadCount;
	}

}