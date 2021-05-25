package com.tcl.dias.servicefulfillment.beans.teamsdr;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

import java.util.List;

/**
 * TeamsDR Managed services Site bean
 *
 * @author Syed Ali.
 * @createdAt 18/02/2021, Thursday, 14:34
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamsDRMSSiteBean extends TaskDetailsBaseBean {
	private String countryName;
	private Integer noOfUsers;
	private List<TeamsDRMSSiteDetailsBean> sites;

	public TeamsDRMSSiteBean() {
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Integer getNoOfUsers() {
		return noOfUsers;
	}

	public void setNoOfUsers(Integer noOfUsers) {
		this.noOfUsers = noOfUsers;
	}

	public List<TeamsDRMSSiteDetailsBean> getSites() {
		return sites;
	}

	public void setSites(List<TeamsDRMSSiteDetailsBean> sites) {
		this.sites = sites;
	}

	@Override
	public String toString() {
		return "TeamsDRMSSiteBean [countryName=" + countryName + ", noOfUsers=" + noOfUsers + "]";
	}
}
