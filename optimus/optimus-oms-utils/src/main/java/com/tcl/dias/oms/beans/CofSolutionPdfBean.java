package com.tcl.dias.oms.beans;

import java.util.List;

public class CofSolutionPdfBean {

	private String procductName;
	private String serviceVariant;
	private String lastMileString;
	private String resiliency;
	private String cpeString;
	private String portBandwidth;

	private List<CofSiteBean> singleInternetAccessSiteDetailsList;
	private List<CofSiteBean> managedSingleInternetAccessSiteDetailsList;
	private List<CofSiteBean> managedWithBackupSiteDetailsList;
	/**
	 * @return the procductName
	 */
	public String getProcductName() {
		return procductName;
	}
	/**
	 * @param procductName the procductName to set
	 */
	public void setProcductName(String procductName) {
		this.procductName = procductName;
	}
	/**
	 * @return the serviceVariant
	 */
	public String getServiceVariant() {
		return serviceVariant;
	}
	/**
	 * @param serviceVariant the serviceVariant to set
	 */
	public void setServiceVariant(String serviceVariant) {
		this.serviceVariant = serviceVariant;
	}
	/**
	 * @return the lastMileString
	 */
	public String getLastMileString() {
		return lastMileString;
	}
	/**
	 * @param lastMileString the lastMileString to set
	 */
	public void setLastMileString(String lastMileString) {
		this.lastMileString = lastMileString;
	}
	/**
	 * @return the resiliency
	 */
	public String getResiliency() {
		return resiliency;
	}
	/**
	 * @param resiliency the resiliency to set
	 */
	public void setResiliency(String resiliency) {
		this.resiliency = resiliency;
	}
	/**
	 * @return the cpeString
	 */
	public String getCpeString() {
		return cpeString;
	}
	/**
	 * @param cpeString the cpeString to set
	 */
	public void setCpeString(String cpeString) {
		this.cpeString = cpeString;
	}
	/**
	 * @return the portBandwidth
	 */
	public String getPortBandwidth() {
		return portBandwidth;
	}
	/**
	 * @param portBandwidth the portBandwidth to set
	 */
	public void setPortBandwidth(String portBandwidth) {
		this.portBandwidth = portBandwidth;
	}
	/**
	 * @return the singleInternetAccessSiteDetailsList
	 */
	public List<CofSiteBean> getSingleInternetAccessSiteDetailsList() {
		return singleInternetAccessSiteDetailsList;
	}
	/**
	 * @param singleInternetAccessSiteDetailsList the singleInternetAccessSiteDetailsList to set
	 */
	public void setSingleInternetAccessSiteDetailsList(List<CofSiteBean> singleInternetAccessSiteDetailsList) {
		this.singleInternetAccessSiteDetailsList = singleInternetAccessSiteDetailsList;
	}
	/**
	 * @return the managedSingleInternetAccessSiteDetailsList
	 */
	public List<CofSiteBean> getManagedSingleInternetAccessSiteDetailsList() {
		return managedSingleInternetAccessSiteDetailsList;
	}
	/**
	 * @param managedSingleInternetAccessSiteDetailsList the managedSingleInternetAccessSiteDetailsList to set
	 */
	public void setManagedSingleInternetAccessSiteDetailsList(
			List<CofSiteBean> managedSingleInternetAccessSiteDetailsList) {
		this.managedSingleInternetAccessSiteDetailsList = managedSingleInternetAccessSiteDetailsList;
	}
	/**
	 * @return the managedWithBackupSiteDetailsList
	 */
	public List<CofSiteBean> getManagedWithBackupSiteDetailsList() {
		return managedWithBackupSiteDetailsList;
	}
	/**
	 * @param managedWithBackupSiteDetailsList the managedWithBackupSiteDetailsList to set
	 */
	public void setManagedWithBackupSiteDetailsList(List<CofSiteBean> managedWithBackupSiteDetailsList) {
		this.managedWithBackupSiteDetailsList = managedWithBackupSiteDetailsList;
	}
	
	

}
