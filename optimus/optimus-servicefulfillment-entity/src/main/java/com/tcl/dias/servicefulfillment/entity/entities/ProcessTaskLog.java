package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * This file contains the ProcessTaskLog.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "process_task_log")
@NamedQuery(name = "ProcessTaskLog.findAll", query = "SELECT p FROM ProcessTaskLog p")
public class ProcessTaskLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String action;

	@Column(name = "action_from")
	private String actionFrom;

	@Column(name = "action_to")
	private String actionTo;

	private String active;

	@Column(name = "created_time")
	private Timestamp createdTime;

	private String descrption;

	@Column(name = "group_from")
	private String groupFrom;

	@Column(name = "group_to")
	private String groupTo;

	@Column(name = "order_code")
	private String orderCode;

	@Column(name = "sc_order_id")
	private Integer scOrderId;

	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "service_code")
	private String serviceCode;
	
	@Column(name="quote_code")
	private String quoteCode;
	
	@Column(name="quote_id")
	private Integer quoteId;
	
	@Column(name="site_code")
	private String siteCode;
	
	@Lob
	@Column(name="error_message")
	private String erroMessage;
	
	@Column(name="site_id")
	private Integer siteId;
	

	@Column(name="category")
	private String category;
	
	@Column(name="sub_category")
	private String subCategory;
	
	

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
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

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	//bi-directional many-to-one association to Task
	@ManyToOne(fetch=FetchType.LAZY,optional=true)
	private Task task;

	public ProcessTaskLog() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getActionFrom() {
		return this.actionFrom;
	}

	public void setActionFrom(String actionFrom) {
		this.actionFrom = actionFrom;
	}

	public String getActionTo() {
		return this.actionTo;
	}

	public void setActionTo(String actionTo) {
		this.actionTo = actionTo;
	}

	public String getActive() {
		return this.active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public Timestamp getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public String getDescrption() {
		return this.descrption;
	}

	public void setDescrption(String descrption) {
		this.descrption = descrption;
	}

	public String getGroupFrom() {
		return this.groupFrom;
	}

	public void setGroupFrom(String groupFrom) {
		this.groupFrom = groupFrom;
	}

	public String getGroupTo() {
		return this.groupTo;
	}

	public void setGroupTo(String groupTo) {
		this.groupTo = groupTo;
	}

	public String getOrderCode() {
		return this.orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Integer getScOrderId() {
		return this.scOrderId;
	}

	public void setScOrderId(Integer scOrderId) {
		this.scOrderId = scOrderId;
	}

	public Task getTask() {
		return this.task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getErroMessage() {
		return erroMessage;
	}

	public void setErroMessage(String erroMessage) {
		this.erroMessage = erroMessage;
	}
	
}