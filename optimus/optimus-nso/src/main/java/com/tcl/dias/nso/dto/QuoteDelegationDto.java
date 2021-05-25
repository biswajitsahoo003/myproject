package com.tcl.dias.nso.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.entity.entities.QuoteDelegation;

/**
 * This file contains the QuoteDelegationDto.java class.
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(Include.NON_NULL)
public class QuoteDelegationDto {
	
	private Integer id;

	private Integer assignTo;

	private Date createdTime;

	private Integer initiatedBy;

	private Byte isActive;

	private Integer parentId;

	private QuoteDto quoteDto;

	private String remarks;

	private String status;

	private Date targetDate;

	private String type;
	
	

	/**
	 * 
	 */
	public QuoteDelegationDto() {
		
	}
	
	public QuoteDelegationDto(QuoteDelegation quoteDelegation) {
		if (quoteDelegation != null) {

			this.assignTo = quoteDelegation.getAssignTo();
			this.createdTime = quoteDelegation.getCreatedTime();
			this.id = quoteDelegation.getId();
			this.initiatedBy = quoteDelegation.getInitiatedBy();
			this.isActive = quoteDelegation.getIsActive();
			this.parentId = quoteDelegation.getParentId();
			//this.quoteDto = new QuoteDto(quoteDelegation.getQuoteToLe());
			this.remarks = quoteDelegation.getRemarks();
			this.status = quoteDelegation.getStatus();
			this.targetDate = quoteDelegation.getTargetDate();
			this.type = quoteDelegation.getType();

	
		}
	}

	/**   this method is to return the id
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/** the id to set
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**  this method is to return the assignTo
	 * @return the assignTo
	 */
	public Integer getAssignTo() {
		return assignTo;
	}

	/**the assignTo to set
	 * @param assignTo the assignTo to set
	 */
	public void setAssignTo(Integer assignTo) {
		this.assignTo = assignTo;
	}

	/**  this method is to return the createdTime
	 * @return the createdTime
	 */
	public Date getCreatedTime() {
		return createdTime;
	}

	/** the createdTime to set
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	/**  this method is to return the initiatedBy
	 * @return the initiatedBy
	 */
	public Integer getInitiatedBy() {
		return initiatedBy;
	}

	/** the initiatedBy to set
	 * @param initiatedBy the initiatedBy to set
	 */
	public void setInitiatedBy(Integer initiatedBy) {
		this.initiatedBy = initiatedBy;
	}

	/**  this method is to return the isActive
	 * @return the isActive
	 */
	public Byte getIsActive() {
		return isActive;
	}

	/** the isActive to set
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Byte isActive) {
		this.isActive = isActive;
	}

	/**  this method is to return the parentId
	 * @return the parentId
	 */
	public Integer getParentId() {
		return parentId;
	}

	/** the parentId to set
	 * @param parentId the parentId to set
	 */
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}


	/**  this method is to return the remarks
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/** the remarks to set
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**  this method is to return the status
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/** the status to set
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**  this method is to return the targetDate
	 * @return the targetDate
	 */
	public Date getTargetDate() {
		return targetDate;
	}

	/** the targetDate to set
	 * @param targetDate the targetDate to set
	 */
	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	/**  this method is to return the type
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/** the type to set
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**  this method is to return the quoteDto
	 * @return the quoteDto
	 */
	public QuoteDto getQuoteDto() {
		return quoteDto;
	}

	/** the quoteDto to set
	 * @param quoteDto the quoteDto to set
	 */
	public void setQuoteDto(QuoteDto quoteDto) {
		this.quoteDto = quoteDto;
	}
	
	
	



}
