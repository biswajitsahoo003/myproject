package com.tcl.dias.servicefulfillment.beans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * @author NaveenG
 *
 */
@JsonInclude(Include.NON_NULL)
public class ConductSiteSurveyManBean extends TaskDetailsBaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String siteDeficiencyObservations;
	private String siteReadinessStatus;
	private String rackSpaceAvailability;
	private String earthingAvailable;
	private String hygienicRoomNearNetworkRoom;
	private String powerSocketAvailable;
	private String airConditioned;

	private String ibdSurveyRemarks;
	private String nodeAIP;
	private String nodeBIP;
	private Integer sapBundleId;
	private String accessRingName;

	private String accessRingTechnologyChangeReason;
	private String muxDeliveryCountry;
	private String muxDeliveryPincode;
	private String muxDeliveryCity;
	private String muxDeliveryLocality;
	private String muxDeliveryAddress;
	private String accessRingTechnology;

	private String isNetworkSuggestionAvailable;
	private String isBOPNetworkSelected;

	private String networkSuggestionRejectionReason;
	
	private String isKroneRequired;
	
	private String isConnectedSite;
	
	private String endMuxNodeIp;
	private String endMuxNodeName;
	private String endMuxNodePort;
	
	
	
	
	

	public String getEndMuxNodeIp() {
		return endMuxNodeIp;
	}

	public void setEndMuxNodeIp(String endMuxNodeIp) {
		this.endMuxNodeIp = endMuxNodeIp;
	}

	public String getEndMuxNodeName() {
		return endMuxNodeName;
	}

	public void setEndMuxNodeName(String endMuxNodeName) {
		this.endMuxNodeName = endMuxNodeName;
	}

	public String getEndMuxNodePort() {
		return endMuxNodePort;
	}

	public void setEndMuxNodePort(String endMuxNodePort) {
		this.endMuxNodePort = endMuxNodePort;
	}

	public String getIsConnectedSite() {
		return isConnectedSite;
	}

	public void setIsConnectedSite(String isConnectedSite) {
		this.isConnectedSite = isConnectedSite;
	}

	public String getAccessRingTechnologyChangeReason() {
		return accessRingTechnologyChangeReason;
	}

	public void setAccessRingTechnologyChangeReason(String accessRingTechnologyChangeReason) {
		this.accessRingTechnologyChangeReason = accessRingTechnologyChangeReason;
	}

	public String getMuxDeliveryCountry() {
		return muxDeliveryCountry;
	}

	public void setMuxDeliveryCountry(String muxDeliveryCountry) {
		this.muxDeliveryCountry = muxDeliveryCountry;
	}

	public String getMuxDeliveryPincode() {
		return muxDeliveryPincode;
	}

	public void setMuxDeliveryPincode(String muxDeliveryPincode) {
		this.muxDeliveryPincode = muxDeliveryPincode;
	}

	public String getMuxDeliveryCity() {
		return muxDeliveryCity;
	}

	public void setMuxDeliveryCity(String muxDeliveryCity) {
		this.muxDeliveryCity = muxDeliveryCity;
	}

	public String getMuxDeliveryLocality() {
		return muxDeliveryLocality;
	}

	public void setMuxDeliveryLocality(String muxDeliveryLocality) {
		this.muxDeliveryLocality = muxDeliveryLocality;
	}

	public String getMuxDeliveryAddress() {
		return muxDeliveryAddress;
	}

	public void setMuxDeliveryAddress(String muxDeliveryAddress) {
		this.muxDeliveryAddress = muxDeliveryAddress;
	}

	public String getAccessRingTechnology() {
		return accessRingTechnology;
	}

	public void setAccessRingTechnology(String accessRingTechnology) {
		this.accessRingTechnology = accessRingTechnology;
	}

	private String powerBackup;

	public String getPowerBackup() {
		return powerBackup;
	}

	public void setPowerBackup(String powerBackup) {
		this.powerBackup = powerBackup;
	}

	private String muxMakeModel;
	private String muxMake;

	public Integer getSapBundleId() {
		return sapBundleId;
	}

	public void setSapBundleId(Integer sapBundleId) {
		this.sapBundleId = sapBundleId;
	}

	private List<AttachmentIdBean> documentIds;

	public String getSiteDeficiencyObservations() {
		return siteDeficiencyObservations;
	}

	public void setSiteDeficiencyObservations(String siteDeficiencyObservations) {
		this.siteDeficiencyObservations = siteDeficiencyObservations;
	}

	public String getSiteReadinessStatus() {
		return siteReadinessStatus;
	}

	public void setSiteReadinessStatus(String siteReadinessStatus) {
		this.siteReadinessStatus = siteReadinessStatus;
	}

	public String getRackSpaceAvailability() {
		return rackSpaceAvailability;
	}

	public void setRackSpaceAvailability(String rackSpaceAvailability) {
		this.rackSpaceAvailability = rackSpaceAvailability;
	}

	public String getEarthingAvailable() {
		return earthingAvailable;
	}

	public void setEarthingAvailable(String earthingAvailable) {
		this.earthingAvailable = earthingAvailable;
	}

	public String getHygienicRoomNearNetworkRoom() {
		return hygienicRoomNearNetworkRoom;
	}

	public void setHygienicRoomNearNetworkRoom(String hygienicRoomNearNetworkRoom) {
		this.hygienicRoomNearNetworkRoom = hygienicRoomNearNetworkRoom;
	}

	public String getPowerSocketAvailable() {
		return powerSocketAvailable;
	}

	public void setPowerSocketAvailable(String powerSocketAvailable) {
		this.powerSocketAvailable = powerSocketAvailable;
	}

	public String getAirConditioned() {
		return airConditioned;
	}

	public void setAirConditioned(String airConditioned) {
		this.airConditioned = airConditioned;
	}

	public String getNodeAIP() {
		return nodeAIP;
	}

	public void setNodeAIP(String nodeAIP) {
		this.nodeAIP = nodeAIP;
	}

	public String getNodeBIP() {
		return nodeBIP;
	}

	public void setNodeBIP(String nodeBIP) {
		this.nodeBIP = nodeBIP;
	}

	public String getIbdSurveyRemarks() {
		return ibdSurveyRemarks;
	}

	public void setIbdSurveyRemarks(String ibdSurveyRemarks) {
		this.ibdSurveyRemarks = ibdSurveyRemarks;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getAccessRingName() {
		return accessRingName;
	}

	public void setAccessRingName(String accessRingName) {
		this.accessRingName = accessRingName;
	}

	public String getMuxMakeModel() {
		return muxMakeModel;
	}

	public void setMuxMakeModel(String muxMakeModel) {
		this.muxMakeModel = muxMakeModel;
	}

	public String getMuxMake() {
		return muxMake;
	}

	public void setMuxMake(String muxMake) {
		this.muxMake = muxMake;
	}

	public String getIsNetworkSuggestionAvailable() {
		return isNetworkSuggestionAvailable;
	}

	public void setIsNetworkSuggestionAvailable(String isNetworkSuggestionAvailable) {
		this.isNetworkSuggestionAvailable = isNetworkSuggestionAvailable;
	}

	public String getIsBOPNetworkSelected() {
		return isBOPNetworkSelected;
	}

	public void setIsBOPNetworkSelected(String isBOPNetworkSelected) {
		this.isBOPNetworkSelected = isBOPNetworkSelected;
	}

	public String getNetworkSuggestionRejectionReason() {
		return networkSuggestionRejectionReason;
	}

	public void setNetworkSuggestionRejectionReason(String networkSuggestionRejectionReason) {
		this.networkSuggestionRejectionReason = networkSuggestionRejectionReason;
	}

	public String getIsKroneRequired() {
		return isKroneRequired;
	}

	public void setIsKroneRequired(String isKroneRequired) {
		this.isKroneRequired = isKroneRequired;
	}

	

	
	
	
}
