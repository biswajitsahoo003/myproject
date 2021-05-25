package com.tcl.dias.oms.gsc.beans;

import java.util.List;

/**
 * To accommodate multiple LE GSC solutions
 * 
 * @author Srinivasa Raghavan
 *
 */
public class GscMultipleLESolutionBean {
	private String source;
	private String destination;
	private List<GscSolutionBean> gscSolutions;

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

	public List<GscSolutionBean> getGscSolutions() {
		return gscSolutions;
	}

	public void setGscSolutions(List<GscSolutionBean> gscSolutions) {
		this.gscSolutions = gscSolutions;
	}
}