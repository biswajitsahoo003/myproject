package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
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
 * This file contains entity class for quote_izosdwan_sites table
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="quote_izosdwan_sites")
@NamedQuery(name="QuoteIzosdwanSite.findAll", query="SELECT q FROM QuoteIzosdwanSite q")
public class QuoteIzosdwanSite implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Double arc;

	@Column(name="created_by")
	private Integer createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_time")
	private Date createdTime;
	
	@Temporal(TemporalType.TIMESTAMP)
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

	@Column(name="is_izo")
	private Byte isIzo;

	@Column(name="is_task_triggered")
	private Integer isTaskTriggered;

	@Column(name="is_tax_exempted")
	private Byte isTaxExempted;

	@Column(name="izosdwan_site_offering")
	private String izosdwanSiteOffering;

	@Column(name="izosdwan_site_type")
	private String izosdwanSiteType;

	@Column(name="macd_change_bandwidth_flag")
	private String macdChangeBandwidthFlag;

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

	@Column(name="quote_version")
	private Integer quoteVersion;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="requestor_date")
	private Date requestorDate;

	@Column(name="site_code")
	private String siteCode;

	private Byte status;

	private Double tcv;
	
	@Column(name = "pri_sec")
	private String priSec;
	
	@Column(name="management_type")
	private String managementType;
	
	@Column(name="izosdwan_site_product")
	private String izosdwanSiteProduct;
	
	@Column(name="lat_long")
	private String latLong;
	
	@Column(name="is_feasiblity_check_required")
	private Integer isFeasiblityCheckRequired;
	
	@Column(name="si_parent_order_id")
	private Integer siParentOrderId;
	
	@Column(name="si_service_detail_id")
	private Integer siServiceDetailsId;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_time")
	private Date updatedTime;
	
	@Column(name="primary_service_id")
	private String primaryServiceId;
	
	@Column(name="service_site_address")
	private String serviceSiteAddress;
	
	@Column(name="service_site_country")
	private String serviceSiteCountry;
	
	@Column(name="site_category_id")
	private Integer siteCategoryId;

	public String getPrimaryServiceId() {
		return primaryServiceId;
	}

	public void setPrimaryServiceId(String primaryServiceId) {
		this.primaryServiceId = primaryServiceId;
	}

	//bi-directional many-to-one association to QuoteIzosdwanSiteSla
	@OneToMany(mappedBy="quoteIzosdwanSite")
	private Set<QuoteIzosdwanSiteSla> quoteIzosdwanSiteSlas;
	
	@Column(name="is_shared")
	private String isShared = "N";
	
	@Column(name="markup_pct")
	private Double markupPct;
	
	@Column(name = "mf_status")
	private String mfStatus;

	@Column(name = "mf_task_triggered")
	private Integer mfTaskTriggered;
	
	@Column(name="is_pricing_check_required")
	private Integer isPricingCheckRequired;
	
	@Column(name="sdwan_price")
	private Byte sdwanPrice;
	
	public Byte getSdwanPrice() {
		return sdwanPrice;
	}

	public void setSdwanPrice(Byte sdwanPrice) {
		this.sdwanPrice = sdwanPrice;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Double getMarkupPct() {
		return markupPct;
	}

	public void setMarkupPct(Double markupPct) {
		this.markupPct = markupPct;
	}

	public String getIzosdwanSiteProduct() {
		return izosdwanSiteProduct;
	}

	public void setIzosdwanSiteProduct(String izosdwanSiteProduct) {
		this.izosdwanSiteProduct = izosdwanSiteProduct;
	}

	public String getLatLong() {
		return latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	public Integer getIsFeasiblityCheckRequired() {
		return isFeasiblityCheckRequired;
	}

	public void setIsFeasiblityCheckRequired(Integer isFeasiblityCheckRequired) {
		this.isFeasiblityCheckRequired = isFeasiblityCheckRequired;
	}

	public String getManagementType() {
		return managementType;
	}

	public void setManagementType(String managementType) {
		this.managementType = managementType;
	}

	public String getPriSec() {
		return priSec;
	}

	public void setPriSec(String priSec) {
		this.priSec = priSec;
	}

	public Byte getIsAutoBwUpgraded() {
		return isAutoBwUpgraded;
	}

	public void setIsAutoBwUpgraded(Byte isAutoBwUpgraded) {
		this.isAutoBwUpgraded = isAutoBwUpgraded;
	}

	@Column(name =  "is_auto_bw_upgraded")
	private Byte isAutoBwUpgraded;

	//bi-directional many-to-one association to ProductSolution
	@ManyToOne
	@JoinColumn(name="product_solutions_id")
	private ProductSolution productSolution;

	public QuoteIzosdwanSite() {
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

	public Byte getIsIzo() {
		return this.isIzo;
	}

	public void setIsIzo(Byte isIzo) {
		this.isIzo = isIzo;
	}

	public Integer getIsTaskTriggered() {
		return this.isTaskTriggered;
	}

	public void setIsTaskTriggered(Integer isTaskTriggered) {
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

	public String getIzosdwanSiteType() {
		return this.izosdwanSiteType;
	}

	public void setIzosdwanSiteType(String izosdwanSiteType) {
		this.izosdwanSiteType = izosdwanSiteType;
	}

	public String getMacdChangeBandwidthFlag() {
		return this.macdChangeBandwidthFlag;
	}

	public void setMacdChangeBandwidthFlag(String macdChangeBandwidthFlag) {
		this.macdChangeBandwidthFlag = macdChangeBandwidthFlag;
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

	public ProductSolution getProductSolution() {
		return this.productSolution;
	}

	public void setProductSolution(ProductSolution productSolution) {
		this.productSolution = productSolution;
	}

	public Set<QuoteIzosdwanSiteSla> getQuoteIzosdwanSiteSlas() {
		return quoteIzosdwanSiteSlas;
	}

	public void setQuoteIzosdwanSiteSlas(Set<QuoteIzosdwanSiteSla> quoteIzosdwanSiteSlas) {
		this.quoteIzosdwanSiteSlas = quoteIzosdwanSiteSlas;
	}

	public Integer getSiParentOrderId() {
		return siParentOrderId;
	}

	public void setSiParentOrderId(Integer siParentOrderId) {
		this.siParentOrderId = siParentOrderId;
	}

	public Integer getSiServiceDetailsId() {
		return siServiceDetailsId;
	}

	public void setSiServiceDetailsId(Integer siServiceDetailsId) {
		this.siServiceDetailsId = siServiceDetailsId;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getServiceSiteAddress() {
		return serviceSiteAddress;
	}

	public void setServiceSiteAddress(String serviceSiteAddress) {
		this.serviceSiteAddress = serviceSiteAddress;
	}

	public String getServiceSiteCountry() {
		return serviceSiteCountry;
	}

	public void setServiceSiteCountry(String serviceSiteCountry) {
		this.serviceSiteCountry = serviceSiteCountry;
	}

	public String getIsShared() {
		return isShared;
	}

	public void setIsShared(String isShared) {
		this.isShared = isShared;
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

	public Integer getIsPricingCheckRequired() {
		return isPricingCheckRequired;
	}

	public void setIsPricingCheckRequired(Integer isPricingCheckRequired) {
		this.isPricingCheckRequired = isPricingCheckRequired;
	}

	public Integer getSiteCategoryId() {
		return siteCategoryId;
	}

	public void setSiteCategoryId(Integer siteCategoryId) {
		this.siteCategoryId = siteCategoryId;
	}
	

}