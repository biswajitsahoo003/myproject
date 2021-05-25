package com.tcl.dias.oms.teamsdr.beans;

import java.util.List;

/**
 * Teams DR Order solution bean
 * 
 * @author Srinivasa Raghavan
 */
public class TeamsDROrderSolutionBean {

	private List<TeamsDROrderServicesBean> teamsDROrderServices;

	public TeamsDROrderSolutionBean() {
	}

	public List<TeamsDROrderServicesBean> getTeamsDROrderServices() {
		return teamsDROrderServices;
	}

	public void setTeamsDROrderServices(List<TeamsDROrderServicesBean> teamsDROrderServices) {
		this.teamsDROrderServices = teamsDROrderServices;
	}

	@Override
	public String toString() {
		return "TeamsDROrderSolutionBean{" + "teamsDROrderServices=" + teamsDROrderServices + '}';
	}
}
