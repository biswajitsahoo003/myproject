package com.tcl.dias.servicefulfillment.beans;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * TestOnnetWirelessLMBean - Bean to conduct test for lm onnet wireless
 *
 * @author Yogesh 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class TestOnnetWirelessLMBean extends TaskDetailsBaseBean {

	private String rfSignalRemark;
	
	private String rslPerLinkDist;
	
	private String radwinServerReachRF;
	
	private List<AttachmentIdBean> documentIds;
	
	//private List<AttachmentIdBean> stabilityTestResults;
	
	private String primaryColorcode;
	
	private String secondaryColorcode;

	private String reflector;
	
	private String rfTestStatus;
	
	private String rfTestFailureRemarks;
	
	private String mastHeight;
	
	private String btsName;
	
	private String btsSiteAddress;
	
	private String sectorID;
	
	private String sectorIP;
	
	private String suMacAddress;
	
	private String btsSuMacAddress;
	
	private String btsSuIP;
	
	private String suIp;

	private String rfFrequency;
	
	private String btsConverterIP;

	private String throughputAcceptance;

	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String rfTestingDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String dateOfAcceptance;
	
	public String getRfSignalRemark() {
		return rfSignalRemark;
	}

	public void setRfSignalRemark(String rfSignalRemark) {
		this.rfSignalRemark = rfSignalRemark;
	}

	public String getRslPerLinkDist() {
		return rslPerLinkDist;
	}

	public void setRslPerLinkDist(String rslPerLinkDist) {
		this.rslPerLinkDist = rslPerLinkDist;
	}

	public String getRadwinServerReachRF() {
		return radwinServerReachRF;
	}

	public void setRadwinServerReachRF(String radwinServerReachRF) {
		this.radwinServerReachRF = radwinServerReachRF;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getPrimaryColorcode() {
		return primaryColorcode;
	}

	public void setPrimaryColorcode(String primaryColorcode) {
		this.primaryColorcode = primaryColorcode;
	}

	public String getSecondaryColorcode() {
		return secondaryColorcode;
	}

	public void setSecondaryColorcode(String secondaryColorcode) {
		this.secondaryColorcode = secondaryColorcode;
	}

	public String getRfTestFailureRemarks() {
		return rfTestFailureRemarks;
	}

	public void setRfTestFailureRemarks(String rfTestFailureRemarks) {
		this.rfTestFailureRemarks = rfTestFailureRemarks;
	}

	public String getRfTestingDate() {
		return rfTestingDate;
	}

	public void setRfTestingDate(String rfTestingDate) {
		this.rfTestingDate = rfTestingDate;
	}

	public String getReflector() {
		return reflector;
	}

	public void setReflector(String reflector) {
		this.reflector = reflector;
	}

	public String getRfTestStatus() {
		return rfTestStatus;
	}

	public void setRfTestStatus(String rfTestStatus) {
		this.rfTestStatus = rfTestStatus;
	}

	public String getMastHeight() {
		return mastHeight;
	}

	public void setMastHeight(String mastHeight) {
		this.mastHeight = mastHeight;
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

	public String getSectorID() {
		return sectorID;
	}

	public void setSectorID(String sectorID) {
		this.sectorID = sectorID;
	}

	public String getSectorIP() {
		return sectorIP;
	}

	public void setSectorIP(String sectorIP) {
		this.sectorIP = sectorIP;
	}

	public String getSuMacAddress() {
		return suMacAddress;
	}

	public void setSuMacAddress(String suMacAddress) {
		this.suMacAddress = suMacAddress;
	}

	public String getBtsSuMacAddress() {
		return btsSuMacAddress;
	}

	public void setBtsSuMacAddress(String btsSuMacAddress) {
		this.btsSuMacAddress = btsSuMacAddress;
	}

	public String getBtsSuIP() {
		return btsSuIP;
	}

	public void setBtsSuIP(String btsSuIP) {
		this.btsSuIP = btsSuIP;
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

	public String getBtsConverterIP() {
		return btsConverterIP;
	}

	public void setBtsConverterIP(String btsConverterIP) {
		this.btsConverterIP = btsConverterIP;
	}

	public String getThroughputAcceptance() { return throughputAcceptance;}

	public void setThroughputAcceptance(String throughputAcceptance) { this.throughputAcceptance = throughputAcceptance; }

	public String getDateOfAcceptance() { return dateOfAcceptance; }

	public void setDateOfAcceptance(String dateOfAcceptance) { this.dateOfAcceptance = dateOfAcceptance; }
}
