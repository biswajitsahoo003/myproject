package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
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
 * Entity Class
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "order_ill_sites")
@NamedQuery(name = "OrderIllSite.findAll", query = "SELECT o FROM OrderIllSite o")
public class OrderIllSite implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@Column(name = "effective_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date effectiveDate;

	@Column(name = "erf_loc_sitea_location_id")
	private Integer erfLocSiteaLocationId;

	@Column(name = "erf_loc_sitea_site_code")
	private String erfLocSiteaSiteCode;

	@Column(name = "erf_loc_siteb_location_id")
	private Integer erfLocSitebLocationId;

	@Column(name = "erf_loc_siteb_site_code")
	private String erfLocSitebSiteCode;

	@Column(name = "erf_lr_solution_id")
	private String erfLrSolutionId;

	@Column(name = "fp_status")
	private String fpStatus;

	private Byte feasibility;

	@Column(name = "is_izo")
	private Byte isIzoPc;

	@Column(name = "tcv")
	private Double tcv;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "is_tax_exempted")
	private Byte isTaxExempted;

	private Double mrc;

	private Double nrc;

	private Double arc;

	@Column(name = "requestor_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date requestorDate;

	@Column(name = "site_code")
	private String siteCode;

	private String stage;

	private Byte status;
	
	

	// bi-directional many-to-one association to MstOrderSiteStage
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mst_order_site_stage_id")
	private MstOrderSiteStage mstOrderSiteStage;

	// bi-directional many-to-one association to MstOrderSiteStatus
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mst_order_site_status_id")
	private MstOrderSiteStatus mstOrderSiteStatus;

	// bi-directional many-to-one association to QuoteIllSiteSla
	@OneToMany(mappedBy = "orderIllSite")
	private Set<OrderIllSiteSla> orderIllSiteSlas;

	// bi-directional many-to-one association to QuoteIllSiteSla
	@OneToMany(mappedBy = "orderIllSite")
	private Set<OrderSiteFeasibility> orderSiteFeasibility;

	// bi-directional many-to-one association to OrderProductSolution
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_solutions_id")
	private OrderProductSolution orderProductSolution;
	
	
	@Column(name = "erf_service_inventory_tps_service_id")
	private String erfServiceInventoryTpsServiceId;
	
	@Column(name = "macd_change_bandwidth_flag")
	private String macdChangeBandwidthFlag;
	
	@Column(name="npl_shift_site_flag")
	private Integer nplShiftSiteFlag;

	@Column(name = "is_colo")
	private Byte isColo;
	
	//added for multi vrf
	// bi-directional many-to-one association to QuoteVrfSites
	@OneToMany(mappedBy = "orderIllSite")
	private Set<OrderVrfSites> orderVrfSites;
	
	@Column(name = "site_bulk_update")
	private String siteBulkUpdate;	
	
	//added for sitewisebilling
	// bi-directional many-to-one association to OrderSiteBillingDetails
	@OneToMany(mappedBy = "orderIllSite")
	private Set<OrderSiteBillingDetails> OrderSiteBillingDetails;
	
	
	
	
	public Set<OrderSiteBillingDetails> getOrderSiteBillingDetails() {
		return OrderSiteBillingDetails;
	}

	public void setOrderSiteBillingDetails(Set<OrderSiteBillingDetails> orderSiteBillingDetails) {
		OrderSiteBillingDetails = orderSiteBillingDetails;
	}

	public String getSiteBulkUpdate() {
		return siteBulkUpdate;
	}

	public void setSiteBulkUpdate(String siteBulkUpdate) {
		this.siteBulkUpdate = siteBulkUpdate;
	}
		
	public Set<OrderVrfSites> getOrderVrfSites() {
		return orderVrfSites;
	}

	public void setOrderVrfSites(Set<OrderVrfSites> orderVrfSites) {
		this.orderVrfSites = orderVrfSites;
	}

	public OrderIllSite() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getEffectiveDate() {
		return this.effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Integer getErfLocSiteaLocationId() {
		return this.erfLocSiteaLocationId;
	}

	public void setErfLocSiteaLocationId(Integer erfLocSiteaLocationId) {
		this.erfLocSiteaLocationId = erfLocSiteaLocationId;
	}

	public String getErfLocSiteaSiteCode() {
		return this.erfLocSiteaSiteCode;
	}

	public void setErfLocSiteaSiteCode(String erfLocSiteaSiteCode) {
		this.erfLocSiteaSiteCode = erfLocSiteaSiteCode;
	}

	public Integer getErfLocSitebLocationId() {
		return this.erfLocSitebLocationId;
	}

	public void setErfLocSitebLocationId(Integer erfLocSitebLocationId) {
		this.erfLocSitebLocationId = erfLocSitebLocationId;
	}

	public String getErfLocSitebSiteCode() {
		return this.erfLocSitebSiteCode;
	}

	public void setErfLocSitebSiteCode(String erfLocSitebSiteCode) {
		this.erfLocSitebSiteCode = erfLocSitebSiteCode;
	}

	public String getErfLrSolutionId() {
		return this.erfLrSolutionId;
	}

	public void setErfLrSolutionId(String erfLrSolutionId) {
		this.erfLrSolutionId = erfLrSolutionId;
	}

	public Byte getFeasibility() {
		return this.feasibility;
	}

	public void setFeasibility(Byte feasibility) {
		this.feasibility = feasibility;
	}

	public String getImageUrl() {
		return this.imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Byte getIsTaxExempted() {
		return this.isTaxExempted;
	}

	public void setIsTaxExempted(Byte isTaxExempted) {
		this.isTaxExempted = isTaxExempted;
	}

	public Double getMrc() {
		return this.mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return this.nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Date getRequestorDate() {
		return this.requestorDate;
	}

	public void setRequestorDate(Date requestorDate) {
		this.requestorDate = requestorDate;
	}

	public String getSiteCode() {
		return this.siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getStage() {
		return this.stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public OrderProductSolution getOrderProductSolution() {
		return this.orderProductSolution;
	}

	public void setOrderProductSolution(OrderProductSolution orderProductSolution) {
		this.orderProductSolution = orderProductSolution;
	}

	/**
	 * @return the orderIllSiteSlas
	 */
	public Set<OrderIllSiteSla> getOrderIllSiteSlas() {
		if (orderIllSiteSlas == null) {
			orderIllSiteSlas = new HashSet<>();
		}
		return orderIllSiteSlas;
	}

	/**
	 * @param orderIllSiteSlas
	 *            the orderIllSiteSlas to set
	 */
	public void setOrderIllSiteSlas(Set<OrderIllSiteSla> orderIllSiteSlas) {
		this.orderIllSiteSlas = orderIllSiteSlas;
	}

	/**
	 * @return the orderSiteFeasibility
	 */
	public Set<OrderSiteFeasibility> getOrderSiteFeasibility() {
		return orderSiteFeasibility;
	}

	/**
	 * @param orderSiteFeasibility
	 *            the orderSiteFeasibility to set
	 */
	public void setOrderSiteFeasibility(Set<OrderSiteFeasibility> orderSiteFeasibility) {
		this.orderSiteFeasibility = orderSiteFeasibility;
	}

	public MstOrderSiteStage getMstOrderSiteStage() {
		return mstOrderSiteStage;
	}

	public void setMstOrderSiteStage(MstOrderSiteStage mstOrderSiteStage) {
		this.mstOrderSiteStage = mstOrderSiteStage;
	}

	public MstOrderSiteStatus getMstOrderSiteStatus() {
		return mstOrderSiteStatus;
	}

	public void setMstOrderSiteStatus(MstOrderSiteStatus mstOrderSiteStatus) {
		this.mstOrderSiteStatus = mstOrderSiteStatus;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public String getFpStatus() {
		return fpStatus;
	}

	public void setFpStatus(String fpStatus) {
		this.fpStatus = fpStatus;
	}

	public Double getTcv() {
		return tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public Byte getIsIzoPc() {
		return isIzoPc;
	}

	public void setIsIzoPc(Byte isIzoPc) {
		this.isIzoPc = isIzoPc;
	}

	/**
	 * @return the erfServiceInventoryTpsServiceId
	 */
	public String getErfServiceInventoryTpsServiceId() {
		return erfServiceInventoryTpsServiceId;
	}

	/**
	 * @param erfServiceInventoryTpsServiceId the erfServiceInventoryTpsServiceId to set
	 */
	public void setErfServiceInventoryTpsServiceId(String erfServiceInventoryTpsServiceId) {
		this.erfServiceInventoryTpsServiceId = erfServiceInventoryTpsServiceId;
	}

	/**
	 * @return the macdChangeBandwidthFlag
	 */
	public String getMacdChangeBandwidthFlag() {
		return macdChangeBandwidthFlag;
	}

	/**
	 * @param macdChangeBandwidthFlag the macdChangeBandwidthFlag to set
	 */
	public void setMacdChangeBandwidthFlag(String macdChangeBandwidthFlag) {
		this.macdChangeBandwidthFlag = macdChangeBandwidthFlag;
	}

	public Integer getNplShiftSiteFlag() {
		return nplShiftSiteFlag;
	}

	public void setNplShiftSiteFlag(Integer nplShiftSiteFlag) {
		this.nplShiftSiteFlag = nplShiftSiteFlag;
	}

	public Byte getIsColo() {
		return isColo;
	}

	public void setIsColo(Byte isColo) {
		this.isColo = isColo;
	}
	
	
	
	

}