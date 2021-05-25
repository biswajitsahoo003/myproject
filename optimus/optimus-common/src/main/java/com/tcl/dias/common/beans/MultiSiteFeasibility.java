package com.tcl.dias.common.beans;

public class MultiSiteFeasibility {
	
	
	private String feasibilityStatus;
	private String feasibilityType;
	private String feasibilityValidity;
	private String feasibilityCode;
	private String feasibilityMode;
	private String type;//Type --primary/secondary
	private String provider;
	private String cd;//CD
	private String responseType; //Response Type
	private String tclPop;
	private String feasibilityCheck;
	private String connectedBuilding;
	private String connectedCustomer;
	private String ospDistance;
	private String ospCapex;//OSP CAPEX
	private String inBuildingCapex;
	private String muxCost;
	private String prowValueOtc;
	private String prowValueArc;
	private String mastHeight;
	private String mastNrc;
	private String offnetNrc;
	private String offnetArc;
	private String siteLevelActions;
	private String feasibilityRemarks;
	
	
	private String feasibilityBStatus;
	private String feasibilityBType;
	private String feasibilityBValidity;
	private String feasibilityBCode;
	private String feasibilityBMode;
	private String typeB;//Type --primary/secondary
	private String cdB;//CD
	private String responseBType; //Response Type
	private String tclBPop;
	private String feasibilityBCheck;
	private String connectedBBuilding;
	private String connectedBCustomer;
	private String ospBDistance;
	private String ospBCapex;//OSP CAPEX
	private String inBuildingBCapex;
	private String muxCostB;
	private String prowValueBOtc;
	private String prowValueBArc;
	private String mastBHeight;
	private String mastBNrc;
	private String offnetBNrc;
	private String offnetBArc;
	private String feasibilityBRemarks;
	
	//for adding npl/nde links
	private String feasibilityTypeb;
	private String feasibilityModeb;
	private String providerb;
	
	
	
	public String getCd() {
		return cd;
	}
	public void setCd(String cd) {
		this.cd = cd;
	}
	public String getFeasibilityTypeb() {
		return feasibilityTypeb;
	}
	public void setFeasibilityTypeb(String feasibilityTypeb) {
		this.feasibilityTypeb = feasibilityTypeb;
	}
	public String getFeasibilityModeb() {
		return feasibilityModeb;
	}
	public void setFeasibilityModeb(String feasibilityModeb) {
		this.feasibilityModeb = feasibilityModeb;
	}
	public String getProviderb() {
		return providerb;
	}
	public void setProviderb(String providerb) {
		this.providerb = providerb;
	}
	
