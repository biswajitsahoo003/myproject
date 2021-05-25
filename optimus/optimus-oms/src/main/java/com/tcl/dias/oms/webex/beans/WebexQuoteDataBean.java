package com.tcl.dias.oms.webex.beans;

import java.util.List;

import com.tcl.dias.oms.gsc.beans.GscQuoteToLeBean;
import com.tcl.dias.oms.gsc.beans.GscSolutionBean;
import com.tcl.dias.oms.gvpn.pdf.beans.GvpnQuotePdfBean;

/**
 * Quote Data Bean which is contains quote information and product solution
 * details.
 * 
 * @author arjayapa
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class WebexQuoteDataBean {

	private Integer quoteId;
	private Integer quoteLeId;
	private String quoteCode;
	private Integer customerId;
	private String productFamilyName;
	private String accessType;
	private String profileName;
	private Integer quoteVersion;
	private List<GscSolutionBean> solutions;
	private WebexSolutionBean webexSolution;
	private List<GscQuoteToLeBean> legalEntities;
	private GvpnQuotePdfBean gvpnQuotes;
	private String quoteType;
	private String quoteCategory;
	private Boolean isDocusign;
	private Boolean isManualCofSigned = false;
	// Only for GSIP MACD case
	private Integer supplierLegalId;
	private String engagementOptyId;
	private String classification;
	// Webex related attributes
	private Integer licenseQuantity;
	private String licenseProvider;
	private String primaryBridge;
	private String audioGreeting;
	private String paymentModel;
	private String audioType;
	private Boolean cugRequired;
	private String gvpnMode;
	private Boolean dialIn;
	private Boolean dialOut;
	private Boolean dialBack;
	private Boolean isLns;
	private Boolean isItfs;
	private Boolean isOutbound;

	// Existing GVPN details bean
	private ExistingGVPNInfo existingGVPNInfo;

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public Integer getQuoteLeId() {
		return quoteLeId;
	}

	public void setQuoteLeId(Integer quoteLeId) {
		this.quoteLeId = quoteLeId;
	}

	public String getQuoteCode() {
		return quoteCode;
	}

	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getProductFamilyName() {
		return productFamilyName;
	}

	public void setProductFamilyName(String productFamilyName) {
		this.productFamilyName = productFamilyName;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public Integer getQuoteVersion() {
		return quoteVersion;
	}

	public void setQuoteVersion(Integer quoteVersion) {
		this.quoteVersion = quoteVersion;
	}

	public List<GscSolutionBean> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<GscSolutionBean> solutions) {
		this.solutions = solutions;
	}

	public WebexSolutionBean getWebexSolution() {
		return webexSolution;
	}

	public void setWebexSolution(WebexSolutionBean webexSolution) {
		this.webexSolution = webexSolution;
	}

	public List<GscQuoteToLeBean> getLegalEntities() {
		return legalEntities;
	}

	public void setLegalEntities(List<GscQuoteToLeBean> legalEntities) {
		this.legalEntities = legalEntities;
	}

	public GvpnQuotePdfBean getGvpnQuotes() {
		return gvpnQuotes;
	}

	public void setGvpnQuotes(GvpnQuotePdfBean gvpnQuotes) {
		this.gvpnQuotes = gvpnQuotes;
	}

	public String getQuoteType() {
		return quoteType;
	}

	public void setQuoteType(String quoteType) {
		this.quoteType = quoteType;
	}

	public String getQuoteCategory() {
		return quoteCategory;
	}

	public void setQuoteCategory(String quoteCategory) {
		this.quoteCategory = quoteCategory;
	}

	public Boolean getIsDocusign() {
		return isDocusign;
	}

	public void setIsDocusign(Boolean isDocusign) {
		this.isDocusign = isDocusign;
	}

	public Boolean getIsManualCofSigned() {
		return isManualCofSigned;
	}

	public void setIsManualCofSigned(Boolean isManualCofSigned) {
		this.isManualCofSigned = isManualCofSigned;
	}

	public Integer getSupplierLegalId() {
		return supplierLegalId;
	}

	public void setSupplierLegalId(Integer supplierLegalId) {
		this.supplierLegalId = supplierLegalId;
	}

	public String getEngagementOptyId() {
		return engagementOptyId;
	}

	public void setEngagementOptyId(String engagementOptyId) {
		this.engagementOptyId = engagementOptyId;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getLicenseProvider() {
		return licenseProvider;
	}

	public void setLicenseProvider(String licenseProvider) {
		this.licenseProvider = licenseProvider;
	}

	public String getPrimaryBridge() {
		return primaryBridge;
	}

	public void setPrimaryBridge(String primaryBridge) {
		this.primaryBridge = primaryBridge;
	}

	public String getAudioGreeting() {
		return audioGreeting;
	}

	public void setAudioGreeting(String audioGreeting) {
		this.audioGreeting = audioGreeting;
	}

	public String getPaymentModel() {
		return paymentModel;
	}

	public void setPaymentModel(String paymentModel) {
		this.paymentModel = paymentModel;
	}

	public String getAudioType() {
		return audioType;
	}

	public void setAudioType(String audioType) {
		this.audioType = audioType;
	}

	public Boolean getCugRequired() {
		return cugRequired;
	}

	public void setCugRequired(Boolean cugRequired) {
		this.cugRequired = cugRequired;
	}

	public String getGvpnMode() {
		return gvpnMode;
	}

	public void setGvpnMode(String gvpnMode) {
		this.gvpnMode = gvpnMode;
	}

	public Boolean getDialIn() {
		return dialIn;
	}

	public void setDialIn(Boolean dialIn) {
		this.dialIn = dialIn;
	}

	public Boolean getDialOut() {
		return dialOut;
	}

	public void setDialOut(Boolean dialOut) {
		this.dialOut = dialOut;
	}

	public Boolean getDialBack() {
		return dialBack;
	}

	public void setDialBack(Boolean dialBack) {
		this.dialBack = dialBack;
	}

	public Boolean getIsLns() {
		return isLns;
	}

	public void setIsLns(Boolean lns) {
		isLns = lns;
	}

	public Boolean getIsItfs() {
		return isItfs;
	}

	public void setIsItfs(Boolean itfs) {
		isItfs = itfs;
	}

	public Boolean getIsOutbound() {
		return isOutbound;
	}

	public void setIsOutbound(Boolean outbound) {
		isOutbound = outbound;
	}

	public Integer getLicenseQuantity() {
		return licenseQuantity;
	}

	public void setLicenseQuantity(Integer licenseQuantity) {
		this.licenseQuantity = licenseQuantity;
	}

	public ExistingGVPNInfo getExistingGVPNInfo() {
		return existingGVPNInfo;
	}

	public void setExistingGVPNInfo(ExistingGVPNInfo existingGVPNInfo) {
		this.existingGVPNInfo = existingGVPNInfo;
	}

	@Override
	public String toString() {
		return "WebexQuoteDataBean [quoteId=" + quoteId + ", quoteLeId=" + quoteLeId + ", quoteCode=" + quoteCode
				+ ", customerId=" + customerId + ", productFamilyName=" + productFamilyName + ", accessType="
				+ accessType + ", profileName=" + profileName + ", quoteVersion=" + quoteVersion + ", solutions="
				+ solutions + ", webexSolution=" + webexSolution + ", legalEntities=" + legalEntities + ", gvpnQuotes="
				+ gvpnQuotes + ", quoteType=" + quoteType + ", quoteCategory=" + quoteCategory + ", isDocusign="
				+ isDocusign + ", isManualCofSigned=" + isManualCofSigned + ", supplierLegalId=" + supplierLegalId
				+ ", engagementOptyId=" + engagementOptyId + ", classification=" + classification + ", licenseQuantity="
				+ licenseQuantity + ", licenseProvider=" + licenseProvider + ", primaryBridge=" + primaryBridge
				+ ", audioGreeting=" + audioGreeting + ", paymentModel=" + paymentModel + ", audioType=" + audioType
				+ ", cugRequired=" + cugRequired + ", gvpnMode=" + gvpnMode + ", dialIn=" + dialIn + ", dialOut="
				+ dialOut + ", dialBack=" + dialBack + "existingGVPNInfo" + existingGVPNInfo + "]";
	}

}
