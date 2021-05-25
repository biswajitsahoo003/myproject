package com.tcl.dias.oms.beans;

import java.util.List;

public class CofPdfBean {

	private List<CofSiteBean> singleInternetAccessList;

	private List<CofSiteBean> managedSingleInternetAccessList;

	private List<CofSiteBean> managedWithBackupList;

	private Long recurringCharges;

	private Long onetimeCharges;

	private Long totalContractValue;

	private Long singleInternetAccessTotalMRC;

	private Long singleInternetAccessTotalNRC;

	private Long managedSingleInternetAccessTotalMRC;

	private Long managedSingleInternetAccessTotalNRC;

	private Long managedWithBackupTotalMRC;

	private Long managedWithBackupTotalNRC;

	private Integer noOfChargeableItemsSingleInternetAccess; // to track the row merge count for S.No
	private Integer noOfChargeableItemsManagedSingleInternetAccess;
	private Integer noOfChargeableItemsManagedWithBackup;
	
	private SolutionBean solutionBean;



	/**
	 * @return the singleInternetAccessList
	 */
	public List<CofSiteBean> getSingleInternetAccessList() {
		return singleInternetAccessList;
	}

	/**
	 * @param singleInternetAccessList
	 *            the singleInternetAccessList to set
	 */
	public void setSingleInternetAccessList(List<CofSiteBean> singleInternetAccessList) {
		this.singleInternetAccessList = singleInternetAccessList;
	}

	/**
	 * @return the managedSingleInternetAccessList
	 */
	public List<CofSiteBean> getManagedSingleInternetAccessList() {
		return managedSingleInternetAccessList;
	}

	/**
	 * @param managedSingleInternetAccessList
	 *            the managedSingleInternetAccessList to set
	 */
	public void setManagedSingleInternetAccessList(List<CofSiteBean> managedSingleInternetAccessList) {
		this.managedSingleInternetAccessList = managedSingleInternetAccessList;
	}

	/**
	 * @return the managedWithBackupList
	 */
	public List<CofSiteBean> getManagedWithBackupList() {
		return managedWithBackupList;
	}

	/**
	 * @param managedWithBackupList
	 *            the managedWithBackupList to set
	 */
	public void setManagedWithBackupList(List<CofSiteBean> managedWithBackupList) {
		this.managedWithBackupList = managedWithBackupList;
	}

	/**
	 * @return the recurringCharges
	 */
	public Long getRecurringCharges() {
		return recurringCharges;
	}

	/**
	 * @param recurringCharges
	 *            the recurringCharges to set
	 */
	public void setRecurringCharges(Long recurringCharges) {
		this.recurringCharges = recurringCharges;
	}

	/**
	 * @return the onetomeCharges
	 */
	public Long getOnetimeCharges() {
		return onetimeCharges;
	}

	/**
	 * @return the totalContractValue
	 */
	public Long getTotalContractValue() {
		return totalContractValue;
	}

	/**
	 * @param totalContractValue
	 *            the totalContractValue to set
	 */
	public void setTotalContractValue(Long totalContractValue) {
		this.totalContractValue = totalContractValue;
	}

	/**
	 * @return the singleInternetAccessTotalMRC
	 */
	public Long getSingleInternetAccessTotalMRC() {
		return singleInternetAccessTotalMRC;
	}

	/**
	 * @param singleInternetAccessTotalMRC
	 *            the singleInternetAccessTotalMRC to set
	 */
	public void setSingleInternetAccessTotalMRC(Long singleInternetAccessTotalMRC) {
		this.singleInternetAccessTotalMRC = singleInternetAccessTotalMRC;
	}

	/**
	 * @return the singleInternetAccessTotalNRC
	 */
	public Long getSingleInternetAccessTotalNRC() {
		return singleInternetAccessTotalNRC;
	}

	/**
	 * @param singleInternetAccessTotalNRC
	 *            the singleInternetAccessTotalNRC to set
	 */
	public void setSingleInternetAccessTotalNRC(Long singleInternetAccessTotalNRC) {
		this.singleInternetAccessTotalNRC = singleInternetAccessTotalNRC;
	}

	/**
	 * @return the managedSingleInternetAccessTotalMRC
	 */
	public Long getManagedSingleInternetAccessTotalMRC() {
		return managedSingleInternetAccessTotalMRC;
	}

