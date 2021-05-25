package com.tcl.dias.oms.gvpn.pdf.beans;

/**
 * 
 * This file contains the SitewiseAnnexureSecondary.java class.
 * 
 *
 * @author NITHYA S
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
public class SecondarySite {

	private Double portArc;
	private Double portNrc;
	private Double lastMileArc;
	private Double lastMileNrc;
	private Double cpeArc;
	private Double cpeNrc;
	private Double shiftingArc;
	private Double shiftingNrc;
	private Double additionalIpArc;
	private Double additionalIpNrc;
	private Double subTotalArc;
	private Double subTotalNrc;
	private String portBandwidth;
	private String mastHeight;
	private String llProvider;
	private Integer rowSpan;
	private Boolean isPortCharges = false;
    private Boolean isLmCharges = false;
    private Boolean isCpeCharges = false;
    private Boolean isShiftingCharges = false;
    private Boolean isAdditionalIpCharges = false;

	public Double getPortArc() {
		return portArc;
	}
	public void setPortArc(Double portArc) {
		this.portArc = portArc;
	}
	public Double getPortNrc() {
		return portNrc;
	}
	public void setPortNrc(Double portNrc) {
		this.portNrc = portNrc;
	}
	public Double getLastMileArc() {
		return lastMileArc;
	}
	public void setLastMileArc(Double lastMileArc) {
		this.lastMileArc = lastMileArc;
	}
	public Double getLastMileNrc() {
		return lastMileNrc;
	}
	public void setLastMileNrc(Double lastMileNrc) {
		this.lastMileNrc = lastMileNrc;
	}
	public Double getCpeArc() {
		return cpeArc;
	}
	public void setCpeArc(Double cpeArc) {
		this.cpeArc = cpeArc;
	}
	public Double getCpeNrc() {
		return cpeNrc;
	}
	public void setCpeNrc(Double cpeNrc) {
		this.cpeNrc = cpeNrc;
	}
	public Double getShiftingArc() {
		return shiftingArc;
	}
	public void setShiftingArc(Double shiftingArc) {
		this.shiftingArc = shiftingArc;
	}
	public Double getShiftingNrc() {
		return shiftingNrc;
	}
	public void setShiftingNrc(Double shiftingNrc) {
		this.shiftingNrc = shiftingNrc;
	}
	public Double getAdditionalIpArc() {
		return additionalIpArc;
	}
	public void setAdditionalIpArc(Double additionalIpArc) {
		this.additionalIpArc = additionalIpArc;
	}
	public Double getAdditionalIpNrc() {
		return additionalIpNrc;
	}
	public void setAdditionalIpNrc(Double additionalIpNrc) {
		this.additionalIpNrc = additionalIpNrc;
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
	public String getPortBandwidth() {
		return portBandwidth;
	}
	public void setPortBandwidth(String portBandwidth) {
		this.portBandwidth = portBandwidth;
	}
	public String getMastHeight() {
		return mastHeight;
	}
	public void setMastHeight(String mastHeight) {
		this.mastHeight = mastHeight;
	}
	public String getLlProvider() {
		return llProvider;
	}
	public void setLlProvider(String llProvider) {
		this.llProvider = llProvider;
	}
	public Integer getRowSpan() {
		return rowSpan;
	}
	public void setRowSpan(Integer rowSpan) {
		this.rowSpan = rowSpan;
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
	public Boolean getIsShiftingCharges() {
		return isShiftingCharges;
	}
	public void setIsShiftingCharges(Boolean isShiftingCharges) {
		this.isShiftingCharges = isShiftingCharges;
	}
	public Boolean getIsAdditionalIpCharges() {
		return isAdditionalIpCharges;
	}
	public void setIsAdditionalIpCharges(Boolean isAdditionalIpCharges) {
		this.isAdditionalIpCharges = isAdditionalIpCharges;
	}
	public Boolean getIsPortCharges() {
		return isPortCharges;
	}
	public void setIsPortCharges(Boolean isPortCharges) {
		this.isPortCharges = isPortCharges;
	}

	@Override
	public String toString() {
		return "SecondarySite [portArc=" + portArc + ", portNrc=" + portNrc + ", lastMileArc=" + lastMileArc
				+ ", lastMileNrc=" + lastMileNrc + ", cpeArc=" + cpeArc + ", cpeNrc=" + cpeNrc + ", shiftingArc="
				+ shiftingArc + ", shiftingNrc=" + shiftingNrc + ", additionalIpArc=" + additionalIpArc
				+ ", additionalIpNrc=" + additionalIpNrc + ", subTotalArc=" + subTotalArc + ", subTotalNrc="
				+ subTotalNrc + ", portBandwidth=" + portBandwidth + ", mastHeight=" + mastHeight + ", llProvider="
				+ llProvider + ", rowSpan=" + rowSpan + ", isLmCharges=" + isLmCharges + ", isCpeCharges="
				+ isCpeCharges + ", isShiftingCharges=" + isShiftingCharges + "]";
	}
	
	
	
}
