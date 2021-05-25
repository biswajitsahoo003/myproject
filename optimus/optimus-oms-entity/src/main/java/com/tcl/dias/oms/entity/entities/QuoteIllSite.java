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
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "quote_ill_sites")
@NamedQuery(name = "QuoteIllSite.findAll", query = "SELECT q FROM QuoteIllSite q")
public class QuoteIllSite implements Serializable {
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

	@Column(name = "tcv")
	private Double tcv;

	@Column(name="feasibility")
	private Byte feasibility;

	@Column(name = "is_izo")
	private Byte isIzoPc;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "is_tax_exempted")
	private Byte isTaxExempted;
	
	private Double mrc;

	private Double nrc;

	private Double arc;

	@Column(name = "requestor_date")
	private Date requestorDate;

	@Column(name = "site_code")
	private String siteCode;

	private Byte status;

	// bi-directional many-to-one association to QuoteIllSiteSla
	@OneToMany(mappedBy = "quoteIllSite")
	private Set<QuoteIllSiteSla> quoteIllSiteSlas;

	// bi-directional many-to-one association to ProductSolution
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_solutions_id")
	private ProductSolution productSolution;
	
	@Column(name = "erf_service_inventory_tps_service_id")
	private String erfServiceInventoryTpsServiceId;
	
	@Column(name = "macd_change_bandwidth_flag")
	private String macdChangeBandwidthFlag;
	
	@Column(name = "is_task_triggered")
	private Integer isTaskTriggered;

	@Column(name = "mf_status")
	private String mfStatus;

	@Column(name = "mf_task_triggered")
	private Integer mfTaskTriggered;
	
	@Column(name="npl_shift_site_flag")
	private Integer nplShiftSiteFlag;
	
	@Column(name = "commercial_rejection_status")
	private String commercialRejectionStatus;
	
	@Column(name = "commercial_approve_status")
	private String commercialApproveStatus;
	
	@Column(name = "mf_task_type")
	private String mfTaskType;

	
	
	@Column(name = "is_colo")
	private Byte isColo;
	
	//added for multi vrf
	// bi-directional many-to-one association to QuoteVrfSites
	@OneToMany(mappedBy = "quoteIllSite")
	private Set<QuoteVrfSites> quoteVrfSites;
	
	
	//added for multisite commercial bulk upload
	// bi-directional many-to-one association to CommercialBulkProcessSites
	@OneToMany(mappedBy = "quoteIllSite")
	private Set<CommercialBulkProcessSites> commercialBulkProcessSites;
	
	
	@Column(name = "site_bulk_update")
	private String siteBulkUpdate;	
	
	
	//added for sitewisebilling
	// bi-directional many-to-one association to QuoteSiteBillingDetails
	@OneToMany(mappedBy = "quoteIllSite")
	private Set<QuoteSiteBillingDetails> quoteSiteBillingDetails;
	
	
	
	
	
	public Set<QuoteSiteBillingDetails> getQuoteSiteBillingDetails() {
		return quoteSiteBillingDetails;
	}

	public void setQuoteSiteBillingDetails(Set<QuoteSiteBillingDetails> quoteSiteBillingDetails) {
		this.quoteSiteBillingDetails = quoteSiteBillingDetails;
	}

	public String getSiteBulkUpdate() {
		return siteBulkUpdate;
	}

	public void setSiteBulkUpdate(String siteBulkUpdate) {
		this.siteBulkUpdate = siteBulkUpdate;
	}

	public Set<CommercialBulkProcessSites> getCommercialBulkProcessSites() {
		return commercialBulkProcessSites;
	}

	public void setCommercialBulkProcessSites(Set<CommercialBulkProcessSites> commercialBulkProcessSites) {
		this.commercialBulkProcessSites = commercialBulkProcessSites;
	}

	public Set<QuoteVrfSites> getQuoteVrfSites() {
		return quoteVrfSites;
	}

	public void setQuoteVrfSites(Set<QuoteVrfSites> quoteVrfSites) {
		this.quoteVrfSites = quoteVrfSites;
	}

	public String getCommercialApproveStatus() {
		return commercialApproveStatus;
	}

	public void setCommercialApproveStatus(String commercialApproveStatus) {
		this.commercialApproveStatus = commercialApproveStatus;
	}

	public String getCommercialRejectionStatus() {
		return commercialRejectionStatus;
	}

	public void setCommercialRejectionStatus(String commercialRejectionStatus) {
		this.commercialRejectionStatus = commercialRejectionStatus;
	}

	public Integer getIsTaskTriggered() {
		return isTaskTriggered;
	}

	public void setIsTaskTriggered(Integer isTaskTriggered) {
		this.isTaskTriggered = isTaskTriggered;
	}

	public QuoteIllSite() {
		// DO NOTHING
	}
	
	public QuoteIllSite(Integer id) {
		this.id=id;
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

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Set<QuoteIllSiteSla> getQuoteIllSiteSlas() {
		if (quoteIllSiteSlas == null) {
			quoteIllSiteSlas = new HashSet<>();
		}
		return this.quoteIllSiteSlas;
	}

	public void setQuoteIllSiteSlas(Set<QuoteIllSiteSla> quoteIllSiteSlas) {
		this.quoteIllSiteSlas = quoteIllSiteSlas;
	}

	public QuoteIllSiteSla addQuoteIllSiteSla(QuoteIllSiteSla quoteIllSiteSla) {
		getQuoteIllSiteSlas().add(quoteIllSiteSla);
		quoteIllSiteSla.setQuoteIllSite(this);

		return quoteIllSiteSla;
	}

	public QuoteIllSiteSla removeQuoteIllSiteSla(QuoteIllSiteSla quoteIllSiteSla) {
		getQuoteIllSiteSlas().remove(quoteIllSiteSla);
		quoteIllSiteSla.setQuoteIllSite(null);

		return quoteIllSiteSla;
	}

	public ProductSolution getProductSolution() {
		return this.productSolution;
	}

	public void setProductSolution(ProductSolution productSolution) {
		this.productSolution = productSolution;
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

	public Integer getNplShiftSiteFlag() {
		return nplShiftSiteFlag;
	}

	public void setNplShiftSiteFlag(Integer nplShiftSiteFlag) {
		this.nplShiftSiteFlag = nplShiftSiteFlag;
	}

	public String getMfTaskType() {
		return mfTaskType;
	}

	public void setMfTaskType(String mfTaskType) {
		this.mfTaskType = mfTaskType;
	}

	public Byte getIsColo() {
		return isColo;
	}

	public void setIsColo(Byte isColo) {
		this.isColo = isColo;
	}
	
}