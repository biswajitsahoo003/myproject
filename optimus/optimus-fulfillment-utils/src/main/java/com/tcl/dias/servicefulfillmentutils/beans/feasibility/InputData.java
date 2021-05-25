package com.tcl.dias.servicefulfillmentutils.beans.feasibility;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InputData {

	@JsonProperty("account_id_with_18_digit")
	private String accountIdWith18Digit;

	@JsonProperty("longitude_final")
	private double longitudeFinal;

	@JsonProperty("sector_id")
	private String sectorId;

	@JsonProperty("latitude_final")
	private double latitudeFinal;

	@JsonProperty("bts_ip")
	private String btsIp;

	@JsonProperty("site_id")
	private String siteId;

	@JsonProperty("in_solution_type")
	private String inSolutionType;

	@JsonProperty("bts_id")
	private String btsId;

	@JsonProperty("BW_mbps")
	private int bWMbps;

	public void setAccountIdWith18Digit(String accountIdWith18Digit){
		this.accountIdWith18Digit = accountIdWith18Digit;
	}

	public String getAccountIdWith18Digit(){
		return accountIdWith18Digit;
	}

	public void setLongitudeFinal(double longitudeFinal){
		this.longitudeFinal = longitudeFinal;
	}

	public double getLongitudeFinal(){
		return longitudeFinal;
	}

	public void setSectorId(String sectorId){
		this.sectorId = sectorId;
	}

	public String getSectorId(){
		return sectorId;
	}

	public void setLatitudeFinal(double latitudeFinal){
		this.latitudeFinal = latitudeFinal;
	}

	public double getLatitudeFinal(){
		return latitudeFinal;
	}

	public void setBtsIp(String btsIp){
		this.btsIp = btsIp;
	}

	public String getBtsIp(){
		return btsIp;
	}

	public void setSiteId(String siteId){
		this.siteId = siteId;
	}

	public String getSiteId(){
		return siteId;
	}

	public void setInSolutionType(String inSolutionType){
		this.inSolutionType = inSolutionType;
	}

	public String getInSolutionType(){
		return inSolutionType;
	}

	public void setBtsId(String btsId){
		this.btsId = btsId;
	}

	public String getBtsId(){
		return btsId;
	}

	public void setBWMbps(int bWMbps){
		this.bWMbps = bWMbps;
	}

	public int getBWMbps(){
		return bWMbps;
	}

	@Override
 	public String toString(){
		return 
			"InputDataItem{" + 
			"account_id_with_18_digit = '" + accountIdWith18Digit + '\'' + 
			",longitude_final = '" + longitudeFinal + '\'' + 
			",sector_id = '" + sectorId + '\'' + 
			",latitude_final = '" + latitudeFinal + '\'' + 
			",bts_ip = '" + btsIp + '\'' + 
			",site_id = '" + siteId + '\'' + 
			",in_solution_type = '" + inSolutionType + '\'' + 
			",bts_id = '" + btsId + '\'' + 
			",bW_mbps = '" + bWMbps + '\'' + 
			"}";
		}
}