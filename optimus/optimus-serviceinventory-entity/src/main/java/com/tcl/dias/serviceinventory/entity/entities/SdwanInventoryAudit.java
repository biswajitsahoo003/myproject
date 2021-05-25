package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Entity to hold IZOSDWAN transaction data
 * @author archchan
 *
 */
@Entity
@Table(name="sdwan_inventory_audit")
@NamedQuery(name="SdwanInventoryAudit.findAll", query="SELECT s FROM SdwanInventoryAudit s")
public class SdwanInventoryAudit implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "mdc_token")
	private String mdcToken;

	@Column(name="customer_id")
	private Integer customerId;

	@Column(name="customer_le_id")
	private String customerLeId;

	@Lob
	@Column(name="display_text")
	private String displayText;

	@Column(name="instance_region")
	private String instanceRegion;

	private String operation;

	@Column(name="request_method")
	private String requestMethod;

	@Lob
	@Column(name="request_payload")
	private String requestPayload;

	@Lob
	private String response;

	@Column(name="response_code")
	private Integer responseCode;

	@Column(name="service_id")
	private String serviceId;

	@Column(name="updated_time")
	private Timestamp updatedTime;

	private String url;

	@Column(name="user_id")
	private String userId;

	private String uuid;
	
	@Column(name="request_time")
	private Timestamp requestTime;
	
	@Column(name="response_time")
	private Timestamp responseTime;
	
	@Column(name="organization_name")
	private String organizationName;
	
	@Column(name="template_name")
	private String templateName;

	@Column(name="component_name")
	private String componentName;
	
	@Column(name="component_value")
	private String componentValue;
	
	@Column(name="task_id")
	private Integer taskId;

	public SdwanInventoryAudit() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public String getInstanceRegion() {
		return instanceRegion;
	}

	public void setInstanceRegion(String instanceRegion) {
		this.instanceRegion = instanceRegion;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
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

	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Timestamp getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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

	public String getMdcToken() {
		return mdcToken;
	}

	public void setMdcToken(String mdcToken) {
		this.mdcToken = mdcToken;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getComponentValue() {
		return componentValue;
	}

	public void setComponentValue(String componentValue) {
		this.componentValue = componentValue;
	}
	
}

