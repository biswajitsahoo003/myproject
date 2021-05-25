package com.tcl.dias.response.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * This file contains the PlannedEventsBean.java class. used for response
 * handling
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlannedEventsBean {

	private String closureCode;
	private String rollbackPlan;
	private String plannedEventStartDate;
	private String siteLocation;
	private String ticketId;
	private String description;
	private String maintenanceType;
	private String correlationId;
	private String executionOwner;
	private String plannedEventEndDate;
	private List<ServicesBean> services;
	private String expectedDowntime;
	private String activityStatus;
	private String emergencyReason;
	private String impact;
	private String category;
	private String subcategory;
	private String revisedEndDate;

	/**
	 * @return the closureCode
	 */
	public String getClosureCode() {
		return closureCode;
	}
	/**
	 * @param closureCode the closureCode to set
	 */
	public void setClosureCode(String closureCode) {
		this.closureCode = closureCode;
	}
	/**
	 * @return the rollbackPlan
	 */
	public String getRollbackPlan() {
		return rollbackPlan;
	}
	/**
	 * @param rollbackPlan the rollbackPlan to set
	 */
	public void setRollbackPlan(String rollbackPlan) {
		this.rollbackPlan = rollbackPlan;
	}
	/**
	 * @return the plannedEventStartDate
	 */
	public String getPlannedEventStartDate() {
		return plannedEventStartDate;
	}
	/**
	 * @param plannedEventStartDate the plannedEventStartDate to set
	 */
	public void setPlannedEventStartDate(String plannedEventStartDate) {
		this.plannedEventStartDate = plannedEventStartDate;
	}
	/**
	 * @return the siteLocation
	 */
	public String getSiteLocation() {
		return siteLocation;
	}
	/**
	 * @param siteLocation the siteLocation to set
	 */
	public void setSiteLocation(String siteLocation) {
		this.siteLocation = siteLocation;
	}
	/**
	 * @return the ticketId
	 */
	public String getTicketId() {
		return ticketId;
	}
	/**
	 * @param ticketId the ticketId to set
	 */
	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the maintenanceType
	 */
	public String getMaintenanceType() {
		return maintenanceType;
	}
	/**
	 * @param maintenanceType the maintenanceType to set
	 */
	public void setMaintenanceType(String maintenanceType) {
		this.maintenanceType = maintenanceType;
	}
	/**
	 * @return the correlationId
	 */
	public String getCorrelationId() {
		return correlationId;
	}
	/**
	 * @param correlationId the correlationId to set
	 */
	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}
	/**
	 * @return the executionOwner
	 */
	public String getExecutionOwner() {
		return executionOwner;
	}
	/**
	 * @param executionOwner the executionOwner to set
	 */
	public void setExecutionOwner(String executionOwner) {
		this.executionOwner = executionOwner;
	}
	/**
	 * @return the plannedEventEndDate
	 */
	public String getPlannedEventEndDate() {
		return plannedEventEndDate;
	}
	/**
	 * @param plannedEventEndDate the plannedEventEndDate to set
	 */
	public void setPlannedEventEndDate(String plannedEventEndDate) {
		this.plannedEventEndDate = plannedEventEndDate;
	}
	
	
	/**
	 * @return the services
	 */
	public List<ServicesBean> getServices() {
		return services;
	}
	/**
	 * @param services the services to set
	 */
	public void setServices(List<ServicesBean> services) {
		this.services = services;
	}
	/**
	 * @return the expectedDowntime
	 */
	public String getExpectedDowntime() {
		return expectedDowntime;
	}
	/**
	 * @param expectedDowntime the expectedDowntime to set
	 */
	public void setExpectedDowntime(String expectedDowntime) {
		this.expectedDowntime = expectedDowntime;
	}
	/**
	 * @return the activityStatus
	 */
	public String getActivityStatus() {
		return activityStatus;
	}
	/**
	 * @param activityStatus the activityStatus to set
	 */
	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}
	/**
	 * @return the emergencyReason
	 */
	public String getEmergencyReason() {
		return emergencyReason;
	}
	/**
	 * @param emergencyReason the emergencyReason to set
	 */
	public void setEmergencyReason(String emergencyReason) {
		this.emergencyReason = emergencyReason;
	}
	/**
	 * @return the impact
	 */
	public String getImpact() {
		return impact;
	}
	/**
	 * @param impact the impact to set
	 */
	public void setImpact(String impact) {
		this.impact = impact;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the subcategory
	 */
	public String getSubcategory() {
		return subcategory;
	}
	/**
	 * @param subcategory the subcategory to set
	 */
	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}
	/**
	 * @return the revisedEndDate
	 */
	public String getRevisedEndDate() {
		return revisedEndDate;
	}
	/**
	 * @param revisedEndDate the revisedEndDate to set
	 */
	public void setRevisedEndDate(String revisedEndDate) {
		this.revisedEndDate = revisedEndDate;
	}
	

}
