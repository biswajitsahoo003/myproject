package com.tcl.dias.oms.gvpn.pdf.beans;

import java.util.List;

import com.tcl.dias.oms.beans.SubcomponentLineItems;

/**
 * 
 * This file contains the GvpnSiteCommercial.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GvpnSiteCommercial {
	private String serviceType;
	private String speed;
	private Boolean isCpe = false;
	private Double subTotalMRC;
	private Double subTotalNRC;
	private Double subTotalARC;
	private Boolean isInternetPort = false;
	private Double internetPortMRC = 0D;
	private Double internetPortNRC = 0D;
	private Double internetPortARC = 0D;

	private Double cpeMRC = 0D;
	private Double cpeNRC = 0D;
	private Double cpeARC = 0D;

	private Boolean isLastMile = false;
	private Double lastMileMRC = 0D;
	private Double lastMileNRC = 0D;
	private Double lastMileARC = 0D;

	private Boolean isMastCost = false;
	private Boolean isSecondaryMastCost = false;

	private Double mastCostMRC = 0D;
	private Double mastCostNRC = 0D;
	private Double mastCostARC = 0D;

	private Double secondaryMastCostMRC = 0D;
	private Double secondaryMastCostNRC = 0D;
	private Double secondaryMastCostARC = 0D;
	private Boolean isadditionalIP = false;
	private Double additionalIPMRC = 0D;
	private Double additionalIPNRC = 0D;
	private Double bustableBandwidthCharge = 0D;
	private String lastMileSpeed;
	private String secondaryLastMileSpeed;
	private String secondarySpeed;

	private Boolean isSecondaryInternetPort = false;
	private Boolean isSecondaryLastMile = false;

	private Double secondaryInternetPortMRC = 0D;
	private Double secondaryInternetPortNRC = 0D;
	private Double secondaryInternetPortARC = 0D;

	private Double secondaryCpeMRC = 0D;
	private Double secondaryCpeNRC = 0D;
	private Double secondaryCpeARC = 0D;

	private Boolean isSecondaryCpe = false;

	private Double secondarylastMileMRC = 0D;
	private Double secondarylastMileNRC = 0D;
	private Double secondarylastMileARC = 0D;

	private Double secondaryAdditionalIPMRC = 0D;
	private Double secondaryAadditionalIPNRC = 0D;

	private Boolean isShiftingCharge= false;
	private Double shiftingChargeMRC = 0D;
	private Double shiftingChargeNRC = 0D;
	private Double shiftingChargeARC = 0D;


	//pipf-224
	private Double secondaryShiftingChargeMRC = 0D;
	private Double secondaryShiftingChargeNRC = 0D;
	private Double secondaryShiftingChargeARC = 0D;
	private String secondaryShiftingChargeMRCFormatted;
	private String secondaryShiftingChargeNRCFormatted;
	private String secondaryShiftingChargeARCFormatted;
	private String secondaryShiftingChargeChargeableItem;
	private Boolean secondaryShitingFlag=false;


	// holds all currency based comma seperated values
	private String cpeMRCFormatted;
	private String cpeNRCFormatted;
	private String cpeARCFormatted;

  private String lastMileMRCFormatted ;
	private String lastMileNRCFormatted ;
	private String lastMileARCFormatted ;
  	
	private String mastCostMRCFormatted;
	private String mastCostNRCFormatted;
	private String mastCostARCFormatted;

  private String secondaryMastCostMRCFormatted;
	private String secondaryMastCostNRCFormatted;
	private String secondaryMastCostARCFormatted;

	private String secondaryInternetPortMRCFormatted ;
	private String secondaryInternetPortNRCFormatted ;
	private String secondaryInternetPortARCFormatted ;

	private String secondaryCpeMRCFormatted ;
	private String secondaryCpeNRCFormatted ;
	private String secondaryCpeARCFormatted ;

	private String shiftingChargeMRCFormatted;
	private String shiftingChargeNRCFormatted;
	private String shiftingChargeARCFormatted;

	private String secondarylastMileNRCFormatted;
	private String secondarylastMileARCFormatted;
	
   private String internetPortMRCFormatted;
   private String internetPortNRCFormatted;
	private String internetPortARCFormatted;
	
	private String additionalIPMRCFormatted;
	private String additionalIPNRCFormatted;
	
	private String subTotalMRCFormatted;
	private String subTotalNRCFormatted;
	private String subTotalARCFormatted;

	//------Multi Circuit------//
	private String serviceId;
	private String primaryServiceId;
	private String secondaryServiceId;
	private String linkType;
	private String hsnCode;
	private List<SubcomponentLineItems> cpeLineItemsPrimary;
	private List<SubcomponentLineItems> cpeLineItemsSecondary;
	private String internetPortChargeableItem;
	private String internetPortSecondaryChargeableItem;
	private String lastMileChargeableItem;
	private String lastMileSecondaryChargeableItem;
	private String shiftingChargeChargeableItem;
	private String additionalIPChargeableItem;
	private String mastCostChargeableItem;
	private String burstableChargeableItem;

	private Boolean showMainCpe = false;
	private Boolean showMainSecondaryCpe = false;

	//GVPN MACD INTL CPE Recovery charges
	private Boolean isCpeRecoveryCharges= false;
	private Double cpeRecoveryMRC = 0D;
	private Double cpeRecoveryNRC = 0D;
	private Double cpeRecoveryARC = 0D;
	private String cpeRecoveryChargeableItem;
	private String cpeRecoveryMRCFormatted;
	private String cpeRecoveryNRCFormatted;
	private String cpeRecoveryARCFormatted;

	public Boolean getIsShiftingCharge() {
		return isShiftingCharge;
	}

	public void setIsShiftingCharge(Boolean isShiftingCharge) {
		this.isShiftingCharge = isShiftingCharge;
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

	public String getSubTotalMRCFormatted() {
		return subTotalMRCFormatted;
	}

	public void setSubTotalMRCFormatted(String subTotalMRCFormatted) {
		this.subTotalMRCFormatted = subTotalMRCFormatted;
	}

	public String getSubTotalNRCFormatted() {
		return subTotalNRCFormatted;
	}

	public void setSubTotalNRCFormatted(String subTotalNRCFormatted) {
		this.subTotalNRCFormatted = subTotalNRCFormatted;
	}

	public String getSubTotalARCFormatted() {
		return subTotalARCFormatted;
	}

	public void setSubTotalARCFormatted(String subTotalARCFomratted) {
		this.subTotalARCFormatted = subTotalARCFomratted;
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

	public String getSecondaryMastCostMRCFormatted() {
		return secondaryMastCostMRCFormatted;
	}

	public void setSecondaryMastCostMRCFormatted(String secondaryMastCostMRCFormatted) {
		this.secondaryMastCostMRCFormatted = secondaryMastCostMRCFormatted;
	}

	public String getSecondaryMastCostNRCFormatted() {
		return secondaryMastCostNRCFormatted;
	}

	public void setSecondaryMastCostNRCFormatted(String secondaryMastCostNRCFormatted) {
		this.secondaryMastCostNRCFormatted = secondaryMastCostNRCFormatted;
	}

	public String getSecondaryMastCostARCFormatted() {
		return secondaryMastCostARCFormatted;
	}

	public void setSecondaryMastCostARCFormatted(String secondaryMastCostARCFormatted) {
		this.secondaryMastCostARCFormatted = secondaryMastCostARCFormatted;
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


	public String getSecondaryCpeMRCFormatted() {
		return secondaryCpeMRCFormatted;
	}

	public void setSecondaryCpeMRCFormatted(String secondaryCpeMRCFormatted) {
		this.secondaryCpeMRCFormatted = secondaryCpeMRCFormatted;
	}

	public String getSecondaryCpeNRCFormatted() {
		return secondaryCpeNRCFormatted;
	}

	public void setSecondaryCpeNRCFormatted(String secondaryCpeNRCFormatted) {
		this.secondaryCpeNRCFormatted = secondaryCpeNRCFormatted;
	}

	public String getSecondaryCpeARCFormatted() {
		return secondaryCpeARCFormatted;
	}

	public void setSecondaryCpeARCFormatted(String secondaryCpeARCFormatted) {
		this.secondaryCpeARCFormatted = secondaryCpeARCFormatted;
	}
		


	private String secondarylastMileMRCFormatted;
	public String getSecondarylastMileMRCFormatted() {
		return secondarylastMileMRCFormatted;
	}

	public void setSecondarylastMileMRCFormatted(String secondarylastMileMRCFormatted) {
		this.secondarylastMileMRCFormatted = secondarylastMileMRCFormatted;
	}

	public String getSecondarylastMileNRCFormatted() {
		return secondarylastMileNRCFormatted;
	}

	public void setSecondarylastMileNRCFormatted(String secondarylastMileNRCFormatted) {
		this.secondarylastMileNRCFormatted = secondarylastMileNRCFormatted;
	}

	public String getSecondarylastMileARCFormatted() {
		return secondarylastMileARCFormatted;
	}

	public void setSecondarylastMileARCFormatted(String secondarylastMileARCFormatted) {
		this.secondarylastMileARCFormatted = secondarylastMileARCFormatted;
	}

	public String getInternetPortMRCFormatted() {
		return internetPortMRCFormatted;
	}

	public void setInternetPortMRCFormatted(String internetPortMRCFormatted) {
		this.internetPortMRCFormatted = internetPortMRCFormatted;
	}

	public String getInternetPortNRCFormatted() {
		return internetPortNRCFormatted;
	}

	public void setInternetPortNRCFormatted(String internetPortNRCFormatted) {
		this.internetPortNRCFormatted = internetPortNRCFormatted;
	}

	public String getInternetPortARCFormatted() {
		return internetPortARCFormatted;
	}

	public void setInternetPortARCFormatted(String internetPortARCFormatted) {
		this.internetPortARCFormatted = internetPortARCFormatted;
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

	public Boolean getIsCpe() {
		return isCpe;
	}

	public void setIsCpe(Boolean isCpe) {
		this.isCpe = isCpe;
	}

	public Double getSubTotalMRC() {
		return subTotalMRC;
	}

	public void setSubTotalMRC(Double subTotalMRC) {
		this.subTotalMRC = subTotalMRC;
	}

	public Double getSubTotalNRC() {
		return subTotalNRC;
	}

	public void setSubTotalNRC(Double subTotalNRC) {
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
	 * @param lastMileSpeed the lastMileSpeed to set
	 */
	public void setLastMileSpeed(String lastMileSpeed) {
		this.lastMileSpeed = lastMileSpeed;
	}

	/**
	 * @return the secondaryInternetPortMRC
	 */
	public Double getSecondaryInternetPortMRC() {
		return secondaryInternetPortMRC;
	}

	/**
	 * @param secondaryInternetPortMRC the secondaryInternetPortMRC to set
	 */
	public void setSecondaryInternetPortMRC(Double secondaryInternetPortMRC) {
		this.secondaryInternetPortMRC = secondaryInternetPortMRC;
	}

	/**
	 * @return the secondaryInternetPortNRC
	 */
	public Double getSecondaryInternetPortNRC() {
		return secondaryInternetPortNRC;
	}

	/**
	 * @param secondaryInternetPortNRC the secondaryInternetPortNRC to set
	 */
	public void setSecondaryInternetPortNRC(Double secondaryInternetPortNRC) {
		this.secondaryInternetPortNRC = secondaryInternetPortNRC;
	}

	/**
	 * @return the secondaryCpeMRC
	 */
	public Double getSecondaryCpeMRC() {
		return secondaryCpeMRC;
	}

	/**
	 * @param secondaryCpeMRC the secondaryCpeMRC to set
	 */
	public void setSecondaryCpeMRC(Double secondaryCpeMRC) {
		this.secondaryCpeMRC = secondaryCpeMRC;
	}

	/**
	 * @return the secondaryCpeNRC
	 */
	public Double getSecondaryCpeNRC() {
		return secondaryCpeNRC;
	}

	/**
	 * @param secondaryCpeNRC the secondaryCpeNRC to set
	 */
	public void setSecondaryCpeNRC(Double secondaryCpeNRC) {
		this.secondaryCpeNRC = secondaryCpeNRC;
	}

	/**
	 * @return the secondarylastMileMRC
	 */
	public Double getSecondarylastMileMRC() {
		return secondarylastMileMRC;
	}

	/**
	 * @param secondarylastMileMRC the secondarylastMileMRC to set
	 */
	public void setSecondarylastMileMRC(Double secondarylastMileMRC) {
		this.secondarylastMileMRC = secondarylastMileMRC;
	}

	/**
	 * @return the secondarylastMileNRC
	 */
	public Double getSecondarylastMileNRC() {
		return secondarylastMileNRC;
	}

	/**
	 * @param secondarylastMileNRC the secondarylastMileNRC to set
	 */
	public void setSecondarylastMileNRC(Double secondarylastMileNRC) {
		this.secondarylastMileNRC = secondarylastMileNRC;
	}

	/**
	 * @return the secondaryAdditionalIPMRC
	 */
	public Double getSecondaryAdditionalIPMRC() {
		return secondaryAdditionalIPMRC;
	}

	/**
	 * @param secondaryAdditionalIPMRC the secondaryAdditionalIPMRC to set
	 */
	public void setSecondaryAdditionalIPMRC(Double secondaryAdditionalIPMRC) {
		this.secondaryAdditionalIPMRC = secondaryAdditionalIPMRC;
	}

	/**
	 * @return the secondaryAadditionalIPNRC
	 */
	public Double getSecondaryAadditionalIPNRC() {
		return secondaryAadditionalIPNRC;
	}

	/**
	 * @param secondaryAadditionalIPNRC the secondaryAadditionalIPNRC to set
	 */
	public void setSecondaryAadditionalIPNRC(Double secondaryAadditionalIPNRC) {
		this.secondaryAadditionalIPNRC = secondaryAadditionalIPNRC;
	}

	/**
	 * @return the isSecondaryInternetPort
	 */
	public Boolean getIsSecondaryInternetPort() {
		return isSecondaryInternetPort;
	}

	/**
	 * @param isSecondaryInternetPort the isSecondaryInternetPort to set
	 */
	public void setIsSecondaryInternetPort(Boolean isSecondaryInternetPort) {
		this.isSecondaryInternetPort = isSecondaryInternetPort;
	}

	/**
	 * @return the isSecondaryLastMile
	 */
	public Boolean getIsSecondaryLastMile() {
		return isSecondaryLastMile;
	}

	/**
	 * @param isSecondaryLastMile the isSecondaryLastMile to set
	 */
	public void setIsSecondaryLastMile(Boolean isSecondaryLastMile) {
		this.isSecondaryLastMile = isSecondaryLastMile;
	}

	/**
	 * @return the secondaryLastMileSpeed
	 */
	public String getSecondaryLastMileSpeed() {
		return secondaryLastMileSpeed;
	}

	/**
	 * @param secondaryLastMileSpeed the secondaryLastMileSpeed to set
	 */
	public void setSecondaryLastMileSpeed(String secondaryLastMileSpeed) {
		this.secondaryLastMileSpeed = secondaryLastMileSpeed;
	}

	/**
	 * @return the secondarySpeed
	 */
	public String getSecondarySpeed() {
		return secondarySpeed;
	}

	/**
	 * @param secondarySpeed the secondarySpeed to set
	 */
	public void setSecondarySpeed(String secondarySpeed) {
		this.secondarySpeed = secondarySpeed;
	}

	/**
	 * @return the secondaryIsCpe
	 */
	public Boolean getIsSecondaryCpe() {
		return isSecondaryCpe;
	}

	/**
	 * @param isSecondaryCpe the secondaryIsCpe to set
	 */
	public void setIsSecondaryCpe(Boolean isSecondaryCpe) {
		this.isSecondaryCpe = isSecondaryCpe;
	}

	/**
	 * @return the subTotalARC
	 */
	public Double getSubTotalARC() {
		return subTotalARC;
	}

	/**
	 * @param subTotalARC the subTotalARC to set
	 */
	public void setSubTotalARC(Double subTotalARC) {
		this.subTotalARC = subTotalARC;
	}

	/**
	 * @return the lastMileARC
	 */
	public Double getLastMileARC() {
		return lastMileARC;
	}

	/**
	 * @param lastMileARC the lastMileARC to set
	 */
	public void setLastMileARC(Double lastMileARC) {
		this.lastMileARC = lastMileARC;
	}

	/**
	 * @return the secondarylastMileARC
	 */
	public Double getSecondarylastMileARC() {
		return secondarylastMileARC;
	}

	/**
	 * @param secondarylastMileARC the secondarylastMileARC to set
	 */
	public void setSecondarylastMileARC(Double secondarylastMileARC) {
		this.secondarylastMileARC = secondarylastMileARC;
	}

	/**
	 * @return the internetPortARC
	 */
	public Double getInternetPortARC() {
		return internetPortARC;
	}

	/**
	 * @param internetPortARC the internetPortARC to set
	 */
	public void setInternetPortARC(Double internetPortARC) {
		this.internetPortARC = internetPortARC;
	}

	/**
	 * @return the secondaryInternetPortARC
	 */
	public Double getSecondaryInternetPortARC() {
		return secondaryInternetPortARC;
	}

	/**
	 * @param secondaryInternetPortARC the secondaryInternetPortARC to set
	 */
	public void setSecondaryInternetPortARC(Double secondaryInternetPortARC) {
		this.secondaryInternetPortARC = secondaryInternetPortARC;
	}

	/**
	 * @return the cpeARC
	 */
	public Double getCpeARC() {
		return cpeARC;
	}

	/**
	 * @param cpeARC the cpeARC to set
	 */
	public void setCpeARC(Double cpeARC) {
		this.cpeARC = cpeARC;
	}

	/**
	 * @return the secondaryCpeARC
	 */
	public Double getSecondaryCpeARC() {
		return secondaryCpeARC;
	}

	/**
	 * @param secondaryCpeARC the secondaryCpeARC to set
	 */
	public void setSecondaryCpeARC(Double secondaryCpeARC) {
		this.secondaryCpeARC = secondaryCpeARC;
	}

	public Boolean getIsMastCost() {
		return isMastCost;
	}

	public void setIsMastCost(Boolean isMastCost) {
		this.isMastCost = isMastCost;
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

	public Boolean getIsSecondaryMastCost() {
		return isSecondaryMastCost;
	}

	public void setIsSecondaryMastCost(Boolean isSecondaryMastCost) {
		this.isSecondaryMastCost = isSecondaryMastCost;
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


	public String getCpeMRCFormatted() {
		return cpeMRCFormatted;
	}

	public void setCpeMRCFormatted(String cpeMRCFormatted) {
		this.cpeMRCFormatted = cpeMRCFormatted;
	}

	public String getCpeNRCFormatted() {
		return cpeNRCFormatted;
	}

	public void setCpeNRCFormatted(String cpeNRCFormatted) {
		this.cpeNRCFormatted = cpeNRCFormatted;
	}

	public String getCpeARCFormatted() {
		return cpeARCFormatted;
	}

	public void setCpeARCFormatted(String cpeARCFormatted) {
		this.cpeARCFormatted = cpeARCFormatted;
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

	public String getServiceId() { return serviceId; }

	public void setServiceId(String serviceId) { this.serviceId = serviceId; }

	public String getPrimaryServiceId() { return primaryServiceId; }

	public void setPrimaryServiceId(String primaryServiceId) { this.primaryServiceId = primaryServiceId; }

	public String getSecondaryServiceId() { return secondaryServiceId; }

	public void setSecondaryServiceId(String secondaryServiceId) { this.secondaryServiceId = secondaryServiceId; }

	public String getLinkType() { return linkType; }

	public void setLinkType(String linkType) { this.linkType = linkType; }
	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
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

	public String getInternetPortChargeableItem() {
		return internetPortChargeableItem;
	}

	public void setInternetPortChargeableItem(String internetPortChargeableItem) {
		this.internetPortChargeableItem = internetPortChargeableItem;
	}

	public String getInternetPortSecondaryChargeableItem() {
		return internetPortSecondaryChargeableItem;
	}

	public void setInternetPortSecondaryChargeableItem(String internetPortSecondaryChargeableItem) {
		this.internetPortSecondaryChargeableItem = internetPortSecondaryChargeableItem;
	}

	public String getLastMileChargeableItem() {
		return lastMileChargeableItem;
	}

	public void setLastMileChargeableItem(String lastMileChargeableItem) {
		this.lastMileChargeableItem = lastMileChargeableItem;
	}

	public String getLastMileSecondaryChargeableItem() {
		return lastMileSecondaryChargeableItem;
	}

	public void setLastMileSecondaryChargeableItem(String lastMileSecondaryChargeableItem) {
		this.lastMileSecondaryChargeableItem = lastMileSecondaryChargeableItem;
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

	public Double getBustableBandwidthCharge() {
		return bustableBandwidthCharge;
	}

	public void setBustableBandwidthCharge(Double bustableBandwidthCharge) {
		this.bustableBandwidthCharge = bustableBandwidthCharge;
	}
	public Boolean getIsCpeRecoveryCharges() {
		return isCpeRecoveryCharges;
	}

	public void setIsCpeRecoveryCharges(Boolean isCpeRecoveryCharges) {
		this.isCpeRecoveryCharges = isCpeRecoveryCharges;
	}

	public Double getCpeRecoveryMRC() {
		return cpeRecoveryMRC;
	}

	public void setCpeRecoveryMRC(Double cpeRecoveryMRC) {
		this.cpeRecoveryMRC = cpeRecoveryMRC;
	}

	public Double getCpeRecoveryNRC() {
		return cpeRecoveryNRC;
	}

	public void setCpeRecoveryNRC(Double cpeRecoveryNRC) {
		this.cpeRecoveryNRC = cpeRecoveryNRC;
	}

	public Double getCpeRecoveryARC() {
		return cpeRecoveryARC;
	}

	public void setCpeRecoveryARC(Double cpeRecoveryARC) {
		this.cpeRecoveryARC = cpeRecoveryARC;
	}

	public String getCpeRecoveryChargeableItem() {
		return cpeRecoveryChargeableItem;
	}

	public void setCpeRecoveryChargeableItem(String cpeRecoveryChargeableItem) {
		this.cpeRecoveryChargeableItem = cpeRecoveryChargeableItem;
	}

	public String getCpeRecoveryMRCFormatted() {
		return cpeRecoveryMRCFormatted;
	}

	public void setCpeRecoveryMRCFormatted(String cpeRecoveryMRCFormatted) {
		this.cpeRecoveryMRCFormatted = cpeRecoveryMRCFormatted;
	}

	public String getCpeRecoveryNRCFormatted() {
		return cpeRecoveryNRCFormatted;
	}

	public void setCpeRecoveryNRCFormatted(String cpeRecoveryNRCFormatted) {
		this.cpeRecoveryNRCFormatted = cpeRecoveryNRCFormatted;
	}

	public String getCpeRecoveryARCFormatted() {
		return cpeRecoveryARCFormatted;
	}

	public void setCpeRecoveryARCFormatted(String cpeRecoveryARCFormatted) {
		this.cpeRecoveryARCFormatted = cpeRecoveryARCFormatted;
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

	@Override
	public String toString() {
		return "GvpnSiteCommercial{" +
				"serviceType='" + serviceType + '\'' +
				", speed='" + speed + '\'' +
				", isCpe=" + isCpe +
				", subTotalMRC=" + subTotalMRC +
				", subTotalNRC=" + subTotalNRC +
				", subTotalARC=" + subTotalARC +
				", isInternetPort=" + isInternetPort +
				", internetPortMRC=" + internetPortMRC +
				", internetPortNRC=" + internetPortNRC +
				", internetPortARC=" + internetPortARC +
				", cpeMRC=" + cpeMRC +
				", cpeNRC=" + cpeNRC +
				", cpeARC=" + cpeARC +
				", isLastMile=" + isLastMile +
				", lastMileMRC=" + lastMileMRC +
				", lastMileNRC=" + lastMileNRC +
				", lastMileARC=" + lastMileARC +
				", isMastCost=" + isMastCost +
				", isSecondaryMastCost=" + isSecondaryMastCost +
				", mastCostMRC=" + mastCostMRC +
				", mastCostNRC=" + mastCostNRC +
				", mastCostARC=" + mastCostARC +
				", secondaryMastCostMRC=" + secondaryMastCostMRC +
				", secondaryMastCostNRC=" + secondaryMastCostNRC +
				", secondaryMastCostARC=" + secondaryMastCostARC +
				", isadditionalIP=" + isadditionalIP +
				", additionalIPMRC=" + additionalIPMRC +
				", additionalIPNRC=" + additionalIPNRC +
				", bustableBandwidthCharge=" + bustableBandwidthCharge +
				", lastMileSpeed='" + lastMileSpeed + '\'' +
				", secondaryLastMileSpeed='" + secondaryLastMileSpeed + '\'' +
				", secondarySpeed='" + secondarySpeed + '\'' +
				", isSecondaryInternetPort=" + isSecondaryInternetPort +
				", isSecondaryLastMile=" + isSecondaryLastMile +
				", secondaryInternetPortMRC=" + secondaryInternetPortMRC +
				", secondaryInternetPortNRC=" + secondaryInternetPortNRC +
				", secondaryInternetPortARC=" + secondaryInternetPortARC +
				", secondaryCpeMRC=" + secondaryCpeMRC +
				", secondaryCpeNRC=" + secondaryCpeNRC +
				", secondaryCpeARC=" + secondaryCpeARC +
				", isSecondaryCpe=" + isSecondaryCpe +
				", secondarylastMileMRC=" + secondarylastMileMRC +
				", secondarylastMileNRC=" + secondarylastMileNRC +
				", secondarylastMileARC=" + secondarylastMileARC +
				", secondaryAdditionalIPMRC=" + secondaryAdditionalIPMRC +
				", secondaryAadditionalIPNRC=" + secondaryAadditionalIPNRC +
				", isShiftingCharge=" + isShiftingCharge +
				", shiftingChargeMRC=" + shiftingChargeMRC +
				", shiftingChargeNRC=" + shiftingChargeNRC +
				", shiftingChargeARC=" + shiftingChargeARC +
				", secondaryShiftingChargeMRC=" + secondaryShiftingChargeMRC +
				", secondaryShiftingChargeNRC=" + secondaryShiftingChargeNRC +
				", secondaryShiftingChargeARC=" + secondaryShiftingChargeARC +
				", secondaryShiftingChargeMRCFormatted='" + secondaryShiftingChargeMRCFormatted + '\'' +
				", secondaryShiftingChargeNRCFormatted='" + secondaryShiftingChargeNRCFormatted + '\'' +
				", secondaryShiftingChargeARCFormatted='" + secondaryShiftingChargeARCFormatted + '\'' +
				", secondaryShiftingChargeChargeableItem='" + secondaryShiftingChargeChargeableItem + '\'' +
				", secondaryShitingFlag=" + secondaryShitingFlag +
				", cpeMRCFormatted='" + cpeMRCFormatted + '\'' +
				", cpeNRCFormatted='" + cpeNRCFormatted + '\'' +
				", cpeARCFormatted='" + cpeARCFormatted + '\'' +
				", lastMileMRCFormatted='" + lastMileMRCFormatted + '\'' +
				", lastMileNRCFormatted='" + lastMileNRCFormatted + '\'' +
				", lastMileARCFormatted='" + lastMileARCFormatted + '\'' +
				", mastCostMRCFormatted='" + mastCostMRCFormatted + '\'' +
				", mastCostNRCFormatted='" + mastCostNRCFormatted + '\'' +
				", mastCostARCFormatted='" + mastCostARCFormatted + '\'' +
				", secondaryMastCostMRCFormatted='" + secondaryMastCostMRCFormatted + '\'' +
				", secondaryMastCostNRCFormatted='" + secondaryMastCostNRCFormatted + '\'' +
				", secondaryMastCostARCFormatted='" + secondaryMastCostARCFormatted + '\'' +
				", secondaryInternetPortMRCFormatted='" + secondaryInternetPortMRCFormatted + '\'' +
				", secondaryInternetPortNRCFormatted='" + secondaryInternetPortNRCFormatted + '\'' +
				", secondaryInternetPortARCFormatted='" + secondaryInternetPortARCFormatted + '\'' +
				", secondaryCpeMRCFormatted='" + secondaryCpeMRCFormatted + '\'' +
				", secondaryCpeNRCFormatted='" + secondaryCpeNRCFormatted + '\'' +
				", secondaryCpeARCFormatted='" + secondaryCpeARCFormatted + '\'' +
				", shiftingChargeMRCFormatted='" + shiftingChargeMRCFormatted + '\'' +
				", shiftingChargeNRCFormatted='" + shiftingChargeNRCFormatted + '\'' +
				", shiftingChargeARCFormatted='" + shiftingChargeARCFormatted + '\'' +
				", secondarylastMileNRCFormatted='" + secondarylastMileNRCFormatted + '\'' +
				", secondarylastMileARCFormatted='" + secondarylastMileARCFormatted + '\'' +
				", internetPortMRCFormatted='" + internetPortMRCFormatted + '\'' +
				", internetPortNRCFormatted='" + internetPortNRCFormatted + '\'' +
				", internetPortARCFormatted='" + internetPortARCFormatted + '\'' +
				", additionalIPMRCFormatted='" + additionalIPMRCFormatted + '\'' +
				", additionalIPNRCFormatted='" + additionalIPNRCFormatted + '\'' +
				", subTotalMRCFormatted='" + subTotalMRCFormatted + '\'' +
				", subTotalNRCFormatted='" + subTotalNRCFormatted + '\'' +
				", subTotalARCFormatted='" + subTotalARCFormatted + '\'' +
				", serviceId='" + serviceId + '\'' +
				", primaryServiceId='" + primaryServiceId + '\'' +
				", secondaryServiceId='" + secondaryServiceId + '\'' +
				", linkType='" + linkType + '\'' +
				", hsnCode='" + hsnCode + '\'' +
				", cpeLineItemsPrimary=" + cpeLineItemsPrimary +
				", cpeLineItemsSecondary=" + cpeLineItemsSecondary +
				", internetPortChargeableItem='" + internetPortChargeableItem + '\'' +
				", internetPortSecondaryChargeableItem='" + internetPortSecondaryChargeableItem + '\'' +
				", lastMileChargeableItem='" + lastMileChargeableItem + '\'' +
				", lastMileSecondaryChargeableItem='" + lastMileSecondaryChargeableItem + '\'' +
				", shiftingChargeChargeableItem='" + shiftingChargeChargeableItem + '\'' +
				", additionalIPChargeableItem='" + additionalIPChargeableItem + '\'' +
				", mastCostChargeableItem='" + mastCostChargeableItem + '\'' +
				", burstableChargeableItem='" + burstableChargeableItem + '\'' +
				", showMainCpe=" + showMainCpe +
				", showMainSecondaryCpe=" + showMainSecondaryCpe +
				", isCpeRecoveryCharges=" + isCpeRecoveryCharges +
				", cpeRecoveryMRC=" + cpeRecoveryMRC +
				", cpeRecoveryNRC=" + cpeRecoveryNRC +
				", cpeRecoveryARC=" + cpeRecoveryARC +
				", cpeRecoveryChargeableItem='" + cpeRecoveryChargeableItem + '\'' +
				", cpeRecoveryMRCFormatted='" + cpeRecoveryMRCFormatted + '\'' +
				", cpeRecoveryNRCFormatted='" + cpeRecoveryNRCFormatted + '\'' +
				", cpeRecoveryARCFormatted='" + cpeRecoveryARCFormatted + '\'' +
				", secondarylastMileMRCFormatted='" + secondarylastMileMRCFormatted + '\'' +
				'}';
	}
}
