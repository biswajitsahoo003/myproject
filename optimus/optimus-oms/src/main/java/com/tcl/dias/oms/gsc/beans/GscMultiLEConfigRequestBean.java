package com.tcl.dias.oms.gsc.beans;

import java.util.List;

/**
 * Request bean for get configuration details for multiple solutions
 * 
 * @author Srinivasa Raghavan
 */
public class GscMultiLEConfigRequestBean {
	private Integer solutionId;
	private Integer gscQuoteId;
	private List<GscQuoteConfigurationBean> configurations;

	public Integer getSolutionId() {
		return solutionId;
	}

	public void setSolutionId(Integer solutionId) {
		this.solutionId = solutionId;
	}

	public Integer getGscQuoteId() {
		return gscQuoteId;
	}

	public void setGscQuoteId(Integer gscQuoteId) {
		this.gscQuoteId = gscQuoteId;
	}

	public List<GscQuoteConfigurationBean> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(List<GscQuoteConfigurationBean> configurations) {
		this.configurations = configurations;
	}
}
