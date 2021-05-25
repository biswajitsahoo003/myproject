package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * 
 * This file contains the OrderIzosdwanSite.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="order_izosdwan_sites")
@NamedQuery(name="OrderIzosdwanSite.findAll", query="SELECT o FROM OrderIzosdwanSite o")
public class OrderIzosdwanSite implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Double arc;

	@Column(name="created_by")
	private Integer createdBy;

	@Column(name="created_time")
	private Date createdTime;

	@Column(name="effective_date")
	private Date effectiveDate;

	@Column(name="erf_loc_sitea_location_id")
	private Integer erfLocSiteaLocationId;

	@Column(name="erf_loc_sitea_site_code")
	private String erfLocSiteaSiteCode;

	@Column(name="erf_loc_siteb_location_id")
	private Integer erfLocSitebLocationId;

	@Column(name="erf_loc_siteb_site_code")
	private String erfLocSitebSiteCode;

	@Column(name="erf_lr_solution_id")
	private String erfLrSolutionId;

	@Column(name="erf_service_inventory_tps_service_id")
	private String erfServiceInventoryTpsServiceId;

	private Byte feasibility;

	@Column(name="fp_status")
	private String fpStatus;

	@Column(name="image_url")
	private String imageUrl;

	@Column(name="is_auto_bw_upgraded")
	private Byte isAutoBwUpgraded;

	@Column(name="is_feasiblity_check_required")
	private Byte isFeasiblityCheckRequired;

	@Column(name="is_izo")
	private Byte isIzo;

	@Column(name="is_shared")
	private String isShared;

	@Column(name="is_task_triggered")
	private Byte isTaskTriggered;

	@Column(name="is_tax_exempted")
	private Byte isTaxExempted;

	@Column(name="izosdwan_site_offering")
	private String izosdwanSiteOffering;

	@Column(name="izosdwan_site_product")
	private String izosdwanSiteProduct;

	@Column(name="izosdwan_site_type")
	private String izosdwanSiteType;

	@Column(name="lat_long")
	private String latLong;

	@Column(name="macd_change_bandwidth_flag")
	private String macdChangeBandwidthFlag;

	@Column(name="management_type")
	private String managementType;

	@Column(name="markup_pct")
	private Double markupPct;

	private Double mrc;

	@Column(name="new_cpe")
	private String newCpe;

	@Column(name="new_lastmile_bandwidth")
	private String newLastmileBandwidth;

	@Column(name="new_port_bandwidth")
	private String newPortBandwidth;

	private Double nrc;

	@Column(name="old_cpe")
	private String oldCpe;

	@Column(name="old_lastmile_bandwidth")
	private String oldLastmileBandwidth;

	@Column(name="old_port_bandwidth")
	private String oldPortBandwidth;

	@Column(name="pri_sec")
	private String priSec;

	@Column(name="primary_service_id")
	private String primaryServiceId;

	@Column(name="quote_version")
	private Integer quoteVersion;

	@Column(name="requestor_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date requestorDate;

	@Lob
	@Column(name="service_site_address")
	private String serviceSiteAddress;

	@Column(name="service_site_country")
	private String serviceSiteCountry;

	@Column(name="si_parent_order_id")
	private Integer siParentOrderId;

	@Column(name="si_service_detail_id")
	private Integer siServiceDetailId;

	@Column(name="site_code")
	private String siteCode;

	private String stage;

	private Byte status;

	private Double tcv;

	@Column(name="updated_by")
	private Integer updatedBy;

	@Column(name="updated_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedTime;
	
	@Column(name="site_category_id")
	private Integer siteCategoryId;

	//bi-directional many-to-one association to OrderIzosdwanSiteSla
	@OneToMany(mappedBy="orderIzosdwanSite")
	private Set<OrderIzosdwanSiteSla> orderIzosdwanSiteSlas;

	//bi-directional many-to-one association to OrderProductSolution
	@ManyToOne
	@JoinColumn(name="product_solutions_id")
	private OrderProductSolution orderProductSolution;
	
	@OneToMany(mappedBy = "orderIzosdwanSite")
	private Set<OrderIzosdwanSiteFeasibility> orderIzosdwanSiteFeasibilities;

	public OrderIzosdwanSite() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getArc() {
		return this.arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
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

	public String getErfServiceInventoryTpsServiceId() {
		return this.erfServiceInventoryTpsServiceId;
	}

	public void setErfServiceInventoryTpsServiceId(String erfServiceInventoryTpsServiceId) {
		this.erfServiceInventoryTpsServiceId = erfServiceInventoryTpsServiceId;
	}

	public Byte getFeasibility() {
		return this.feasibility;
	}

	public void setFeasibility(Byte feasibility) {
		this.feasibility = feasibility;
	}

	public String getFpStatus() {
		return this.fpStatus;
	}

	public void setFpStatus(String fpStatus) {
		this.fpStatus = fpStatus;
	}

	public String getImageUrl() {
		return this.imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Byte getIsAutoBwUpgraded() {
		return this.isAutoBwUpgraded;
	}

	public void setIsAutoBwUpgraded(Byte isAutoBwUpgraded) {
		this.isAutoBwUpgraded = isAutoBwUpgraded;
	}

	public Byte getIsFeasiblityCheckRequired() {
		return this.isFeasiblityCheckRequired;
	}

	public void setIsFeasiblityCheckRequired(Byte isFeasiblityCheckRequired) {
		this.isFeasiblityCheckRequired = isFeasiblityCheckRequired;
	}

	public Byte getIsIzo() {
		return this.isIzo;
	}

	public void setIsIzo(Byte isIzo) {
		this.isIzo = isIzo;
	}

	public String getIsShared() {
		return this.isShared;
	}

	public void setIsShared(String isShared) {
		this.isShared = isShared;
	}

	public Byte getIsTaskTriggered() {
		return this.isTaskTriggered;
	}

	public void setIsTaskTriggered(Byte isTaskTriggered) {
		this.isTaskTriggered = isTaskTriggered;
	}

	public Byte getIsTaxExempted() {
		return this.isTaxExempted;
	}

	public void setIsTaxExempted(Byte isTaxExempted) {
		this.isTaxExempted = isTaxExempted;
	}

	public String getIzosdwanSiteOffering() {
		return this.izosdwanSiteOffering;
	}

	public void setIzosdwanSiteOffering(String izosdwanSiteOffering) {
		this.izosdwanSiteOffering = izosdwanSiteOffering;
	}

	public String getIzosdwanSiteProduct() {
		return this.izosdwanSiteProduct;
	}

	public void setIzosdwanSiteProduct(String izosdwanSiteProduct) {
		this.izosdwanSiteProduct = izosdwanSiteProduct;
	}

	public String getIzosdwanSiteType() {
		return this.izosdwanSiteType;
	}

	public void setIzosdwanSiteType(String izosdwanSiteType) {
		this.izosdwanSiteType = izosdwanSiteType;
	}

	public String getLatLong() {
		return this.latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	public String getMacdChangeBandwidthFlag() {
		return this.macdChangeBandwidthFlag;
	}

	public void setMacdChangeBandwidthFlag(String macdChangeBandwidthFlag) {
		this.macdChangeBandwidthFlag = macdChangeBandwidthFlag;
	}

	public String getManagementType() {
		return this.managementType;
	}

	public void setManagementType(String managementType) {
		this.managementType = managementType;
	}

	public Double getMarkupPct() {
		return this.markupPct;
	}

	public void setMarkupPct(Double markupPct) {
		this.markupPct = markupPct;
	}

	public Double getMrc() {
		return this.mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public String getNewCpe() {
		return this.newCpe;
	}

	public void setNewCpe(String newCpe) {
		this.newCpe = newCpe;
	}

	public String getNewLastmileBandwidth() {
		return this.newLastmileBandwidth;
	}

	public void setNewLastmileBandwidth(String newLastmileBandwidth) {
		this.newLastmileBandwidth = newLastmileBandwidth;
	}

	public String getNewPortBandwidth() {
		return this.newPortBandwidth;
	}

	public void setNewPortBandwidth(String newPortBandwidth) {
		this.newPortBandwidth = newPortBandwidth;
	}

	public Double getNrc() {
		return this.nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public String getOldCpe() {
		return this.oldCpe;
	}

	public void setOldCpe(String oldCpe) {
		this.oldCpe = oldCpe;
	}

	public String getOldLastmileBandwidth() {
		return this.oldLastmileBandwidth;
	}

	public void setOldLastmileBandwidth(String oldLastmileBandwidth) {
		this.oldLastmileBandwidth = oldLastmileBandwidth;
	}

	public String getOldPortBandwidth() {
		return this.oldPortBandwidth;
	}

	public void setOldPortBandwidth(String oldPortBandwidth) {
		this.oldPortBandwidth = oldPortBandwidth;
	}

	public String getPriSec() {
		return this.priSec;
	}

	public void setPriSec(String priSec) {
		this.priSec = priSec;
	}

	public String getPrimaryServiceId() {
		return this.primaryServiceId;
	}

	public void setPrimaryServiceId(String primaryServiceId) {
		this.primaryServiceId = primaryServiceId;
	}

	public Integer getQuoteVersion() {
		return this.quoteVersion;
	}

	public void setQuoteVersion(Integer quoteVersion) {
		this.quoteVersion = quoteVersion;
	}

	public Date getRequestorDate() {
		return this.requestorDate;
	}

	public void setRequestorDate(Date requestorDate) {
		this.requestorDate = requestorDate;
	}

	public String getServiceSiteAddress() {
		return this.serviceSiteAddress;
	}

	public void setServiceSiteAddress(String serviceSiteAddress) {
		this.serviceSiteAddress = serviceSiteAddress;
	}

	public String getServiceSiteCountry() {
		return this.serviceSiteCountry;
	}

	public void setServiceSiteCountry(String serviceSiteCountry) {
		this.serviceSiteCountry = serviceSiteCountry;
	}

	public Integer getSiParentOrderId() {
		return this.siParentOrderId;
	}

	public void setSiParentOrderId(Integer siParentOrderId) {
		this.siParentOrderId = siParentOrderId;
	}

	public Integer getSiServiceDetailId() {
		return this.siServiceDetailId;
	}

	public void setSiServiceDetailId(Integer siServiceDetailId) {
		this.siServiceDetailId = siServiceDetailId;
	}

	public String getSiteCode() {
		return this.siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Double getTcv() {
		return this.tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public Integer getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	
	public Integer getSiteCategoryId() {
		return siteCategoryId;
	}

	public void setSiteCategoryId(Integer siteCategoryId) {
		this.siteCategoryId = siteCategoryId;
	}

	public Set<OrderIzosdwanSiteSla> getOrderIzosdwanSiteSlas() {
		return this.orderIzosdwanSiteSlas;
	}

	public void setOrderIzosdwanSiteSlas(Set<OrderIzosdwanSiteSla> orderIzosdwanSiteSlas) {
		this.orderIzosdwanSiteSlas = orderIzosdwanSiteSlas;
	}

	public OrderIzosdwanSiteSla addOrderIzosdwanSiteSla(OrderIzosdwanSiteSla orderIzosdwanSiteSla) {
		getOrderIzosdwanSiteSlas().add(orderIzosdwanSiteSla);
		orderIzosdwanSiteSla.setOrderIzosdwanSite(this);

		return orderIzosdwanSiteSla;
	}

	public OrderIzosdwanSiteSla removeOrderIzosdwanSiteSla(OrderIzosdwanSiteSla orderIzosdwanSiteSla) {
		getOrderIzosdwanSiteSlas().remove(orderIzosdwanSiteSla);
		orderIzosdwanSiteSla.setOrderIzosdwanSite(null);

		return orderIzosdwanSiteSla;
	}

	public OrderProductSolution getOrderProductSolution() {
		return this.orderProductSolution;
	}

	public void setOrderProductSolution(OrderProductSolution orderProductSolution) {
		this.orderProductSolution = orderProductSolution;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public Set<OrderIzosdwanSiteFeasibility> getOrderIzosdwanSiteFeasibilities() {
		return orderIzosdwanSiteFeasibilities;
	}

	public void setOrderIzosdwanSiteFeasibilities(Set<OrderIzosdwanSiteFeasibility> orderIzosdwanSiteFeasibilities) {
		this.orderIzosdwanSiteFeasibilities = orderIzosdwanSiteFeasibilities;
	}

}