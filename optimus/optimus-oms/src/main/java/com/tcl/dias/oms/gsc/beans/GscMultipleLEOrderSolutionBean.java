package com.tcl.dias.oms.gsc.beans;

import java.util.List;

/**
 * Gsc Multiple LE Order solution bean
 * 
 * @author Srinivasa Raghavan
 * 
 */
public class GscMultipleLEOrderSolutionBean {

	private String source;
	private String destination;
	private List<GscOrderSolutionBean> gscOrderSolutions;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public List<GscOrderSolutionBean> getGscOrderSolutions() {
		return gscOrderSolutions;
	}

	public void setGscOrderSolutions(List<GscOrderSolutionBean> gscOrderSolutions) {
		this.gscOrderSolutions = gscOrderSolutions;
	}

}
