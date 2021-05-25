package com.tcl.dias.oms.gsc.beans;

import java.util.List;

/**
 * Request bean for saving configuration attributes of multiple solutions
 * 
 * @author Srinivasa Raghavan
 */
public class GscMultiLEProductComponentRequest {
	private Integer solutionId;
	private Integer gscQuoteId;
	private List<GscProductComponentBean> gscProductComponents;

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

	public List<GscProductComponentBean> getGscProductComponents() {
		return gscProductComponents;
	}

	public void setGscProductComponents(List<GscProductComponentBean> gscProductComponents) {
		this.gscProductComponents = gscProductComponents;
	}
}
