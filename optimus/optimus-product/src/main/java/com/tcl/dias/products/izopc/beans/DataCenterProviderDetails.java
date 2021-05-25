package com.tcl.dias.products.izopc.beans;

/**
 * POJO class for  Data center provider details.
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class DataCenterProviderDetails {

	private String dataCenterCity;

	private String dataCenterSiteCode;

	private String dataCenterDec;

	private String dataCentreAddress;

	private String interfaceName;

	private String remarks;

	private String dataCenterLongitude;

	private String dataCenterLatitude;
	
	private String dataCenterCd;

	public String getDataCenterCity() {
		return dataCenterCity;
	}

	public void setDataCenterCity(String dataCenterCity) {
		this.dataCenterCity = dataCenterCity;
	}

	public String getDataCenterSiteCode() {
		return dataCenterSiteCode;
	}

	public void setDataCenterSiteCode(String dataCenterSiteCode) {
		this.dataCenterSiteCode = dataCenterSiteCode;
	}

	public String getDataCenterDec() {
		return dataCenterDec;
	}

	public void setDataCenterDec(String dataCenterDec) {
		this.dataCenterDec = dataCenterDec;
	}

	public String getDataCentreAddress() {
		return dataCentreAddress;
	}

	public void setDataCentreAddress(String dataCentreAddress) {
		this.dataCentreAddress = dataCentreAddress;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDataCenterLongitude() {
		return dataCenterLongitude;
	}

	public void setDataCenterLongitude(String dataCenterLongitude) {
		this.dataCenterLongitude = dataCenterLongitude;
	}

	public String getDataCenterLatitude() {
		return dataCenterLatitude;
	}

	public void setDataCenterLatitude(String dataCenterLatitude) {
		this.dataCenterLatitude = dataCenterLatitude;
	}

	/**
	 * @return the dataCenterCd
	 */
	public String getDataCenterCd() {
		return dataCenterCd;
	}

	/**
	 * @param dataCenterCd the dataCenterCd to set
	 */
	public void setDataCenterCd(String dataCenterCd) {
		this.dataCenterCd = dataCenterCd;
	}

}