	/**
	 * @param managedSingleInternetAccessTotalMRC
	 *            the managedSingleInternetAccessTotalMRC to set
	 */
	public void setManagedSingleInternetAccessTotalMRC(Long managedSingleInternetAccessTotalMRC) {
		this.managedSingleInternetAccessTotalMRC = managedSingleInternetAccessTotalMRC;
	}

	/**
	 * @return the managedSingleInternetAccessTotalNRC
	 */
	public Long getManagedSingleInternetAccessTotalNRC() {
		return managedSingleInternetAccessTotalNRC;
	}

	/**
	 * @param managedSingleInternetAccessTotalNRC
	 *            the managedSingleInternetAccessTotalNRC to set
	 */
	public void setManagedSingleInternetAccessTotalNRC(Long managedSingleInternetAccessTotalNRC) {
		this.managedSingleInternetAccessTotalNRC = managedSingleInternetAccessTotalNRC;
	}

	/**
	 * @return the managedWithBackupTotalMRC
	 */
	public Long getManagedWithBackupTotalMRC() {
		return managedWithBackupTotalMRC;
	}

	/**
	 * @param managedWithBackupTotalMRC
	 *            the managedWithBackupTotalMRC to set
	 */
	public void setManagedWithBackupTotalMRC(Long managedWithBackupTotalMRC) {
		this.managedWithBackupTotalMRC = managedWithBackupTotalMRC;
	}

	/**
	 * @return the managedWithBackupTotalNRC
	 */
	public Long getManagedWithBackupTotalNRC() {
		return managedWithBackupTotalNRC;
	}

	/**
	 * @param managedWithBackupTotalNRC
	 *            the managedWithBackupTotalNRC to set
	 */
	public void setManagedWithBackupTotalNRC(Long managedWithBackupTotalNRC) {
		this.managedWithBackupTotalNRC = managedWithBackupTotalNRC;
	}

	/**
	 * @param onetimeCharges
	 *            the onetimeCharges to set
	 */
	public void setOnetimeCharges(Long onetimeCharges) {
		this.onetimeCharges = onetimeCharges;
	}

	/**
	 * @return the noOfChargeableItemsSingleInternetAccess
	 */
	public Integer getNoOfChargeableItemsSingleInternetAccess() {
		return noOfChargeableItemsSingleInternetAccess;
	}

	/**
	 * @param noOfChargeableItemsSingleInternetAccess
	 *            the noOfChargeableItemsSingleInternetAccess to set
	 */
	public void setNoOfChargeableItemsSingleInternetAccess(Integer noOfChargeableItemsSingleInternetAccess) {
		this.noOfChargeableItemsSingleInternetAccess = noOfChargeableItemsSingleInternetAccess;
	}

	/**
	 * @return the noOfChargeableItemsManagedSingleInternetAccess
	 */
	public Integer getNoOfChargeableItemsManagedSingleInternetAccess() {
		return noOfChargeableItemsManagedSingleInternetAccess;
	}

	/**
	 * @param noOfChargeableItemsManagedSingleInternetAccess
	 *            the noOfChargeableItemsManagedSingleInternetAccess to set
	 */
	public void setNoOfChargeableItemsManagedSingleInternetAccess(
			Integer noOfChargeableItemsManagedSingleInternetAccess) {
		this.noOfChargeableItemsManagedSingleInternetAccess = noOfChargeableItemsManagedSingleInternetAccess;
	}

	/**
	 * @return the noOfChargeableItemsManagedWithBackup
	 */
	public Integer getNoOfChargeableItemsManagedWithBackup() {
		return noOfChargeableItemsManagedWithBackup;
	}

	/**
	 * @param noOfChargeableItemsManagedWithBackup
	 *            the noOfChargeableItemsManagedWithBackup to set
	 */
	public void setNoOfChargeableItemsManagedWithBackup(Integer noOfChargeableItemsManagedWithBackup) {
		this.noOfChargeableItemsManagedWithBackup = noOfChargeableItemsManagedWithBackup;
	}

	public SolutionBean getSolutionBean() {
		return solutionBean;
	}

	public void setSolutionBean(SolutionBean solutionBean) {
		this.solutionBean = solutionBean;
	}
	
	
}
