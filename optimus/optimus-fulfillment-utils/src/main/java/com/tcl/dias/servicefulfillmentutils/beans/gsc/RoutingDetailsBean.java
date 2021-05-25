package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import java.util.List;

public class RoutingDetailsBean {

	private String requiredAction;
	private String platform;
	private String serviceId;
	private String pcc;
	private Integer callTypeNo;
	private String mainIncomingDigits;
	private String serviceName;
	private String tollfreeNumber;
	
	private List<InRoutesbean> inRoutes;
	
	private String forcedOutRouteLabelId;
	private String outRouteNbRoutesPerCall ;
	private String outRoutePrioritizationType ;
	private String outRouteAction;
	private String outRouteScriptId ;
	private String outRoutePartition ;
	private String outRouteAttributes ;
	private String outRouteRoutingCriteria ;
	private String outRouteNbRoutesRequested  ;
	private String outRouteEndPointsRequired ;
	private String outRouteEndPointFormat ;
	private String sid ;
	private String cid ;
	
	private List<OutRouteEndPointsBean> outRouteEndPoints;
	
	private String outpulseType;
	private Integer outpulseNbDigits;
	private String outpulsePrefix;
	private String outpulseCustomDigits;
	private String outpulseCustomDigitsPccPosition;
	private String clidType;
	private String clidCustomDigits;
	private String clidCustomDigitsPccPosition;
	private String clidNatureOfAddressType;
	private String clidNumberingPlanIndicatorType;
	private String clidPresentationType;
	private String clidScreeningType;
	private String billingNumberType;
	private String billingNumberCustomDigits;
	private String billingNumberCustomDigitsPccPosition;
	private String billingNumberNatureOfAddressType;
	private String billingNumberNumberingPlanIndicatorType;
	private String oliType;
	private String connectionDtgHeader;
	
	
	public Integer getOutpulseNbDigits() {
		return outpulseNbDigits;
	}
	public void setOutpulseNbDigits(Integer outpulseNbDigits) {
		this.outpulseNbDigits = outpulseNbDigits;
	}
	public String getRequiredAction() {
		return requiredAction;
	}
	public void setRequiredAction(String requiredAction) {
		this.requiredAction = requiredAction;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getPcc() {
		return pcc;
	}
	public void setPcc(String pcc) {
		this.pcc = pcc;
	}
	public Integer getCallTypeNo() {
		return callTypeNo;
	}
	public void setCallTypeNo(Integer callTypeNo) {
		this.callTypeNo = callTypeNo;
	}
	public String getMainIncomingDigits() {
		return mainIncomingDigits;
	}
	public void setMainIncomingDigits(String mainIncomingDigits) {
		this.mainIncomingDigits = mainIncomingDigits;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getTollfreeNumber() {
		return tollfreeNumber;
	}
	public void setTollfreeNumber(String tollfreeNumber) {
		this.tollfreeNumber = tollfreeNumber;
	}
	public List<InRoutesbean> getInRoutes() {
		return inRoutes;
	}
	public void setInRoutes(List<InRoutesbean> inRoutes) {
		this.inRoutes = inRoutes;
	}
	public String getForcedOutRouteLabelId() {
		return forcedOutRouteLabelId;
	}
	public void setForcedOutRouteLabelId(String forcedOutRouteLabelId) {
		this.forcedOutRouteLabelId = forcedOutRouteLabelId;
	}
	public String getOutRouteNbRoutesPerCall() {
		return outRouteNbRoutesPerCall;
	}
	public void setOutRouteNbRoutesPerCall(String outRouteNbRoutesPerCall) {
		this.outRouteNbRoutesPerCall = outRouteNbRoutesPerCall;
	}
	public String getOutRoutePrioritizationType() {
		return outRoutePrioritizationType;
	}
	public void setOutRoutePrioritizationType(String outRoutePrioritizationType) {
		this.outRoutePrioritizationType = outRoutePrioritizationType;
	}
	public String getOutRouteAction() {
		return outRouteAction;
	}
	public void setOutRouteAction(String outRouteAction) {
		this.outRouteAction = outRouteAction;
	}
	public String getOutRouteScriptId() {
		return outRouteScriptId;
	}
	public void setOutRouteScriptId(String outRouteScriptId) {
		this.outRouteScriptId = outRouteScriptId;
	}
	public String getOutRoutePartition() {
		return outRoutePartition;
	}
	public void setOutRoutePartition(String outRoutePartition) {
		this.outRoutePartition = outRoutePartition;
	}
	public String getOutRouteAttributes() {
		return outRouteAttributes;
	}
	public void setOutRouteAttributes(String outRouteAttributes) {
		this.outRouteAttributes = outRouteAttributes;
	}
	public String getOutRouteRoutingCriteria() {
		return outRouteRoutingCriteria;
	}
	public void setOutRouteRoutingCriteria(String outRouteRoutingCriteria) {
		this.outRouteRoutingCriteria = outRouteRoutingCriteria;
	}
	public String getOutRouteNbRoutesRequested() {
		return outRouteNbRoutesRequested;
	}
	public void setOutRouteNbRoutesRequested(String outRouteNbRoutesRequested) {
		this.outRouteNbRoutesRequested = outRouteNbRoutesRequested;
	}
	public String getOutRouteEndPointsRequired() {
		return outRouteEndPointsRequired;
	}
	public void setOutRouteEndPointsRequired(String outRouteEndPointsRequired) {
		this.outRouteEndPointsRequired = outRouteEndPointsRequired;
	}
	public String getOutRouteEndPointFormat() {
		return outRouteEndPointFormat;
	}
	public void setOutRouteEndPointFormat(String outRouteEndPointFormat) {
		this.outRouteEndPointFormat = outRouteEndPointFormat;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public List<OutRouteEndPointsBean> getOutRouteEndPoints() {
		return outRouteEndPoints;
	}
	public void setOutRouteEndPoints(List<OutRouteEndPointsBean> outRouteEndPoints) {
		this.outRouteEndPoints = outRouteEndPoints;
	}
	public String getOutpulseType() {
		return outpulseType;
	}
	public void setOutpulseType(String outpulseType) {
		this.outpulseType = outpulseType;
	}
	public String getOutpulsePrefix() {
		return outpulsePrefix;
	}
	public void setOutpulsePrefix(String outpulsePrefix) {
		this.outpulsePrefix = outpulsePrefix;
	}
	public String getOutpulseCustomDigits() {
		return outpulseCustomDigits;
	}
	public void setOutpulseCustomDigits(String outpulseCustomDigits) {
		this.outpulseCustomDigits = outpulseCustomDigits;
	}
	public String getOutpulseCustomDigitsPccPosition() {
		return outpulseCustomDigitsPccPosition;
	}
	public void setOutpulseCustomDigitsPccPosition(String outpulseCustomDigitsPccPosition) {
		this.outpulseCustomDigitsPccPosition = outpulseCustomDigitsPccPosition;
	}
	public String getClidType() {
		return clidType;
	}
	public void setClidType(String clidType) {
		this.clidType = clidType;
	}
	public String getClidCustomDigits() {
		return clidCustomDigits;
	}
	public void setClidCustomDigits(String clidCustomDigits) {
		this.clidCustomDigits = clidCustomDigits;
	}
	public String getClidCustomDigitsPccPosition() {
		return clidCustomDigitsPccPosition;
	}
	public void setClidCustomDigitsPccPosition(String clidCustomDigitsPccPosition) {
		this.clidCustomDigitsPccPosition = clidCustomDigitsPccPosition;
	}
	public String getClidNatureOfAddressType() {
		return clidNatureOfAddressType;
	}
	public void setClidNatureOfAddressType(String clidNatureOfAddressType) {
		this.clidNatureOfAddressType = clidNatureOfAddressType;
	}
	public String getClidNumberingPlanIndicatorType() {
		return clidNumberingPlanIndicatorType;
	}
	public void setClidNumberingPlanIndicatorType(String clidNumberingPlanIndicatorType) {
		this.clidNumberingPlanIndicatorType = clidNumberingPlanIndicatorType;
	}
	public String getClidPresentationType() {
		return clidPresentationType;
	}
	public void setClidPresentationType(String clidPresentationType) {
		this.clidPresentationType = clidPresentationType;
	}
	public String getClidScreeningType() {
		return clidScreeningType;
	}
	public void setClidScreeningType(String clidScreeningType) {
		this.clidScreeningType = clidScreeningType;
	}
	public String getBillingNumberType() {
		return billingNumberType;
	}
	public void setBillingNumberType(String billingNumberType) {
		this.billingNumberType = billingNumberType;
	}
	public String getBillingNumberCustomDigits() {
		return billingNumberCustomDigits;
	}
	public void setBillingNumberCustomDigits(String billingNumberCustomDigits) {
		this.billingNumberCustomDigits = billingNumberCustomDigits;
	}
	public String getBillingNumberCustomDigitsPccPosition() {
		return billingNumberCustomDigitsPccPosition;
	}
	public void setBillingNumberCustomDigitsPccPosition(String billingNumberCustomDigitsPccPosition) {
		this.billingNumberCustomDigitsPccPosition = billingNumberCustomDigitsPccPosition;
	}
	public String getBillingNumberNatureOfAddressType() {
		return billingNumberNatureOfAddressType;
	}
	public void setBillingNumberNatureOfAddressType(String billingNumberNatureOfAddressType) {
		this.billingNumberNatureOfAddressType = billingNumberNatureOfAddressType;
	}
	public String getBillingNumberNumberingPlanIndicatorType() {
		return billingNumberNumberingPlanIndicatorType;
	}
	public void setBillingNumberNumberingPlanIndicatorType(String billingNumberNumberingPlanIndicatorType) {
		this.billingNumberNumberingPlanIndicatorType = billingNumberNumberingPlanIndicatorType;
	}
	public String getOliType() {
		return oliType;
	}
	public void setOliType(String oliType) {
		this.oliType = oliType;
	}
	public String getConnectionDtgHeader() {
		return connectionDtgHeader;
	}
	public void setConnectionDtgHeader(String connectionDtgHeader) {
		this.connectionDtgHeader = connectionDtgHeader;
	}
	
	
	
}
