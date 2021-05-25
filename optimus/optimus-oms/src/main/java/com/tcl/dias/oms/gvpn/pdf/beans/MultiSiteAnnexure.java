package com.tcl.dias.oms.gvpn.pdf.beans;

import java.util.List;

/**
 * 
 * This file contains the MultiSiteAnnexure.java class.
 * 
 *
 * @author NITHYA S
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
public class MultiSiteAnnexure {

	private String totalArc;
	private String totalOtc;
	List<SitewiseBillingAnnexureBean> sitewiseBillingAnnexureBean;
	private Boolean cpeCheckBasedOnProfile = false;
	private Boolean isShiftingCharges = false;
	
	public String getTotalArc() {
		return totalArc;
	}
	public void setTotalArc(String totalArc) {
		this.totalArc = totalArc;
	}
	public String getTotalOtc() {
		return totalOtc;
	}
	public void setTotalOtc(String totalOtc) {
		this.totalOtc = totalOtc;
	}
	public List<SitewiseBillingAnnexureBean> getSitewiseBillingAnnexureBean() {
		return sitewiseBillingAnnexureBean;
	}
	public void setSitewiseBillingAnnexureBean(List<SitewiseBillingAnnexureBean> sitewiseBillingAnnexureBean) {
		this.sitewiseBillingAnnexureBean = sitewiseBillingAnnexureBean;
	}
	public Boolean getCpeCheckBasedOnProfile() {
		return cpeCheckBasedOnProfile;
	}
	public void setCpeCheckBasedOnProfile(Boolean cpeCheckBasedOnProfile) {
		this.cpeCheckBasedOnProfile = cpeCheckBasedOnProfile;
	}
	public Boolean getIsShiftingCharges() {
		return isShiftingCharges;
	}
	public void setIsShiftingCharges(Boolean isShiftingCharges) {
		this.isShiftingCharges = isShiftingCharges;
	}
	@Override
	public String toString() {
		return "MultiSiteAnnexure [totalArc=" + totalArc + ", totalOtc=" + totalOtc + ", sitewiseBillingAnnexureBean="
				+ sitewiseBillingAnnexureBean + ", cpeCheckBasedOnProfile=" + cpeCheckBasedOnProfile
				+ ", isShiftingCharges=" + isShiftingCharges + "]";
	}
	
	
}
