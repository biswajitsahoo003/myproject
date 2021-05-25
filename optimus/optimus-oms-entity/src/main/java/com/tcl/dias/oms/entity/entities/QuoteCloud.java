package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * Entity Class for quote_cloud table
 *
 *
 * @author Selvakumar Palaniandy
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "quote_cloud")
public class QuoteCloud implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", insertable = false, nullable = false)
  private Integer id;

  @Column(name = "service_id")
  private String serviceId;

  @Column(name = "cloud_code")
  private String cloudCode;

  @Column(name = "parent_cloud_code")
  private String parentCloudCode;

  @Column(name = "quote_to_le_id")
  private Integer quoteToLeId;

  @Column(name = "quote_id")
  private Integer quoteId;

  // bi-directional many-to-one association to ProductSolution
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_solutions_id")
  private ProductSolution productSolution;

  @Column(name = "dc_cloud_type")
  private String dcCloudType;
  
  @Column(name = "dc_location_id")
  private String dcLocationId;

  @Column(name = "resource_display_name")
  private String resourceDisplayName = "NULL";

  @Column(name = "arc")
  private Double arc;

  @Column(name = "mrc")
  private Double mrc;

  @Column(name = "nrc")
  private Double nrc;

  @Column(name = "tcv")
  private Double tcv;
  
  @Column(name = "ppu_rate")
  private Double ppuRate;

  @Column(name = "effective_date")
  @Temporal(TemporalType.TIMESTAMP)
  private Date effectiveDate;

  @Column(name = "fp_status")
  private String fpStatus;

  private Byte status;

  @Column(name = "is_task_triggered")
  private Integer isTaskTriggered;

  @Column(name = "created_time")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdTime;

  @Column(name = "created_by")
  private Integer createdBy;

  @Column(name = "updated_by")
  private Integer updatedBy;

  @Column(name = "updated_time")
  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedTime;
  
  //bi-directional many-to-one association to MstProductFamily
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_family_id")
  private MstProductFamily mstProductFamily;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getServiceId() { return serviceId; }

  public void setServiceId(String serviceId) { this.serviceId = serviceId; }

  public String getCloudCode() { return cloudCode; }

  public void setCloudCode(String cloudCode) { this.cloudCode = cloudCode; }

  public String getParentCloudCode() { return parentCloudCode; }

  public void setParentCloudCode(String parentCloudCode) { this.parentCloudCode = parentCloudCode; }

  public Integer getQuoteToLeId() {
    return quoteToLeId;
  }

  public void setQuoteToLeId(Integer quoteToLeId) {
    this.quoteToLeId = quoteToLeId;
  }

  public Integer getQuoteId() {
    return quoteId;
  }

  public void setQuoteId(Integer quoteId) {
    this.quoteId = quoteId;
  }

  public ProductSolution getProductSolution() { return productSolution; }

  public void setProductSolution(ProductSolution productSolution) { this.productSolution = productSolution; }

  public String getDcCloudType() {
	return dcCloudType;
  }

  public void setDcCloudType(String dcCloudType) {
	this.dcCloudType = dcCloudType;
  }

  public String getDcLocationId() {
    return dcLocationId;
  }

  public void setDcLocationId(String dcLocationId) {
    this.dcLocationId = dcLocationId;
  }

  public String getResourceDisplayName() {
    return resourceDisplayName;
  }

  public void setResourceDisplayName(String resourceDisplayName) {
    this.resourceDisplayName = resourceDisplayName;
  }

  public Double getArc() {
    return arc;
  }

  public void setArc(Double arc) {
    this.arc = arc;
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

  public Double getTcv() { return tcv; }

  public void setTcv(Double tcv) { this.tcv = tcv; }

  public Double getPpuRate() {
	return ppuRate;
  }

  public void setPpuRate(Double ppuRate) {
	this.ppuRate = ppuRate;
  }

  public Date getEffectiveDate() { return effectiveDate; }

  public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; }

  public String getFpStatus() { return fpStatus; }

  public void setFpStatus(String fpStatus) { this.fpStatus = fpStatus; }

  public Byte getStatus() { return status; }

  public void setStatus(Byte status) { this.status = status; }

  public Integer getIsTaskTriggered() { return isTaskTriggered; }

  public void setIsTaskTriggered(Integer isTaskTriggered) { this.isTaskTriggered = isTaskTriggered; }

  public Date getCreatedTime() { return createdTime; }

  public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }

  public Integer getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(Integer createdBy) {
    this.createdBy = createdBy;
  }

  public Integer getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(Integer updatedBy) {
    this.updatedBy = updatedBy;
  }

  public Date getUpdatedTime() { return updatedTime; }

  public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }

  public MstProductFamily getMstProductFamily() {
	return mstProductFamily;
  }
  
  public void setMstProductFamily(MstProductFamily mstProductFamily) {
	this.mstProductFamily = mstProductFamily;
  }

  public String toString() {
    return "QuoteCloud{id=" + id + 
      ", quoteToLeId=" + quoteToLeId + 
      ", quoteId=" + quoteId + 
      ", productSolutionId=" + productSolution.getId() +
      ", dcLocationId=" + dcLocationId + 
      ", resourceDisplayName=" + resourceDisplayName + 
      ", arc=" + arc + 
      ", mrc=" + mrc + 
      ", nrc=" + nrc + 
      ", ppu_rate=" + ppuRate + 
      ", status=" + status +
      ", isTaskTriggered=" + isTaskTriggered +
      ", createdTime=" + createdTime + 
      ", createdBy=" + createdBy + 
      ", updatedBy=" + updatedBy + 
      ", updatedTime=" + updatedTime + 
      "}";
  }
}