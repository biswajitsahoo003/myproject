package com.tcl.dias.oms.pdf.beans;

import java.util.List;

import com.tcl.dias.oms.beans.SubcomponentLineItems;

/**
 * This file contains the IllSiteCommercial.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class IllSiteCommercial {
	private String serviceType;
	private String serviceFlavor;
	private String offeringName;
	private String speed;
	private Boolean isCpe = false;
	private String subTotalMRC;
	private String subTotalNRC;
	private String subTotalARC;
	private Boolean isInternetPort = false;
	private Double internetPortMRC = 0D;
	private Double internetPortNRC = 0D;
	private Double internetPortARC = 0D;
	private Double cpeMRC = 0D;
	private Double cpeNRC = 0D;
	private Double cpeARC = 0D;
	private Boolean isLastMile = false;
	private String lastMileSpeed;
	private Double lastMileMRC = 0D;
	private Double lastMileNRC = 0D;
	private Double lastMileARC = 0D;
	private Boolean isadditionalIP = false;
	private Double additionalIPMRC = 0D;
	private Double additionalIPNRC = 0D;
	private Double additionalIPARC = 0D;
	private Double bustableBandwidthCharge = 0D;
	
	private Boolean isMastCost = false;
	private Boolean isSecondaryMastCost = false;

	private Double mastCostMRC = 0D;
	private Double mastCostNRC = 0D;
	private Double mastCostARC = 0D;

	private Double secondaryMastCostMRC = 0D;
	private Double secondaryMastCostNRC = 0D;
	private Double secondaryMastCostARC = 0D;

	private Boolean isShiftingCharge= false;
	private Double shiftingChargeMRC = 0D;
	private Double shiftingChargeNRC = 0D;
	private Double shiftingChargeARC = 0D;

	private String shiftingChargeARCFormatted;
	private String shiftingChargeMRCFormatted;
	private String shiftingChargeNRCFormatted;

	private String cpeARCFormatted;
	private String cpeNRCFormatted;
	private String cpeMRCFormatted;

	private String internetPortNRCFormatted ;
	private String internetPortARCFormatted;
	private String internetPortMRCFormatted ;

	private String lastMileNRCFormatted;
	private String lastMileARCFormatted;
	private String lastMileMRCFormatted;

	private String additionalIPMRCFormatted;
	private String additionalIPNRCFormatted;
	private String additionalIPARCFormatted;

	private Boolean isSecondaryInternetPort = false;
	private Boolean isSecondaryLastMile = false;
	private Boolean isSecondaryCpe = false;

	private String secondarySpeed;

	private Double secondaryInternetPortMRC = 0D;
	private Double secondaryInternetPortNRC = 0D;
	private Double secondaryInternetPortARC = 0D;

	private String secondaryLastMileSpeed;

	private Double secondaryLastMileMRC = 0D;
	private Double secondaryLastMileNRC = 0D;
	private Double secondaryLastMileARC = 0D;

	private String secondaryLastMileMRCFormatted;
	private String secondaryLastMileNRCFormatted;
	private String secondaryLastMileARCFormatted;

	private String secondaryInternetPortMRCFormatted;
	private String secondaryInternetPortNRCFormatted;
	private String secondaryInternetPortARCFormatted;

	private Double secondaryCpeMRC = 0D;
	private Double secondaryCpeNRC = 0D;
	private Double secondaryCpeARC = 0D;

	private String secondaryCpeNRCFormatted;
	private String secondaryCpeMRCFormatted;
	private String secondaryCpeARCFormatted;
	private String mastCostNRCFormatted;
	private String mastCostARCFormatted;
	
	 private String internetPortChargeableItem;
	 private String internetPortSecondaryChargeableItem;
	  private String lastMileChargeableItem;
	  private String lastMileSecondaryChargeableItem;
	  private String shiftingChargeChargeableItem;
	  private String additionalIPChargeableItem;
	  private String mastCostChargeableItem;
	  private String burstableChargeableItem;
	  private String hsnCode;

	//------Multi Circuit------//
	private String serviceId;
	private String primaryServiceId;
	private String secondaryServiceId;
	private String linkType;


	private List<SubcomponentLineItems> cpeLineItemsPrimary;
	private List<SubcomponentLineItems> cpeLineItemsSecondary;
	
	  //Cross Connect related
	  private Double crossConnectARC=0D;
	  private Double crossConnectNRC=0D;
	  private Double fiberEntryARC=0D;
	  private Double fiberEntryNRC=0D;
	  private String crossConnectARCFormatted;
	  private String crossConnectNRCFormatted;
	  private String fiberEntryARCFormatted;
	  private String fiberEntryNRCFormatted;
	  private  Boolean isCrossConnect=false;
	  private  Boolean isFiberEntry=false;
	  private String serialNumberCount;



	  private Boolean showMainCpe = false;
	private Boolean showMainSecondaryCpe = false;

	private Byte isColo = 0;


	//pipf-224
	private Double secondaryShiftingChargeMRC = 0D;
	private Double secondaryShiftingChargeNRC = 0D;
	private Double secondaryShiftingChargeARC = 0D;
	private String secondaryShiftingChargeMRCFormatted;
	private String secondaryShiftingChargeNRCFormatted;
	private String secondaryShiftingChargeARCFormatted;
	private String secondaryShiftingChargeChargeableItem;
	private Boolean secondaryShitingFlag=false;



	public Boolean getIsCrossConnect() {
		return isCrossConnect;
	}

	public void setIsCrossConnect(Boolean crossConnect) {
		isCrossConnect = crossConnect;
	}

	public Boolean getIsFiberEntry() {
		return isFiberEntry;
	}

	public void setIsFiberEntry(Boolean fiberEntry) {
		isFiberEntry = fiberEntry;
	}

	public String getCrossConnectARCFormatted() {
		return crossConnectARCFormatted;
	}

	public void setCrossConnectARCFormatted(String crossConnectARCFormatted) {
		this.crossConnectARCFormatted = crossConnectARCFormatted;
	}

	public String getCrossConnectNRCFormatted() {
		return crossConnectNRCFormatted;
	}

	public void setCrossConnectNRCFormatted(String crossConnectNRCFormatted) {
		this.crossConnectNRCFormatted = crossConnectNRCFormatted;
	}

	public String getFiberEntryARCFormatted() {
		return fiberEntryARCFormatted;
	}

	public void setFiberEntryARCFormatted(String fiberEntryARCFormatted) {
		this.fiberEntryARCFormatted = fiberEntryARCFormatted;
	}

	public String getFiberEntryNRCFormatted() {
		return fiberEntryNRCFormatted;
	}

	public void setFiberEntryNRCFormatted(String fiberEntryNRCFormatted) {
		this.fiberEntryNRCFormatted = fiberEntryNRCFormatted;
	}





	public Double getCrossConnectARC() {
		return crossConnectARC;
	}

	public void setCrossConnectARC(Double crossConnectARC) {
		this.crossConnectARC = crossConnectARC;
	}

	public Double getCrossConnectNRC() {
		return crossConnectNRC;
	}

	public void setCrossConnectNRC(Double crossConnectNRC) {
		this.crossConnectNRC = crossConnectNRC;
	}

	public Double getFiberEntryARC() {
		return fiberEntryARC;
	}

	public void setFiberEntryARC(Double fiberEntryARC) {
		this.fiberEntryARC = fiberEntryARC;
	}

	public Double getFiberEntryNRC() {
		return fiberEntryNRC;
	}

	public void setFiberEntryNRC(Double fiberEntryNRC) {
		this.fiberEntryNRC = fiberEntryNRC;
	}

	public String getSecondarySpeed() {
		return secondarySpeed;
	}

	public void setSecondarySpeed(String secondarySpeed) {
		this.secondarySpeed = secondarySpeed;
	}

	public String getSecondaryLastMileSpeed() {
		return secondaryLastMileSpeed;
	}

	public void setSecondaryLastMileSpeed(String secondaryLastMileSpeed) {
		this.secondaryLastMileSpeed = secondaryLastMileSpeed;
	}

	public Boolean getIsSecondaryCpe() {
		return isSecondaryCpe;
	}

	public void setIsSecondaryCpe(Boolean secondaryCpe) {
		isSecondaryCpe = secondaryCpe;
	}

	public Boolean getSecondaryInternetPort() {
		return isSecondaryInternetPort;
	}

	public void setSecondaryInternetPort(Boolean secondaryInternetPort) {
		isSecondaryInternetPort = secondaryInternetPort;
	}

	public Boolean getSecondaryLastMile() {
		return isSecondaryLastMile;
	}

	public void setSecondaryLastMile(Boolean secondaryLastMile) {
		isSecondaryLastMile = secondaryLastMile;
	}

	public Double getSecondaryCpeMRC() {
		return secondaryCpeMRC;
	}

	public void setSecondaryCpeMRC(Double secondaryCpeMRC) {
		this.secondaryCpeMRC = secondaryCpeMRC;
	}

	public Double getSecondaryCpeNRC() {
		return secondaryCpeNRC;
	}

	public void setSecondaryCpeNRC(Double secondaryCpeNRC) {
		this.secondaryCpeNRC = secondaryCpeNRC;
	}

	public Double getSecondaryCpeARC() {
		return secondaryCpeARC;
	}

	public void setSecondaryCpeARC(Double secondaryCpeARC) {
		this.secondaryCpeARC = secondaryCpeARC;
	}

	public String getSecondaryCpeNRCFormatted() {
		return secondaryCpeNRCFormatted;
	}

	public void setSecondaryCpeNRCFormatted(String secondaryCpeNRCFormatted) {
		this.secondaryCpeNRCFormatted = secondaryCpeNRCFormatted;
	}

	public String getSecondaryCpeMRCFormatted() {
		return secondaryCpeMRCFormatted;
	}

	public void setSecondaryCpeMRCFormatted(String secondaryCpeMRCFormatted) {
		this.secondaryCpeMRCFormatted = secondaryCpeMRCFormatted;
	}

	public String getSecondaryCpeARCFormatted() {
		return secondaryCpeARCFormatted;
	}

	public void setSecondaryCpeARCFormatted(String secondaryCpeARCFormatted) {
		this.secondaryCpeARCFormatted = secondaryCpeARCFormatted;
	}

	public String getSecondaryInternetPortMRCFormatted() {
		return secondaryInternetPortMRCFormatted;
	}

	public void setSecondaryInternetPortMRCFormatted(String secondaryInternetPortMRCFormatted) {
		this.secondaryInternetPortMRCFormatted = secondaryInternetPortMRCFormatted;
	}

	public String getSecondaryInternetPortNRCFormatted() {
		return secondaryInternetPortNRCFormatted;
	}

	public void setSecondaryInternetPortNRCFormatted(String secondaryInternetPortNRCFormatted) {
		this.secondaryInternetPortNRCFormatted = secondaryInternetPortNRCFormatted;
	}

	public String getSecondaryInternetPortARCFormatted() {
		return secondaryInternetPortARCFormatted;
	}

	public void setSecondaryInternetPortARCFormatted(String secondaryInternetPortARCFormatted) {
		this.secondaryInternetPortARCFormatted = secondaryInternetPortARCFormatted;
	}


	public String getSecondaryLastMileMRCFormatted() {
		return secondaryLastMileMRCFormatted;
	}

	public void setSecondaryLastMileMRCFormatted(String secondaryLastMileMRCFormatted) {
		this.secondaryLastMileMRCFormatted = secondaryLastMileMRCFormatted;
	}

	public String getSecondaryLastMileNRCFormatted() {
		return secondaryLastMileNRCFormatted;
	}

	public void setSecondaryLastMileNRCFormatted(String secondaryLastMileNRCFormatted) {
		this.secondaryLastMileNRCFormatted = secondaryLastMileNRCFormatted;
	}

	public String getSecondaryLastMileARCFormatted() {
		return secondaryLastMileARCFormatted;
	}

	public void setSecondaryLastMileARCFormatted(String secondaryLastMileARCFormatted) {
		this.secondaryLastMileARCFormatted = secondaryLastMileARCFormatted;
	}

	public Double getSecondaryLastMileMRC() {
		return secondaryLastMileMRC;
	}

	public void setSecondaryLastMileMRC(Double secondaryLastMileMRC) {
		this.secondaryLastMileMRC = secondaryLastMileMRC;
	}

	public Double getSecondaryLastMileNRC() {
		return secondaryLastMileNRC;
	}

	public void setSecondaryLastMileNRC(Double secondaryLastMileNRC) {
		this.secondaryLastMileNRC = secondaryLastMileNRC;
	}

	public Double getSecondaryLastMileARC() {
		return secondaryLastMileARC;
	}

	public void setSecondaryLastMileARC(Double secondaryLastMileARC) {
		this.secondaryLastMileARC = secondaryLastMileARC;
	}

	public Double getSecondaryInternetPortMRC() {
		return secondaryInternetPortMRC;
	}

	public void setSecondaryInternetPortMRC(Double secondaryInternetPortMRC) {
		this.secondaryInternetPortMRC = secondaryInternetPortMRC;
	}

	public Double getSecondaryInternetPortNRC() {
		return secondaryInternetPortNRC;
	}

	public void setSecondaryInternetPortNRC(Double secondaryInternetPortNRC) {
		this.secondaryInternetPortNRC = secondaryInternetPortNRC;
	}

	public Double getSecondaryInternetPortARC() {
		return secondaryInternetPortARC;
	}

	public void setSecondaryInternetPortARC(Double secondaryInternetPortARC) {
		this.secondaryInternetPortARC = secondaryInternetPortARC;
	}


	public Boolean getIsSecondaryInternetPort() {
		return isSecondaryInternetPort;
	}

	public void setIsSecondaryInternetPort(Boolean secondaryInternetPort) {
		isSecondaryInternetPort = secondaryInternetPort;
	}

	public Boolean getIsSecondaryLastMile() {
		return isSecondaryLastMile;
	}

	public void setIsSecondaryLastMile(Boolean secondaryLastMile) {
		isSecondaryLastMile = secondaryLastMile;
	}



	public String getInternetPortARCFormatted() {
		return internetPortARCFormatted;
	}

	public void setInternetPortARCFormatted(String internetPortARCFormatted) {
		this.internetPortARCFormatted = internetPortARCFormatted;
	}

	public String getInternetPortNRCFormatted() {
		return internetPortNRCFormatted;
	}

	public void setInternetPortNRCFormatted(String internetPortNRCFormatted) {
		this.internetPortNRCFormatted = internetPortNRCFormatted;
	}

	public String getCpeARCFormatted() {
		return cpeARCFormatted;
	}

	public void setCpeARCFormatted(String cpeARCFormatted) {
		this.cpeARCFormatted = cpeARCFormatted;
	}

	public String getCpeNRCFormatted() {
		return cpeNRCFormatted;
	}

	public void setCpeNRCFormatted(String cpeNRCFormatted) {
		this.cpeNRCFormatted = cpeNRCFormatted;
	}

	public String getCpeMRCFormatted() {
		return cpeMRCFormatted;
	}

	public void setCpeMRCFormatted(String cpeMRCFormatted) {
		this.cpeMRCFormatted = cpeMRCFormatted;
	}

	public String getLastMileMRCFormatted() {
		return lastMileMRCFormatted;
	}

	public void setLastMileMRCFormatted(String lastMileMRCFormatted) {
		this.lastMileMRCFormatted = lastMileMRCFormatted;
	}

	public String getLastMileNRCFormatted() {
		return lastMileNRCFormatted;
	}

	public void setLastMileNRCFormatted(String lastMileNRCFormatted) {
		this.lastMileNRCFormatted = lastMileNRCFormatted;
	}

	public String getLastMileARCFormatted() {
		return lastMileARCFormatted;
	}

	public void setLastMileARCFormatted(String lastMileARCFormatted) {
		this.lastMileARCFormatted = lastMileARCFormatted;
	}

	public String getAdditionalIPMRCFormatted() {
		return additionalIPMRCFormatted;
	}

	public void setAdditionalIPMRCFormatted(String additionalIPMRCFormatted) {
		this.additionalIPMRCFormatted = additionalIPMRCFormatted;
	}

	public String getAdditionalIPNRCFormatted() {
		return additionalIPNRCFormatted;
	}

	public void setAdditionalIPNRCFormatted(String additionalIPNRCFormatted) {
		this.additionalIPNRCFormatted = additionalIPNRCFormatted;
	}

	public String getAdditionalIPARCFormatted() {
		return additionalIPARCFormatted;
	}

	public void setAdditionalIPARCFormatted(String additionalIPARCFormatted) {
		this.additionalIPARCFormatted = additionalIPARCFormatted;
	}

	public String getInternetPortMRCFormatted() {
		return internetPortMRCFormatted;
	}

	public void setInternetPortMRCFormatted(String internetPortMRCFormatted) {
		this.internetPortMRCFormatted = internetPortMRCFormatted;
	}

	
	private String mastCostMRCFormatted;
	public String getMastCostMRCFormatted() {
		return mastCostMRCFormatted;
	}

	public void setMastCostMRCFormatted(String mastCostMRCFormatted) {
		this.mastCostMRCFormatted = mastCostMRCFormatted;
	}

	public String getMastCostNRCFormatted() {
		return mastCostNRCFormatted;
	}

	public void setMastCostNRCFormatted(String mastCostNRCFormatted) {
		this.mastCostNRCFormatted = mastCostNRCFormatted;
	}

	public String getMastCostARCFormatted() {
		return mastCostARCFormatted;
	}

	public void setMastCostARCFormatted(String mastCostARCFormatted) {
		this.mastCostARCFormatted = mastCostARCFormatted;
	}

	public String getShiftingChargeMRCFormatted() {
		return shiftingChargeMRCFormatted;
	}

	public void setShiftingChargeMRCFormatted(String shiftingChargeMRCFormatted) {
		this.shiftingChargeMRCFormatted = shiftingChargeMRCFormatted;
	}

	public String getShiftingChargeNRCFormatted() {
		return shiftingChargeNRCFormatted;
	}

	public void setShiftingChargeNRCFormatted(String shiftingChargeNRCFormatted) {
		this.shiftingChargeNRCFormatted = shiftingChargeNRCFormatted;
	}

	public String getShiftingChargeARCFormatted() {
		return shiftingChargeARCFormatted;
	}

	public void setShiftingChargeARCFormatted(String shiftingChargeARCFormatted) {
		this.shiftingChargeARCFormatted = shiftingChargeARCFormatted;
	}



	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceFlavor() {
		return serviceFlavor;
	}

	public void setServiceFlavor(String serviceFlavor) {
		this.serviceFlavor = serviceFlavor;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public Boolean getIsCpe() {
		return isCpe;
	}

	public void setIsCpe(Boolean isCpe) {
		this.isCpe = isCpe;
	}

	public String getSubTotalMRC() {
		return subTotalMRC;
	}

	public void setSubTotalMRC(String subTotalMRC) {
		this.subTotalMRC = subTotalMRC;
	}

	public String getSubTotalNRC() {
		return subTotalNRC;
	}

	public void setSubTotalNRC(String subTotalNRC) {
		this.subTotalNRC = subTotalNRC;
	}

	public Boolean getIsInternetPort() {
		return isInternetPort;
	}

	public void setIsInternetPort(Boolean isInternetPort) {
		this.isInternetPort = isInternetPort;
	}

	public Double getInternetPortMRC() {
		return internetPortMRC;
	}

	public void setInternetPortMRC(Double internetPortMRC) {
		this.internetPortMRC = internetPortMRC;
	}

	public Double getInternetPortNRC() {
		return internetPortNRC;
	}

	public void setInternetPortNRC(Double internetPortNRC) {
		this.internetPortNRC = internetPortNRC;
	}

	public Double getCpeMRC() {
		return cpeMRC;
	}

	public void setCpeMRC(Double cpeMRC) {
		this.cpeMRC = cpeMRC;
	}

	public Double getCpeNRC() {
		return cpeNRC;
	}

	public void setCpeNRC(Double cpeNRC) {
		this.cpeNRC = cpeNRC;
	}

	public Boolean getIsLastMile() {
		return isLastMile;
	}

	public void setIsLastMile(Boolean isLastMile) {
		this.isLastMile = isLastMile;
	}

	public Double getLastMileMRC() {
		return lastMileMRC;
	}

	public void setLastMileMRC(Double lastMileMRC) {
		this.lastMileMRC = lastMileMRC;
	}

	public Double getLastMileNRC() {
		return lastMileNRC;
	}

	public void setLastMileNRC(Double lastMileNRC) {
		this.lastMileNRC = lastMileNRC;
	}

	public Boolean getIsadditionalIP() {
		return isadditionalIP;
	}

	public void setIsadditionalIP(Boolean isadditionalIP) {
		this.isadditionalIP = isadditionalIP;
	}

	public Double getAdditionalIPMRC() {
		return additionalIPMRC;
	}

	public void setAdditionalIPMRC(Double additionalIPMRC) {
		this.additionalIPMRC = additionalIPMRC;
	}

	public Double getAdditionalIPNRC() {
		return additionalIPNRC;
	}

	public void setAdditionalIPNRC(Double additionalIPNRC) {
		this.additionalIPNRC = additionalIPNRC;
	}

	/**
	 * @return the lastMileSpeed
	 */
	public String getLastMileSpeed() {
		return lastMileSpeed;
	}

	/**
	 * @param lastMileSpeed
	 *            the lastMileSpeed to set
	 */
	public void setLastMileSpeed(String lastMileSpeed) {
		this.lastMileSpeed = lastMileSpeed;
	}

	public String getSubTotalARC() {
		return subTotalARC;
	}

	public void setSubTotalARC(String subTotalARC) {
		this.subTotalARC = subTotalARC;
	}

	public Double getInternetPortARC() {
		return internetPortARC;
	}

	public void setInternetPortARC(Double internetPortARC) {
		this.internetPortARC = internetPortARC;
	}

	public Double getLastMileARC() {
		return lastMileARC;
	}

	public void setLastMileARC(Double lastMileARC) {
		this.lastMileARC = lastMileARC;
	}

	public Double getAdditionalIPARC() {
		return additionalIPARC;
	}

	public void setAdditionalIPARC(Double additionalIPARC) {
		this.additionalIPARC = additionalIPARC;
	}

	public Double getCpeARC() {
		return cpeARC;
	}

	public void setCpeARC(Double cpeARC) {
		this.cpeARC = cpeARC;
	}

	public Double getBustableBandwidthCharge() {
		return bustableBandwidthCharge;
	}

	public void setBustableBandwidthCharge(Double bustableBandwidthCharge) {
		this.bustableBandwidthCharge = bustableBandwidthCharge;
	}

	public Boolean getIsMastCost() {
		return isMastCost;
	}

	public void setIsMastCost(Boolean isMastCost) {
		this.isMastCost = isMastCost;
	}

	public Boolean getIsSecondaryMastCost() {
		return isSecondaryMastCost;
	}

	public void setIsSecondaryMastCost(Boolean isSecondaryMastCost) {
		this.isSecondaryMastCost = isSecondaryMastCost;
	}

	public Double getMastCostMRC() {
		return mastCostMRC;
	}

	public void setMastCostMRC(Double mastCostMRC) {
		this.mastCostMRC = mastCostMRC;
	}

	public Double getMastCostNRC() {
		return mastCostNRC;
	}

	public void setMastCostNRC(Double mastCostNRC) {
		this.mastCostNRC = mastCostNRC;
	}

	public Double getMastCostARC() {
		return mastCostARC;
	}

	public void setMastCostARC(Double mastCostARC) {
		this.mastCostARC = mastCostARC;
	}

	public Double getSecondaryMastCostMRC() {
		return secondaryMastCostMRC;
	}

	public void setSecondaryMastCostMRC(Double secondaryMastCostMRC) {
		this.secondaryMastCostMRC = secondaryMastCostMRC;
	}

	public Double getSecondaryMastCostNRC() {
		return secondaryMastCostNRC;
	}

	public void setSecondaryMastCostNRC(Double secondaryMastCostNRC) {
		this.secondaryMastCostNRC = secondaryMastCostNRC;
	}

	public Double getSecondaryMastCostARC() {
		return secondaryMastCostARC;
	}

	public void setSecondaryMastCostARC(Double secondaryMastCostARC) {
		this.secondaryMastCostARC = secondaryMastCostARC;
	}

	public Double getShiftingChargeMRC() {
		return shiftingChargeMRC;
	}

	public void setShiftingChargeMRC(Double shiftingChargeMRC) {
		this.shiftingChargeMRC = shiftingChargeMRC;
	}

	public Double getShiftingChargeNRC() {
		return shiftingChargeNRC;
	}

	public void setShiftingChargeNRC(Double shiftingChargeNRC) {
		this.shiftingChargeNRC = shiftingChargeNRC;
	}

	public Double getShiftingChargeARC() {
		return shiftingChargeARC;
	}

	public void setShiftingChargeARC(Double shiftingChargeARC) {
		this.shiftingChargeARC = shiftingChargeARC;
	}

	public Boolean getIsShiftingCharge() {
		return isShiftingCharge;
	}
	/**
	 * @param isShiftingCharge the isShiftingCharge to set
	 */
	public void setIsShiftingCharge(Boolean isShiftingCharge) {
		this.isShiftingCharge = isShiftingCharge;
	}

	public String getInternetPortChargeableItem() {
		return internetPortChargeableItem;
	}

	public void setInternetPortChargeableItem(String internetPortChargeableItem) {
		this.internetPortChargeableItem = internetPortChargeableItem;
	}

	public String getLastMileChargeableItem() {
		return lastMileChargeableItem;
	}

	public void setLastMileChargeableItem(String lastMileChargeableItem) {
		this.lastMileChargeableItem = lastMileChargeableItem;
	}

	public String getShiftingChargeChargeableItem() {
		return shiftingChargeChargeableItem;
	}

	public void setShiftingChargeChargeableItem(String shiftingChargeChargeableItem) {
		this.shiftingChargeChargeableItem = shiftingChargeChargeableItem;
	}

	public String getAdditionalIPChargeableItem() {
		return additionalIPChargeableItem;
	}

	public void setAdditionalIPChargeableItem(String additionalIPChargeableItem) {
		this.additionalIPChargeableItem = additionalIPChargeableItem;
	}

	public String getMastCostChargeableItem() {
		return mastCostChargeableItem;
	}

	public void setMastCostChargeableItem(String mastCostChargeableItem) {
		this.mastCostChargeableItem = mastCostChargeableItem;
	}

	public String getBurstableChargeableItem() {
		return burstableChargeableItem;
	}

	public void setBurstableChargeableItem(String burstableChargeableItem) {
		this.burstableChargeableItem = burstableChargeableItem;
	}


	public List<SubcomponentLineItems> getCpeLineItemsPrimary() {
		return cpeLineItemsPrimary;
	}

	public void setCpeLineItemsPrimary(List<SubcomponentLineItems> cpeLineItemsPrimary) {
		this.cpeLineItemsPrimary = cpeLineItemsPrimary;
	}

	public List<SubcomponentLineItems> getCpeLineItemsSecondary() {
		return cpeLineItemsSecondary;
	}

	public void setCpeLineItemsSecondary(List<SubcomponentLineItems> cpeLineItemsSecondary) {
		this.cpeLineItemsSecondary = cpeLineItemsSecondary;
	}

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}
	
	

	public String getInternetPortSecondaryChargeableItem() {
		return internetPortSecondaryChargeableItem;
	}

	public void setInternetPortSecondaryChargeableItem(String internetPortSecondaryChargeableItem) {
		this.internetPortSecondaryChargeableItem = internetPortSecondaryChargeableItem;
	}

	public String getLastMileSecondaryChargeableItem() {
		return lastMileSecondaryChargeableItem;
	}

	public void setLastMileSecondaryChargeableItem(String lastMileSecondaryChargeableItem) {
		this.lastMileSecondaryChargeableItem = lastMileSecondaryChargeableItem;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getPrimaryServiceId() {
		return primaryServiceId;
	}

	public void setPrimaryServiceId(String primaryServiceId) {
		this.primaryServiceId = primaryServiceId;
	}

	public String getSecondaryServiceId() {
		return secondaryServiceId;
	}

	public void setSecondaryServiceId(String secondaryServiceId) {
		this.secondaryServiceId = secondaryServiceId;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public Boolean getShowMainCpe() {
		return showMainCpe;
	}

	public void setShowMainCpe(Boolean showMainCpe) {
		this.showMainCpe = showMainCpe;
	}

	public Boolean getShowMainSecondaryCpe() {
		return showMainSecondaryCpe;
	}

	public void setShowMainSecondaryCpe(Boolean showMainSecondaryCpe) {
		this.showMainSecondaryCpe = showMainSecondaryCpe;
	}

	public String getSerialNumberCount() {
		return serialNumberCount;
	}

	public void setSerialNumberCount(String serialNumberCount) {
		this.serialNumberCount = serialNumberCount;
	}

	public Byte getIsColo() {
		return isColo;
	}

	public void setIsColo(Byte isColo) {
		this.isColo = isColo;
	}


	public Double getSecondaryShiftingChargeMRC() {
		return secondaryShiftingChargeMRC;
	}

	public void setSecondaryShiftingChargeMRC(Double secondaryShiftingChargeMRC) {
		this.secondaryShiftingChargeMRC = secondaryShiftingChargeMRC;
	}

	public Double getSecondaryShiftingChargeNRC() {
		return secondaryShiftingChargeNRC;
	}

	public void setSecondaryShiftingChargeNRC(Double secondaryShiftingChargeNRC) {
		this.secondaryShiftingChargeNRC = secondaryShiftingChargeNRC;
	}

	public Double getSecondaryShiftingChargeARC() {
		return secondaryShiftingChargeARC;
	}

	public void setSecondaryShiftingChargeARC(Double secondaryShiftingChargeARC) {
		this.secondaryShiftingChargeARC = secondaryShiftingChargeARC;
	}

	public String getSecondaryShiftingChargeMRCFormatted() {
		return secondaryShiftingChargeMRCFormatted;
	}

	public void setSecondaryShiftingChargeMRCFormatted(String secondaryShiftingChargeMRCFormatted) {
		this.secondaryShiftingChargeMRCFormatted = secondaryShiftingChargeMRCFormatted;
	}

	public String getSecondaryShiftingChargeNRCFormatted() {
		return secondaryShiftingChargeNRCFormatted;
	}

	public void setSecondaryShiftingChargeNRCFormatted(String secondaryShiftingChargeNRCFormatted) {
		this.secondaryShiftingChargeNRCFormatted = secondaryShiftingChargeNRCFormatted;
	}

	public String getSecondaryShiftingChargeARCFormatted() {
		return secondaryShiftingChargeARCFormatted;
	}

	public void setSecondaryShiftingChargeARCFormatted(String secondaryShiftingChargeARCFormatted) {
		this.secondaryShiftingChargeARCFormatted = secondaryShiftingChargeARCFormatted;
	}

	public String getSecondaryShiftingChargeChargeableItem() {
		return secondaryShiftingChargeChargeableItem;
	}

	public void setSecondaryShiftingChargeChargeableItem(String secondaryShiftingChargeChargeableItem) {
		this.secondaryShiftingChargeChargeableItem = secondaryShiftingChargeChargeableItem;
	}

	public Boolean getSecondaryShitingFlag() {
		return secondaryShitingFlag;
	}

	public void setSecondaryShitingFlag(Boolean secondaryShitingFlag) {
		this.secondaryShitingFlag = secondaryShitingFlag;
	}

	public String getOfferingName() {
		return offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	@Override
	public String toString() {
		return "IllSiteCommercial{" +
				"serviceType='" + serviceType + '\'' +
				", speed='" + speed + '\'' +
				", isCpe=" + isCpe +
				", subTotalMRC='" + subTotalMRC + '\'' +
				", subTotalNRC='" + subTotalNRC + '\'' +
				", subTotalARC='" + subTotalARC + '\'' +
				", isInternetPort=" + isInternetPort +
				", internetPortMRC=" + internetPortMRC +
				", internetPortNRC=" + internetPortNRC +
				", internetPortARC=" + internetPortARC +
				", cpeMRC=" + cpeMRC +
				", cpeNRC=" + cpeNRC +
				", cpeARC=" + cpeARC +
				", isLastMile=" + isLastMile +
				", lastMileSpeed='" + lastMileSpeed + '\'' +
				", lastMileMRC=" + lastMileMRC +
				", lastMileNRC=" + lastMileNRC +
				", lastMileARC=" + lastMileARC +
				", isadditionalIP=" + isadditionalIP +
				", additionalIPMRC=" + additionalIPMRC +
				", additionalIPNRC=" + additionalIPNRC +
				", additionalIPARC=" + additionalIPARC +
				", bustableBandwidthCharge=" + bustableBandwidthCharge +
				", isMastCost=" + isMastCost +
				", isSecondaryMastCost=" + isSecondaryMastCost +
				", mastCostMRC=" + mastCostMRC +
				", mastCostNRC=" + mastCostNRC +
				", mastCostARC=" + mastCostARC +
				", secondaryMastCostMRC=" + secondaryMastCostMRC +
				", secondaryMastCostNRC=" + secondaryMastCostNRC +
				", secondaryMastCostARC=" + secondaryMastCostARC +
				", isShiftingCharge=" + isShiftingCharge +
				", shiftingChargeMRC=" + shiftingChargeMRC +
				", shiftingChargeNRC=" + shiftingChargeNRC +
				", shiftingChargeARC=" + shiftingChargeARC +
				", shiftingChargeARCFormatted='" + shiftingChargeARCFormatted + '\'' +
				", shiftingChargeMRCFormatted='" + shiftingChargeMRCFormatted + '\'' +
				", shiftingChargeNRCFormatted='" + shiftingChargeNRCFormatted + '\'' +
				", cpeARCFormatted='" + cpeARCFormatted + '\'' +
				", cpeNRCFormatted='" + cpeNRCFormatted + '\'' +
				", cpeMRCFormatted='" + cpeMRCFormatted + '\'' +
				", internetPortNRCFormatted='" + internetPortNRCFormatted + '\'' +
				", internetPortARCFormatted='" + internetPortARCFormatted + '\'' +
				", internetPortMRCFormatted='" + internetPortMRCFormatted + '\'' +
				", lastMileNRCFormatted='" + lastMileNRCFormatted + '\'' +
				", lastMileARCFormatted='" + lastMileARCFormatted + '\'' +
				", lastMileMRCFormatted='" + lastMileMRCFormatted + '\'' +
				", additionalIPMRCFormatted='" + additionalIPMRCFormatted + '\'' +
				", additionalIPNRCFormatted='" + additionalIPNRCFormatted + '\'' +
				", additionalIPARCFormatted='" + additionalIPARCFormatted + '\'' +
				", isSecondaryInternetPort=" + isSecondaryInternetPort +
				", isSecondaryLastMile=" + isSecondaryLastMile +
				", isSecondaryCpe=" + isSecondaryCpe +
				", secondarySpeed='" + secondarySpeed + '\'' +
				", secondaryInternetPortMRC=" + secondaryInternetPortMRC +
				", secondaryInternetPortNRC=" + secondaryInternetPortNRC +
				", secondaryInternetPortARC=" + secondaryInternetPortARC +
				", secondaryLastMileSpeed='" + secondaryLastMileSpeed + '\'' +
				", secondaryLastMileMRC=" + secondaryLastMileMRC +
				", secondaryLastMileNRC=" + secondaryLastMileNRC +
				", secondaryLastMileARC=" + secondaryLastMileARC +
				", secondaryLastMileMRCFormatted='" + secondaryLastMileMRCFormatted + '\'' +
				", secondaryLastMileNRCFormatted='" + secondaryLastMileNRCFormatted + '\'' +
				", secondaryLastMileARCFormatted='" + secondaryLastMileARCFormatted + '\'' +
				", secondaryInternetPortMRCFormatted='" + secondaryInternetPortMRCFormatted + '\'' +
				", secondaryInternetPortNRCFormatted='" + secondaryInternetPortNRCFormatted + '\'' +
				", secondaryInternetPortARCFormatted='" + secondaryInternetPortARCFormatted + '\'' +
				", secondaryCpeMRC=" + secondaryCpeMRC +
				", secondaryCpeNRC=" + secondaryCpeNRC +
				", secondaryCpeARC=" + secondaryCpeARC +
				", secondaryCpeNRCFormatted='" + secondaryCpeNRCFormatted + '\'' +
				", secondaryCpeMRCFormatted='" + secondaryCpeMRCFormatted + '\'' +
				", secondaryCpeARCFormatted='" + secondaryCpeARCFormatted + '\'' +
				", mastCostNRCFormatted='" + mastCostNRCFormatted + '\'' +
				", mastCostARCFormatted='" + mastCostARCFormatted + '\'' +
				", internetPortChargeableItem='" + internetPortChargeableItem + '\'' +
				", internetPortSecondaryChargeableItem='" + internetPortSecondaryChargeableItem + '\'' +
				", lastMileChargeableItem='" + lastMileChargeableItem + '\'' +
				", lastMileSecondaryChargeableItem='" + lastMileSecondaryChargeableItem + '\'' +
				", shiftingChargeChargeableItem='" + shiftingChargeChargeableItem + '\'' +
				", additionalIPChargeableItem='" + additionalIPChargeableItem + '\'' +
				", mastCostChargeableItem='" + mastCostChargeableItem + '\'' +
				", burstableChargeableItem='" + burstableChargeableItem + '\'' +
				", hsnCode='" + hsnCode + '\'' +
				", serviceId='" + serviceId + '\'' +
				", primaryServiceId='" + primaryServiceId + '\'' +
				", secondaryServiceId='" + secondaryServiceId + '\'' +
				", linkType='" + linkType + '\'' +
				", cpeLineItemsPrimary=" + cpeLineItemsPrimary +
				", cpeLineItemsSecondary=" + cpeLineItemsSecondary +
				", crossConnectARC=" + crossConnectARC +
				", crossConnectNRC=" + crossConnectNRC +
				", fiberEntryARC=" + fiberEntryARC +
				", fiberEntryNRC=" + fiberEntryNRC +
				", crossConnectARCFormatted='" + crossConnectARCFormatted + '\'' +
				", crossConnectNRCFormatted='" + crossConnectNRCFormatted + '\'' +
				", fiberEntryARCFormatted='" + fiberEntryARCFormatted + '\'' +
				", fiberEntryNRCFormatted='" + fiberEntryNRCFormatted + '\'' +
				", isCrossConnect=" + isCrossConnect +
				", isFiberEntry=" + isFiberEntry +
				", serialNumberCount='" + serialNumberCount + '\'' +
				", showMainCpe=" + showMainCpe +
				", showMainSecondaryCpe=" + showMainSecondaryCpe +
				", isColo=" + isColo +
				", secondaryShiftingChargeMRC=" + secondaryShiftingChargeMRC +
				", secondaryShiftingChargeNRC=" + secondaryShiftingChargeNRC +
				", secondaryShiftingChargeARC=" + secondaryShiftingChargeARC +
				", secondaryShiftingChargeMRCFormatted='" + secondaryShiftingChargeMRCFormatted + '\'' +
				", secondaryShiftingChargeNRCFormatted='" + secondaryShiftingChargeNRCFormatted + '\'' +
				", secondaryShiftingChargeARCFormatted='" + secondaryShiftingChargeARCFormatted + '\'' +
				", secondaryShiftingChargeChargeableItem='" + secondaryShiftingChargeChargeableItem + '\'' +
				", secondaryShitingFlag=" + secondaryShitingFlag +
				", mastCostMRCFormatted='" + mastCostMRCFormatted + '\'' +
				'}';
	}
}
