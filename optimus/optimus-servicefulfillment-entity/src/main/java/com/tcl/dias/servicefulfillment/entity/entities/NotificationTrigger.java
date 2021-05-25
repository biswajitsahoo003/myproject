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
 * 
 * This file contains the ScServiceDetail.java class.
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */

@Entity
@Table(name = "notification_trigger")
@NamedQuery(name = "NotificationTrigger.findAll", query = "SELECT s FROM NotificationTrigger s")
public class NotificationTrigger implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "service_id")
	private Integer serviceId;

	@Column(name = "service_code")
	private String serviceCode;

	@Column(name = "reminder_key")
	private String reminderKey;

	@Column(name = "task_def_key")
	private String taskDefKey;

	@Column(name = "status_trigger")
	private String statusTrigger;

	@Column(name = "trigger_time")
	private Timestamp triggerTime;

	@Column(name = "logger")
	private String logger;

	@Column(name = "day_logger")
	private String dayLogger;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getReminderKey() {
		return reminderKey;
	}

	public void setReminderKey(String reminderKey) {
		this.reminderKey = reminderKey;
	}

	public String getTaskDefKey() {
		return taskDefKey;
	}

	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}

	public String getStatusTrigger() {
		return statusTrigger;
	}

	public void setStatusTrigger(String statusTrigger) {
		this.statusTrigger = statusTrigger;
	}

	public Timestamp getTriggerTime() {
		return triggerTime;
	}

	public void setTriggerTime(Timestamp triggerTime) {
		this.triggerTime = triggerTime;
	}

	public String getLogger() {
		return logger;
	}

	public void setLogger(String logger) {
		this.logger = logger;
	}

	public String getDayLogger() {
		return dayLogger;
	}

	public void setDayLogger(String dayLogger) {
		this.dayLogger = dayLogger;
	}

}