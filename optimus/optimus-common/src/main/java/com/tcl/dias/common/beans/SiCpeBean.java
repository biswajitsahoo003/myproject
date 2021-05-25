package com.tcl.dias.common.beans;

import java.io.Serializable;
/**
 * 
 * This is the bean class created for SDWAN to get the CPE details
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SiCpeBean implements Serializable{
	private String model;
	private String serialNo;
	private String scope;
	private String supportType;
	private String isShared;
	private String suggestedModel;
	private String cpe;
	private String rackmount;
	private String nmc;
	private String sfp;
	private String sfpplus;
	private String powerCord;
	private String sfpDesc;
	private String nmcDesc;
	private String rackmountDesc;
	private String sfpPlusDesc;
	private String cpeDesc;
	private String powerCordDesc;
	private Integer l2Ports;
	private Integer l3Ports;
	private String cpeMaxBw;
	private String cpeModelEndOfSale;
	private String cpeModelEndOfLife;
	
	
	public String getCpeModelEndOfSale() {
		return cpeModelEndOfSale;
	}
	public void setCpeModelEndOfSale(String cpeModelEndOfSale) {
		this.cpeModelEndOfSale = cpeModelEndOfSale;
	}
	public String getCpeModelEndOfLife() {
		return cpeModelEndOfLife;
	}
	public void setCpeModelEndOfLife(String cpeModelEndOfLife) {
		this.cpeModelEndOfLife = cpeModelEndOfLife;
	}
	public String getPowerCordDesc() {
		return powerCordDesc;
	}
	public void setPowerCordDesc(String powerCordDesc) {
		this.powerCordDesc = powerCordDesc;
	}
	public String getSfpDesc() {
		return sfpDesc;
	}
	public void setSfpDesc(String sfpDesc) {
		this.sfpDesc = sfpDesc;
	}
	public String getNmcDesc() {
		return nmcDesc;
	}
	public void setNmcDesc(String nmcDesc) {
		this.nmcDesc = nmcDesc;
	}
	public String getRackmountDesc() {
		return rackmountDesc;
	}
	public void setRackmountDesc(String rackmountDesc) {
		this.rackmountDesc = rackmountDesc;
	}
	public String getSfpPlusDesc() {
		return sfpPlusDesc;
	}
	public void setSfpPlusDesc(String sfpPlusDesc) {
		this.sfpPlusDesc = sfpPlusDesc;
	}
	public String getCpeDesc() {
		return cpeDesc;
	}
	public void setCpeDesc(String cpeDesc) {
		this.cpeDesc = cpeDesc;
	}
	public String getPowerCord() {
		return powerCord;
	}
	public void setPowerCord(String powerCord) {
		this.powerCord = powerCord;
	}
	public String getCpe() {
		return cpe;
	}
	public void setCpe(String cpe) {
		this.cpe = cpe;
	}
	public String getRackmount() {
		return rackmount;
	}
	public void setRackmount(String rackmount) {
		this.rackmount = rackmount;
	}
	public String getNmc() {
		return nmc;
	}
	public void setNmc(String nmc) {
		this.nmc = nmc;
	}
	public String getSfp() {
		return sfp;
	}
	public void setSfp(String sfp) {
		this.sfp = sfp;
	}
	public String getSfpplus() {
		return sfpplus;
	}
	public void setSfpplus(String sfpplus) {
		this.sfpplus = sfpplus;
	}
	public String getSuggestedModel() {
		return suggestedModel;
	}
	public void setSuggestedModel(String suggestedModel) {
		this.suggestedModel = suggestedModel;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getSupportType() {
		return supportType;
	}
	public void setSupportType(String supportType) {
		this.supportType = supportType;
	}
	public String getIsShared() {
		return isShared;
	}
	public void setIsShared(String isShared) {
		this.isShared = isShared;
	}
	public Integer getL2Ports() {
		return l2Ports;
	}
	public void setL2Ports(Integer l2Ports) {
		this.l2Ports = l2Ports;
	}
	public Integer getL3Ports() {
		return l3Ports;
	}
	public void setL3Ports(Integer l3Ports) {
		this.l3Ports = l3Ports;
	}
	public String getCpeMaxBw() {
		return cpeMaxBw;
	}
	public void setCpeMaxBw(String cpeMaxBw) {
		this.cpeMaxBw = cpeMaxBw;
	}
	
}
