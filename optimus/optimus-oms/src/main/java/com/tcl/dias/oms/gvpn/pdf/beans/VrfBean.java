package com.tcl.dias.oms.gvpn.pdf.beans;

import java.util.ArrayList;
import java.util.List;

public class VrfBean {
	
	private Double totalArc;
	private Double totalNrc;
	private String billingType;
	private String multiVrfSolution;
	List<GvpnMultiVrfCofAnnexureBean> gvpnMultiVrfCofAnnexureBean;
    private Boolean checkForBillingAndVrf = false;
    private Boolean cpeCheckBasedOnProfile = false;
    private Boolean isShiftingCharges = false;


	public VrfBean() {
		this.gvpnMultiVrfCofAnnexureBean = new ArrayList<GvpnMultiVrfCofAnnexureBean>();
	}
	public Double getTotalArc() {
		return totalArc;
	}
	public void setTotalArc(Double totalArc) {
		this.totalArc = totalArc;
	}
	public Double getTotalNrc() {
		return totalNrc;
	}
	public void setTotalNrc(Double totalNrc) {
		this.totalNrc = totalNrc;
	}
	public String getBillingType() {
		return billingType;
	}
	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}
	public String getMultiVrfSolution() {
		return multiVrfSolution;
	}
	public void setMultiVrfSolution(String multiVrfSolution) {
		this.multiVrfSolution = multiVrfSolution;
	}
	public List<GvpnMultiVrfCofAnnexureBean> getGvpnMultiVrfCofAnnexureBean() {
		return gvpnMultiVrfCofAnnexureBean;
	}
	public void setGvpnMultiVrfCofAnnexureBean(List<GvpnMultiVrfCofAnnexureBean> gvpnMultiVrfCofAnnexureBean) {
		this.gvpnMultiVrfCofAnnexureBean = gvpnMultiVrfCofAnnexureBean;
	}
	public Boolean getCheckForBillingAndVrf() {
		return checkForBillingAndVrf;
	}
	public void setCheckForBillingAndVrf(Boolean checkForBillingAndVrf) {
		this.checkForBillingAndVrf = checkForBillingAndVrf;
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
	
	
	
	

}
