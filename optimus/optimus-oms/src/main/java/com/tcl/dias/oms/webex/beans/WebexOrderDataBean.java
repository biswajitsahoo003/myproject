package com.tcl.dias.oms.webex.beans;

import java.util.Date;
import java.util.List;

import com.tcl.dias.oms.gsc.beans.GscOrderLeBean;
import com.tcl.dias.oms.gsc.beans.GscOrderSolutionBean;
import com.tcl.dias.oms.gsc.pdf.beans.GscOrderAttributesBean;

/**
 * Order Data Bean which is contains quote information and product solution
 * details.
 * 
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class WebexOrderDataBean {
	private Integer orderId;
	private Integer orderLeId;
	private Integer quoteId;
	private Integer customerId;
	private Integer createdBy;
	private Date createdTime;
	private String orderCode;
	private String productFamilyName;
	private Integer orderVersion;
	private String accessType;
	private String downstreamRequestStatus;
	private String engagementOptyId;
	private List<GscOrderSolutionBean> solutions;
	private WebexOrderSolutionBean webexOrderSolutionBean;
	private List<GscOrderLeBean> legalEntities;
	private GscOrderAttributesBean attributes;
	private String classification;
	private Double partnerOptyExpectedArc;
	private Double partnerOptyExpectedNrc;
	private String partnerOptyExpectedCurrency;

//    webex Attributes
	private String licenseProvider;
	private String primaryBridge;
	private String audioGreeting;
	private String paymentModel;
	private String audioType;
	private Boolean cugRequired;
	private String gvpnMode;
	private Boolean dialIn;
	private Boolean isLns;
	private Boolean isOutBound;
	private Boolean isItfs;
	private Integer licenseQuantity;

	// Existing GVPN details bean
	private ExistingGVPNInfo existingGVPNInfo;

	public WebexOrderDataBean() {
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

	private Boolean dialOut;
	private Boolean dialBack;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderLeId() {
		return orderLeId;
	}

	public void setOrderLeId(Integer orderLeId) {
		this.orderLeId = orderLeId;
	}

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getProductFamilyName() {
		return productFamilyName;
	}

	public void setProductFamilyName(String productFamilyName) {
		this.productFamilyName = productFamilyName;
	}

	public Integer getOrderVersion() {
		return orderVersion;
	}

	public void setOrderVersion(Integer orderVersion) {
		this.orderVersion = orderVersion;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getDownstreamRequestStatus() {
		return downstreamRequestStatus;
	}

	public void setDownstreamRequestStatus(String downstreamRequestStatus) {
		this.downstreamRequestStatus = downstreamRequestStatus;
	}

	public String getEngagementOptyId() {
		return engagementOptyId;
	}

	public void setEngagementOptyId(String engagementOptyId) {
		this.engagementOptyId = engagementOptyId;
	}

	public List<GscOrderSolutionBean> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<GscOrderSolutionBean> solutions) {
		this.solutions = solutions;
	}

	public WebexOrderSolutionBean getWebexOrderSolutionBean() {
		return webexOrderSolutionBean;
	}

	public void setWebexOrderSolutionBean(WebexOrderSolutionBean webexOrderSolutionBean) {
		this.webexOrderSolutionBean = webexOrderSolutionBean;
	}

	public List<GscOrderLeBean> getLegalEntities() {
		return legalEntities;
	}

	public void setLegalEntities(List<GscOrderLeBean> legalEntities) {
		this.legalEntities = legalEntities;
	}

	public GscOrderAttributesBean getAttributes() {
		return attributes;
	}

	public void setAttributes(GscOrderAttributesBean attributes) {
		this.attributes = attributes;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public Double getPartnerOptyExpectedArc() {
		return partnerOptyExpectedArc;
	}

	public void setPartnerOptyExpectedArc(Double partnerOptyExpectedArc) {
		this.partnerOptyExpectedArc = partnerOptyExpectedArc;
	}

	public Double getPartnerOptyExpectedNrc() {
		return partnerOptyExpectedNrc;
	}

	public void setPartnerOptyExpectedNrc(Double partnerOptyExpectedNrc) {
		this.partnerOptyExpectedNrc = partnerOptyExpectedNrc;
	}

	public String getPartnerOptyExpectedCurrency() {
		return partnerOptyExpectedCurrency;
	}

	public void setPartnerOptyExpectedCurrency(String partnerOptyExpectedCurrency) {
		this.partnerOptyExpectedCurrency = partnerOptyExpectedCurrency;
	}

	public Boolean getIsLns() {
		return isLns;
	}

	public void setIsLns(Boolean isLns) {
		this.isLns = isLns;
	}

	public Boolean getIsOutBound() {
		return isOutBound;
	}

	public void setIsOutBound(Boolean isOutBound) {
		this.isOutBound = isOutBound;
	}

	public Boolean getIsItfs() {
		return isItfs;
	}

	public void setIsItfs(Boolean isItfs) {
		this.isItfs = isItfs;
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
		return "WebexOrderDataBean [orderId=" + orderId + ", orderLeId=" + orderLeId + ", quoteId=" + quoteId
				+ ", customerId=" + customerId + ", createdBy=" + createdBy + ", createdTime=" + createdTime
				+ ", orderCode=" + orderCode + ", productFamilyName=" + productFamilyName + ", orderVersion="
				+ orderVersion + ", accessType=" + accessType + ", downstreamRequestStatus=" + downstreamRequestStatus
				+ ", engagementOptyId=" + engagementOptyId + ", solutions=" + solutions + ", webexOrderSolutionBean="
				+ webexOrderSolutionBean + ", legalEntities=" + legalEntities + ", attributes=" + attributes
				+ ", classification=" + classification + ", partnerOptyExpectedArc=" + partnerOptyExpectedArc
				+ ", partnerOptyExpectedNrc=" + partnerOptyExpectedNrc + ", partnerOptyExpectedCurrency="
				+ partnerOptyExpectedCurrency + ", licenseProvider=" + licenseProvider + ", primaryBridge="
				+ primaryBridge + ", audioGreeting=" + audioGreeting + ", paymentModel=" + paymentModel + ", audioType="
				+ audioType + ", cugRequired=" + cugRequired + ", gvpnMode=" + gvpnMode + ", dialIn=" + dialIn
				+ ", isLns=" + isLns + ", isOutBound=" + isOutBound + ", isItfs=" + isItfs + ", licenseQuantity="
				+ licenseQuantity + ", existingGVPNInfo" + existingGVPNInfo + ", dialOut=" + dialOut + ", dialBack="
				+ dialBack + "]";
	}

}
