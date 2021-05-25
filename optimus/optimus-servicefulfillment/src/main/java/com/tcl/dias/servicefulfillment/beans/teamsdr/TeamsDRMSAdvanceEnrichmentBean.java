package com.tcl.dias.servicefulfillment.beans.teamsdr;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * TeamsDR
 *
 * @author Syed Ali.
 * @createdAt 18/02/2021, Thursday, 14:27
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamsDRMSAdvanceEnrichmentBean extends TaskDetailsBaseBean {
	private TeamsDRManagedServiceBean managedServicesDetails;
	private TeamsDRManagementAndMonitoringBean managementAndMonitoringDetails;
	private TeamsDRTrainingBean trainingTaskDetails;

	public TeamsDRMSAdvanceEnrichmentBean() {
	}

	public TeamsDRManagedServiceBean getManagedServicesDetails() {
		return managedServicesDetails;
	}

	public void setManagedServicesDetails(TeamsDRManagedServiceBean managedServicesDetails) {
		this.managedServicesDetails = managedServicesDetails;
	}

	public TeamsDRManagementAndMonitoringBean getManagementAndMonitoringDetails() {
		return managementAndMonitoringDetails;
	}

	public void setManagementAndMonitoringDetails(TeamsDRManagementAndMonitoringBean managementAndMonitoringDetails) {
		this.managementAndMonitoringDetails = managementAndMonitoringDetails;
	}

	public TeamsDRTrainingBean getTrainingTaskDetails() {
		return trainingTaskDetails;
	}

	public void setTrainingTaskDetails(TeamsDRTrainingBean trainingTaskDetails) {
		this.trainingTaskDetails = trainingTaskDetails;
	}

	@Override
	public String toString() {
		return "TeamsDRMSAdvanceEnrichmentBean{" + "managedServicesDetails=" + managedServicesDetails + ", "
				+ "managementAndMonitoringDetails=" + managementAndMonitoringDetails + ", trainingTaskDetails="
				+ trainingTaskDetails + '}';
	}
}
