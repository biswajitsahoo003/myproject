package com.tcl.dias.serviceinventory.beans;


import java.sql.Timestamp;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

public class AuditHistoryCustomer {
	@CsvBindByPosition(position = 0)
	@CsvBindByName(column = "uuid")
	private String uuid;
	@CsvBindByPosition(position = 1)
	@CsvBindByName(column = "user id")
	private String userId;
	@CsvBindByPosition(position = 2)
	@CsvBindByName(column = "operation")
	private String operation;
	@CsvBindByPosition(position = 3)
	@CsvDate("dd/MM/yyyy hh:mm:ss")
	@CsvBindByName(column = "updated time")
	private Timestamp updatedTime;
	@CsvBindByPosition(position = 4)
	@CsvBindByName(column = "template name")
	private String templateName;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public Timestamp getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

}
