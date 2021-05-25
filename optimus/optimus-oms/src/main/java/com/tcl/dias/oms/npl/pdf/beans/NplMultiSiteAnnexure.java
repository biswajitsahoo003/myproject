package com.tcl.dias.oms.npl.pdf.beans;

import java.util.List;
/**
 * 
 * This file contains the NplMultiSiteAnnexure.java class.
 * 
 *
 * @author NITHYA S
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
public class NplMultiSiteAnnexure {

	private List<NplSitewiseBillingAnnexure> nplSitewiseBillingAnnexure;
	private String totalArc;
	private String totalOtc;
	
	public List<NplSitewiseBillingAnnexure> getNplSitewiseBillingAnnexure() {
		return nplSitewiseBillingAnnexure;
	}
	public void setNplSitewiseBillingAnnexure(List<NplSitewiseBillingAnnexure> nplSitewiseBillingAnnexure) {
		this.nplSitewiseBillingAnnexure = nplSitewiseBillingAnnexure;
	}
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
	@Override
	public String toString() {
		return "NplMultiSiteAnnexure [nplSitewiseBillingAnnexure=" + nplSitewiseBillingAnnexure + ", totalArc="
				+ totalArc + ", totalOtc=" + totalOtc + "]";
	}
	
}
