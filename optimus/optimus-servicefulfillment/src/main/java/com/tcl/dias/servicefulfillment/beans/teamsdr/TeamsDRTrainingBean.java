package com.tcl.dias.servicefulfillment.beans.teamsdr;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

import java.util.List;

/**
 * Bean for teamsdr training details
 *
 * @author Syed Ali.
 * @createdAt 18/02/2021, Thursday, 14:28
 */
public class TeamsDRTrainingBean extends TaskDetailsBaseBean {
	//training details
	private TeamsDRTrainingCommonDataBean commonData;
	private List<TeamsDRTrainingDetailsBean> trainingDetails;

	public TeamsDRTrainingBean() {
	}

	public TeamsDRTrainingCommonDataBean getCommonData() {
		return commonData;
	}

	public void setCommonData(TeamsDRTrainingCommonDataBean commonData) {
		this.commonData = commonData;
	}

	public List<TeamsDRTrainingDetailsBean> getTrainingDetails() {
		return trainingDetails;
	}

	public void setTrainingDetails(List<TeamsDRTrainingDetailsBean> trainingDetails) {
		this.trainingDetails = trainingDetails;
	}

	@Override
	public String toString() {
		return "TeamsDRTrainingBean{" + "trainingDetails=" + trainingDetails + '}';
	}
}
