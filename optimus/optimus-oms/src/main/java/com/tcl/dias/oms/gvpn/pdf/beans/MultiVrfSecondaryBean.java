package com.tcl.dias.oms.gvpn.pdf.beans;

import java.util.ArrayList;
/**
 * 
 * This file contains the MultiVrfSecondaryBean.java class.
 * 
 *
 * @author NITHYA S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
import java.util.List;

public class MultiVrfSecondaryBean {

	private String localLoopBandwidth;
	private String totalPortSize;
	private Double lmChargesArc;
    private Double lmChargesNrc;
    private Double cpeChargesArc;
    private Double cpeChargesNrc;
    private Double shiftingChargesArc;
    private Double shiftingChargesNrc;
    private Double subTotalArc;
    private Double subTotalNrc;
    private List<MultiVrfBean> multiVrfBeanList;
    private Boolean isLmCharges = false;
    private Boolean isCpeCharges = false;
    private Boolean isShiftingCharges = false;
    private Integer rowSpan;
   
    
    public MultiVrfSecondaryBean() {
    	this.multiVrfBeanList = new ArrayList<MultiVrfBean>();
    }
    
	public String getLocalLoopBandwidth() {
		return localLoopBandwidth;
	}
	public void setLocalLoopBandwidth(String localLoopBandwidth) {
		this.localLoopBandwidth = localLoopBandwidth;
	}
	public Double getLmChargesArc() {
		return lmChargesArc;
	}
	public void setLmChargesArc(Double lmChargesArc) {
		this.lmChargesArc = lmChargesArc;
	}
	public Double getLmChargesNrc() {
		return lmChargesNrc;
	}
	public void setLmChargesNrc(Double lmChargesNrc) {
		this.lmChargesNrc = lmChargesNrc;
	}
	public Double getCpeChargesArc() {
		return cpeChargesArc;
	}
	public void setCpeChargesArc(Double cpeChargesArc) {
		this.cpeChargesArc = cpeChargesArc;
	}
	public Double getCpeChargesNrc() {
		return cpeChargesNrc;
	}
	public void setCpeChargesNrc(Double cpeChargesNrc) {
		this.cpeChargesNrc = cpeChargesNrc;
	}
	public List<MultiVrfBean> getMultiVrfBeanList() {
		return multiVrfBeanList;
	}
	public void setMultiVrfBeanList(List<MultiVrfBean> multiVrfBeanList) {
		this.multiVrfBeanList = multiVrfBeanList;
	}
	public Double getSubTotalArc() {
		return subTotalArc;
	}
	public void setSubTotalArc(Double subTotalArc) {
		this.subTotalArc = subTotalArc;
	}
	public Double getSubTotalNrc() {
		return subTotalNrc;
	}
	public void setSubTotalNrc(Double subTotalNrc) {
		this.subTotalNrc = subTotalNrc;
	}

	public String getTotalPortSize() {
		return totalPortSize;
	}

	public void setTotalPortSize(String totalPortSize) {
		this.totalPortSize = totalPortSize;
	}

	public Boolean getIsLmCharges() {
		return isLmCharges;
	}

	public void setIsLmCharges(Boolean isLmCharges) {
		this.isLmCharges = isLmCharges;
	}

	public Boolean getIsCpeCharges() {
		return isCpeCharges;
	}

	public void setIsCpeCharges(Boolean isCpeCharges) {
		this.isCpeCharges = isCpeCharges;
	}

	

	public Integer getRowSpan() {
		return rowSpan;
	}

	public void setRowSpan(Integer rowSpan) {
		this.rowSpan = rowSpan;
	}

	public Double getShiftingChargesArc() {
		return shiftingChargesArc;
	}

	public void setShiftingChargesArc(Double shiftingChargesArc) {
		this.shiftingChargesArc = shiftingChargesArc;
	}

	public Double getShiftingChargesNrc() {
		return shiftingChargesNrc;
	}

	public void setShiftingChargesNrc(Double shiftingChargesNrc) {
		this.shiftingChargesNrc = shiftingChargesNrc;
	}

	public Boolean getIsShiftingCharges() {
		return isShiftingCharges;
	}

	public void setIsShiftingCharges(Boolean isShiftingCharges) {
		this.isShiftingCharges = isShiftingCharges;
	}

	@Override
	public String toString() {
		return "MultiVrfSecondaryBean [localLoopBandwidth=" + localLoopBandwidth + ", totalPortSize=" + totalPortSize
				+ ", lmChargesArc=" + lmChargesArc + ", lmChargesNrc=" + lmChargesNrc + ", cpeChargesArc="
				+ cpeChargesArc + ", cpeChargesNrc=" + cpeChargesNrc + ", subTotalArc=" + subTotalArc + ", subTotalNrc="
				+ subTotalNrc + ", multiVrfBeanList=" + multiVrfBeanList + "]";
	}

	
}
