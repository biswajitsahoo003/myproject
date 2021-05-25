package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class SharedInCircuitBean {
	private String tataSwtchUnitCd;
	private String linkSwitchUnit;
	private String circuitGroupCd;
	private String servAbbr;
	private String ownerType;
	private String direction;
	private String cgClass;
	private String cgRole;
	private String roleDirectionRestriction;
	private String sharedVirTrigger;
	private Integer incomingPrfxLen;
	private Integer outgoingPrfxLen;
	private Integer inCMS;
	private Integer outCMS;
	private Integer invoiceGroup;
	private String incomingTrafficType;
	private String trnsmType;
	private String trnsmCd;
	private String scrnClassCd;
	private String signalCd;
	private String category;
	private String cgType;
	private Boolean isVoiceInternetProtocol;
	private String clliType;
	private Integer circtGrId;
	private String popNo;
	private Boolean isDomesticCLI;
	private String connectType;
	private String originCountry;
	private Integer opcPointCodeDCM;
	private Integer dpcPointCodeDCM;
	private Integer requiredPortQuantity;
	private Integer actualPortQuantity;
	private List<IPAddressBean> ipAddresses;
	
	public String getTataSwtchUnitCd() {
		return tataSwtchUnitCd;
	}
	public void setTataSwtchUnitCd(String tataSwtchUnitCd) {
		this.tataSwtchUnitCd = tataSwtchUnitCd;
	}
	public String getLinkSwitchUnit() {
		return linkSwitchUnit;
	}
	public void setLinkSwitchUnit(String linkSwitchUnit) {
		this.linkSwitchUnit = linkSwitchUnit;
	}
	public String getCircuitGroupCd() {
		return circuitGroupCd;
	}
	public void setCircuitGroupCd(String circuitGroupCd) {
		this.circuitGroupCd = circuitGroupCd;
	}
	public String getServAbbr() {
		return servAbbr;
	}
	public void setServAbbr(String servAbbr) {
		this.servAbbr = servAbbr;
	}
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getCgClass() {
		return cgClass;
	}
	public void setCgClass(String cgClass) {
		this.cgClass = cgClass;
	}
	public String getCgRole() {
		return cgRole;
	}
	public void setCgRole(String cgRole) {
		this.cgRole = cgRole;
	}
	public String getRoleDirectionRestriction() {
		return roleDirectionRestriction;
	}
	public void setRoleDirectionRestriction(String roleDirectionRestriction) {
		this.roleDirectionRestriction = roleDirectionRestriction;
	}
	public String getSharedVirTrigger() {
		return sharedVirTrigger;
	}
	public void setSharedVirTrigger(String sharedVirTrigger) {
		this.sharedVirTrigger = sharedVirTrigger;
	}
	public Integer getIncomingPrfxLen() {
		return incomingPrfxLen;
	}
	public void setIncomingPrfxLen(Integer incomingPrfxLen) {
		this.incomingPrfxLen = incomingPrfxLen;
	}
	public Integer getOutgoingPrfxLen() {
		return outgoingPrfxLen;
	}
	public void setOutgoingPrfxLen(Integer outgoingPrfxLen) {
		this.outgoingPrfxLen = outgoingPrfxLen;
	}
	public Integer getInCMS() {
		return inCMS;
	}
	public void setInCMS(Integer inCMS) {
		this.inCMS = inCMS;
	}
	public Integer getOutCMS() {
		return outCMS;
	}
	public void setOutCMS(Integer outCMS) {
		this.outCMS = outCMS;
	}
	public Integer getInvoiceGroup() {
		return invoiceGroup;
	}
	public void setInvoiceGroup(Integer invoiceGroup) {
		this.invoiceGroup = invoiceGroup;
	}
	public String getIncomingTrafficType() {
		return incomingTrafficType;
	}
	public void setIncomingTrafficType(String incomingTrafficType) {
		this.incomingTrafficType = incomingTrafficType;
	}
	public String getTrnsmType() {
		return trnsmType;
	}
	public void setTrnsmType(String trnsmType) {
		this.trnsmType = trnsmType;
	}
	public String getTrnsmCd() {
		return trnsmCd;
	}
	public void setTrnsmCd(String trnsmCd) {
		this.trnsmCd = trnsmCd;
	}
	public String getScrnClassCd() {
		return scrnClassCd;
	}
	public void setScrnClassCd(String scrnClassCd) {
		this.scrnClassCd = scrnClassCd;
	}
	public String getSignalCd() {
		return signalCd;
	}
	public void setSignalCd(String signalCd) {
		this.signalCd = signalCd;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCgType() {
		return cgType;
	}
	public void setCgType(String cgType) {
		this.cgType = cgType;
	}
	public Boolean getIsVoiceInternetProtocol() {
		return isVoiceInternetProtocol;
	}
	public void setIsVoiceInternetProtocol(Boolean isVoiceInternetProtocol) {
		this.isVoiceInternetProtocol = isVoiceInternetProtocol;
	}
	public String getClliType() {
		return clliType;
	}
	public void setClliType(String clliType) {
		this.clliType = clliType;
	}
	public Integer getCirctGrId() {
		return circtGrId;
	}
	public void setCirctGrId(Integer circtGrId) {
		this.circtGrId = circtGrId;
	}
	public String getPopNo() {
		return popNo;
	}
	public void setPopNo(String popNo) {
		this.popNo = popNo;
	}
	public Boolean getIsDomesticCLI() {
		return isDomesticCLI;
	}
	public void setIsDomesticCLI(Boolean isDomesticCLI) {
		this.isDomesticCLI = isDomesticCLI;
	}
	public String getConnectType() {
		return connectType;
	}
	public void setConnectType(String connectType) {
		this.connectType = connectType;
	}
	public String getOriginCountry() {
		return originCountry;
	}
	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}
	public Integer getOpcPointCodeDCM() {
		return opcPointCodeDCM;
	}
	public void setOpcPointCodeDCM(Integer opcPointCodeDCM) {
		this.opcPointCodeDCM = opcPointCodeDCM;
	}
	public Integer getDpcPointCodeDCM() {
		return dpcPointCodeDCM;
	}
	public void setDpcPointCodeDCM(Integer dpcPointCodeDCM) {
		this.dpcPointCodeDCM = dpcPointCodeDCM;
	}
	public Integer getRequiredPortQuantity() {
		return requiredPortQuantity;
	}
	public void setRequiredPortQuantity(Integer requiredPortQuantity) {
		this.requiredPortQuantity = requiredPortQuantity;
	}
	public Integer getActualPortQuantity() {
		return actualPortQuantity;
	}
	public void setActualPortQuantity(Integer actualPortQuantity) {
		this.actualPortQuantity = actualPortQuantity;
	}
	public List<IPAddressBean> getIpAddresses() {
		return ipAddresses;
	}
	public void setIpAddresses(List<IPAddressBean> ipAddresses) {
		this.ipAddresses = ipAddresses;
	}
	
}
