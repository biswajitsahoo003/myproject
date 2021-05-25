/**
 * 
 */
package com.tcl.dias.nso.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.nso.beans.QuoteProductComponentBean;
import com.tcl.dias.nso.beans.QuoteSlaBean;
import com.tcl.dias.nso.beans.SiteFeasibilityBean;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;

/**
 * @author KarMani
 *
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteIllSiteBean implements Serializable {

	private static final long serialVersionUID = -1878272972533597277L;

	private Integer siteId;

	private String siteCode;

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


	private List<Map> existingComponentsList;

	public List<Map> getExistingComponentsList() {
		return existingComponentsList;
	}

	public void setExistingComponentsList(List<Map> existingComponentsList) {
		this.existingComponentsList = existingComponentsList;
	}

	private Integer isTaskTriggered;

	private String mfStatus;

	public String getMfStatus() {
		return mfStatus;
	}

	public void setMfStatus(String mfStatus) {
		this.mfStatus = mfStatus;
	}

	public Integer getIsTaskTriggered() {
		return isTaskTriggered;
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

}
