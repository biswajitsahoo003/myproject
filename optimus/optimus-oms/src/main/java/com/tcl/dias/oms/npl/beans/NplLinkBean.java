package com.tcl.dias.oms.npl.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.beans.MACDExistingComponentsBean;
import com.tcl.dias.oms.beans.OrderProductComponentBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.QuoteSiteServiceTerminationDetailsBean;
import com.tcl.dias.oms.beans.QuoteSlaBean;
import com.tcl.dias.oms.entity.entities.OrderNplLink;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;

/**
 * Bean class to hold NPL link related information
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(Include.NON_NULL)
public class NplLinkBean implements Serializable {

	private static final long serialVersionUID = -1925745641365693042L;

	private Integer id;

	private String linkCode;

	private Integer siteA;

	private Integer siteB;

	private Integer productSolutionId;

	private byte status;

	private List<QuoteNplSiteBean> sites;

	private List<QuoteNplSiteDto> sitesDtos;

	private List<OrderNplSiteBean> orderSites;

	private List<OrderNplsiteDto> orderSitesDtos;

	private List<QuoteSlaBean> quoteSla;

	private Integer createdBy;

	private List<QuoteProductComponentBean> components;

	private List<OrderProductComponentBean> orderProductComponentBeans;

	private Double mrc;

	private Double nrc;

	private Double tcv;
	
	private Double arc;

	private Integer orderId;
	
	private Integer quoteId;

	private String workflowStatus;

	private String chargeableDistance;

	private Date requestorDate;

	private String linkType;

	private String siteAType;

	private String siteBType;

	private Date createdDate;

	private Date effectiveDate;
	
	private Byte feasibility;
	
	private List<LinkFeasibilityBean> linkFeasibility;
	
	private String currentStatus;

	private String currentStage;
	
	private String serviceId;
	
	private String fpStatus;

	private Integer isTaskTriggered;
	
	private Integer mfTaskTriggered;
	
	private String mfStatus;
	
	private String siteShiftFlag;
	
	private Boolean rejectionStatus=false;
	
	private Boolean approveStatus=false;
	
	private Boolean isActionTaken=false;
	
	

    private Boolean bulkuploadStatus=false;

	private List<QuoteSiteServiceTerminationDetailsBean> quoteSiteServiceTerminationsBean;
	

	
	
	


	

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

	public Boolean getRejectionStatus() {
		return rejectionStatus;
	}

	public void setRejectionStatus(Boolean rejectionStatus) {
		this.rejectionStatus = rejectionStatus;
	}

	public Boolean getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(Boolean approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getSiteShiftFlag() {
		return siteShiftFlag;
	}

	public void setSiteShiftFlag(String siteShiftFlag) {
		this.siteShiftFlag = siteShiftFlag;
	}

	public String getMfStatus() {
		return mfStatus;
	}

	public void setMfStatus(String mfStatus) {
		this.mfStatus = mfStatus;
	}

	public Integer getMfTaskTriggered() {
		return mfTaskTriggered;
	}

	public void setMfTaskTriggered(Integer mfTaskTriggered) {
		this.mfTaskTriggered = mfTaskTriggered;
	}

	private List<MACDExistingComponentsBean> existingComponentsList;
	
	public String getFpStatus() {
		return fpStatus;
	}

	public void setFpStatus(String fpStatus) {
		this.fpStatus = fpStatus;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	public Byte getFeasibility() {
		return feasibility;
	}

	public void setFeasibility(Byte feasibility) {
		this.feasibility = feasibility;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public String getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	public String getChargeableDistance() {
		return chargeableDistance;
	}

	public void setChargeableDistance(String chargeableDistance) {
		this.chargeableDistance = chargeableDistance;
	}

	public Date getRequestorDate() {
		return requestorDate;
	}

	public void setRequestorDate(Date requestorDate) {
		this.requestorDate = requestorDate;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public String getSiteAType() {
		return siteAType;
	}

	public void setSiteAType(String siteAType) {
		this.siteAType = siteAType;
	}

	public String getSiteBType() {
		return siteBType;
	}

	public void setSiteBType(String siteBType) {
		this.siteBType = siteBType;
	}

	public List<QuoteSlaBean> getQuoteSla() {
		return quoteSla;
	}

	public void setQuoteSla(List<QuoteSlaBean> quoteSla) {
		this.quoteSla = quoteSla;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLinkCode() {
		return linkCode;
	}

	public void setLinkCode(String linkCode) {
		this.linkCode = linkCode;
	}

	public Integer getSiteA() {
		return siteA;
	}

	public void setSiteA(Integer siteA) {
		this.siteA = siteA;
	}

	public Integer getSiteB() {
		return siteB;
	}

	public void setSiteB(Integer siteB) {
		this.siteB = siteB;
	}

	public Integer getProductSolutionId() {
		return productSolutionId;
	}

	public void setProductSolutionId(Integer productSolutionId) {
		this.productSolutionId = productSolutionId;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public NplLinkBean() {

	}

	public Integer getIsTaskTriggered() { return isTaskTriggered; }

	public void setIsTaskTriggered(Integer isTaskTriggered){
		this.isTaskTriggered = isTaskTriggered;
		}

	public NplLinkBean(OrderNplLink orderNplLinkEntity) {
		this.setId(orderNplLinkEntity.getId());
		this.setLinkCode(orderNplLinkEntity.getLinkCode());
		this.setProductSolutionId(orderNplLinkEntity.getProductSolutionId());
		this.setSiteA(orderNplLinkEntity.getSiteAId());
		this.setSiteB(orderNplLinkEntity.getSiteBId());
		this.setStatus(orderNplLinkEntity.getStatus());
		this.setOrderId(orderNplLinkEntity.getOrderId());
		this.setRequestorDate(orderNplLinkEntity.getRequestorDate());
		this.setArc(orderNplLinkEntity.getArc());
		this.setMrc(orderNplLinkEntity.getMrc());
		this.setNrc(orderNplLinkEntity.getNrc());
		this.setTcv(orderNplLinkEntity.getTcv());
		this.setLinkType(orderNplLinkEntity.getLinkType());
		this.setStatus(orderNplLinkEntity.getStatus());
		this.setChargeableDistance(orderNplLinkEntity.getChargeableDistance());
		this.setCreatedBy(orderNplLinkEntity.getCreatedBy());
		this.setCreatedDate(orderNplLinkEntity.getCreatedDate());
		this.setSiteAType(orderNplLinkEntity.getSiteAType());
		this.setSiteBType(orderNplLinkEntity.getSiteBType());
		this.setEffectiveDate(orderNplLinkEntity.getEffectiveDate());
		this.setFeasibility(orderNplLinkEntity.getFeasibility());
		if (orderNplLinkEntity.getMstOrderLinkStage()!=null)
		this.setCurrentStage(orderNplLinkEntity.getMstOrderLinkStage().getName());
		if (orderNplLinkEntity.getMstOrderLinkStatus()!=null)
		this.setCurrentStatus(orderNplLinkEntity.getMstOrderLinkStatus().getName());
		if (orderNplLinkEntity.getLinkFeasibilities()!=null &&! orderNplLinkEntity.getLinkFeasibilities().isEmpty())
		this.setLinkFeasibility(orderNplLinkEntity.getLinkFeasibilities().stream().map(LinkFeasibilityBean::new).collect(Collectors.toList()));
		this.setServiceId(orderNplLinkEntity.getServiceId());

	}

	public NplLinkBean(QuoteNplLink quoteNplLinkEntity) {
		this.setId(quoteNplLinkEntity.getId());
		this.setLinkCode(quoteNplLinkEntity.getLinkCode());
		this.setProductSolutionId(quoteNplLinkEntity.getProductSolutionId());
		this.setSiteA(quoteNplLinkEntity.getSiteAId());
		this.setSiteB(quoteNplLinkEntity.getSiteBId());
		this.setStatus(quoteNplLinkEntity.getStatus());
		this.setQuoteId(quoteNplLinkEntity.getQuoteId());
		this.setRequestorDate(quoteNplLinkEntity.getRequestorDate());
		this.setArc(quoteNplLinkEntity.getArc());
		this.setMrc(quoteNplLinkEntity.getMrc());
		this.setNrc(quoteNplLinkEntity.getNrc());
		this.setTcv(quoteNplLinkEntity.getTcv());
		this.setLinkType(quoteNplLinkEntity.getLinkType());
		this.setStatus(quoteNplLinkEntity.getStatus());
		this.setChargeableDistance(quoteNplLinkEntity.getChargeableDistance());
		this.setCreatedBy(quoteNplLinkEntity.getCreatedBy());
		this.setCreatedDate(quoteNplLinkEntity.getCreatedDate());
		this.setWorkflowStatus(quoteNplLinkEntity.getWorkflowStatus());
		this.setSiteAType(quoteNplLinkEntity.getSiteAType());
		this.setSiteBType(quoteNplLinkEntity.getSiteBType());
		this.setEffectiveDate(quoteNplLinkEntity.getEffectiveDate());
		this.setFeasibility(quoteNplLinkEntity.getFeasibility());
		this.setFpStatus(quoteNplLinkEntity.getFpStatus());
	//	this.setIsTaskTriggered(quoteNplLinkEntity.getIsTaskTriggered());
	}

	@Override
	public String toString() {
		return "NplLinkBean [id=" + id + ", linkCode=" + linkCode + ", siteA=" + siteA + ", siteB=" + siteB
				+ ", productSolutionId=" + productSolutionId + ", status=" + status + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((linkCode == null) ? 0 : linkCode.hashCode());
		result = prime * result + ((orderSites == null) ? 0 : orderSites.hashCode());
		result = prime * result + ((orderSitesDtos == null) ? 0 : orderSitesDtos.hashCode());
		result = prime * result + ((productSolutionId == null) ? 0 : productSolutionId.hashCode());
		result = prime * result + ((siteA == null) ? 0 : siteA.hashCode());
		result = prime * result + ((siteB == null) ? 0 : siteB.hashCode());
		result = prime * result + ((sites == null) ? 0 : sites.hashCode());
		result = prime * result + ((sitesDtos == null) ? 0 : sitesDtos.hashCode());
		result = prime * result + status;
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
		NplLinkBean other = (NplLinkBean) obj;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (linkCode == null) {
			if (other.linkCode != null)
				return false;
		} else if (!linkCode.equals(other.linkCode))
			return false;
		if (orderSites == null) {
			if (other.orderSites != null)
				return false;
		} else if (!orderSites.equals(other.orderSites))
			return false;
		if (orderSitesDtos == null) {
			if (other.orderSitesDtos != null)
				return false;
		} else if (!orderSitesDtos.equals(other.orderSitesDtos))
			return false;
		if (productSolutionId == null) {
			if (other.productSolutionId != null)
				return false;
		} else if (!productSolutionId.equals(other.productSolutionId))
			return false;
		if (siteA == null) {
			if (other.siteA != null)
				return false;
		} else if (!siteA.equals(other.siteA))
			return false;
		if (siteB == null) {
			if (other.siteB != null)
				return false;
		} else if (!siteB.equals(other.siteB))
			return false;
		if (sites == null) {
			if (other.sites != null)
				return false;
		} else if (!sites.equals(other.sites))
			return false;
		if (sitesDtos == null) {
			if (other.sitesDtos != null)
				return false;
		} else if (!sitesDtos.equals(other.sitesDtos))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	public List<QuoteNplSiteBean> getSites() {
		return sites;
	}

	public void setSites(List<QuoteNplSiteBean> sites) {
		this.sites = sites;
	}

	public List<QuoteNplSiteDto> getSitesDtos() {
		return sitesDtos;
	}

	public void setSitesDtos(List<QuoteNplSiteDto> sitesDtos) {
		this.sitesDtos = sitesDtos;
	}

	public List<OrderNplSiteBean> getOrderSites() {
		return orderSites;
	}

	public void setOrderSites(List<OrderNplSiteBean> orderSites) {
		this.orderSites = orderSites;
	}

	public List<OrderNplsiteDto> getOrderSitesDtos() {
		return orderSitesDtos;
	}

	public void setOrderSitesDtos(List<OrderNplsiteDto> orderSitesDtos) {
		this.orderSitesDtos = orderSitesDtos;
	}

	public List<QuoteProductComponentBean> getComponents() {
		return components;
	}

	public void setComponents(List<QuoteProductComponentBean> components) {
		this.components = components;
	}

	public List<OrderProductComponentBean> getOrderProductComponentBeans() {
		return orderProductComponentBeans;
	}

	public void setOrderProductComponentBeans(List<OrderProductComponentBean> orderProductComponentBeans) {
		this.orderProductComponentBeans = orderProductComponentBeans;
	}

	public List<LinkFeasibilityBean> getLinkFeasibility() {
		return linkFeasibility;
	}

	public void setLinkFeasibility(List<LinkFeasibilityBean> linkFeasibility) {
		this.linkFeasibility = linkFeasibility;
	}

	public Double getTcv() {
		return tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getCurrentStage() {
		return currentStage;
	}

	public void setCurrentStage(String currentStage) {
		this.currentStage = currentStage;
	}

	public List<MACDExistingComponentsBean> getExistingComponentsList() {
		return existingComponentsList;
	}

	public void setExistingComponentsList(List<MACDExistingComponentsBean> existingComponentsList) {
		this.existingComponentsList = existingComponentsList;
	}

	public List<QuoteSiteServiceTerminationDetailsBean> getQuoteSiteServiceTerminationsBean() {
		return quoteSiteServiceTerminationsBean;
	}

	public void setQuoteSiteServiceTerminationsBean(
			List<QuoteSiteServiceTerminationDetailsBean> quoteSiteServiceTerminationsBean) {
		this.quoteSiteServiceTerminationsBean = quoteSiteServiceTerminationsBean;
	}
	
	

	
	
}
