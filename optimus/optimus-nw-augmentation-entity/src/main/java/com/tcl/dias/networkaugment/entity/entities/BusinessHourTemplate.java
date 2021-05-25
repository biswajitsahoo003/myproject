package com.tcl.dias.networkaugment.entity.entities;

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
 * This file contains the BusinessHourTemplate.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "business_hour_template")
@NamedQuery(name = "BusinessHourTemplate.findAll", query = "SELECT b FROM BusinessHourTemplate b")
public class BusinessHourTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String day;

	@Column(name = "end_time")
	private Timestamp endTime;

	@Column(name = "is_working_day")
	private String isWorkingDay;

	@Column(name = "start_time")
	private Timestamp startTime;

	@Column(name = "template_code")
	private String templateCode;

	public BusinessHourTemplate() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDay() {
		return this.day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public Timestamp getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public String getIsWorkingDay() {
		return this.isWorkingDay;
	}

	public void setIsWorkingDay(String isWorkingDay) {
		this.isWorkingDay = isWorkingDay;
	}

	public Timestamp getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public String getTemplateCode() {
		return this.templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

}