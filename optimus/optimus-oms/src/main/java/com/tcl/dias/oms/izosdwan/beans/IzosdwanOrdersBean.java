package com.tcl.dias.oms.izosdwan.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.beans.QuoteIzoSdwanAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanSite;

/**
 * Bean class to hold Izosdwan order data
 * @author Madhumiethaa Palanisamy
 *
 */
@JsonInclude(Include.NON_NULL)
public class IzosdwanOrdersBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String orderCode;

	private Integer createdBy;

	private Date createdTime;

	private Date effectiveDate;

	private Date endDate;

	private String stage;

	private Date startDate;

	private Byte status;

	private Integer termInMonths;
	
	private Integer izoSdwanTotalNoOfSites;
	
	private String vendorName;
	
	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Integer getIzoSdwanTotalNoOfSites() {
		return izoSdwanTotalNoOfSites;
	}

	public void setIzoSdwanTotalNoOfSites(Integer izoSdwanTotalNoOfSites) {
		this.izoSdwanTotalNoOfSites = izoSdwanTotalNoOfSites;
	}

	private Set<OrderToLeBean> orderToLeBeans;
	
	private List<OrderIzosdwanCgwDetails> orderIzosdwanCgwDetails;

	private List<QuoteIzoSdwanAttributeValue> orderSdwanAttributeValues;

	public List<OrderIzosdwanCgwDetails> getOrderIzosdwanCgwDetails() {
		return orderIzosdwanCgwDetails;
	}

	public void setOrderIzosdwanCgwDetails(List<OrderIzosdwanCgwDetails> orderIzosdwanCgwDetails) {
		this.orderIzosdwanCgwDetails = orderIzosdwanCgwDetails;
	}

	public List<QuoteIzoSdwanAttributeValue> getOrderSdwanAttributeValues() {
		return orderSdwanAttributeValues;
	}

	public void setOrderSdwanAttributeValues(List<QuoteIzoSdwanAttributeValue> orderSdwanAttributeValues) {
		this.orderSdwanAttributeValues = orderSdwanAttributeValues;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String checkList;
	
	private Integer customerId;
	
	private String orderType;
	
	private String orderCategory;
	
	private Boolean isMacdInitiated = false;
	
	private String serviceId;
	
	private String imageUrl;

	private List<OrderCwbAuditTrailDetailsBean> orderCwbAuditTrailDetails;

	private List<OrderSiteCategoryBean> orderSiteCategory;

	private List<OrderIzosdwanCpeConfigDetailsBean> orderIzosdwanCpeConfigDetails;
	
	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
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
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
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
	 * @return the termInMonths
	 */
	public Integer getTermInMonths() {
		return termInMonths;
	}

	/**
	 * @param termInMonths
	 *            the termInMonths to set
	 */
	public void setTermInMonths(Integer termInMonths) {
		this.termInMonths = termInMonths;
	}

	/**
	 * @return the orderToLeBeans
	 */
	public Set<OrderToLeBean> getOrderToLeBeans() {
		return orderToLeBeans;
	}

	/**
	 * @param orderToLeBeans
	 *            the orderToLeBeans to set
	 */
	public void setOrderToLeBeans(Set<OrderToLeBean> orderToLeBeans) {
		this.orderToLeBeans = orderToLeBeans;
	}

	/**
	 * @return the orderCode
	 */

	public String getOrderCode() {
		return orderCode;
	}

	/**
	 * @param orderCode
	 *            the orderCode to set
	 */

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getCheckList() {
		return checkList;
	}

	public void setCheckList(String checkList) {
		this.checkList = checkList;
	}
	
	

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public Boolean getIsMacdInitiated() {
		return isMacdInitiated;
	}

	public void setIsMacdInitiated(Boolean isMacdInitiated) {
		this.isMacdInitiated = isMacdInitiated;
	}
	
	

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	@Override
	public String toString() {
		return "OrdersBean [id=" + id + ", orderCode=" + orderCode + ", createdBy=" + createdBy + ", createdTime="
				+ createdTime + ", effectiveDate=" + effectiveDate + ", endDate=" + endDate + ", stage=" + stage + ", startDate=" + startDate
				+ ", status=" + status + ", termInMonths=" + termInMonths + ", orderToLeBeans=" + orderToLeBeans
				+ ", checkList=" + checkList 
				+ ", orderType=" + orderType 
				+ ", orderCategory=" + orderCategory 
				+ ", isMacdInitiated=" + isMacdInitiated
				+ ", serviceId=" + serviceId + "]";
	}
	public IzosdwanOrdersBean() {

	}
	/**
	 * 
	 * @param site
	 */
	public IzosdwanOrdersBean(OrderIzosdwanSite site) {
		super();
		this.id = site.getId();
		this.imageUrl = site.getImageUrl();
		this.stage = site.getStage();	
		this.effectiveDate = site.getEffectiveDate();
		this.imageUrl = site.getImageUrl();
		
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

	public void setOrderCwbAuditTrailDetails(List<OrderCwbAuditTrailDetailsBean> orderCwbAuditTrailDetails) {
		this.orderCwbAuditTrailDetails = orderCwbAuditTrailDetails;
	}

	public List<OrderCwbAuditTrailDetailsBean> getOrderCwbAuditTrailDetails() {
		return orderCwbAuditTrailDetails;
	}

	public void setOrderSiteCategory(List<OrderSiteCategoryBean> orderSiteCategory) {
		this.orderSiteCategory = orderSiteCategory;

	}

	public List<OrderSiteCategoryBean> getOrderSiteCategory() {
		return orderSiteCategory;
	}

	public void setOrderIzosdwanCpeConfigDetails(
			List<OrderIzosdwanCpeConfigDetailsBean> orderIzosdwanCpeConfigDetails) {
		this.orderIzosdwanCpeConfigDetails = orderIzosdwanCpeConfigDetails;
	}

	public List<OrderIzosdwanCpeConfigDetailsBean> getOrderIzosdwanCpeConfigDetails() {
		return orderIzosdwanCpeConfigDetails;
	}

}