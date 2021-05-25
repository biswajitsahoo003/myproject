package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * This class is used to for conduct site survey onnet wireless
 * 
 *
 * @author NaveenG
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

@JsonInclude(Include.NON_NULL)
public class ConductRfSiteSurveyBean extends TaskDetailsBaseBean {

	private String airConditioned;
	private String powerAvailable;
	private String powerSocketAvailable;
	private String hygienicRoomNearNetworkRoom;	
	private String earthingAvailable;
	private String powerBackup;
	private String earthPitAvailable;
	private String mastInstallationPermission;
	private String terraceAccessAvailable;
	private String poeRackWallFixPermission;
	private String cableRoutingPermission;

	private String siteAddress;
	private String demarcationWing;
	private String demarcationFloor;
	private String demarcationRoom;
	private String demarcationBuildingName;

	private String siteReadinessStatus;
	private String siteDeficiencyObservations;
	
	private String feasibilityStatus;
	private String feasiblityFailureReason;
	
	private String buildingHeight;
	private String elevation;
	private String typeOfTerrain;
	private String towerHeight;
	private String distanceFromSite1;
	private String distanceFromOtherEnd;
	
	
	private String cpeRack;
	private String electricianSupport;
	private String permissionToISS;
	private String typeOfPoleTowerMount;
	private String structureType;
	private String poleHeight;
	private String typeOfPoleAntennaErection;
	private String mastProvidedBy;
	private String mastHeight;
	private String mastReusable;
	private String mastReusableRemarks;
	private String typeOfMastAntennaErection;
	private String mastHeightDeviation;
	private String mastHeightDeviationReason;
	
	private String polarization;
	private String antennaAzimuth;
	private String poeToAntennaCableLength;
	
	private String antennaToEarthPitCableLength;
	
	private String obstructionType;
	private String obstructionHeight;
	private String phaseNeutral;
	private String earthNeutral;
	private String phaseEarth;
	private String surveyRemarks;
	private String rfInstallationPlannedDate;
	private String rfInstallationPlannedTime;
	private String sameDayInstallation;
	private String btsId;
	private String btsName;
	private String btsSiteAddress;
	private String btsIp;
	private String sectorId;
	private String btsLat;
	private String btsLong;
	private String customerSiteLat;
	private String customerSiteLong;


	private String rfMakeModel;
	
	private String rfMake;
	
	private String reasonForMastRequirement;
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String rfLmTentativeDate;


	public String getCustomerSiteLat() { return customerSiteLat; }

	public void setCustomerSiteLat(String customerSiteLat) { this.customerSiteLat = customerSiteLat; }

	public String getCustomerSiteLong() { return customerSiteLong; }

	public void setCustomerSiteLong(String customerSiteLong) { this.customerSiteLong = customerSiteLong; }

	public String getReasonForMastRequirement() {
		return reasonForMastRequirement;
	}

	public void setReasonForMastRequirement(String reasonForMastRequirement) {
		this.reasonForMastRequirement = reasonForMastRequirement;
	}

	public String getRfMakeModel() {
		return rfMakeModel;
	}

	public void setRfMakeModel(String rfMakeModel) {
		this.rfMakeModel = rfMakeModel;
	}

	public String getRfMake() {
		return rfMake;
	}

	public void setRfMake(String rfMake) {
		this.rfMake = rfMake;
	}

	private List<AttachmentIdBean> documentIds;
	
	private Integer rfSapBundleId;
	
	


	public Integer getRfSapBundleId() {
		return rfSapBundleId;
	}

	public void setRfSapBundleId(Integer rfSapBundleId) {
		this.rfSapBundleId = rfSapBundleId;
	}

	public String getAirConditioned() {
		return airConditioned;
	}

	public void setAirConditioned(String airConditioned) {
		this.airConditioned = airConditioned;
	}

	public String getPowerAvailable() {
		return powerAvailable;
	}

	public void setPowerAvailable(String powerAvailable) {
		this.powerAvailable = powerAvailable;
	}

	public String getPowerSocketAvailable() {
		return powerSocketAvailable;
	}

	public void setPowerSocketAvailable(String powerSocketAvailable) {
		this.powerSocketAvailable = powerSocketAvailable;
	}

	public String getHygienicRoomNearNetworkRoom() {
		return hygienicRoomNearNetworkRoom;
	}

	public void setHygienicRoomNearNetworkRoom(String hygienicRoomNearNetworkRoom) {
		this.hygienicRoomNearNetworkRoom = hygienicRoomNearNetworkRoom;
	}

	public String getEarthingAvailable() {
		return earthingAvailable;
	}

	public void setEarthingAvailable(String earthingAvailable) {
		this.earthingAvailable = earthingAvailable;
	}

	public String getPowerBackup() {
		return powerBackup;
	}

	public void setPowerBackup(String powerBackup) {
		this.powerBackup = powerBackup;
	}

	public String getEarthPitAvailable() {
		return earthPitAvailable;
	}

	public void setEarthPitAvailable(String earthPitAvailable) {
		this.earthPitAvailable = earthPitAvailable;
	}

	public String getMastInstallationPermission() {
		return mastInstallationPermission;
	}

