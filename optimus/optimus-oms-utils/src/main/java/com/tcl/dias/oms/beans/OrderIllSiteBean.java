package com.tcl.dias.oms.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.entity.entities.OrderIllSite;

@JsonInclude(Include.NON_NULL)
public class OrderIllSiteBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String siteCode;

	private Integer createdBy;

	private Date createdTime;

	private Date effectiveDate;

	private Integer erfLocSiteBLocationId;

	private String erfLocSiteBSiteCode;

	private Integer erfLocSiteALocationId;

	private String erfLocSiteASiteCode;

	private String erfLrSolutionId;

	private byte isFeasible;

	private String imageUrl;

	private String offeringName;

	private Byte isTaxExempted;

	private Double mrc;

	private Double nrc;

	private Double arc;

	private Double tcv;

	private Byte status;

	private Date requestorDate;

	private String stage;

	private Integer erfCusCustomerLeId;

	private List<OrderProductComponentBean> orderProductComponentBeans;

	private String currentStatus;

	private String currentStage;

	private List<OrderSlaBean> orderSla;

	private List<SiteFeasibilityBean> siteFeasibility;

	private String erfServiceInventoryTpsServiceId;
	
	private String siteBillingGstNo;
	
	


	public String getSiteBillingGstNo() {
		return siteBillingGstNo;
	}

	public void setSiteBillingGstNo(String siteBillingGstNo) {
		this.siteBillingGstNo = siteBillingGstNo;
	}

	public OrderIllSiteBean() {

	}

	/**
	 * 
	 * @param site
	 */
	public OrderIllSiteBean(OrderIllSite site) {
		super();
		this.id = site.getId();
		this.siteCode = site.getSiteCode();
		this.imageUrl = site.getImageUrl();
		this.isFeasible = site.getFeasibility();
		this.erfLocSiteALocationId = site.getErfLocSiteaLocationId();
		this.erfLocSiteBLocationId = site.getErfLocSitebLocationId();
		this.mrc = site.getMrc();
		this.nrc = site.getNrc();
		this.arc = site.getArc();
		this.erfLocSiteASiteCode = site.getErfLocSiteaSiteCode();
		this.tcv = site.getTcv();
		this.stage = site.getStage();
		this.requestorDate = site.getRequestorDate();
		this.isTaxExempted = site.getIsTaxExempted();
		this.effectiveDate = site.getEffectiveDate();
		this.imageUrl = site.getImageUrl();
		if (site.getOrderProductSolution() != null && site.getOrderProductSolution().getMstProductOffering() != null) {
			this.offeringName = site.getOrderProductSolution().getMstProductOffering().getProductName();
		}
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
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

	/**
	 * @return the createdTime
	 */
	public Date getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime
	 *            the createdTime to set
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * @return the effectiveDate
	 */
	public Date getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * @param effectiveDate
	 *            the effectiveDate to set
	 */
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * @return the erfLocSiteBLocationId
	 */
	public Integer getErfLocSiteBLocationId() {
		return erfLocSiteBLocationId;
	}

	/**
	 * @param erfLocSiteBLocationId
	 *            the erfLocSiteBLocationId to set
	 */
	public void setErfLocSiteBLocationId(Integer erfLocSiteBLocationId) {
		this.erfLocSiteBLocationId = erfLocSiteBLocationId;
	}

	/**
	 * @return the erfLocSiteBSiteCode
	 */
	public String getErfLocSiteBSiteCode() {
		return erfLocSiteBSiteCode;
	}

	/**
	 * @param erfLocSiteBSiteCode
	 *            the erfLocSiteBSiteCode to set
	 */
	public void setErfLocSiteBSiteCode(String erfLocSiteBSiteCode) {
		this.erfLocSiteBSiteCode = erfLocSiteBSiteCode;
	}

	/**
	 * @return the erfLocSiteALocationId
	 */
	public Integer getErfLocSiteALocationId() {
		return erfLocSiteALocationId;
	}

	/**
	 * @param erfLocSiteALocationId
	 *            the erfLocSiteALocationId to set
	 */
	public void setErfLocSiteALocationId(Integer erfLocSiteALocationId) {
		this.erfLocSiteALocationId = erfLocSiteALocationId;
	}

	/**
	 * @return the erfLocSiteASiteCode
	 */
	public String getErfLocSiteASiteCode() {
		return erfLocSiteASiteCode;
	}

	/**
	 * @param erfLocSiteASiteCode
	 *            the erfLocSiteASiteCode to set
	 */
	public void setErfLocSiteASiteCode(String erfLocSiteASiteCode) {
		this.erfLocSiteASiteCode = erfLocSiteASiteCode;
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

	/**
	 * @return the status
	 */
	public Byte getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Byte status) {
		this.status = status;
	}

	
	/**
	 * @return the requestorDate
	 */
	public Date getRequestorDate() {
		return requestorDate;
	}

	/**
	 * @param requestorDate
	 *            the requestorDate to set
	 */
	public void setRequestorDate(Date requestorDate) {
		this.requestorDate = requestorDate;
	}

	/**
	 * @return the stage
	 */
	public String getStage() {
		return stage;
	}

	/**
	 * @param stage
	 *            the stage to set
	 */
	public void setStage(String stage) {
		this.stage = stage;
	}

	/**
	 * @return the orderProductComponentBeans
	 */
	public List<OrderProductComponentBean> getOrderProductComponentBeans() {
		if (orderProductComponentBeans == null) {
			orderProductComponentBeans = new ArrayList<>();
		}
		return orderProductComponentBeans;
	}

	/**
	 * @param orderProductComponentBeans
	 *            the orderProductComponentBeans to set
	 */
	public void setOrderProductComponentBeans(List<OrderProductComponentBean> orderProductComponentBeans) {
		this.orderProductComponentBeans = orderProductComponentBeans;
	}

	/**
	 * @return the isFeasible
	 */
	public byte getIsFeasible() {
		return isFeasible;
	}

	/**
	 * @param isFeasible
	 *            the isFeasible to set
	 */
	public void setIsFeasible(byte isFeasible) {
		this.isFeasible = isFeasible;
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
	 * @return the offeringName
	 */
	public String getOfferingName() {
		return offeringName;
	}

	/**
	 * @param offeringName
	 *            the offeringName to set
	 */
	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	/**
	 * 
	 * getErfCusCustomerLeId
	 * 
	 * @return the erfCusCustomerLeId
	 */

	public Integer getErfCusCustomerLeId() {
		return erfCusCustomerLeId;
	}

	/**
	 * 
	 * setErfCusCustomerLeId
	 * 
	 * @param erfCusCustomerLeId
	 *            the erfCusCustomerLeId to set
	 */

	public void setErfCusCustomerLeId(Integer erfCusCustomerLeId) {
		this.erfCusCustomerLeId = erfCusCustomerLeId;
	}

	/**
	 * @return the orderSla
	 */
	public List<OrderSlaBean> getOrderSla() {

		if (orderSla == null) {
			orderSla = new ArrayList<>();
		}
		return orderSla;
	}

	/**
	 * @param orderSla
	 *            the orderSla to set
	 */
	public void setOrderSla(List<OrderSlaBean> orderSla) {
		this.orderSla = orderSla;
	}

	public List<SiteFeasibilityBean> getSiteFeasibility() {
		return siteFeasibility;
	}

	public void setSiteFeasibility(List<SiteFeasibilityBean> siteFeasibility) {
		this.siteFeasibility = siteFeasibility;
	}

	/**
	 * @return the currentStatus
	 */
	public String getCurrentStatus() {
		return currentStatus;
	}

	/**
	 * @param currentStatus
	 *            the currentStatus to set
	 */
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	/**
	 * @return the currentStage
	 */
	public String getCurrentStage() {
		return currentStage;
	}

	/**
	 * @param currentStage
	 *            the currentStage to set
	 */
	public void setCurrentStage(String currentStage) {
		this.currentStage = currentStage;
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

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getErfServiceInventoryTpsServiceId() {
		return erfServiceInventoryTpsServiceId;
	}

	public void setErfServiceInventoryTpsServiceId(String erfServiceInventoryTpsServiceId) {
		this.erfServiceInventoryTpsServiceId = erfServiceInventoryTpsServiceId;
	}
}