package com.tcl.dias.servicefulfillment.beans.teamsdr;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

import java.util.List;

/**
 * TeamsDR management and monitoring data bean
 *
 * @author Syed Ali.
 * @createdAt 18/02/2021, Thursday, 14:28
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamsDRManagementAndMonitoringBean extends TaskDetailsBaseBean {
	//tenant configuration details
	private String customerDisplayName;
	private String applicationID;
	private String directoryID;
	private String clientSecret;
	private String valueID;
	private List<ManagementAndMonitoringCountryDetailBean> tenantAlertConfig;

	private Integer batchId;
	private Integer msSiteId;
	private String tenantConfigurationCompleted;
	private String tenantAlertConfigCompleted;

	public TeamsDRManagementAndMonitoringBean() {
	}

	public String getCustomerDisplayName() {
		return customerDisplayName;
	}

	public void setCustomerDisplayName(String customerDisplayName) {
		this.customerDisplayName = customerDisplayName;
	}

	public String getApplicationID() {
		return applicationID;
	}

	public void setApplicationID(String applicationID) {
		this.applicationID = applicationID;
	}

	public String getDirectoryID() {
		return directoryID;
	}

	public void setDirectoryID(String directoryID) {
		this.directoryID = directoryID;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getValueID() {
		return valueID;
	}

	public void setValueID(String valueID) {
		this.valueID = valueID;
	}

	public List<ManagementAndMonitoringCountryDetailBean> getTenantAlertConfig() {
		return tenantAlertConfig;
	}

	public void setTenantAlertConfig(List<ManagementAndMonitoringCountryDetailBean> tenantAlertConfig) {
		this.tenantAlertConfig = tenantAlertConfig;
	}

	public String getTenantConfigurationCompleted() {
		return tenantConfigurationCompleted;
	}

	public void setTenantConfigurationCompleted(String tenantConfigurationCompleted) {
		this.tenantConfigurationCompleted = tenantConfigurationCompleted;
	}

	public String getTenantAlertConfigCompleted() {
		return tenantAlertConfigCompleted;
	}

	public void setTenantAlertConfigCompleted(String tenantAlertConfigCompleted) {
		this.tenantAlertConfigCompleted = tenantAlertConfigCompleted;
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

	public Integer getMsSiteId() {
		return msSiteId;
	}

	public void setMsSiteId(Integer msSiteId) {
		this.msSiteId = msSiteId;
	}

	@Override
	public String toString() {
		return "TeamsDRManagementAndMonitoringBean{" +
				"customerDisplayName='" + customerDisplayName + '\'' +
				", applicationID='" + applicationID + '\'' +
				", directoryID='" + directoryID + '\'' +
				", clientSecret='" + clientSecret + '\'' +
				", valueID='" + valueID + '\'' +
				", tenantAlertConfig=" + tenantAlertConfig +
				", tenantConfigurationCompleted='" + tenantConfigurationCompleted + '\'' +
				", tenantConfigAlertCompletion='" + tenantAlertConfigCompleted + '\'' +
				'}';
	}
}
