package com.tcl.dias.servicefulfillmentutils.beans.feasibility;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Results {

	@JsonProperty("account_id_with_18_digit")
	private String accountIdWith18Digit;

	@JsonProperty("solution_type")
	private String solutionType;

	@JsonProperty("bts_azimuth")
	private String btsAzimuth;

	@JsonProperty("in_solution_type")
	private String inSolutionType;

	@JsonProperty("Selected_solution_BW")
	private int selectedSolutionBW;

	@JsonProperty("bts_num_BTS")
	private String btsNumBTS;

	@JsonProperty("Network_Feasibility_Check")
	private String networkFeasibilityCheck;

	@JsonProperty("Sector_network_check")
	private String sectorNetworkCheck;

	@JsonProperty("bts_ip")
	private String btsIp;

	@JsonProperty("bts_site_address")
	private String btsSiteAddress;

	@JsonProperty("bts_lat")
	private String btsLat;

	@JsonProperty("Backhaul_Network_check_reason")
	private String backhaulNetworkCheckReason;

	@JsonProperty("error_msg")
	private String errorMsg;

	@JsonProperty("bts_IP_PMP")
	private String btsIPPMP;

	@JsonProperty("latitude_final")
	private double latitudeFinal;

	@JsonProperty("newbts_Network_Feasibility_Check")
	private String newbtsNetworkFeasibilityCheck;

	@JsonProperty("newbts_Sector_network_check")
	private String newbtsSectorNetworkCheck;

	@JsonProperty("newbts_Backhaul_Network_check")
	private String newbtsBackhaulNetworkCheck;

	@JsonProperty("bts_id")
	private String btsId;

	@JsonProperty("bts_long")
	private String btsLong;

	@JsonProperty("ip_address")
	private String ipAddress;

	@JsonProperty("version")
	private int version;

	@JsonProperty("newbts_check_comment")
	private String newbtsCheckComment;

	@JsonProperty("bts_Closest_Site_type")
	private String btsClosestSiteType;

	@JsonProperty("bts_min_dist_km")
	private String btsMinDistKm;

	@JsonProperty("bts_Closest_infra_provider")
	private String btsClosestInfraProvider;

	@JsonProperty("error_msg_display")
	private String errorMsgDisplay;

	@JsonProperty("longitude_final")
	private double longitudeFinal;

	@JsonProperty("bts_device_type")
	private String btsDeviceType;

	@JsonProperty("sector_id")
	private String sectorId;

	@JsonProperty("Backhaul_Network_check")
	private String backhaulNetworkCheck;

	@JsonProperty("site_id")
	private String siteId;

	@JsonProperty("error_code")
	private String errorCode;

	@JsonProperty("BW_mbps")
	private int bWMbps;

	@JsonProperty("error_flag")
	private int errorFlag;

	@JsonProperty("bts_site_name")
	private String btsSiteName;

	public void setAccountIdWith18Digit(String accountIdWith18Digit){
		this.accountIdWith18Digit = accountIdWith18Digit;
	}

	public String getAccountIdWith18Digit(){
		return accountIdWith18Digit;
	}

	public void setSolutionType(String solutionType){
		this.solutionType = solutionType;
	}

	public String getSolutionType(){
		return solutionType;
	}

	public void setBtsAzimuth(String btsAzimuth){
		this.btsAzimuth = btsAzimuth;
	}

	public String getBtsAzimuth(){
		return btsAzimuth;
	}

	public void setInSolutionType(String inSolutionType){
		this.inSolutionType = inSolutionType;
	}

	public String getInSolutionType(){
		return inSolutionType;
	}

	public void setSelectedSolutionBW(int selectedSolutionBW){
		this.selectedSolutionBW = selectedSolutionBW;
	}

	public int getSelectedSolutionBW(){
		return selectedSolutionBW;
	}

	public void setBtsNumBTS(String btsNumBTS){
		this.btsNumBTS = btsNumBTS;
	}

	public String getBtsNumBTS(){
		return btsNumBTS;
	}

	public void setNetworkFeasibilityCheck(String networkFeasibilityCheck){
		this.networkFeasibilityCheck = networkFeasibilityCheck;
	}

	public String getNetworkFeasibilityCheck(){
		return networkFeasibilityCheck;
	}

	public void setSectorNetworkCheck(String sectorNetworkCheck){
		this.sectorNetworkCheck = sectorNetworkCheck;
	}

	public String getSectorNetworkCheck(){
		return sectorNetworkCheck;
	}

	public void setBtsIp(String btsIp){
		this.btsIp = btsIp;
	}

	public String getBtsIp(){
		return btsIp;
	}

	public void setBtsSiteAddress(String btsSiteAddress){
		this.btsSiteAddress = btsSiteAddress;
	}

	public String getBtsSiteAddress(){
		return btsSiteAddress;
	}

	public void setBtsLat(String btsLat){
		this.btsLat = btsLat;
	}

	public String getBtsLat(){
		return btsLat;
	}

	public void setBackhaulNetworkCheckReason(String backhaulNetworkCheckReason){
		this.backhaulNetworkCheckReason = backhaulNetworkCheckReason;
	}

	public String getBackhaulNetworkCheckReason(){
		return backhaulNetworkCheckReason;
	}

	public void setErrorMsg(String errorMsg){
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg(){
		return errorMsg;
	}

	public void setBtsIPPMP(String btsIPPMP){
		this.btsIPPMP = btsIPPMP;
	}

	public String getBtsIPPMP(){
		return btsIPPMP;
	}

	public void setLatitudeFinal(double latitudeFinal){
		this.latitudeFinal = latitudeFinal;
	}

	public double getLatitudeFinal(){
		return latitudeFinal;
	}

	public void setNewbtsNetworkFeasibilityCheck(String newbtsNetworkFeasibilityCheck){
		this.newbtsNetworkFeasibilityCheck = newbtsNetworkFeasibilityCheck;
	}

	public String getNewbtsNetworkFeasibilityCheck(){
		return newbtsNetworkFeasibilityCheck;
	}

	public void setNewbtsSectorNetworkCheck(String newbtsSectorNetworkCheck){
		this.newbtsSectorNetworkCheck = newbtsSectorNetworkCheck;
	}

	public String getNewbtsSectorNetworkCheck(){
		return newbtsSectorNetworkCheck;
	}

	public void setNewbtsBackhaulNetworkCheck(String newbtsBackhaulNetworkCheck){
		this.newbtsBackhaulNetworkCheck = newbtsBackhaulNetworkCheck;
	}

	public String getNewbtsBackhaulNetworkCheck(){
		return newbtsBackhaulNetworkCheck;
	}

	public void setBtsId(String btsId){
		this.btsId = btsId;
	}

	public String getBtsId(){
		return btsId;
	}

	public void setBtsLong(String btsLong){
		this.btsLong = btsLong;
	}

	public String getBtsLong(){
		return btsLong;
	}

	public void setIpAddress(String ipAddress){
		this.ipAddress = ipAddress;
	}

	public String getIpAddress(){
		return ipAddress;
	}

	public void setVersion(int version){
		this.version = version;
	}

	public int getVersion(){
		return version;
	}

	public void setNewbtsCheckComment(String newbtsCheckComment){
		this.newbtsCheckComment = newbtsCheckComment;
	}

	public String getNewbtsCheckComment(){
		return newbtsCheckComment;
	}

	public void setBtsClosestSiteType(String btsClosestSiteType){
		this.btsClosestSiteType = btsClosestSiteType;
	}

	public String getBtsClosestSiteType(){
		return btsClosestSiteType;
	}

	public void setBtsMinDistKm(String btsMinDistKm){
		this.btsMinDistKm = btsMinDistKm;
	}

	public String getBtsMinDistKm(){
		return btsMinDistKm;
	}

	public void setBtsClosestInfraProvider(String btsClosestInfraProvider){
		this.btsClosestInfraProvider = btsClosestInfraProvider;
	}

	public String getBtsClosestInfraProvider(){
		return btsClosestInfraProvider;
	}

	public void setErrorMsgDisplay(String errorMsgDisplay){
		this.errorMsgDisplay = errorMsgDisplay;
	}

	public String getErrorMsgDisplay(){
		return errorMsgDisplay;
	}

	public void setLongitudeFinal(double longitudeFinal){
		this.longitudeFinal = longitudeFinal;
	}

	public double getLongitudeFinal(){
		return longitudeFinal;
	}

	public void setBtsDeviceType(String btsDeviceType){
		this.btsDeviceType = btsDeviceType;
	}

	public String getBtsDeviceType(){
		return btsDeviceType;
	}

	public void setSectorId(String sectorId){
		this.sectorId = sectorId;
	}

	public String getSectorId(){
		return sectorId;
	}

	public void setBackhaulNetworkCheck(String backhaulNetworkCheck){
		this.backhaulNetworkCheck = backhaulNetworkCheck;
	}

	public String getBackhaulNetworkCheck(){
		return backhaulNetworkCheck;
	}

	public void setSiteId(String siteId){
		this.siteId = siteId;
	}

	public String getSiteId(){
		return siteId;
	}

	public void setErrorCode(String errorCode){
		this.errorCode = errorCode;
	}

	public String getErrorCode(){
		return errorCode;
	}

	public void setBWMbps(int bWMbps){
		this.bWMbps = bWMbps;
	}

	public int getBWMbps(){
		return bWMbps;
	}

	public void setErrorFlag(int errorFlag){
		this.errorFlag = errorFlag;
	}

	public int getErrorFlag(){
		return errorFlag;
	}

	public void setBtsSiteName(String btsSiteName){
		this.btsSiteName = btsSiteName;
	}

	public String getBtsSiteName(){
		return btsSiteName;
	}

	@Override
 	public String toString(){
		return 
			"ResultsItem{" + 
			"account_id_with_18_digit = '" + accountIdWith18Digit + '\'' + 
			",solution_type = '" + solutionType + '\'' + 
			",bts_azimuth = '" + btsAzimuth + '\'' + 
			",in_solution_type = '" + inSolutionType + '\'' + 
			",selected_solution_BW = '" + selectedSolutionBW + '\'' + 
			",bts_num_BTS = '" + btsNumBTS + '\'' + 
			",network_Feasibility_Check = '" + networkFeasibilityCheck + '\'' + 
			",sector_network_check = '" + sectorNetworkCheck + '\'' + 
			",bts_ip = '" + btsIp + '\'' + 
			",bts_site_address = '" + btsSiteAddress + '\'' + 
			",bts_lat = '" + btsLat + '\'' + 
			",backhaul_Network_check_reason = '" + backhaulNetworkCheckReason + '\'' + 
			",error_msg = '" + errorMsg + '\'' + 
			",bts_IP_PMP = '" + btsIPPMP + '\'' + 
			",latitude_final = '" + latitudeFinal + '\'' + 
			",newbts_Network_Feasibility_Check = '" + newbtsNetworkFeasibilityCheck + '\'' + 
			",newbts_Sector_network_check = '" + newbtsSectorNetworkCheck + '\'' + 
			",newbts_Backhaul_Network_check = '" + newbtsBackhaulNetworkCheck + '\'' + 
			",bts_id = '" + btsId + '\'' + 
			",bts_long = '" + btsLong + '\'' + 
			",ip_address = '" + ipAddress + '\'' + 
			",version = '" + version + '\'' + 
			",newbts_check_comment = '" + newbtsCheckComment + '\'' + 
			",bts_Closest_Site_type = '" + btsClosestSiteType + '\'' + 
			",bts_min_dist_km = '" + btsMinDistKm + '\'' + 
			",bts_Closest_infra_provider = '" + btsClosestInfraProvider + '\'' + 
			",error_msg_display = '" + errorMsgDisplay + '\'' + 
			",longitude_final = '" + longitudeFinal + '\'' + 
			",bts_device_type = '" + btsDeviceType + '\'' + 
			",sector_id = '" + sectorId + '\'' + 
			",backhaul_Network_check = '" + backhaulNetworkCheck + '\'' + 
			",site_id = '" + siteId + '\'' + 
			",error_code = '" + errorCode + '\'' + 
			",bW_mbps = '" + bWMbps + '\'' + 
			",error_flag = '" + errorFlag + '\'' + 
			",bts_site_name = '" + btsSiteName + '\'' + 
			"}";
		}
}