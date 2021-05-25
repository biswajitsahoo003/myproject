package com.tcl.dias.oms.teamsdr.beans;

import java.util.List;

/**
 * Bean for Teams DR Solutions
 * 
 * @author Srinivas Raghavan
 *
 */
public class TeamsDRSolutionBean {

	private List<TeamsDRServicesBean> teamsDRServices;

	public TeamsDRSolutionBean() {
	}

	public List<TeamsDRServicesBean> getTeamsDRServices() {
		return teamsDRServices;
	}

	public void setTeamsDRServices(List<TeamsDRServicesBean> teamsDRServices) {
		this.teamsDRServices = teamsDRServices;
	}

	@Override
	public String toString() {
		return "TeamsDRSolutionBean{" + "teamsDRServices=" + teamsDRServices + '}';
	}
}