	public String getResponseType() {
		return responseType;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	
	
	public String getFeasibilityStatus() {
		return feasibilityStatus;
	}
	public void setFeasibilityStatus(String feasibilityStatus) {
		this.feasibilityStatus = feasibilityStatus;
	}
	public String getFeasibilityType() {
		return feasibilityType;
	}
	public void setFeasibilityType(String feasibilityType) {
		this.feasibilityType = feasibilityType;
	}
	public String getFeasibilityValidity() {
		return feasibilityValidity;
	}
	public void setFeasibilityValidity(String feasibilityValidity) {
		this.feasibilityValidity = feasibilityValidity;
	}
	public String getFeasibilityCode() {
		return feasibilityCode;
	}
	public void setFeasibilityCode(String feasibilityCode) {
		this.feasibilityCode = feasibilityCode;
	}
	public String getFeasibilityMode() {
		return feasibilityMode;
	}
	public void setFeasibilityMode(String feasibilityMode) {
		this.feasibilityMode = feasibilityMode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getFeasibilityCheck() {
		return feasibilityCheck;
	}
	public void setFeasibilityCheck(String feasibilityCheck) {
		this.feasibilityCheck = feasibilityCheck;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getTclPop() {
		return tclPop;
	}
	public void setTclPop(String tclPop) {
		this.tclPop = tclPop;
	}
	
	public String getConnectedBuilding() {
		return connectedBuilding;
	}
	public void setConnectedBuilding(String connectedBuilding) {
		this.connectedBuilding = connectedBuilding;
	}
	public String getConnectedCustomer() {
		return connectedCustomer;
	}
	public void setConnectedCustomer(String connectedCustomer) {
		this.connectedCustomer = connectedCustomer;
	}
	
	
	public String getOspCapex() {
		return ospCapex;
	}
	public void setOspCapex(String ospCapex) {
		this.ospCapex = ospCapex;
	}
	public void setOspBCapex(String ospBCapex) {
		this.ospBCapex = ospBCapex;
	}
	
	public String getProwValueOtc() {
		return prowValueOtc;
	}
	public void setProwValueOtc(String prowValueOtc) {
		this.prowValueOtc = prowValueOtc;
	}
	public String getProwValueArc() {
		return prowValueArc;
	}
	public void setProwValueArc(String prowValueArc) {
		this.prowValueArc = prowValueArc;
	}
	
	public String getOffnetNrc() {
		return offnetNrc;
	}
	public void setOffnetNrc(String offnetNrc) {
		this.offnetNrc = offnetNrc;
	}
	public String getOffnetArc() {
		return offnetArc;
	}
	public void setOffnetArc(String offnetArc) {
		this.offnetArc = offnetArc;
	}
	public String getSiteLevelActions() {
		return siteLevelActions;
	}
	public void setSiteLevelActions(String siteLevelActions) {
		this.siteLevelActions = siteLevelActions;
	}
	public String getFeasibilityRemarks() {
		return feasibilityRemarks;
	}
	public void setFeasibilityRemarks(String feasibilityRemarks) {
		this.feasibilityRemarks = feasibilityRemarks;
	}
	public String getFeasibilityBStatus() {
		return feasibilityBStatus;
	}
	public void setFeasibilityBStatus(String feasibilityBStatus) {
		this.feasibilityBStatus = feasibilityBStatus;
	}
	public String getFeasibilityBType() {
		return feasibilityBType;
	}
	public void setFeasibilityBType(String feasibilityBType) {
		this.feasibilityBType = feasibilityBType;
	}
	public String getFeasibilityBValidity() {
		return feasibilityBValidity;
	}
	public void setFeasibilityBValidity(String feasibilityBValidity) {
		this.feasibilityBValidity = feasibilityBValidity;
	}
	public String getFeasibilityBCode() {
		return feasibilityBCode;
	}
	public void setFeasibilityBCode(String feasibilityBCode) {
		this.feasibilityBCode = feasibilityBCode;
	}
	public String getFeasibilityBMode() {
		return feasibilityBMode;
	}
	public void setFeasibilityBMode(String feasibilityBMode) {
		this.feasibilityBMode = feasibilityBMode;
	}
	public String getTypeB() {
		return typeB;
	}
	public void setTypeB(String typeB) {
		this.typeB = typeB;
	}
	public String getCdB() {
		return cdB;
	}
	public void setCdB(String cdB) {
		this.cdB = cdB;
	}
	public String getResponseBType() {
		return responseBType;
	}
	public void setResponseBType(String responseBType) {
		this.responseBType = responseBType;
	}
	public String getTclBPop() {
		return tclBPop;
	}
	public void setTclBPop(String tclBPop) {
		this.tclBPop = tclBPop;
	}
	public String getFeasibilityBCheck() {
		return feasibilityBCheck;
	}
	public void setFeasibilityBCheck(String feasibilityBCheck) {
		this.feasibilityBCheck = feasibilityBCheck;
	}
	
	public String getConnectedBBuilding() {
		return connectedBBuilding;
	}
	public void setConnectedBBuilding(String connectedBBuilding) {
		this.connectedBBuilding = connectedBBuilding;
	}
	public String getConnectedBCustomer() {
		return connectedBCustomer;
	}
	public void setConnectedBCustomer(String connectedBCustomer) {
		this.connectedBCustomer = connectedBCustomer;
	}
	
	public String getProwValueBOtc() {
		return prowValueBOtc;
	}
	public void setProwValueBOtc(String prowValueBOtc) {
		this.prowValueBOtc = prowValueBOtc;
	}
	public String getProwValueBArc() {
		return prowValueBArc;
	}
	public void setProwValueBArc(String prowValueBArc) {
		this.prowValueBArc = prowValueBArc;
	}
	
	public String getOffnetBNrc() {
		return offnetBNrc;
	}
	public void setOffnetBNrc(String offnetBNrc) {
		this.offnetBNrc = offnetBNrc;
	}
	public String getOffnetBArc() {
		return offnetBArc;
	}
	public void setOffnetBArc(String offnetBArc) {
		this.offnetBArc = offnetBArc;
	}
	public String getFeasibilityBRemarks() {
		return feasibilityBRemarks;
	}
	public void setFeasibilityBRemarks(String feasibilityBRemarks) {
		this.feasibilityBRemarks = feasibilityBRemarks;
	}
	public String getOspBCapex() {
		return ospBCapex;
	}
	public String getOspDistance() {
		return ospDistance;
	}
	public void setOspDistance(String ospDistance) {
		this.ospDistance = ospDistance;
	}
	public String getInBuildingCapex() {
		return inBuildingCapex;
	}
	public void setInBuildingCapex(String inBuildingCapex) {
		this.inBuildingCapex = inBuildingCapex;
	}
	public String getMuxCost() {
		return muxCost;
	}
	public void setMuxCost(String muxCost) {
		this.muxCost = muxCost;
	}
	public String getMastHeight() {
		return mastHeight;
	}
	public void setMastHeight(String mastHeight) {
		this.mastHeight = mastHeight;
	}
	public String getMastNrc() {
		return mastNrc;
	}
	public void setMastNrc(String mastNrc) {
		this.mastNrc = mastNrc;
	}
	public String getOspBDistance() {
		return ospBDistance;
	}
	public void setOspBDistance(String ospBDistance) {
		this.ospBDistance = ospBDistance;
	}
	public String getInBuildingBCapex() {
		return inBuildingBCapex;
	}
	public void setInBuildingBCapex(String inBuildingBCapex) {
		this.inBuildingBCapex = inBuildingBCapex;
	}
	public String getMuxCostB() {
		return muxCostB;
	}
	public void setMuxCostB(String muxCostB) {
		this.muxCostB = muxCostB;
	}
	public String getMastBHeight() {
		return mastBHeight;
	}
	public void setMastBHeight(String mastBHeight) {
		this.mastBHeight = mastBHeight;
	}
	public String getMastBNrc() {
		return mastBNrc;
	}
	public void setMastBNrc(String mastBNrc) {
		this.mastBNrc = mastBNrc;
	}

}
