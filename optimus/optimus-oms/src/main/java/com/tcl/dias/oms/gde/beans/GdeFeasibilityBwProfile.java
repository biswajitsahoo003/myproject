package com.tcl.dias.oms.gde.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Bean class for feasibility bwprofilr attributes
 * @author archchan
 *
 */
public class GdeFeasibilityBwProfile implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
//	@JsonProperty("bw_cbs")
//	private Integer bwCbs;
	
	@JsonProperty("bw_cir")
	private Integer bwCir;
	
//	@JsonProperty("bw_ebs")
//	private Integer bwEbs;
	
//	@JsonProperty("bw_eir")
//	private Integer bwEir;

//	public Integer getBwCbs() {
//		return bwCbs;
//	}
//
//	public void setBwCbs(Integer bwCbs) {
//		this.bwCbs = bwCbs;
//	}

	public Integer getBwCir() {
		return bwCir;
	}

	public void setBwCir(Integer bwCir) {
		this.bwCir = bwCir;
	}

//	public Integer getBwEbs() {
//		return bwEbs;
//	}
//
//	public void setBwEbs(Integer bwEbs) {
//		this.bwEbs = bwEbs;
//	}
//
//	public Integer getBwEir() {
//		return bwEir;
//	}
//
//	public void setBwEir(Integer bwEir) {
//		this.bwEir = bwEir;
//	}
	


}
