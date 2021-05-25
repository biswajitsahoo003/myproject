package com.tcl.dias.oms.gvpn.pdf.beans;

/**
 * 
 * This file contains the GvpnMultiVrfCofAnnexureBean.java class.
 * 
 *
 * @author NITHYA S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GvpnMultiVrfCofAnnexureBean {

	private String siteAddress;
	private MultiVrfPrimaryBean multiVrfPrimaryBean;
	private MultiVrfSecondaryBean multiVrfSecondaryBean;
	private Integer primaryNoOfVrfs;
	private Integer secondaryNoOfVrfs;
	private Integer countForPrimaryRowSpan;
	private Boolean isSecondary = false;
    private Boolean checkForBillingAndVrf = false;
	
	public GvpnMultiVrfCofAnnexureBean() {
		this.multiVrfPrimaryBean = new MultiVrfPrimaryBean();
		this.multiVrfSecondaryBean = new MultiVrfSecondaryBean();
	}
	
	public String getSiteAddress() {
		return siteAddress;
	}
	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}
	public MultiVrfPrimaryBean getMultiVrfPrimaryBean() {
		return multiVrfPrimaryBean;
	}
	public void setMultiVrfPrimaryBean(MultiVrfPrimaryBean multiVrfPrimaryBean) {
		this.multiVrfPrimaryBean = multiVrfPrimaryBean;
	}
	public MultiVrfSecondaryBean getMultiVrfSecondaryBean() {
		return multiVrfSecondaryBean;
	}
	public void setMultiVrfSecondaryBean(MultiVrfSecondaryBean multiVrfSecondaryBean) {
		this.multiVrfSecondaryBean = multiVrfSecondaryBean;
	}
	public Integer getPrimaryNoOfVrfs() {
		return primaryNoOfVrfs;
	}
	public void setPrimaryNoOfVrfs(Integer primaryNoOfVrfs) {
		this.primaryNoOfVrfs = primaryNoOfVrfs;
	}
	public Integer getSecondaryNoOfVrfs() {
		return secondaryNoOfVrfs;
	}
	public void setSecondaryNoOfVrfs(Integer secondaryNoOfVrfs) {
		this.secondaryNoOfVrfs = secondaryNoOfVrfs;
	}
	public Integer getCountForPrimaryRowSpan() {
		return countForPrimaryRowSpan;
	}
	public void setCountForPrimaryRowSpan(Integer countForPrimaryRowSpan) {
		this.countForPrimaryRowSpan = countForPrimaryRowSpan;
	}
	public Boolean getIsSecondary() {
		return isSecondary;
	}
	public void setIsSecondary(Boolean isSecondary) {
		this.isSecondary = isSecondary;
	}
	public Boolean getCheckForBillingAndVrf() {
		return checkForBillingAndVrf;
	}
	public void setCheckForBillingAndVrf(Boolean checkForBillingAndVrf) {
		this.checkForBillingAndVrf = checkForBillingAndVrf;
	}

	
}
