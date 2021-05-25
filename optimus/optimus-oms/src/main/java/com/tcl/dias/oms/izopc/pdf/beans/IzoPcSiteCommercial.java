package com.tcl.dias.oms.izopc.pdf.beans;

/**
 * 
 * Bean class to hold commercial information of IZO PC site
 * 
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class IzoPcSiteCommercial {
	
	private String bandwidth;
	private String cloudProvider;
	
	private String serviceType;
	private String speed;
	private Boolean isCpe = false;
	private Double subTotalMRC;
	private Double subTotalNRC;
	private Double subTotalARC;
	private Boolean isIzoPort = false;
	private Double izoPortMRC = 0D;
	private Double izoPortNRC = 0D;
	private Double izoPortARC = 0D;

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

	private Boolean secondaryIsCpe = false;

	private Double secondarylastMileMRC = 0D;
	private Double secondarylastMileNRC = 0D;
	private Double secondarylastMileARC = 0D;

	private Double secondaryAdditionalIPMRC = 0D;
	private Double secondaryAadditionalIPNRC = 0D;
	
	// Added to hold currency specific formatted values
	
	private String subTotalMRCFormatted;
	private String subTotalNRCFormatted;
	private String subTotalARCFormatted;
	private String izoPortMRCFormatted;
	private String izoPortNRCFormatted;
	private String izoPortARCFormatted;

	public String getIzoPortMRCFormatted() {
		return izoPortMRCFormatted;
	}

	public void setIzoPortMRCFormatted(String izoPortMRCFormatted) {
		this.izoPortMRCFormatted = izoPortMRCFormatted;
	}

	public String getIzoPortNRCFormatted() {
		return izoPortNRCFormatted;
	}

	public void setIzoPortNRCFormatted(String izoPortNRCFormatted) {
		this.izoPortNRCFormatted = izoPortNRCFormatted;
	}

	public String getIzoPortARCFormatted() {
		return izoPortARCFormatted;
	}

	public void setIzoPortARCFormatted(String izoPortARCFormatted) {
		this.izoPortARCFormatted = izoPortARCFormatted;
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

	public void setSubTotalARCFormatted(String subTotalARCFormatted) {
		this.subTotalARCFormatted = subTotalARCFormatted;
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

	public Boolean getIsIzoPort() {
		return isIzoPort;
	}

	public void setIsIzoPort(Boolean isIzoPort) {
		this.isIzoPort = isIzoPort;
	}

	public Double getIzoPortMRC() {
		return izoPortMRC;
	}

	public void setIzoPortMRC(Double izoPortMRC) {
		this.izoPortMRC = izoPortMRC;
	}

	public Double getIzoPortNRC() {
		return izoPortNRC;
	}

	public void setIzoPortNRC(Double izoPortNRC) {
		this.izoPortNRC = izoPortNRC;
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
	public Boolean getSecondaryIsCpe() {
		return secondaryIsCpe;
	}

	/**
	 * @param secondaryIsCpe the secondaryIsCpe to set
	 */
	public void setSecondaryIsCpe(Boolean secondaryIsCpe) {
		this.secondaryIsCpe = secondaryIsCpe;
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
	public Double getIzoPortARC() {
		return izoPortARC;
	}

	/**
	 * @param internetPortARC the internetPortARC to set
	 */
	public void setIzoPortARC(Double izoPortARC) {
		this.izoPortARC = izoPortARC;
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

	public String getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	public String getCloudProvider() {
		return cloudProvider;
	}

	public void setCloudProvider(String cloudProvider) {
		this.cloudProvider = cloudProvider;
	}

}
