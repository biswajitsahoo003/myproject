package com.tcl.dias.oms.discount.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DiscountInputData {

	@JsonProperty("site_id")
	String siteId;
	@JsonProperty("bw_mbps")
	String bwMbps;
	@JsonProperty("burstable_bw")
	String burstableBw;
	@JsonProperty("product_name")
	String productName;
	@JsonProperty("local_loop_interface")
	String localLoopInterface;
	@JsonProperty("connection_type")
	String connectionType;
	@JsonProperty("cpe_variant")
	String cpeVariant;
	@JsonProperty("cpe_management_type")
	String cpeManagementType;
	@JsonProperty("cpe_supply_type")
	String cpeSupplyType;
	@JsonProperty("topology")
	String topology;
	@JsonProperty("sp_lm_arc_bw_onwl")
	String spLmArcBwOnwl;
	@JsonProperty("sp_lm_nrc_bw_onwl")
	String spLmNrcBwOnwl;
	@JsonProperty("sp_lm_nrc_mux_onwl")
	String spLmNrcMuxOnwl;
	/*sp_lm_nrc_inbldg_onwl
	sp_lm_nrc_ospcapex_onwl
	sp_lm_nrc_nerental_onwl
	sp_lm_arc_bw_prov_ofrf
	sp_lm_nrc_bw_prov_ofrf
	sp_lm_nrc_mast_ofrf
	sp_lm_arc_bw_onrf
	sp_lm_nrc_bw_onrf
	sp_lm_nrc_mast_onrf
	Orch_Connection
	Orch_LM_Type
	ip_address_arrangement
	ipv4_address_pool_size
	ipv6_address_pool_size
	sp_CPE_Outright_NRC
	sp_CPE_Rental_NRC
	sp_CPE_Install_NRC
	sp_CPE_Management_ARC'
	sp_port_arc
	sp_port_nrc
	sp_burst_per_MB_price_ARC
	sp_additional_IP_ARC*/
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getBwMbps() {
		return bwMbps;
	}
	public void setBwMbps(String bwMbps) {
		this.bwMbps = bwMbps;
	}
	public String getBurstableBw() {
		return burstableBw;
	}
	public void setBurstableBw(String burstableBw) {
		this.burstableBw = burstableBw;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getLocalLoopInterface() {
		return localLoopInterface;
	}
	public void setLocalLoopInterface(String localLoopInterface) {
		this.localLoopInterface = localLoopInterface;
	}
	public String getConnectionType() {
		return connectionType;
	}
	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}
	public String getCpeVariant() {
		return cpeVariant;
	}
	public void setCpeVariant(String cpeVariant) {
		this.cpeVariant = cpeVariant;
	}
	public String getCpeManagementType() {
		return cpeManagementType;
	}
	public void setCpeManagementType(String cpeManagementType) {
		this.cpeManagementType = cpeManagementType;
	}
	public String getCpeSupplyType() {
		return cpeSupplyType;
	}
	public void setCpeSupplyType(String cpeSupplyType) {
		this.cpeSupplyType = cpeSupplyType;
	}
	public String getTopology() {
		return topology;
	}
	public void setTopology(String topology) {
		this.topology = topology;
	}
	public String getSpLmArcBwOnwl() {
		return spLmArcBwOnwl;
	}
	public void setSpLmArcBwOnwl(String spLmArcBwOnwl) {
		this.spLmArcBwOnwl = spLmArcBwOnwl;
	}
	public String getSpLmNrcBwOnwl() {
		return spLmNrcBwOnwl;
	}
	public void setSpLmNrcBwOnwl(String spLmNrcBwOnwl) {
		this.spLmNrcBwOnwl = spLmNrcBwOnwl;
	}
	public String getSpLmNrcMuxOnwl() {
		return spLmNrcMuxOnwl;
	}
	public void setSpLmNrcMuxOnwl(String spLmNrcMuxOnwl) {
		this.spLmNrcMuxOnwl = spLmNrcMuxOnwl;
	}

	
}
