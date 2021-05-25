package com.tcl.dias.ticketing.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;


/**
 * 
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlannedEventsRequest {

	private String activityStatus;
	private String correlationId;
	private String limit;
	private String maintenanceType;
	private String offset;
	private String plannedEventEndDate;
	private String plannedEventStartDate;
	private String serviceIdentifier;
	private String sortBy;
	private String sortOrder;
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
	 * @return the limit
	 */
	public String getLimit() {
		return limit;
	}
	/**
	 * @param limit the limit to set
	 */
	public void setLimit(String limit) {
		this.limit = limit;
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
	 * @return the offset
	 */
	public String getOffset() {
		return offset;
	}
	/**
	 * @param offset the offset to set
	 */
	public void setOffset(String offset) {
		this.offset = offset;
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
	 * @return the serviceIdentifier
	 */
	public String getServiceIdentifier() {
		return serviceIdentifier;
	}
	/**
	 * @param serviceIdentifier the serviceIdentifier to set
	 */
	public void setServiceIdentifier(String serviceIdentifier) {
		this.serviceIdentifier = serviceIdentifier;
	}
	/**
	 * @return the sortBy
	 */
	public String getSortBy() {
		return sortBy;
	}
	/**
	 * @param sortBy the sortBy to set
	 */
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	/**
	 * @return the sortOrder
	 */
	public String getSortOrder() {
		return sortOrder;
	}
	/**
	 * @param sortOrder the sortOrder to set
	 */
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	
}
