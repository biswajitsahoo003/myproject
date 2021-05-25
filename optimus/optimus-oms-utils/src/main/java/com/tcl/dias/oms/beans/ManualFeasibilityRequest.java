package com.tcl.dias.oms.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Bean Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManualFeasibilityRequest {

	private Integer siteFeasibilityId;
	@JsonProperty("lm_arc_bw_onwl")
	private String lmArcBwOnwl;
	@JsonProperty("lm_nrc_bw_onwl")
	private String lmNrcBwOnwl;
	@JsonProperty("lm_nrc_inbldg_onwl")
	private String lmNrcInbldgOnwl;
	@JsonProperty("lm_nrc_mux_onwl")
	private String lmNrcMuxOnwl;
	@JsonProperty("lm_nrc_nerental_onwl")
	private String lmNrcNerentalOnwl;
	@JsonProperty("lm_nrc_ospcapex_onwl")
	private String lmNrcOspcapexOnwl;
	@JsonProperty("Mast_3KM_avg_mast_ht")
	private String mast3KMAvgMastHt;
	@JsonProperty("lm_arc_bw_onrf")
	private String lmArcBwOnrf;
	@JsonProperty("lm_nrc_bw_onrf")
	private String lmNrcBwOnrf;
	@JsonProperty("avg_mast_ht")
	private String avgMastHt;
	@JsonProperty("lm_arc_bw_prov_ofrf")
	private String lmArcBwProvOfrf;
	@JsonProperty("lm_nrc_bw_prov_ofrf")
	private String lmNrcBwProvOfrf;
	@JsonProperty("a_min_hh_fatg")
	private String aMinhhfatg;
	@JsonProperty("b_min_hh_fatg")
	private String bMinhhfatg;
	@JsonProperty("min_hh_fatg")
	private String minhhfatg;
	@JsonProperty("POP_DIST_KM_SERVICE_MOD")
	private String popDistKmServiceMod;

	//GVPN INTL MACD RELATED ATTRIBUTES
	@JsonProperty("provider_Provider_Product_Name")
	private String providerProviderProductName;
	@JsonProperty("provider_pop_all_site_lat_long")
	private String providerPopAllSiteLatLong;
	@JsonProperty("provider_Provider_Name")
	private String providerProviderName;
	@JsonProperty("provider_pop_addresses")
	private String providerPopAddresses;
	@JsonProperty("x_connect_Xconnect_Provider_Name")
	private String xConnectXConnectProviderName;
	@JsonProperty("provider_Local_Loop_Capacity")
	private String providerLocalLoopCapacity;
	@JsonProperty("provider_Local_Loop_Interface")
	private String providerLocalLoopInterface;
	@JsonProperty("provider_MRC_BW_Currency_access")
	private String providerMRCBWCurrencyAccess;
	@JsonProperty("provider_MRC_BW")
	private String providerMRCBW;
	@JsonProperty("provider_OTC_NRC_Installation")
	private String providerOTCNRCInstallation;
	@JsonProperty("x_connect_Xconnect_MRC_Currency_access")
	private String xConnectXconnectMRCCurrencyAccess;
	@JsonProperty("x_connect_Xconnect_MRC_access")
	private String xConnectXConnectMRCAccess;
	@JsonProperty("x_connect_Xconnect_NRC_access")
	private String xConnectXConnectNRCAccess;

	public Integer getSiteFeasibilityId() {
		return siteFeasibilityId;
	}

	public void setSiteFeasibilityId(Integer siteFeasibilityId) {
		this.siteFeasibilityId = siteFeasibilityId;
	}

	public String getLmArcBwOnwl() {
		return lmArcBwOnwl;
	}

	public void setLmArcBwOnwl(String lmArcBwOnwl) {
		this.lmArcBwOnwl = lmArcBwOnwl;
	}

	public String getLmNrcBwOnwl() {
		return lmNrcBwOnwl;
	}

	public void setLmNrcBwOnwl(String lmNrcBwOnwl) {
		this.lmNrcBwOnwl = lmNrcBwOnwl;
	}

	public String getLmNrcInbldgOnwl() {
		return lmNrcInbldgOnwl;
	}

	public void setLmNrcInbldgOnwl(String lmNrcInbldgOnwl) {
		this.lmNrcInbldgOnwl = lmNrcInbldgOnwl;
	}

	public String getLmNrcMuxOnwl() {
		return lmNrcMuxOnwl;
	}

	public void setLmNrcMuxOnwl(String lmNrcMuxOnwl) {
		this.lmNrcMuxOnwl = lmNrcMuxOnwl;
	}

	public String getLmNrcNerentalOnwl() {
		return lmNrcNerentalOnwl;
	}

	public void setLmNrcNerentalOnwl(String lmNrcNerentalOnwl) {
		this.lmNrcNerentalOnwl = lmNrcNerentalOnwl;
	}

	public String getLmNrcOspcapexOnwl() {
		return lmNrcOspcapexOnwl;
	}

	public void setLmNrcOspcapexOnwl(String lmNrcOspcapexOnwl) {
		this.lmNrcOspcapexOnwl = lmNrcOspcapexOnwl;
	}

	public String getMast3KMAvgMastHt() {
		return mast3KMAvgMastHt;
	}

	public void setMast3KMAvgMastHt(String mast3kmAvgMastHt) {
		mast3KMAvgMastHt = mast3kmAvgMastHt;
	}

	public String getLmArcBwOnrf() {
		return lmArcBwOnrf;
	}

	public void setLmArcBwOnrf(String lmArcBwOnrf) {
		this.lmArcBwOnrf = lmArcBwOnrf;
	}

	public String getLmNrcBwOnrf() {
		return lmNrcBwOnrf;
	}

	public void setLmNrcBwOnrf(String lmNrcBwOnrf) {
		this.lmNrcBwOnrf = lmNrcBwOnrf;
	}

	public String getAvgMastHt() {
		return avgMastHt;
	}

	public void setAvgMastHt(String avgMastHt) {
		this.avgMastHt = avgMastHt;
	}

	public String getLmArcBwProvOfrf() {
		return lmArcBwProvOfrf;
	}

	public void setLmArcBwProvOfrf(String lmArcBwProvOfrf) {
		this.lmArcBwProvOfrf = lmArcBwProvOfrf;
	}

	public String getLmNrcBwProvOfrf() {
		return lmNrcBwProvOfrf;
	}

	public void setLmNrcBwProvOfrf(String lmNrcBwProvOfrf) {
		this.lmNrcBwProvOfrf = lmNrcBwProvOfrf;
	}

	public String getaMinhhfatg() {
		return aMinhhfatg;
	}

	public void setaMinhhfatg(String aMinhhfatg) {
		this.aMinhhfatg = aMinhhfatg;
	}

	public String getbMinhhfatg() {
		return bMinhhfatg;
	}

	public void setbMinhhfatg(String bMinhhfatg) {
		this.bMinhhfatg = bMinhhfatg;
	}

	public String getMinhhfatg() {
		return minhhfatg;
	}

	public void setMinhhfatg(String minhhfatg) {
		this.minhhfatg = minhhfatg;
	}

	public String getPopDistKmServiceMod() {
		return popDistKmServiceMod;
	}

	public void setPopDistKmServiceMod(String popDistKmServiceMod) {
		this.popDistKmServiceMod = popDistKmServiceMod;
	}

	public String getProviderProviderProductName() {
		return providerProviderProductName;
	}

	public void setProviderProviderProductName(String providerProviderProductName) {
		this.providerProviderProductName = providerProviderProductName;
	}

	public String getProviderPopAllSiteLatLong() {
		return providerPopAllSiteLatLong;
	}

	public void setProviderPopAllSiteLatLong(String providerPopAllSiteLatLong) {
		this.providerPopAllSiteLatLong = providerPopAllSiteLatLong;
	}

	public String getProviderProviderName() {
		return providerProviderName;
	}

	public void setProviderProviderName(String providerProviderName) {
		this.providerProviderName = providerProviderName;
	}

	public String getProviderPopAddresses() {
		return providerPopAddresses;
	}

	public void setProviderPopAddresses(String providerPopAddresses) {
		this.providerPopAddresses = providerPopAddresses;
	}

	public String getxConnectXConnectProviderName() {
		return xConnectXConnectProviderName;
	}

	public void setxConnectXConnectProviderName(String xConnectXConnectProviderName) {
		this.xConnectXConnectProviderName = xConnectXConnectProviderName;
	}

	public String getProviderLocalLoopCapacity() {
		return providerLocalLoopCapacity;
	}

	public void setProviderLocalLoopCapacity(String providerLocalLoopCapacity) {
		this.providerLocalLoopCapacity = providerLocalLoopCapacity;
	}

	public String getProviderLocalLoopInterface() {
		return providerLocalLoopInterface;
	}

	public void setProviderLocalLoopInterface(String providerLocalLoopInterface) {
		this.providerLocalLoopInterface = providerLocalLoopInterface;
	}

	public String getProviderMRCBWCurrencyAccess() {
		return providerMRCBWCurrencyAccess;
	}

	public void setProviderMRCBWCurrencyAccess(String providerMRCBWCurrencyAccess) {
		this.providerMRCBWCurrencyAccess = providerMRCBWCurrencyAccess;
	}

	public String getProviderMRCBW() {
		return providerMRCBW;
	}

	public void setProviderMRCBW(String providerMRCBW) {
		this.providerMRCBW = providerMRCBW;
	}

	public String getProviderOTCNRCInstallation() {
		return providerOTCNRCInstallation;
	}

	public void setProviderOTCNRCInstallation(String providerOTCNRCInstallation) {
		this.providerOTCNRCInstallation = providerOTCNRCInstallation;
	}

	public String getxConnectXconnectMRCCurrencyAccess() {
		return xConnectXconnectMRCCurrencyAccess;
	}

	public void setxConnectXconnectMRCCurrencyAccess(String xConnectXconnectMRCCurrencyAccess) {
		this.xConnectXconnectMRCCurrencyAccess = xConnectXconnectMRCCurrencyAccess;
	}

	public String getxConnectXConnectMRCAccess() {
		return xConnectXConnectMRCAccess;
	}

	public void setxConnectXConnectMRCAccess(String xConnectXConnectMRCAccess) {
		this.xConnectXConnectMRCAccess = xConnectXConnectMRCAccess;
	}

	public String getxConnectXConnectNRCAccess() {
		return xConnectXConnectNRCAccess;
	}

	public void setxConnectXConnectNRCAccess(String xConnectXConnectNRCAccess) {
		this.xConnectXConnectNRCAccess = xConnectXConnectNRCAccess;
	}

	@Override
	public String toString() {
		return "ManualFeasibilityRequest [siteFeasibilityId=" + siteFeasibilityId + ", lmArcBwOnwl=" + lmArcBwOnwl
				+ ", lmNrcBwOnwl=" + lmNrcBwOnwl + ", lmNrcInbldgOnwl=" + lmNrcInbldgOnwl + ", lmNrcMuxOnwl="
				+ lmNrcMuxOnwl + ", lmNrcNerentalOnwl=" + lmNrcNerentalOnwl + ", lmNrcOspcapexOnwl=" + lmNrcOspcapexOnwl
				+ ", mast3KMAvgMastHt=" + mast3KMAvgMastHt + ", lmArcBwOnrf=" + lmArcBwOnrf + ", lmNrcBwOnrf="
				+ lmNrcBwOnrf + ", avgMastHt=" + avgMastHt + ", lmArcBwProvOfrf=" + lmArcBwProvOfrf
				+ ", lmNrcBwProvOfrf=" + lmNrcBwProvOfrf + ", aMinhhfatg=" + aMinhhfatg + ", bMinhhfatg=" + bMinhhfatg
				+ ", minhhfatg=" + minhhfatg + "]";
	}

}
