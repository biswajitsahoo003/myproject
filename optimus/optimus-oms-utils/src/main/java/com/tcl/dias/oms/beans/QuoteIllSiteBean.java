package com.tcl.dias.oms.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanSite;

/**
 * This file IllSiteBean is for site specific information IllSiteBean
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteIllSiteBean implements Serializable {

	private static final long serialVersionUID = -1878272972533597277L;

	private Integer siteId;

	private String siteCode;

	private String serviceId;

	private String relatedServiceId;
	
	private String linkType;

	private Integer createdBy;

	private Timestamp createdTime;

	private String fpStatus;

	private String imageUrl;

	private Byte isFeasible;

	private Double mrc;

	private Double nrc;

	private Double arc;

	private Double tcv;

	private List<QuoteProductComponentBean> components;

	private Integer locationId;

	private Integer popLocationId;

	private String erfLocSiteCode;

	private String erfLrSolutionId;

	private Integer quoteVersion;

	private String bandwidth;

	private Byte isTaxExempted;

	private String changeBandwidthFlag;

	private List<SiteFeasibilityBean> feasibility;

	private Date requestorDate;

	private Date effectiveDate;

	private List<QuoteSlaBean> quoteSla;
	
	private Boolean rejectionStatus=false;
	
	private Boolean approveStatus=false;
	
	private Boolean isActionTaken=false;

	private Byte isColo;
	

	private Boolean bulkuploadStatus=false;
	
	

	private List<QuoteSiteServiceTerminationDetailsBean> quoteSiteServiceTerminationsBean;
	
    private String siteBillingGstNo;


    
	

	public String getSiteBillingGstNo() {
		return siteBillingGstNo;
	}

	public void setSiteBillingGstNo(String siteBillingGstNo) {
		this.siteBillingGstNo = siteBillingGstNo;
	}

	private Integer nplShiftSiteFlag;


	

	public Boolean getBulkuploadStatus() {
		return bulkuploadStatus;
	}

	public void setBulkuploadStatus(Boolean bulkuploadStatus) {
		this.bulkuploadStatus = bulkuploadStatus;
	}

	public Boolean getIsActionTaken() {
		return isActionTaken;
	}

	public void setIsActionTaken(Boolean isActionTaken) {
		this.isActionTaken = isActionTaken;
	}

	public Boolean getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(Boolean approveStatus) {
		this.approveStatus = approveStatus;
	}

	

	public Boolean getRejectionStatus() {
		return rejectionStatus;
	}

	public void setRejectionStatus(Boolean rejectionStatus) {
		this.rejectionStatus = rejectionStatus;
	}

	
	private String siteType;
	
	
	private String siteProductName;
	
	private String siteProductOfferingName;
	
	private String priSec;
	
	private String primaryServiceId;
	

	public String getPrimaryServiceId() {
		return primaryServiceId;
	}

	public void setPrimaryServiceId(String primaryServiceId) {
		this.primaryServiceId = primaryServiceId;
	}

	public String getSiteProductName() {
		return siteProductName;
	}

	public void setSiteProductName(String siteProductName) {
		this.siteProductName = siteProductName;
	}

	public String getSiteProductOfferingName() {
		return siteProductOfferingName;
	}

	public void setSiteProductOfferingName(String siteProductOfferingName) {
		this.siteProductOfferingName = siteProductOfferingName;
	}

	public String getPriSec() {
		return priSec;
	}

	public void setPriSec(String priSec) {
		this.priSec = priSec;
	}
	private List<MACDExistingComponentsBean> existingComponentsList;

	public List<MACDExistingComponentsBean> getExistingComponentsList() {
		return existingComponentsList;
	}

	public void setExistingComponentsList(List<MACDExistingComponentsBean> existingComponentsList) {
		this.existingComponentsList = existingComponentsList;
	}

	private Integer isTaskTriggered;

	private String mfStatus;
	
	private Integer mfTaskTriggered;
	
	private Byte sdwanPrice; 
	
	public String getMfStatus() {
		return mfStatus;
	}

	public void setMfStatus(String mfStatus) {
		this.mfStatus = mfStatus;
	}

	public Integer getIsTaskTriggered() {
		return isTaskTriggered;
	}

	public Byte getSdwanPrice() {
		return sdwanPrice;
	}

	public void setSdwanPrice(Byte sdwanPrice) {
		this.sdwanPrice = sdwanPrice;
	}

	public void setIsTaskTriggered(Integer isTaskTriggered) {
		this.isTaskTriggered = isTaskTriggered;
	}


	public QuoteIllSiteBean() {
		// DO NOTHING
	}

	/**
	 * 
	 * @param site
	 */
	public QuoteIllSiteBean(QuoteIllSite site) {
		super();
		this.siteId = site.getId();
		this.siteCode = site.getSiteCode();
		this.imageUrl = site.getImageUrl();
		this.isFeasible = site.getFeasibility();
		this.locationId = site.getErfLocSitebLocationId();
		this.popLocationId = site.getErfLocSiteaLocationId();
		this.erfLocSiteCode = site.getErfLocSitebSiteCode();
		this.mrc = site.getMrc();
		this.isTaxExempted = site.getIsTaxExempted();
		this.nrc = site.getNrc();
		this.arc = site.getArc();
		this.tcv = site.getTcv();
		this.fpStatus = site.getFpStatus();
		this.erfLocSiteCode = site.getErfLocSitebSiteCode();
		this.requestorDate = site.getRequestorDate();
		this.effectiveDate = site.getEffectiveDate();
		this.isTaskTriggered = site.getIsTaskTriggered();
		this.mfTaskTriggered = site.getMfTaskTriggered();
		if(Objects.nonNull(site.getIsColo())) {
			this.isColo = site.getIsColo();
		}
	}
	
	/**
	 * 
	 * @param site
	 */
	public QuoteIllSiteBean(QuoteIzosdwanSite site) {
		super();
		this.siteId = site.getId();
		this.siteCode = site.getSiteCode();
		this.imageUrl = site.getImageUrl();
		this.isFeasible = site.getFeasibility();
		this.locationId = site.getErfLocSitebLocationId();
		this.popLocationId = site.getErfLocSiteaLocationId();
		this.erfLocSiteCode = site.getErfLocSitebSiteCode();
		this.mrc = site.getMrc();
		this.isTaxExempted = site.getIsTaxExempted();
		this.nrc = site.getNrc();
		this.arc = site.getArc();
		this.tcv = site.getTcv();
		this.fpStatus = site.getFpStatus();
		this.erfLocSiteCode = site.getErfLocSitebSiteCode();
		this.requestorDate = site.getRequestorDate();
		this.effectiveDate = site.getEffectiveDate();
		this.serviceId = site.getErfServiceInventoryTpsServiceId();
		this.siteType = site.getIzosdwanSiteType();
		this.siteProductName = site.getIzosdwanSiteProduct();
		this.siteProductOfferingName = site.getIzosdwanSiteOffering();
		this.priSec = site.getPriSec();
		this.primaryServiceId = site.getPrimaryServiceId();
		this.sdwanPrice = site.getSdwanPrice();
	}

	/**
	 * @return the siteId
	 */
	public Integer getSiteId() {
		return siteId;
	}

	/**
	 * @param siteId
	 *            the siteId to set
	 */
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	/**
	 * @return the createdBy
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}


	public String getChangeBandwidthFlag() {
		return changeBandwidthFlag;
	}

	public void setChangeBandwidthFlag(String changeBandwidthFlag) {
		this.changeBandwidthFlag = changeBandwidthFlag;
	}

	/**
	 * @return the createdTime
	 */
	public Timestamp getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime
	 *            the createdTime to set
	 */
	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}
	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @param imageUrl
	 *            the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * @return the mrc
	 */
	public Double getMrc() {
		return mrc;
	}

	/**
	 * @param mrc
	 *            the mrc to set
	 */
	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	/**
	 * @return the nrc
	 */
	public Double getNrc() {
		return nrc;
	}

	/**
	 * @param nrc
	 *            the nrc to set
	 */
	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	/**
	 * @return the components
	 */
	public List<QuoteProductComponentBean> getComponents() {
		return components;
	}

	/**
	 * @param components
	 *            the components to set
	 */
	public void setComponents(List<QuoteProductComponentBean> components) {
		this.components = components;
	}

	/**
	 * @return the locationId
	 */
	public Integer getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId
	 *            the locationId to set
	 */
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the erfLocSiteCode
	 */
	public String getErfLocSiteCode() {
		return erfLocSiteCode;
	}

	/**
	 * @param erfLocSiteCode
	 *            the erfLocSiteCode to set
	 */
	public void setErfLocSiteCode(String erfLocSiteCode) {
		this.erfLocSiteCode = erfLocSiteCode;
	}

	/**
	 * @return the erfLrSolutionId
	 */
	public String getErfLrSolutionId() {
		return erfLrSolutionId;
	}

	/**
	 * @param erfLrSolutionId
	 *            the erfLrSolutionId to set
	 */
	public void setErfLrSolutionId(String erfLrSolutionId) {
		this.erfLrSolutionId = erfLrSolutionId;
	}

	/**
	 * @return the bandwidth
	 */
	public String getBandwidth() {
		return bandwidth;
	}

	/**
	 * @param bandwidth
	 *            the bandwidth to set
	 */
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	/**
	 * @return the isFeasible
	 */
	public Byte getIsFeasible() {
		return isFeasible;
	}

	/**
	 * @param isFeasible
	 *            the isFeasible to set
	 */
	public void setIsFeasible(Byte isFeasible) {
		this.isFeasible = isFeasible;
	}

	/**
	 * @return the quoteVersion
	 */
	public Integer getQuoteVersion() {
		return quoteVersion;
	}

	/**
	 * @param quoteVersion
	 *            the quoteVersion to set
	 */
	public void setQuoteVersion(Integer quoteVersion) {
		this.quoteVersion = quoteVersion;
	}

	public Integer getPopLocationId() {
		return popLocationId;
	}

	public void setPopLocationId(Integer popLocationId) {
		this.popLocationId = popLocationId;
	}

	/**
	 * @return the quoteSla
	 */
	public List<QuoteSlaBean> getQuoteSla() {
		if (quoteSla == null) {
			quoteSla = new ArrayList<>();
		}
		return quoteSla;
	}

	/**
	 * @param quoteSla
	 *            the quoteSla to set
	 */
	public void setQuoteSla(List<QuoteSlaBean> quoteSla) {
		this.quoteSla = quoteSla;
	}

	public List<SiteFeasibilityBean> getFeasibility() {
		return feasibility;
	}

	public void setFeasibility(List<SiteFeasibilityBean> feasibility) {
		this.feasibility = feasibility;
	}

	/**
	 * @return the isTaxExempted
	 */
	public Byte getIsTaxExempted() {
		return isTaxExempted;
	}

	/**
	 * @param isTaxExempted
	 *            the isTaxExempted to set
	 */
	public void setIsTaxExempted(Byte isTaxExempted) {
		this.isTaxExempted = isTaxExempted;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public Double getTcv() {
		return tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public Date getRequestorDate() {
		return requestorDate;
	}

	public void setRequestorDate(Date requestorDate) {
		this.requestorDate = requestorDate;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getFpStatus() {
		return fpStatus;
	}

	public void setFpStatus(String fpStatus) {
		this.fpStatus = fpStatus;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getRelatedServiceId() {
		return relatedServiceId;
	}

	public void setRelatedServiceId(String relatedServiceId) {
		this.relatedServiceId = relatedServiceId;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	private List<Map> existingComponentsListMap;
	
	public Integer getMfTaskTriggered() {
		return mfTaskTriggered;
	}

	public void setMfTaskTriggered(Integer mfTaskTriggered) {
		this.mfTaskTriggered = mfTaskTriggered;
	}

	public Byte getIsColo() {
		return isColo;
	}

	public List<Map> getExistingComponentsListMap() {
		return existingComponentsListMap;
	}

	public void setExistingComponentsListMap(List<Map> existingComponentsListMap) {
		this.existingComponentsListMap = existingComponentsListMap;
	}
	
	public void setIsColo(Byte isColo) {
		this.isColo = isColo;
	}

	public List<QuoteSiteServiceTerminationDetailsBean> getQuoteSiteServiceTerminationsBean() {
		return quoteSiteServiceTerminationsBean;
	}

	public void setQuoteSiteServiceTerminationsBean(
			List<QuoteSiteServiceTerminationDetailsBean> quoteSiteServiceTerminationsBean) {
		this.quoteSiteServiceTerminationsBean = quoteSiteServiceTerminationsBean;
	}

	public Integer getNplShiftSiteFlag() {
		return nplShiftSiteFlag;
	}

	public void setNplShiftSiteFlag(Integer nplShiftSiteFlag) {
		this.nplShiftSiteFlag = nplShiftSiteFlag;
	}

	@Override
	public String toString() {
		return "QuoteIllSiteBean{" +
				"siteId=" + siteId +
				", siteCode='" + siteCode + '\'' +
				", serviceId='" + serviceId + '\'' +
				", relatedServiceId='" + relatedServiceId + '\'' +
				", linkType='" + linkType + '\'' +
				", createdBy=" + createdBy +
				", createdTime=" + createdTime +
				", fpStatus='" + fpStatus + '\'' +
				", imageUrl='" + imageUrl + '\'' +
				", isFeasible=" + isFeasible +
				", mrc=" + mrc +
				", nrc=" + nrc +
				", arc=" + arc +
				", tcv=" + tcv +
				", components=" + components +
				", locationId=" + locationId +
				", popLocationId=" + popLocationId +
				", erfLocSiteCode='" + erfLocSiteCode + '\'' +
				", erfLrSolutionId='" + erfLrSolutionId + '\'' +
				", quoteVersion=" + quoteVersion +
				", bandwidth='" + bandwidth + '\'' +
				", isTaxExempted=" + isTaxExempted +
				", changeBandwidthFlag='" + changeBandwidthFlag + '\'' +
				", feasibility=" + feasibility +
				", requestorDate=" + requestorDate +
				", effectiveDate=" + effectiveDate +
				", quoteSla=" + quoteSla +
				", rejectionStatus=" + rejectionStatus +
				", approveStatus=" + approveStatus +
				", isActionTaken=" + isActionTaken +
				", isColo=" + isColo +
				", bulkuploadStatus=" + bulkuploadStatus +
				", quoteSiteServiceTerminationsBean=" + quoteSiteServiceTerminationsBean +
				", siteType='" + siteType + '\'' +
				", siteProductName='" + siteProductName + '\'' +
				", siteProductOfferingName='" + siteProductOfferingName + '\'' +
				", priSec='" + priSec + '\'' +
				", primaryServiceId='" + primaryServiceId + '\'' +
				", existingComponentsList=" + existingComponentsList +
				", isTaskTriggered=" + isTaskTriggered +
				", mfStatus='" + mfStatus + '\'' +
				", mfTaskTriggered=" + mfTaskTriggered +
				", sdwanPrice=" + sdwanPrice +
				", existingComponentsListMap=" + existingComponentsListMap +
				'}';
	}
}
