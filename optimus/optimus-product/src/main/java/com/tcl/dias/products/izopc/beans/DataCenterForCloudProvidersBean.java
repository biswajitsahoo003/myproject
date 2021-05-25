package com.tcl.dias.products.izopc.beans;

import java.util.List;

/**
 * POJO class for  Data center list for a cloud provider name.
 * 
 *
 * @author Prabhu a
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class DataCenterForCloudProvidersBean {

	private String cloudProviderName;
	private List<DataCenterProviderDetails> dataCenterList;
	/**
	 * @return the cloudProviderName
	 */
	public String getCloudProviderName() {
		return cloudProviderName;
	}
	/**
	 * @param cloudProviderName the cloudProviderName to set
	 */
	public void setCloudProviderName(String cloudProviderName) {
		this.cloudProviderName = cloudProviderName;
	}
	/**
	 * @return the dataCenterList
	 */
	public List<DataCenterProviderDetails> getDataCenterList() {
		return dataCenterList;
	}
	/**
	 * @param dataCenterList the dataCenterList to set
	 */
	public void setDataCenterList(List<DataCenterProviderDetails> dataCenterList) {
		this.dataCenterList = dataCenterList;
	}
	public DataCenterForCloudProvidersBean(String cloudProviderName, List<DataCenterProviderDetails> dataCenterList) {
		super();
		this.cloudProviderName = cloudProviderName;
		this.dataCenterList = dataCenterList;
	}
	
	
	
}
