package com.tcl.dias.oms.npl.pricing.bean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This file contains the ManualFeasibilityRequest.java class.
 * 
 *
 * @author Prabhu A
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManualFeasibilityRequest {

	@NotNull
	@Min(1)
	@JsonProperty("link_feasibility_id")
	private Integer linkFeasibilityId;
	@NotNull
	@JsonProperty("a_lm_arc_bw_onwl")
	private String aLmArcBwOnwl;
	@NotNull
	@JsonProperty("a_lm_nrc_bw_onwl")
	private String aLmNrcBwOnwl;
	@NotNull
	@JsonProperty("a_lm_nrc_inbldg_onwl")
	private String aLmNrcInbldgOnwl;
	@NotNull
	@JsonProperty("a_lm_nrc_mux_onwl")
	private String aLmNrcMuxOnwl;
	@NotNull
	@JsonProperty("a_lm_nrc_nerental_onwl")
	private String aLmNrcNerentalOnwl;
	@NotNull
	@JsonProperty("a_lm_nrc_ospcapex_onwl")
	private String aLmNrcOspcapexOnwl;
	@NotNull
	@JsonProperty("a_min_hh_fatg")
	private String aMinHhFatg;
	@NotNull
	@JsonProperty("a_hh_name")
	private String aHhName;
	@NotNull
	@JsonProperty("b_lm_arc_bw_onwl")
	private String bLmArcBwOnwl;
	@NotNull
	@JsonProperty("b_lm_nrc_bw_onwl")
	private String bLmNrcBwOnwl;
	@NotNull
	@JsonProperty("b_lm_nrc_inbldg_onwl")
	private String bLmNrcInbldgOnwl;
	@NotNull
	@JsonProperty("b_lm_nrc_mux_onwl")
	private String bLmNrcMuxOnwl;
	@NotNull
	@JsonProperty("b_lm_nrc_nerental_onwl")
	private String bLmNrcNerentalOnwl;
	@NotNull
	@JsonProperty("b_lm_nrc_ospcapex_onwl")
	private String bLmNrcOspcapexOnwl;
	@NotNull
	@JsonProperty("b_min_hh_fatg")
	private String bMinHhFatg;
	@NotNull
	@JsonProperty("a_POP_DIST_KM_SERVICE_MOD")
	private String aPopDistKmServiceMod;
	@NotNull
	@JsonProperty("b_POP_DIST_KM_SERVICE_MOD")
	private String bPopDistKmServiceMod;
	@NotNull
	@JsonProperty("a_Predicted_Access_Feasibility")
	private String aPredictedAccessFeasibility;
	@NotNull
	@JsonProperty("b_Predicted_Access_Feasibility")
	private String bPredictedAccessFeasibility;


	/**
	 * @return the linkFeasibilityId
	 */
	public Integer getLinkFeasibilityId() {
		return linkFeasibilityId;
	}
	/**
	 * @param linkFeasibilityId the linkFeasibilityId to set
	 */
	public void setLinkFeasibilityId(Integer linkFeasibilityId) {
		this.linkFeasibilityId = linkFeasibilityId;
	}
	/**
	 * @return the aLmArcBwOnwl
	 */
	public String getaLmArcBwOnwl() {
		return aLmArcBwOnwl;
	}
	/**
	 * @param aLmArcBwOnwl the aLmArcBwOnwl to set
	 */
	public void setaLmArcBwOnwl(String aLmArcBwOnwl) {
		this.aLmArcBwOnwl = aLmArcBwOnwl;
	}
	/**
	 * @return the aLmNrcBwOnwl
	 */
	public String getaLmNrcBwOnwl() {
		return aLmNrcBwOnwl;
	}
	/**
	 * @param aLmNrcBwOnwl the aLmNrcBwOnwl to set
	 */
	public void setaLmNrcBwOnwl(String aLmNrcBwOnwl) {
		this.aLmNrcBwOnwl = aLmNrcBwOnwl;
	}
	/**
	 * @return the aLmNrcInbldgOnwl
	 */
	public String getaLmNrcInbldgOnwl() {
		return aLmNrcInbldgOnwl;
	}
	/**
	 * @param aLmNrcInbldgOnwl the aLmNrcInbldgOnwl to set
	 */
	public void setaLmNrcInbldgOnwl(String aLmNrcInbldgOnwl) {
		this.aLmNrcInbldgOnwl = aLmNrcInbldgOnwl;
	}
	/**
	 * @return the aLmNrcMuxOnwl
	 */
	public String getaLmNrcMuxOnwl() {
		return aLmNrcMuxOnwl;
	}
	/**
	 * @param aLmNrcMuxOnwl the aLmNrcMuxOnwl to set
	 */
	public void setaLmNrcMuxOnwl(String aLmNrcMuxOnwl) {
		this.aLmNrcMuxOnwl = aLmNrcMuxOnwl;
	}
	/**
	 * @return the aLmNrcNerentalOnwl
	 */
	public String getaLmNrcNerentalOnwl() {
		return aLmNrcNerentalOnwl;
	}
	/**
	 * @param aLmNrcNerentalOnwl the aLmNrcNerentalOnwl to set
	 */
	public void setaLmNrcNerentalOnwl(String aLmNrcNerentalOnwl) {
		this.aLmNrcNerentalOnwl = aLmNrcNerentalOnwl;
	}
	/**
	 * @return the aLmNrcOspcapexOnwl
	 */
	public String getaLmNrcOspcapexOnwl() {
		return aLmNrcOspcapexOnwl;
	}
	/**
	 * @param aLmNrcOspcapexOnwl the aLmNrcOspcapexOnwl to set
	 */
	public void setaLmNrcOspcapexOnwl(String aLmNrcOspcapexOnwl) {
		this.aLmNrcOspcapexOnwl = aLmNrcOspcapexOnwl;
	}
	/**
	 * @return the aMinHhFatg
	 */
	public String getaMinHhFatg() {
		return aMinHhFatg;
	}
	/**
	 * @param aMinHhFatg the aMinHhFatg to set
	 */
	public void setaMinHhFatg(String aMinHhFatg) {
		this.aMinHhFatg = aMinHhFatg;
	}
	/**
	 * @return the aHhName
	 */
	public String getaHhName() {
		return aHhName;
	}
	/**
	 * @param aHhName the aHhName to set
	 */
	public void setaHhName(String aHhName) {
		this.aHhName = aHhName;
	}
	/**
	 * @return the bLmArcBwOnwl
	 */
	public String getbLmArcBwOnwl() {
		return bLmArcBwOnwl;
	}
	/**
	 * @param bLmArcBwOnwl the bLmArcBwOnwl to set
	 */
	public void setbLmArcBwOnwl(String bLmArcBwOnwl) {
		this.bLmArcBwOnwl = bLmArcBwOnwl;
	}
	/**
	 * @return the bLmNrcBwOnwl
	 */
	public String getbLmNrcBwOnwl() {
		return bLmNrcBwOnwl;
	}
	/**
	 * @param bLmNrcBwOnwl the bLmNrcBwOnwl to set
	 */
	public void setbLmNrcBwOnwl(String bLmNrcBwOnwl) {
		this.bLmNrcBwOnwl = bLmNrcBwOnwl;
	}
	/**
	 * @return the bLmNrcInbldgOnwl
	 */
	public String getbLmNrcInbldgOnwl() {
		return bLmNrcInbldgOnwl;
	}
	/**
	 * @param bLmNrcInbldgOnwl the bLmNrcInbldgOnwl to set
	 */
	public void setbLmNrcInbldgOnwl(String bLmNrcInbldgOnwl) {
		this.bLmNrcInbldgOnwl = bLmNrcInbldgOnwl;
	}
	/**
	 * @return the bLmNrcMuxOnwl
	 */
	public String getbLmNrcMuxOnwl() {
		return bLmNrcMuxOnwl;
	}
	/**
	 * @param bLmNrcMuxOnwl the bLmNrcMuxOnwl to set
	 */
	public void setbLmNrcMuxOnwl(String bLmNrcMuxOnwl) {
		this.bLmNrcMuxOnwl = bLmNrcMuxOnwl;
	}
	/**
	 * @return the bLmNrcNerentalOnwl
	 */
	public String getbLmNrcNerentalOnwl() {
		return bLmNrcNerentalOnwl;
	}
	/**
	 * @param bLmNrcNerentalOnwl the bLmNrcNerentalOnwl to set
	 */
	public void setbLmNrcNerentalOnwl(String bLmNrcNerentalOnwl) {
		this.bLmNrcNerentalOnwl = bLmNrcNerentalOnwl;
	}
	/**
	 * @return the bLmNrcOspcapexOnwl
	 */
	public String getbLmNrcOspcapexOnwl() {
		return bLmNrcOspcapexOnwl;
	}
	/**
	 * @param bLmNrcOspcapexOnwl the bLmNrcOspcapexOnwl to set
	 */
	public void setbLmNrcOspcapexOnwl(String bLmNrcOspcapexOnwl) {
		this.bLmNrcOspcapexOnwl = bLmNrcOspcapexOnwl;
	}
	/**
	 * @return the bMinHhFatg
	 */
	public String getbMinHhFatg() {
		return bMinHhFatg;
	}
	/**
	 * @param bMinHhFatg the bMinHhFatg to set
	 */
	public void setbMinHhFatg(String bMinHhFatg) {
		this.bMinHhFatg = bMinHhFatg;
	}
	/**
	 * @return the aPopDistKmServiceMod
	 */
	public String getaPopDistKmServiceMod() {
		return aPopDistKmServiceMod;
	}
	/**
	 * @param aPopDistKmServiceMod the aPopDistKmServiceMod to set
	 */
	public void setaPopDistKmServiceMod(String aPopDistKmServiceMod) {
		this.aPopDistKmServiceMod = aPopDistKmServiceMod;
	}
	/**
	 * @return the bPopDistKmServiceMod
	 */
	public String getbPopDistKmServiceMod() {
		return bPopDistKmServiceMod;
	}
	/**
	 * @param bPopDistKmServiceMod the bPopDistKmServiceMod to set
	 */
	public void setbPopDistKmServiceMod(String bPopDistKmServiceMod) {
		this.bPopDistKmServiceMod = bPopDistKmServiceMod;
	}

	public String getaPredictedAccessFeasibility() {
		return aPredictedAccessFeasibility;
	}

	public void setaPredictedAccessFeasibility(String aPredictedAccessFeasibility) {
		this.aPredictedAccessFeasibility = aPredictedAccessFeasibility;
	}

	public String getbPredictedAccessFeasibility() {
		return bPredictedAccessFeasibility;
	}

	public void setbPredictedAccessFeasibility(String bPredictedAccessFeasibility) {
		this.bPredictedAccessFeasibility = bPredictedAccessFeasibility;
	}
}
