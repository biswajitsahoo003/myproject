package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstallRfBean extends TaskDetailsBaseBean {

	private String suMacAddress;
	private String typeOfAntenna;
	private String btsSuMacAddress;
	private String btsSuIp;
	private String btsSuSerialNumber;
	private String radwinServerReachRf;
	private String suIp;
	private String rfFrequency;
	private String suSerialNumber;
	private String hsuHeightFromGround;
	private String averageMeanSeaLevel;
	private String hopDistance;
	private String cableLength;
	private String ethernetExtenderUsed;
	private String cableType;
	private String patchCord;
	private String rfConnectorType;
	private String customerAcceptance;
	private String lClampPoleUsed;
	private String poleHeight;
	private String rfCableLength;
	private String earthingCableLength;
	private String powerCableLength;
	private String noOfMcb;
	private String noOfInstallationkits;
	private String noOfWeatherProofInstallationKits;
	private String noOfPatchCordUsed;
	private String pvcConduit;
	private String btsIp;
	private String hsuIp;
	private String hsuSubnet;
	private String gateWayIp;
	private String dataVlanId;
	private String hsuMacAddress;
	private String authenticationKey;
	private String realm;
	private String defaultPortVid;
	private String deviceType;
	private String provider;
	private String customerRadioFrequency;
	private String btsSwitchIp;
	private String baseStationIp;
	private String smsNo;
	private String poeSINo;
	private String frequency;
	private String mastHeight;
	private String btsName;
	private String btsSiteAddress;
	private String sectorIp;
	private String channelBandwidth;
	private String phaseNetural;
	private String phaseEarth;
	private String earthNeutral;
	private String poleMastHeight;
	private String typeOfPole;
	private String earthingCableLengthCustomerEnd;
	private String earthingCableLengthBtsEnd;
	private String rfCableLengthCustomerEnd;
	private String rfCableLengthBtsEnd;
	private String powerCableLengthBtsEnd;
	private String  poleMastHeightCustomerEnd;
	private String rfMakeModel;
	private String btsId;
	private String btsLat;
	private String btsLong;
	private String customerSiteLat;
	private String customerSiteLong;
	private String colorCode;


	public String getColorCode() { return colorCode; }

	public void setColorCode(String colorCode) { this.colorCode = colorCode; }

	public String getCustomerSiteLat() { return customerSiteLat; }

	public void setCustomerSiteLat(String customerSiteLat) { this.customerSiteLat = customerSiteLat; }

	public String getCustomerSiteLong() { return customerSiteLong; }

	public void setCustomerSiteLong(String customerSiteLong) { this.customerSiteLong = customerSiteLong; }

	public String getBtsLat() { return btsLat; }

	public void setBtsLat(String btsLat) { this.btsLat = btsLat; }

	public String getBtsLong() { return btsLong; }

	public void setBtsLong(String btsLong) { this.btsLong = btsLong; }

	public String getBtsSwitchIp() {
		return btsSwitchIp;
	}

	public void setBtsSwitchIp(String btsSwitchIp) {
		this.btsSwitchIp = btsSwitchIp;
	}

	public String getBaseStationIp() {
		return baseStationIp;
	}

	public void setBaseStationIp(String baseStationIp) {
		this.baseStationIp = baseStationIp;
	}

	public String getHsuIp() {
		return hsuIp;
	}

	public void setHsuIp(String hsuIp) {
		this.hsuIp = hsuIp;
	}

	public String getHsuSubnet() {
		return hsuSubnet;
	}

	public void setHsuSubnet(String hsuSubnet) {
		this.hsuSubnet = hsuSubnet;
	}

	public String getGateWayIp() {
		return gateWayIp;
	}

	public void setGateWayIp(String gateWayIp) {
		this.gateWayIp = gateWayIp;
	}

	public String getDataVlanId() {
		return dataVlanId;
	}

	public void setDataVlanId(String dataVlanId) {
		this.dataVlanId = dataVlanId;
	}

	public String getHsuMacAddress() {
		return hsuMacAddress;
	}

	public void setHsuMacAddress(String hsuMacAddress) {
		this.hsuMacAddress = hsuMacAddress;
	}

	public String getAuthenticationKey() {
		return authenticationKey;
	}

	public void setAuthenticationKey(String authenticationKey) {
		this.authenticationKey = authenticationKey;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getDefaultPortVid() {
		return defaultPortVid;
	}

	public void setDefaultPortVid(String defaultPortVid) {
		this.defaultPortVid = defaultPortVid;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getCustomerRadioFrequency() {
		return customerRadioFrequency;
	}

	public void setCustomerRadioFrequency(String customerRadioFrequency) {
		this.customerRadioFrequency = customerRadioFrequency;
	}



	private List<AttachmentIdBean> documentIds;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String rfEquipmentInstallationDate;
	private String btsConverterIp;
	private String sectorId;

	private boolean status = true;

	private String errorMessage;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getSuMacAddress() {
		return suMacAddress;
	}

	public void setSuMacAddress(String suMacAddress) {
		this.suMacAddress = suMacAddress;
	}

	public String getTypeOfAntenna() {
		return typeOfAntenna;
	}

	public void setTypeOfAntenna(String typeOfAntenna) {
		this.typeOfAntenna = typeOfAntenna;
	}

	public String getBtsSuMacAddress() {
		return btsSuMacAddress;
	}

	public void setBtsSuMacAddress(String btsSuMacAddress) {
		this.btsSuMacAddress = btsSuMacAddress;
	}

	public String getBtsSuIp() {
		return btsSuIp;
	}

	public void setBtsSuIp(String btsSuIp) {
		this.btsSuIp = btsSuIp;
	}

	public String getBtsSuSerialNumber() {
		return btsSuSerialNumber;
	}

	public void setBtsSuSerialNumber(String btsSuSerialNumber) {
		this.btsSuSerialNumber = btsSuSerialNumber;
	}

	public String getRadwinServerReachRf() {
		return radwinServerReachRf;
	}

	public void setRadwinServerReachRf(String radwinServerReachRf) {
		this.radwinServerReachRf = radwinServerReachRf;
	}

	public String getSuIp() {
		return suIp;
	}

	public void setSuIp(String suIp) {
		this.suIp = suIp;
	}

	public String getRfFrequency() {
		return rfFrequency;
	}

	public void setRfFrequency(String rfFrequency) {
		this.rfFrequency = rfFrequency;
	}

	public String getSuSerialNumber() {
		return suSerialNumber;
	}

	public void setSuSerialNumber(String suSerialNumber) {
		this.suSerialNumber = suSerialNumber;
	}

	public String getHsuHeightFromGround() {
		return hsuHeightFromGround;
	}

	public void setHsuHeightFromGround(String hsuHeightFromGround) {
		this.hsuHeightFromGround = hsuHeightFromGround;
	}

	public String getAverageMeanSeaLevel() {
		return averageMeanSeaLevel;
	}

	public void setAverageMeanSeaLevel(String averageMeanSeaLevel) {
		this.averageMeanSeaLevel = averageMeanSeaLevel;
	}

	public String getHopDistance() {
		return hopDistance;
	}

	public void setHopDistance(String hopDistance) {
		this.hopDistance = hopDistance;
	}

	public String getCableLength() {
		return cableLength;
	}

	public void setCableLength(String cableLength) {
		this.cableLength = cableLength;
	}

	public String getEthernetExtenderUsed() {
		return ethernetExtenderUsed;
	}

	public void setEthernetExtenderUsed(String ethernetExtenderUsed) {
		this.ethernetExtenderUsed = ethernetExtenderUsed;
	}

	public String getCableType() {
		return cableType;
	}

	public void setCableType(String cableType) {
		this.cableType = cableType;
	}

	public String getPatchCord() {
		return patchCord;
	}

	public void setPatchCord(String patchCord) {
		this.patchCord = patchCord;
	}

	public String getRfConnectorType() {
		return rfConnectorType;
	}

	public void setRfConnectorType(String rfConnectorType) {
		this.rfConnectorType = rfConnectorType;
	}

	public String getCustomerAcceptance() {
		return customerAcceptance;
	}

	public void setCustomerAcceptance(String customerAcceptance) {
		this.customerAcceptance = customerAcceptance;
	}

	public String getlClampPoleUsed() {
		return lClampPoleUsed;
	}

	public void setlClampPoleUsed(String lClampPoleUsed) {
		this.lClampPoleUsed = lClampPoleUsed;
	}

	public String getPoleHeight() {
		return poleHeight;
	}

	public void setPoleHeight(String poleHeight) {
		this.poleHeight = poleHeight;
	}

	public String getRfCableLength() {
		return rfCableLength;
	}

	public void setRfCableLength(String rfCableLength) {
		this.rfCableLength = rfCableLength;
	}

	public String getEarthingCableLength() {
		return earthingCableLength;
	}

	public void setEarthingCableLength(String earthingCableLength) {
		this.earthingCableLength = earthingCableLength;
	}

	public String getPowerCableLength() {
		return powerCableLength;
	}

	public void setPowerCableLength(String powerCableLength) {
		this.powerCableLength = powerCableLength;
	}

	public String getNoOfMcb() {
		return noOfMcb;
	}

	public void setNoOfMcb(String noOfMcb) {
		this.noOfMcb = noOfMcb;
	}

	public String getNoOfInstallationkits() {
		return noOfInstallationkits;
	}

	public void setNoOfInstallationkits(String noOfInstallationkits) {
		this.noOfInstallationkits = noOfInstallationkits;
	}

	public String getNoOfWeatherProofInstallationKits() {
		return noOfWeatherProofInstallationKits;
	}

	public void setNoOfWeatherProofInstallationKits(String noOfWeatherProofInstallationKits) {
		this.noOfWeatherProofInstallationKits = noOfWeatherProofInstallationKits;
	}

	public String getNoOfPatchCordUsed() {
		return noOfPatchCordUsed;
	}

	public void setNoOfPatchCordUsed(String noOfPatchCordUsed) {
		this.noOfPatchCordUsed = noOfPatchCordUsed;
	}

	public String getPvcConduit() {
		return pvcConduit;
	}

	public void setPvcConduit(String pvcConduit) {
		this.pvcConduit = pvcConduit;
	}

	public String getRfEquipmentInstallationDate() {
		return rfEquipmentInstallationDate;
	}

	public void setRfEquipmentInstallationDate(String rfEquipmentInstallationDate) {
		this.rfEquipmentInstallationDate = rfEquipmentInstallationDate;
	}

	public String getBtsConverterIp() {
		return btsConverterIp;
	}

	public void setBtsConverterIp(String btsConverterIp) {
		this.btsConverterIp = btsConverterIp;
	}

	public String getSectorId() {
		return sectorId;
	}

	public void setSectorId(String sectorId) {
		this.sectorId = sectorId;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getBtsIp() {
		return btsIp;
	}

	public void setBtsIp(String btsIp) {
		this.btsIp = btsIp;
	}

	public String getSmsNo() {
		return smsNo;
	}

	public void setSmsNo(String smsNo) {
		this.smsNo = smsNo;
	}

	public String getPoeSINo() {
		return poeSINo;
	}

	public void setPoeSINo(String poeSINo) {
		this.poeSINo = poeSINo;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	

	public String getBtsName() {
		return btsName;
	}

	public void setBtsName(String btsName) {
		this.btsName = btsName;
	}

	public String getBtsSiteAddress() {
		return btsSiteAddress;
	}

	public void setBtsSiteAddress(String btsSiteAddress) {
		this.btsSiteAddress = btsSiteAddress;
	}

	public String getSectorIp() {
		return sectorIp;
	}

	public void setSectorIp(String sectorIp) {
		this.sectorIp = sectorIp;
	}

	public String getChannelBandwidth() {
		return channelBandwidth;
	}

	public void setChannelBandwidth(String channelBandwidth) {
		this.channelBandwidth = channelBandwidth;
	}

	
	public String getPoleMastHeight() {
		return poleMastHeight;
	}

	public void setPoleMastHeight(String poleMastHeight) {
		this.poleMastHeight = poleMastHeight;
	}

	public String getTypeOfPole() {
		return typeOfPole;
	}

	public void setTypeOfPole(String typeOfPole) {
		this.typeOfPole = typeOfPole;
	}

	public String getEarthingCableLengthCustomerEnd() {
		return earthingCableLengthCustomerEnd;
	}

	public void setEarthingCableLengthCustomerEnd(String earthingCableLengthCustomerEnd) {
		this.earthingCableLengthCustomerEnd = earthingCableLengthCustomerEnd;
	}

	public String getEarthingCableLengthBtsEnd() {
		return earthingCableLengthBtsEnd;
	}

	public void setEarthingCableLengthBtsEnd(String earthingCableLengthBtsEnd) {
		this.earthingCableLengthBtsEnd = earthingCableLengthBtsEnd;
	}

	public String getRfCableLengthCustomerEnd() {
		return rfCableLengthCustomerEnd;
	}

	public void setRfCableLengthCustomerEnd(String rfCableLengthCustomerEnd) {
		this.rfCableLengthCustomerEnd = rfCableLengthCustomerEnd;
	}

	public String getRfCableLengthBtsEnd() {
		return rfCableLengthBtsEnd;
	}

	public void setRfCableLengthBtsEnd(String rfCableLengthBtsEnd) {
		this.rfCableLengthBtsEnd = rfCableLengthBtsEnd;
	}

	public String getPowerCableLengthBtsEnd() {
		return powerCableLengthBtsEnd;
	}

	public void setPowerCableLengthBtsEnd(String powerCableLengthBtsEnd) {
		this.powerCableLengthBtsEnd = powerCableLengthBtsEnd;
	}

	public String getPoleMastHeightCustomerEnd() {
		return poleMastHeightCustomerEnd;
	}

	public void setPoleMastHeightCustomerEnd(String poleMastHeightCustomerEnd) {
		this.poleMastHeightCustomerEnd = poleMastHeightCustomerEnd;
	}

	public String getRfMakeModel() {
		return rfMakeModel;
	}

	public void setRfMakeModel(String rfMakeModel) {
		this.rfMakeModel = rfMakeModel;
	}

	public String getMastHeight() {
		return mastHeight;
	}

	public void setMastHeight(String mastHeight) {
		this.mastHeight = mastHeight;
	}

	public String getPhaseNetural() {
		return phaseNetural;
	}

	public void setPhaseNetural(String phaseNetural) {
		this.phaseNetural = phaseNetural;
	}

	public String getPhaseEarth() {
		return phaseEarth;
	}

	public void setPhaseEarth(String phaseEarth) {
		this.phaseEarth = phaseEarth;
	}

	public String getEarthNeutral() {
		return earthNeutral;
	}

	public void setEarthNeutral(String earthNeutral) {
		this.earthNeutral = earthNeutral;
	}

	public String getBtsId() { return btsId; }

	public void setBtsId(String btsId) { this.btsId = btsId; }
}

