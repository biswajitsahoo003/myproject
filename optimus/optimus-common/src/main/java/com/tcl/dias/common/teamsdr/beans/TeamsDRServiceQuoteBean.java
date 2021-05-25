package com.tcl.dias.common.teamsdr.beans;

import java.util.List;

/**
 * Bean for request/response of Teams DR Quote prices
 * 
 * @author Srinivasa Raghavan
 *
 */
public class TeamsDRServiceQuoteBean {

	private List<TeamsDRServicesQuoteOfferingsBean> teamsDRServicesQuoteOfferings;
	private String contractPeriod;

	public TeamsDRServiceQuoteBean() {
	}

	public List<TeamsDRServicesQuoteOfferingsBean> getTeamsDRServicesQuoteOfferings() {
		return teamsDRServicesQuoteOfferings;
	}

	public void setTeamsDRServicesQuoteOfferings(
			List<TeamsDRServicesQuoteOfferingsBean> teamsDRServicesQuoteOfferings) {
		this.teamsDRServicesQuoteOfferings = teamsDRServicesQuoteOfferings;
	}

	public String getContractPeriod() {
		return contractPeriod;
	}

	public void setContractPeriod(String contractPeriod) {
		this.contractPeriod = contractPeriod;
	}

	@Override
	public String toString() {
		return "TeamsDRServiceQuoteBean{" + "teamsDRServicesQuoteOfferings=" + teamsDRServicesQuoteOfferings
				+ ", contractPeriod='" + contractPeriod + '\'' + '}';
	}
}