	public void setMastInstallationPermission(String mastInstallationPermission) {
		this.mastInstallationPermission = mastInstallationPermission;
	}

	public String getTerraceAccessAvailable() {
		return terraceAccessAvailable;
	}

	public void setTerraceAccessAvailable(String terraceAccessAvailable) {
		this.terraceAccessAvailable = terraceAccessAvailable;
	}

	public String getPoeRackWallFixPermission() {
		return poeRackWallFixPermission;
	}

	public void setPoeRackWallFixPermission(String poeRackWallFixPermission) {
		this.poeRackWallFixPermission = poeRackWallFixPermission;
	}

	public String getCableRoutingPermission() {
		return cableRoutingPermission;
	}

	public void setCableRoutingPermission(String cableRoutingPermission) {
		this.cableRoutingPermission = cableRoutingPermission;
	}

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public String getDemarcationWing() {
		return demarcationWing;
	}

	public void setDemarcationWing(String demarcationWing) {
		this.demarcationWing = demarcationWing;
	}

	public String getDemarcationFloor() {
		return demarcationFloor;
	}

	public void setDemarcationFloor(String demarcationFloor) {
		this.demarcationFloor = demarcationFloor;
	}

	public String getDemarcationRoom() {
		return demarcationRoom;
	}

	public void setDemarcationRoom(String demarcationRoom) {
		this.demarcationRoom = demarcationRoom;
	}

	public String getDemarcationBuildingName() {
		return demarcationBuildingName;
	}

	public void setDemarcationBuildingName(String demarcationBuildingName) {
		this.demarcationBuildingName = demarcationBuildingName;
	}

	public String getSiteReadinessStatus() {
		return siteReadinessStatus;
	}

	public void setSiteReadinessStatus(String siteReadinessStatus) {
		this.siteReadinessStatus = siteReadinessStatus;
	}

	public String getSiteDeficiencyObservations() {
		return siteDeficiencyObservations;
	}

	public void setSiteDeficiencyObservations(String siteDeficiencyObservations) {
		this.siteDeficiencyObservations = siteDeficiencyObservations;
	}

	public String getFeasibilityStatus() {
		return feasibilityStatus;
	}

	public void setFeasibilityStatus(String feasibilityStatus) {
		this.feasibilityStatus = feasibilityStatus;
	}

	public String getFeasiblityFailureReason() {
		return feasiblityFailureReason;
	}

	public void setFeasiblityFailureReason(String feasiblityFailureReason) {
		this.feasiblityFailureReason = feasiblityFailureReason;
	}

	public String getBuildingHeight() {
		return buildingHeight;
	}

	public void setBuildingHeight(String buildingHeight) {
		this.buildingHeight = buildingHeight;
	}

	public String getElevation() {
		return elevation;
	}

	public void setElevation(String elevation) {
		this.elevation = elevation;
	}

	public String getTypeOfTerrain() {
		return typeOfTerrain;
	}

	public void setTypeOfTerrain(String typeOfTerrain) {
		this.typeOfTerrain = typeOfTerrain;
	}

	public String getTowerHeight() {
		return towerHeight;
	}

	public void setTowerHeight(String towerHeight) {
		this.towerHeight = towerHeight;
	}

	public String getDistanceFromSite1() {
		return distanceFromSite1;
	}

	public void setDistanceFromSite1(String distanceFromSite1) {
		this.distanceFromSite1 = distanceFromSite1;
	}

	public String getDistanceFromOtherEnd() {
		return distanceFromOtherEnd;
	}

	public void setDistanceFromOtherEnd(String distanceFromOtherEnd) {
		this.distanceFromOtherEnd = distanceFromOtherEnd;
	}

	public String getStructureType() {
		return structureType;
	}

	public void setStructureType(String structureType) {
		this.structureType = structureType;
	}

	public String getPoleHeight() {
		return poleHeight;
	}

	public void setPoleHeight(String poleHeight) {
		this.poleHeight = poleHeight;
	}

	public String getTypeOfPoleAntennaErection() {
		return typeOfPoleAntennaErection;
	}

	public void setTypeOfPoleAntennaErection(String typeOfPoleAntennaErection) {
		this.typeOfPoleAntennaErection = typeOfPoleAntennaErection;
	}

	public String getMastProvidedBy() {
		return mastProvidedBy;
	}

	public void setMastProvidedBy(String mastProvidedBy) {
		this.mastProvidedBy = mastProvidedBy;
	}

	public String getMastHeight() {
		return mastHeight;
	}

	public void setMastHeight(String mastHeight) {
		this.mastHeight = mastHeight;
	}

	public String getMastReusable() {
		return mastReusable;
	}

	public void setMastReusable(String mastReusable) {
		this.mastReusable = mastReusable;
	}

	public String getMastReusableRemarks() {
		return mastReusableRemarks;
	}

	public void setMastReusableRemarks(String mastReusableRemarks) {
		this.mastReusableRemarks = mastReusableRemarks;
	}

	public String getTypeOfMastAntennaErection() {
		return typeOfMastAntennaErection;
	}

