package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * Entity Class
 * 
 *
 * @author AnneF
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "order_ill_site_to_service")
@NamedQuery(name = "OrderIllSiteToService.findAll", query = "SELECT o FROM OrderIllSiteToService o")
public class OrderIllSiteToService implements Serializable {
	
private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "erf_service_inventory_tps_service_id")
	private String erfServiceInventoryTpsServiceId;
	
	@Column(name = "erf_service_inventory_parent_order_id")
	private Integer erfServiceInventoryParentOrderId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name= "order_site_id")
	private OrderIllSite orderIllSite;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name= "order_izosdwan_site_id")
	private OrderIzosdwanSite orderIzosdwanSite;
	
	public OrderIzosdwanSite getOrderIzosdwanSite() {
		return orderIzosdwanSite;
	}

	public void setOrderIzosdwanSite(OrderIzosdwanSite orderIzosdwanSite) {
		this.orderIzosdwanSite = orderIzosdwanSite;
	}

	@Column(name = "erf_service_inventory_service_detail_id")
	private Integer erfServiceInventoryServiceDetailId;
	
	@Column(name = "erf_service_inventory_parent_opty_id")
	private Integer tpsSfdcParentOptyId;
	
	@Column(name="change_request_summary")
	private String changeRequestSummary;

	@Column(name="erf_service_inventory_service_link_type")
	private String type;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="order_to_le_id")
	private OrderToLe orderToLe;
	
	@Column(name="bandwidth_changed")
	private Byte bandwidthChanged;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="order_link_id")
	private OrderNplLink orderNplLink;

	@Column(name="parent_site_id")
	private Integer parentSiteId;

	@Column(name="parent_order_id")
	private Integer parentOrderId;

	@Column(name="o2c_service_id")
	private String o2cServiceId;

	@Column(name="service_type")
	private String serviceType;
	
	@Column(name="erf_sfdc_order_type")
	private String erfSfdcOrderType;
	
	@Column(name="erf_sfdc_sub_type")
	private String erfSfdcSubType;

	@Column(name="allow_amendment")
	private String allowAmendment;
	
	@Column(name="erf_service_inventory_pri_sec_link_service_id")
	private String erfServiceInventoryPriSecLinkServiceId;
	
	@Column(name="erf_service_inventory_o2c_link_type")
	private String erfServiceInventoryO2cLinkType;
	
	@Column(name ="cancelled_parent_order_id")
	private Integer cancelledParentOrderId;
	
	@Column(name ="cancelled_parent_site_id")
	private Integer cancelledParentSiteId;
	
	@Column(name="cancelled_service_type")
	private String cancelledServiceType;
	
	@Column(name="allow_cancellation")
	private String allowCancellation;
	
	@Column(name ="cancellation_reason")
	private String cancellationReason;
	
	@Column(name="absorbed_or_passed_on")
	private String absorbedOrPassedOn;
	
	@Column(name="lead_to_rfs_date")
	private String leadToRFSDate;
	
	@Column(name="effective_date_of_change")
	@Temporal(TemporalType.TIMESTAMP)
	private Date effectiveDateOfChange;
	
	@Column(name = "tps_sfdc_product_id")
	private String tpsSfdcProductId;

	@Column(name = "tps_sfdc_product_name")
	private String tpsSfdcProductName;
	
	@Column(name = "tps_sfdc_opty_id")
	private String tpsSfdcOptyId;
	
	@Column(name="tps_sfdc_waiver_id")
	private String tpsSfdcWaiverId;
	
	@Column(name="tps_sfdc_waiver_name")
	private String tpsSfdcWaiverName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getErfServiceInventoryTpsServiceId() {
		return erfServiceInventoryTpsServiceId;
	}

	public void setErfServiceInventoryTpsServiceId(String erfServiceInventoryTpsServiceId) {
		this.erfServiceInventoryTpsServiceId = erfServiceInventoryTpsServiceId;
	}

	public Integer getErfServiceInventoryParentOrderId() {
		return erfServiceInventoryParentOrderId;
	}

	public void setErfServiceInventoryParentOrderId(Integer erfServiceInventoryParentOrderId) {
		this.erfServiceInventoryParentOrderId = erfServiceInventoryParentOrderId;
	}

	public OrderIllSite getOrderIllSite() {
		return orderIllSite;
	}

	public void setOrderIllSite(OrderIllSite orderIllSite) {
		this.orderIllSite = orderIllSite;
	}

	public Integer getErfServiceInventoryServiceDetailId() {
		return erfServiceInventoryServiceDetailId;
	}

	public void setErfServiceInventoryServiceDetailId(Integer erfServiceInventoryServiceDetailId) {
		this.erfServiceInventoryServiceDetailId = erfServiceInventoryServiceDetailId;
	}

	public Integer getTpsSfdcParentOptyId() {
		return tpsSfdcParentOptyId;
	}

	public void setTpsSfdcParentOptyId(Integer tpsSfdcParentOptyId) {
		this.tpsSfdcParentOptyId = tpsSfdcParentOptyId;
	}

	public String getChangeRequestSummary() {
		return changeRequestSummary;
	}

	public void setChangeRequestSummary(String changeRequestSummary) {
		this.changeRequestSummary = changeRequestSummary;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public OrderToLe getOrderToLe() {
		return orderToLe;
	}

	public void setOrderToLe(OrderToLe orderToLe) {
		this.orderToLe = orderToLe;
	}

	public Byte getBandwidthChanged() {
		return bandwidthChanged;
	}

	public void setBandwidthChanged(Byte bandwidthChanged) {
		this.bandwidthChanged = bandwidthChanged;
	}

	public OrderNplLink getOrderNplLink() {
		return orderNplLink;
	}

	public void setOrderNplLink(OrderNplLink orderNplLink) {
		this.orderNplLink = orderNplLink;
	}
	
	public Integer getParentSiteId() {
		return parentSiteId;
	}

	public void setParentSiteId(Integer parentSiteId) {
		this.parentSiteId = parentSiteId;
	}

	public Integer getParentOrderId() {
		return parentOrderId;
	}

	public void setParentOrderId(Integer parentOrderId) {
		this.parentOrderId = parentOrderId;
	}

	public String getO2cServiceId() {
		return o2cServiceId;
	}

	public void setO2cServiceId(String o2cServiceId) {
		this.o2cServiceId = o2cServiceId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getErfSfdcOrderType() {
		return erfSfdcOrderType;
	}

	public void setErfSfdcOrderType(String erfSfdcOrderType) {
		this.erfSfdcOrderType = erfSfdcOrderType;
	}

	public String getErfSfdcSubType() {
		return erfSfdcSubType;
	}

	public void setErfSfdcSubType(String erfSfdcSubType) {
		this.erfSfdcSubType = erfSfdcSubType;
	}

	public String getAllowAmendment() {
		return allowAmendment;
	}

	public void setAllowAmendment(String allowAmendment) {
		this.allowAmendment = allowAmendment;
	}

	public String getErfServiceInventoryPriSecLinkServiceId() {
		return erfServiceInventoryPriSecLinkServiceId;
	}

	public void setErfServiceInventoryPriSecLinkServiceId(String erfServiceInventoryPriSecLinkServiceId) {
		this.erfServiceInventoryPriSecLinkServiceId = erfServiceInventoryPriSecLinkServiceId;
	}

	public String getErfServiceInventoryO2cLinkType() {
		return erfServiceInventoryO2cLinkType;
	}

	public void setErfServiceInventoryO2cLinkType(String erfServiceInventoryO2cLinkType) {
		this.erfServiceInventoryO2cLinkType = erfServiceInventoryO2cLinkType;
	}

	public Integer getCancelledParentOrderId() {
		return cancelledParentOrderId;
	}

	public void setCancelledParentOrderId(Integer cancelledParentOrderId) {
		this.cancelledParentOrderId = cancelledParentOrderId;
	}

	public Integer getCancelledParentSiteId() {
		return cancelledParentSiteId;
	}

	public void setCancelledParentSiteId(Integer cancelledParentSiteId) {
		this.cancelledParentSiteId = cancelledParentSiteId;
	}

	public String getCancelledServiceType() {
		return cancelledServiceType;
	}

	public void setCancelledServiceType(String cancelledServiceType) {
		this.cancelledServiceType = cancelledServiceType;
	}

	public String getAllowCancellation() {
		return allowCancellation;
	}

	public void setAllowCancellation(String allowCancellation) {
		this.allowCancellation = allowCancellation;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public String getAbsorbedOrPassedOn() {
		return absorbedOrPassedOn;
	}

	public void setAbsorbedOrPassedOn(String absorbedOrPassedOn) {
		this.absorbedOrPassedOn = absorbedOrPassedOn;
	}

	public String getLeadToRFSDate() {
		return leadToRFSDate;
	}

	public void setLeadToRFSDate(String leadToRFSDate) {
		this.leadToRFSDate = leadToRFSDate;
	}

	public Date getEffectiveDateOfChange() {
		return effectiveDateOfChange;
	}

	public void setEffectiveDateOfChange(Date effectiveDateOfChange) {
		this.effectiveDateOfChange = effectiveDateOfChange;
	}

	public String getTpsSfdcProductId() {
		return tpsSfdcProductId;
	}

	public void setTpsSfdcProductId(String tpsSfdcProductId) {
		this.tpsSfdcProductId = tpsSfdcProductId;
	}

	public String getTpsSfdcProductName() {
		return tpsSfdcProductName;
	}

	public void setTpsSfdcProductName(String tpsSfdcProductName) {
		this.tpsSfdcProductName = tpsSfdcProductName;
	}

	public String getTpsSfdcOptyId() {
		return tpsSfdcOptyId;
	}

	public void setTpsSfdcOptyId(String tpsSfdcOptyId) {
		this.tpsSfdcOptyId = tpsSfdcOptyId;
	}

	public String getTpsSfdcWaiverId() {
		return tpsSfdcWaiverId;
	}

	public void setTpsSfdcWaiverId(String tpsSfdcWaiverId) {
		this.tpsSfdcWaiverId = tpsSfdcWaiverId;
	}

	public String getTpsSfdcWaiverName() {
		return tpsSfdcWaiverName;
	}

	public void setTpsSfdcWaiverName(String tpsSfdcWaiverName) {
		this.tpsSfdcWaiverName = tpsSfdcWaiverName;
	}

	
	
	
}
