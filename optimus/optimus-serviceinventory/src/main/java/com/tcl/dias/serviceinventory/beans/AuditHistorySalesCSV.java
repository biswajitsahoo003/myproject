package com.tcl.dias.serviceinventory.beans;

import java.sql.Timestamp;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

public class AuditHistorySalesCSV {
	@CsvBindByPosition(position = 0)
	@CsvBindByName(column = "uuid")
	private String uuid;
	@CsvBindByPosition(position = 1)
	@CsvBindByName(column = "user id")
	private String userId;
	@CsvBindByPosition(position = 2)
	@CsvBindByName(column = "customer id")
	private Integer customerId;
	@CsvBindByPosition(position = 3)
	@CsvBindByName(column = "customer le id")
	private String customerLeId;
	@CsvBindByPosition(position = 4)
	@CsvBindByName(column = "request payload")
	private String requestPayload;
	@CsvBindByPosition(position = 5)
	@CsvBindByName(column = "response")
	private String response;
	@CsvBindByPosition(position = 6)
	@CsvBindByName(column = "service id")
	private String serviceId;
	@CsvBindByPosition(position = 7)
	@CsvBindByName(column = "url")
	private String url;
	@CsvBindByPosition(position = 8)
	@CsvBindByName(column = "operation")
	private String operation;
	@CsvBindByPosition(position = 9)
	@CsvDate("dd/MM/yyyy hh:mm:ss")
	@CsvBindByName(column = "updated time")
	private Timestamp updatedTime;
	@CsvBindByPosition(position = 10)
	@CsvBindByName(column = "instance region")
	private String instanceRegion;
	@CsvBindByPosition(position = 11)
	@CsvDate("dd/MM/yyyy hh:mm:ss")
	@CsvBindByName(column = "request time")
	private Timestamp requestTime;
	@CsvBindByPosition(position = 12)
	@CsvDate("dd/MM/yyyy hh:mm:ss")
	@CsvBindByName(column = "response time")
	private Timestamp responseTime;
	@CsvBindByPosition(position = 13)
	@CsvBindByName(column = "organization name")
	private String organizationName;
	@CsvBindByPosition(position = 14)
	@CsvBindByName(column = "template name")
	private String templateName;
	@CsvBindByPosition(position = 15)
	@CsvBindByName(column = "component value")
	private String componentValue;
	@CsvBindByPosition(position = 16)
	@CsvBindByName(column = "task id")
	private Integer taskId;
	

	public AuditHistorySalesCSV() {
	}

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

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerLeId() {
		return customerLeId;
	}

	public void setCustomerLeId(String customerLeId) {
		this.customerLeId = customerLeId;
	}

	public String getRequestPayload() {
		return requestPayload;
	}

	public void setRequestPayload(String requestPayload) {
		this.requestPayload = requestPayload;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getInstanceRegion() {
		return instanceRegion;
	}

	public void setInstanceRegion(String instanceRegion) {
		this.instanceRegion = instanceRegion;
	}

	public Timestamp getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Timestamp requestTime) {
		this.requestTime = requestTime;
	}

	public Timestamp getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Timestamp responseTime) {
		this.responseTime = responseTime;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getComponentValue() {
		return componentValue;
	}

	public void setComponentValue(String componentValue) {
		this.componentValue = componentValue;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}


}

