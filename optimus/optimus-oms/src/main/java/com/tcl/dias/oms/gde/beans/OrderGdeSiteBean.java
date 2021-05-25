package com.tcl.dias.oms.gde.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.beans.OrderProductComponentBean;
import com.tcl.dias.oms.beans.OrderSlaBean;
import com.tcl.dias.oms.beans.SiteFeasibilityBean;
import com.tcl.dias.oms.entity.entities.OrderIllSite;

@JsonInclude(Include.NON_NULL)
public class OrderGdeSiteBean implements Serializable {
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

	public OrderGdeSiteBean() {

	}

	/**
	 * 
	 * @param site
	 */
	public OrderGdeSiteBean(OrderIllSite site) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arc == null) ? 0 : arc.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((createdTime == null) ? 0 : createdTime.hashCode());
		result = prime * result + ((currentStage == null) ? 0 : currentStage.hashCode());
		result = prime * result + ((currentStatus == null) ? 0 : currentStatus.hashCode());
		result = prime * result + ((effectiveDate == null) ? 0 : effectiveDate.hashCode());
		result = prime * result + ((erfCusCustomerLeId == null) ? 0 : erfCusCustomerLeId.hashCode());
		result = prime * result + ((erfLocSiteALocationId == null) ? 0 : erfLocSiteALocationId.hashCode());
		result = prime * result + ((erfLocSiteASiteCode == null) ? 0 : erfLocSiteASiteCode.hashCode());
		result = prime * result + ((erfLocSiteBLocationId == null) ? 0 : erfLocSiteBLocationId.hashCode());
		result = prime * result + ((erfLocSiteBSiteCode == null) ? 0 : erfLocSiteBSiteCode.hashCode());
		result = prime * result + ((erfLrSolutionId == null) ? 0 : erfLrSolutionId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((imageUrl == null) ? 0 : imageUrl.hashCode());
		result = prime * result + isFeasible;
		result = prime * result + ((isTaxExempted == null) ? 0 : isTaxExempted.hashCode());
		result = prime * result + ((mrc == null) ? 0 : mrc.hashCode());
		result = prime * result + ((nrc == null) ? 0 : nrc.hashCode());
		result = prime * result + ((offeringName == null) ? 0 : offeringName.hashCode());
		result = prime * result + ((orderProductComponentBeans == null) ? 0 : orderProductComponentBeans.hashCode());
		result = prime * result + ((orderSla == null) ? 0 : orderSla.hashCode());
		result = prime * result + ((requestorDate == null) ? 0 : requestorDate.hashCode());
		result = prime * result + ((siteCode == null) ? 0 : siteCode.hashCode());
		result = prime * result + ((siteFeasibility == null) ? 0 : siteFeasibility.hashCode());
		result = prime * result + ((stage == null) ? 0 : stage.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((tcv == null) ? 0 : tcv.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderGdeSiteBean other = (OrderGdeSiteBean) obj;
		if (arc == null) {
			if (other.arc != null)
				return false;
		} else if (!arc.equals(other.arc))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (createdTime == null) {
			if (other.createdTime != null)
				return false;
		} else if (!createdTime.equals(other.createdTime))
			return false;
		if (currentStage == null) {
			if (other.currentStage != null)
				return false;
		} else if (!currentStage.equals(other.currentStage))
			return false;
		if (currentStatus == null) {
			if (other.currentStatus != null)
				return false;
		} else if (!currentStatus.equals(other.currentStatus))
			return false;
		if (effectiveDate == null) {
			if (other.effectiveDate != null)
				return false;
		} else if (!effectiveDate.equals(other.effectiveDate))
			return false;
		if (erfCusCustomerLeId == null) {
			if (other.erfCusCustomerLeId != null)
				return false;
		} else if (!erfCusCustomerLeId.equals(other.erfCusCustomerLeId))
			return false;
		if (erfLocSiteALocationId == null) {
			if (other.erfLocSiteALocationId != null)
				return false;
		} else if (!erfLocSiteALocationId.equals(other.erfLocSiteALocationId))
			return false;
		if (erfLocSiteASiteCode == null) {
			if (other.erfLocSiteASiteCode != null)
				return false;
		} else if (!erfLocSiteASiteCode.equals(other.erfLocSiteASiteCode))
			return false;
		if (erfLocSiteBLocationId == null) {
			if (other.erfLocSiteBLocationId != null)
				return false;
		} else if (!erfLocSiteBLocationId.equals(other.erfLocSiteBLocationId))
			return false;
		if (erfLocSiteBSiteCode == null) {
			if (other.erfLocSiteBSiteCode != null)
				return false;
		} else if (!erfLocSiteBSiteCode.equals(other.erfLocSiteBSiteCode))
			return false;
		if (erfLrSolutionId == null) {
			if (other.erfLrSolutionId != null)
				return false;
		} else if (!erfLrSolutionId.equals(other.erfLrSolutionId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (imageUrl == null) {
			if (other.imageUrl != null)
				return false;
		} else if (!imageUrl.equals(other.imageUrl))
			return false;
		if (isFeasible != other.isFeasible)
			return false;
		if (isTaxExempted == null) {
			if (other.isTaxExempted != null)
				return false;
		} else if (!isTaxExempted.equals(other.isTaxExempted))
			return false;
		if (mrc == null) {
			if (other.mrc != null)
				return false;
		} else if (!mrc.equals(other.mrc))
			return false;
		if (nrc == null) {
			if (other.nrc != null)
				return false;
		} else if (!nrc.equals(other.nrc))
			return false;
		if (offeringName == null) {
			if (other.offeringName != null)
				return false;
		} else if (!offeringName.equals(other.offeringName))
			return false;
		if (orderProductComponentBeans == null) {
			if (other.orderProductComponentBeans != null)
				return false;
		} else if (!orderProductComponentBeans.equals(other.orderProductComponentBeans))
			return false;
		if (orderSla == null) {
			if (other.orderSla != null)
				return false;
		} else if (!orderSla.equals(other.orderSla))
			return false;
		if (requestorDate == null) {
			if (other.requestorDate != null)
				return false;
		} else if (!requestorDate.equals(other.requestorDate))
			return false;
		if (siteCode == null) {
			if (other.siteCode != null)
				return false;
		} else if (!siteCode.equals(other.siteCode))
			return false;
		if (siteFeasibility == null) {
			if (other.siteFeasibility != null)
				return false;
		} else if (!siteFeasibility.equals(other.siteFeasibility))
			return false;
		if (stage == null) {
			if (other.stage != null)
				return false;
		} else if (!stage.equals(other.stage))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (tcv == null) {
			if (other.tcv != null)
				return false;
		} else if (!tcv.equals(other.tcv))
			return false;
		return true;
	}

}