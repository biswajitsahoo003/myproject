package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * Entity Class
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "macd_detail")
@NamedQuery(name = "MacdDetail.findAll", query = "SELECT m FROM MacdDetail m")
public class MacdDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "quote_to_le_id")
	private Integer quoteToLeId;
	
	@Column(name = "tps_service_id")
	private String tpsServiceId;
	
	@Column(name= "tps_sfdc_id")
	private String tpsSfdcId;
	
	@Column(name = "tps_sfdc_parent_opty_id")
	private String tpsSfdcParentOptyId;
	
	@Column(name = "order_type")
	private String orderType;
	
	@Column(name = "order_category")
	private String orderCategory;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_time")
	private Date createdTime;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_time")
	private Date updatedTime;
	
	@Column(name = "updated_by")
	private String updatedBy;
	
	@Column(name = "is_active")
	private Byte isActive;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "cancellation_date")
	private Date cancellationDate;
	
	@Column(name = "cancellation_reason")
	private String cancellationReason;

	private String stage;
	
	@Column(name = "sfdc_stage")
	private String sfdcStage;
	
	@Column(name ="tps_sfdc_product_id")
	private String tpsSfdcProductId;
	
	@Column(name="remarks")
	private String remarks;
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the quoteToLeId
	 */
	public Integer getQuoteToLeId() {
		return quoteToLeId;
	}

	/**
	 * @param quoteToLeId the quoteToLeId to set
	 */
	public void setQuoteToLeId(Integer quoteToLeId) {
		this.quoteToLeId = quoteToLeId;
	}

	/**
	 * @return the tpsServiceId
	 */
	public String getTpsServiceId() {
		return tpsServiceId;
	}

	/**
	 * @param tpsServiceId the tpsServiceId to set
	 */
	public void setTpsServiceId(String tpsServiceId) {
		this.tpsServiceId = tpsServiceId;
	}

	/**
	 * @return the tpsSfdcId
	 */
	public String getTpsSfdcId() {
		return tpsSfdcId;
	}

	/**
	 * @param tpsSfdcId the tpsSfdcId to set
	 */
	public void setTpsSfdcId(String tpsSfdcId) {
		this.tpsSfdcId = tpsSfdcId;
	}

	/**
	 * @return the tpsSfdcParentOptyId
	 */
	public String getTpsSfdcParentOptyId() {
		return tpsSfdcParentOptyId;
	}

	/**
	 * @param tpsSfdcParentOptyId the tpsSfdcParentOptyId to set
	 */
	public void setTpsSfdcParentOptyId(String tpsSfdcParentOptyId) {
		this.tpsSfdcParentOptyId = tpsSfdcParentOptyId;
	}

	/**
	 * @return the orderType
	 */
	public String getOrderType() {
		return orderType;
	}

	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	/**
	 * @return the orderCategory
	 */
	public String getOrderCategory() {
		return orderCategory;
	}

	/**
	 * @param orderCategory the orderCategory to set
	 */
	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	/**
	 * @return the updatedBy
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the isActive
	 */
	public Byte getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Byte isActive) {
		this.isActive = isActive;
	}

	/**

	/**
	 * @return the cancellationReason
	 */
	public String getCancellationReason() {
		return cancellationReason;
	}

	/**
	 * @param cancellationReason the cancellationReason to set
	 */
	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	/**
	 * @return the stage
	 */
	public String getStage() {
		return stage;
	}

	/**
	 * @param stage the stage to set
	 */
	public void setStage(String stage) {
		this.stage = stage;
	}

	/**
	 * @return the updatedTime
	 */
	public Date getUpdatedTime() {
		return updatedTime;
	}

	/**
	 * @param updatedTime the updatedTime to set
	 */
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	/**
	 * @return the cancellationDate
	 */
	public Date getCancellationDate() {
		return cancellationDate;
	}

	/**
	 * @param cancellationDate the cancellationDate to set
	 */
	public void setCancellationDate(Date cancellationDate) {
		this.cancellationDate = cancellationDate;
	}

	/**
	 * @return the createdTime
	 */
	public Date getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * @return the sfdcStage
	 */
	public String getSfdcStage() {
		return sfdcStage;
	}

	/**
	 * @param sfdcStage the sfdcStage to set
	 */
	public void setSfdcStage(String sfdcStage) {
		this.sfdcStage = sfdcStage;
	}

	/**
	 * @return the tpsSfdcProductId
	 */
	public String getTpsSfdcProductId() {
		return tpsSfdcProductId;
	}

	/**
	 * @param tpsSfdcProductId the tpsSfdcProductId to set
	 */
	public void setTpsSfdcProductId(String tpsSfdcProductId) {
		this.tpsSfdcProductId = tpsSfdcProductId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
	

}
