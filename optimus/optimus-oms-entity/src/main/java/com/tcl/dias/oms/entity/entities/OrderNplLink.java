package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * Entity Class for order_npl_link table
 * 
 *
 * @author Biswajit 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "order_npl_link")
@NamedQuery(name = "OrderNplLink.findAll", query = "SELECT q FROM OrderNplLink q")
public class OrderNplLink implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@Column(name = "link_code")
	private String linkCode;

	private Byte status;
	
	private String stage;
	
	@Column(name = "site_A_id")
	private Integer siteAId;
	
	@Column(name = "site_B_id")
	private Integer siteBId;
	
	@Column(name = "product_solution_id")
	private Integer productSolutionId;
	
	
	@Column(name = "order_id")
	private Integer orderId;
	
	

	@Column(name = "created_by")
	private Integer createdBy;
		
	@Column(name = "service_id")
	private String serviceId;
	
	
	@Column(name = "chargeable_distance")
	private String chargeableDistance;
	
	@Column(name = "requestor_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date requestorDate;

	
	@OneToMany(mappedBy = "orderNplLink")
	private Set<OrderNplLinkSla> orderNplLinkSlas;
	
	// bi-directional many-to-one association to MstOrderSiteStage
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mst_order_link_stage_id")
	private MstOrderLinkStage mstOrderLinkStage;

	// bi-directional many-to-one association to MstOrderSiteStatus
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mst_order_link_status_id")
	private MstOrderLinkStatus mstOrderLinkStatus;

	public Date getRequestorDate() {
		return requestorDate;
	}

	public void setRequestorDate(Date requestorDate) {
		this.requestorDate = requestorDate;
	}
	
	@Column(name = "link_type")
	private String linkType;
	
	@Column(name = "siteA_type")
	private String siteAType;
	
	@Column(name = "siteB_type")
	private String siteBType;
	
	@Column(name = "mrc")
	private Double mrc;

	@Column(name = "nrc")
	private Double nrc;

	@Column(name = "arc")
	private Double arc;
	
	@Column(name = "tcv")
	private Double tcv;
	
	
	@Column(name = "effective_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date effectiveDate;
	
	@Column(name = "feasibility")
	private Byte feasibility;
	
	// bi-directional many-to-one association to Product
	@OneToMany(mappedBy = "orderNplLink")
	private List<OrderLinkFeasibility> linkFeasibilities;
	
	@Column(name = "link_bulk_update")
	private String linkBulkUpdate;	
	
	//added for sitewisebilling
	// bi-directional many-to-one association to OrderSiteBillingDetails
	@OneToMany(mappedBy = "orderNplLink")
	private Set<OrderSiteBillingDetails> OrderSiteBillingDetails;
	
	
	

	public Set<OrderSiteBillingDetails> getOrderSiteBillingDetails() {
		return OrderSiteBillingDetails;
	}

	public void setOrderSiteBillingDetails(Set<OrderSiteBillingDetails> orderSiteBillingDetails) {
		OrderSiteBillingDetails = orderSiteBillingDetails;
	}

	public String getLinkBulkUpdate() {
		return linkBulkUpdate;
	}

	public void setLinkBulkUpdate(String linkBulkUpdate) {
		this.linkBulkUpdate = linkBulkUpdate;
	}

	
	public Byte getFeasibility() {
		return feasibility;
	}

	public void setFeasibility(Byte feasibility) {
		this.feasibility = feasibility;
	}
	
	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getChargeableDistance() {
		return chargeableDistance;
	}

	public void setChargeableDistance(String chargeableDistance) {
		this.chargeableDistance = chargeableDistance;
	}

	public OrderNplLink() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getLinkCode() {
		return linkCode;
	}

	public void setLinkCode(String linkCode) {
		this.linkCode = linkCode;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Integer getSiteAId() {
		return siteAId;
	}

	public void setSiteAId(Integer siteAId) {
		this.siteAId = siteAId;
	}

	public Integer getSiteBId() {
		return siteBId;
	}

	public void setSiteBId(Integer siteBId) {
		this.siteBId = siteBId;
	}

	
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getOrderId() {
		return orderId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	

	
	public Integer getProductSolutionId() {
		return productSolutionId;
	}

	public void setProductSolutionId(Integer productSolutionId) {
		this.productSolutionId = productSolutionId;
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

	public Set<OrderNplLinkSla> getOrderNplLinkSlas() {
		return orderNplLinkSlas;
	}

	public void setOrderNplLinkSlas(Set<OrderNplLinkSla> orderNplLinkSlas) {
		this.orderNplLinkSlas = orderNplLinkSlas;
	}

	public List<OrderLinkFeasibility> getLinkFeasibilities() {
		return linkFeasibilities;
	}

	public void setLinkFeasibilities(List<OrderLinkFeasibility> linkFeasibilities) {
		this.linkFeasibilities = linkFeasibilities;
	}

	public MstOrderLinkStage getMstOrderLinkStage() {
		return mstOrderLinkStage;
	}

	public void setMstOrderLinkStage(MstOrderLinkStage mstOrderLinkStage) {
		this.mstOrderLinkStage = mstOrderLinkStage;
	}

	public MstOrderLinkStatus getMstOrderLinkStatus() {
		return mstOrderLinkStatus;
	}

	public void setMstOrderLinkStatus(MstOrderLinkStatus mstOrderLinkStatus) {
		this.mstOrderLinkStatus = mstOrderLinkStatus;
	}

	public Double getTcv() {
		return tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	
}