	public void setTypeOfMastAntennaErection(String typeOfMastAntennaErection) {
		this.typeOfMastAntennaErection = typeOfMastAntennaErection;
	}

	public String getMastHeightDeviation() {
		return mastHeightDeviation;
	}

	public void setMastHeightDeviation(String mastHeightDeviation) {
		this.mastHeightDeviation = mastHeightDeviation;
	}

	public String getMastHeightDeviationReason() {
		return mastHeightDeviationReason;
	}

	public void setMastHeightDeviationReason(String mastHeightDeviationReason) {
		this.mastHeightDeviationReason = mastHeightDeviationReason;
	}

	public String getPolarization() {
		return polarization;
	}

	public void setPolarization(String polarization) {
		this.polarization = polarization;
	}

	public String getAntennaAzimuth() {
		return antennaAzimuth;
	}

	public void setAntennaAzimuth(String antennaAzimuth) {
		this.antennaAzimuth = antennaAzimuth;
	}

	public String getPoeToAntennaCableLength() {
		return poeToAntennaCableLength;
	}

	public void setPoeToAntennaCableLength(String poeToAntennaCableLength) {
		this.poeToAntennaCableLength = poeToAntennaCableLength;
	}

	public String getObstructionType() {
		return obstructionType;
	}

	public void setObstructionType(String obstructionType) {
		this.obstructionType = obstructionType;
	}

	public String getObstructionHeight() {
		return obstructionHeight;
	}

	public void setObstructionHeight(String obstructionHeight) {
		this.obstructionHeight = obstructionHeight;
	}

	public String getPhaseNeutral() {
		return phaseNeutral;
	}

	public void setPhaseNeutral(String phaseNeutral) {
		this.phaseNeutral = phaseNeutral;
	}

	public String getPhaseEarth() {
		return phaseEarth;
	}

	public void setPhaseEarth(String phaseEarth) {
		this.phaseEarth = phaseEarth;
	}

	public String getSurveyRemarks() {
		return surveyRemarks;
	}

	public void setSurveyRemarks(String surveyRemarks) {
		this.surveyRemarks = surveyRemarks;
	}

	public String getRfInstallationPlannedDate() {
		return rfInstallationPlannedDate;
	}

	public void setRfInstallationPlannedDate(String rfInstallationPlannedDate) {
		this.rfInstallationPlannedDate = rfInstallationPlannedDate;
	}

	public String getRfInstallationPlannedTime() {
		return rfInstallationPlannedTime;
	}

	public void setRfInstallationPlannedTime(String rfInstallationPlannedTime) {
		this.rfInstallationPlannedTime = rfInstallationPlannedTime;
	}
	
	public String getSameDayInstallation() {
		return sameDayInstallation;
	}

	public void setSameDayInstallation(String sameDayInstallation) {
		this.sameDayInstallation = sameDayInstallation;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
	
	
	public String getAntennaToEarthPitCableLength() {
		return antennaToEarthPitCableLength;
	}

	public void setAntennaToEarthPitCableLength(String antennaToEarthPitCableLength) {
		this.antennaToEarthPitCableLength = antennaToEarthPitCableLength;
	}

	public String getBtsId() { return btsId; }

	public void setBtsId(String btsId) { this.btsId = btsId; }

	public String getBtsName() { return btsName; }

	public void setBtsName(String btsName) { this.btsName = btsName; }

	public String getBtsSiteAddress() { return btsSiteAddress; }

	public void setBtsSiteAddress(String btsSiteAddress) { this.btsSiteAddress = btsSiteAddress; }

	public String getBtsIp() { return btsIp; }

	public void setBtsIp(String btsIp) { this.btsIp = btsIp; }

	public String getSectorId() { return sectorId; }

	public void setSectorId(String sectorId) { this.sectorId = sectorId; }

	public String getCpeRack() {
		return cpeRack;
	}

	public void setCpeRack(String cpeRack) {
		this.cpeRack = cpeRack;
	}

	public String getElectricianSupport() {
		return electricianSupport;
	}

	public void setElectricianSupport(String electricianSupport) {
		this.electricianSupport = electricianSupport;
	}

	public String getPermissionToISS() {
		return permissionToISS;
	}

	public void setPermissionToISS(String permissionToISS) {
		this.permissionToISS = permissionToISS;
	}

	public String getTypeOfPoleTowerMount() {
		return typeOfPoleTowerMount;
	}

	public void setTypeOfPoleTowerMount(String typeOfPoleTowerMount) {
		this.typeOfPoleTowerMount = typeOfPoleTowerMount;
	}

	public String getEarthNeutral() { return earthNeutral; }

	public void setEarthNeutral(String earthNeutral) {
		this.earthNeutral = earthNeutral;
	}

	public String getRfLmTentativeDate() {
		return rfLmTentativeDate;
	}

	public void setRfLmTentativeDate(String rfLmTentativeDate) {
		this.rfLmTentativeDate = rfLmTentativeDate;
	}

	public String getBtsLat() { return btsLat; }

	public void setBtsLat(String btsLat) { this.btsLat = btsLat; }

	public String getBtsLong() { return btsLong; }

	public void setBtsLong(String btsLong) { this.btsLong = btsLong; }
}
