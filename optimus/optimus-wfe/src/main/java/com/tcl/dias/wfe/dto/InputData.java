package com.tcl.dias.wfe.dto;

/**
 * This file contains the InputData.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

public class InputData {
	private String local_loop_interface;

	private String Feasibility;

	private String Rank;

	private String Latitude_final;

	private String Type;

	private String prospect_name;

	private String createdDate;

	private String BW_mbps;

	private String Burstable_BW;

	private String salesOrg;

	private String Selected;

	private String Site_id;

	private String productName;

	private String last_mile_contract_term;

	private String resp_city;

	private String Longitude_final;

	private String Customer_Segment;

	private String resp_state;

	public String getLocal_loop_interface() {
		return local_loop_interface;
	}

	public void setLocal_loop_interface(String local_loop_interface) {
		this.local_loop_interface = local_loop_interface;
	}

	public String getFeasibility() {
		return Feasibility;
	}

	public void setFeasibility(String Feasibility) {
		this.Feasibility = Feasibility;
	}

	public String getRank() {
		return Rank;
	}

	public void setRank(String Rank) {
		this.Rank = Rank;
	}

	public String getLatitude_final() {
		return Latitude_final;
	}

	public void setLatitude_final(String Latitude_final) {
		this.Latitude_final = Latitude_final;
	}

	public String getType() {
		return Type;
	}

	public void setType(String Type) {
		this.Type = Type;
	}

	public String getProspect_name() {
		return prospect_name;
	}

	public void setProspect_name(String prospect_name) {
		this.prospect_name = prospect_name;
	}

	public String getBW_mbps() {
		return BW_mbps;
	}

	public void setBW_mbps(String BW_mbps) {
		this.BW_mbps = BW_mbps;
	}

	public String getBurstable_BW() {
		return Burstable_BW;
	}

	public void setBurstable_BW(String Burstable_BW) {
		this.Burstable_BW = Burstable_BW;
	}

	public String getSelected() {
		return Selected;
	}

	public void setSelected(String Selected) {
		this.Selected = Selected;
	}

	public String getSite_id() {
		return Site_id;
	}

	public void setSite_id(String Site_id) {
		this.Site_id = Site_id;
	}

	public String getLast_mile_contract_term() {
		return last_mile_contract_term;
	}

	public void setLast_mile_contract_term(String last_mile_contract_term) {
		this.last_mile_contract_term = last_mile_contract_term;
	}

	public String getResp_city() {
		return resp_city;
	}

	public void setResp_city(String resp_city) {
		this.resp_city = resp_city;
	}

	public String getLongitude_final() {
		return Longitude_final;
	}

	public void setLongitude_final(String Longitude_final) {
		this.Longitude_final = Longitude_final;
	}

	public String getCustomer_Segment() {
		return Customer_Segment;
	}

	public void setCustomer_Segment(String Customer_Segment) {
		this.Customer_Segment = Customer_Segment;
	}

	public String getResp_state() {
		return resp_state;
	}

	public void setResp_state(String resp_state) {
		this.resp_state = resp_state;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getSalesOrg() {
		return salesOrg;
	}

	public void setSalesOrg(String salesOrg) {
		this.salesOrg = salesOrg;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Override
	public String toString() {
		return "InputData [local_loop_interface=" + local_loop_interface + ", Feasibility=" + Feasibility + ", Rank="
				+ Rank + ", Latitude_final=" + Latitude_final + ", Type=" + Type + ", prospect_name=" + prospect_name
				+ ", createdDate=" + createdDate + ", BW_mbps=" + BW_mbps + ", Burstable_BW=" + Burstable_BW
				+ ", salesOrg=" + salesOrg + ", Selected=" + Selected + ", Site_id=" + Site_id + ", productName="
				+ productName + ", last_mile_contract_term=" + last_mile_contract_term + ", resp_city=" + resp_city
				+ ", Longitude_final=" + Longitude_final + ", Customer_Segment=" + Customer_Segment + ", resp_state="
				+ resp_state + "]";
	}

}
