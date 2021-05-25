package com.tcl.dias.oms.npl.pdf.beans;

/**
 * This file contains the NplCommercial.java class.
 * 
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class NplLinkCommercial {
	private String serviceType;
	private String speed;
	private String chargeableDistance;
	private String subTotalARC;
	private String subTotalNRC;
	private Boolean isConnectivity = false;
	private Double connectivityARC = 0D;
	private Double connectivityNRC = 0D;
	private Boolean isLastMile = false;
	private Double lastMileARC = 0D;
	private Double lastMileNRC = 0D;
	private Double linkMgmtChargesARC;
	private Double linkMgmtChargesNRC;
	private Double linkMgmtChargesMRC;
	
	// Attributes holds comma separated - formatted currency values.

	private String connectivityARCFormatted;
	private String connectivityNRCFormatted;
	private String linkMgmtChargesARCFormatted;
	private String linkMgmtChargesNRCFormatted;
	private String linkMgmtChargesMRCFormatted;

	private Boolean isShiftingCharge= false;
	private Double shiftingChargeMRC = 0D;
	private Double shiftingChargeNRC = 0D;
	private Double shiftingChargeARC = 0D;

	private String shiftingChargeMRCFormatted;
	private String shiftingChargeNRCFormatted;
	private String shiftingChargeARCFormatted;

	private String shiftingChargeChargeableItem;
	
	
	private String hsnCode;
	
	private String serviceId;
	
	
	

	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getHsnCode() {
		return hsnCode;
	}
	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}
	public String getLinkMgmtChargesARCFormatted() {
		return linkMgmtChargesARCFormatted;
	}
	public void setLinkMgmtChargesARCFormatted(String linkMgmtChargesARCFormatted) {
		this.linkMgmtChargesARCFormatted = linkMgmtChargesARCFormatted;
	}
	public String getLinkMgmtChargesNRCFormatted() {
		return linkMgmtChargesNRCFormatted;
	}
	public void setLinkMgmtChargesNRCFormatted(String linkMgmtChargesNRCFormatted) {
		this.linkMgmtChargesNRCFormatted = linkMgmtChargesNRCFormatted;
	}
	public String getLinkMgmtChargesMRCFormatted() {
		return linkMgmtChargesMRCFormatted;
	}
	public void setLinkMgmtChargesMRCFormatted(String linkMgmtChargesMRCFormatted) {
		this.linkMgmtChargesMRCFormatted = linkMgmtChargesMRCFormatted;
	}
	public String getConnectivityARCFormatted() {
		return connectivityARCFormatted;
	}
	public void setConnectivityARCFormatted(String connectivityARCFormatted) {
		this.connectivityARCFormatted = connectivityARCFormatted;
	}
	public String getConnectivityNRCFormatted() {
		return connectivityNRCFormatted;
	}
	public void setConnectivityNRCFormatted(String connectivityNRCFormatted) {
		this.connectivityNRCFormatted = connectivityNRCFormatted;
	}
	public Double getLinkMgmtChargesMRC() {
		return linkMgmtChargesMRC;
	}
	public void setLinkMgmtChargesMRC(Double linkMgmtChargesMRC) {
		this.linkMgmtChargesMRC = linkMgmtChargesMRC;
	}
	private Boolean isLinkMgmtCharges=false;
	
	public Boolean getIsLinkMgmtCharges() {
		return isLinkMgmtCharges;
	}
	public void setIsLinkMgmtCharges(Boolean isLinkMgmtCharges) {
		this.isLinkMgmtCharges = isLinkMgmtCharges;
	}
	public Double getLinkMgmtChargesARC() {
		return linkMgmtChargesARC;
	}
	public void setLinkMgmtChargesARC(Double linkMgmtChargesARC) {
		this.linkMgmtChargesARC = linkMgmtChargesARC;
	}
	public Double getLinkMgmtChargesNRC() {
		return linkMgmtChargesNRC;
	}
	public void setLinkMgmtChargesNRC(Double linkMgmtChargesNRC) {
		this.linkMgmtChargesNRC = linkMgmtChargesNRC;
	}
	
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public String getChargeableDistance() {
		return chargeableDistance;
	}
	public void setChargeableDistance(String chargeableDistance) {
		this.chargeableDistance = chargeableDistance;
	}
	public String getSubTotalARC() {
		return subTotalARC;
	}
	public void setSubTotalARC(String subTotalARC) {
		this.subTotalARC = subTotalARC;
	}
	public String getSubTotalNRC() {
		return subTotalNRC;
	}
	public void setSubTotalNRC(String subTotalNRC) {
		this.subTotalNRC = subTotalNRC;
	}
	public Boolean getIsConnectivity() {
		return isConnectivity;
	}
	public void setIsConnectivity(Boolean isConnectivity) {
		this.isConnectivity = isConnectivity;
	}
	public Double getConnectivityARC() {
		return connectivityARC;
	}
	public void setConnectivityARC(Double connectivityARC) {
		this.connectivityARC = connectivityARC;
	}
	public Double getConnectivityNRC() {
		return connectivityNRC;
	}
	public void setConnectivityNRC(Double connectivityNRC) {
		this.connectivityNRC = connectivityNRC;
	}
	public Boolean getIsLastMile() {
		return isLastMile;
	}
	public void setIsLastMile(Boolean isLastMile) {
		this.isLastMile = isLastMile;
	}
	public Double getLastMileARC() {
		return lastMileARC;
	}
	public void setLastMileARC(Double lastMileARC) {
		this.lastMileARC = lastMileARC;
	}
	public Double getLastMileNRC() {
		return lastMileNRC;
	}
	public void setLastMileNRC(Double lastMileNRC) {
		this.lastMileNRC = lastMileNRC;
	}

	public Boolean getIsShiftingCharge() { return isShiftingCharge; }

	public void setIsShiftingCharge(Boolean shiftingCharge) { isShiftingCharge = shiftingCharge; }

	public Double getShiftingChargeMRC() { return shiftingChargeMRC; }

	public void setShiftingChargeMRC(Double shiftingChargeMRC) { this.shiftingChargeMRC = shiftingChargeMRC; }

	public Double getShiftingChargeNRC() { return shiftingChargeNRC; }

	public void setShiftingChargeNRC(Double shiftingChargeNRC) { this.shiftingChargeNRC = shiftingChargeNRC; }

	public Double getShiftingChargeARC() { return shiftingChargeARC; }

	public void setShiftingChargeARC(Double shiftingChargeARC) { this.shiftingChargeARC = shiftingChargeARC;}

	public String getShiftingChargeMRCFormatted() { return shiftingChargeMRCFormatted; }

	public void setShiftingChargeMRCFormatted(String shiftingChargeMRCFormatted) { this.shiftingChargeMRCFormatted = shiftingChargeMRCFormatted; }

	public String getShiftingChargeNRCFormatted() { return shiftingChargeNRCFormatted; }

	public void setShiftingChargeNRCFormatted(String shiftingChargeNRCFormatted) { this.shiftingChargeNRCFormatted = shiftingChargeNRCFormatted; }

	public String getShiftingChargeARCFormatted() { return shiftingChargeARCFormatted; }

	public void setShiftingChargeARCFormatted(String shiftingChargeARCFormatted) { this.shiftingChargeARCFormatted = shiftingChargeARCFormatted; }

	public String getShiftingChargeChargeableItem() { return shiftingChargeChargeableItem; }

	public void setShiftingChargeChargeableItem(String shiftingChargeChargeableItem) { this.shiftingChargeChargeableItem = shiftingChargeChargeableItem; }
}
