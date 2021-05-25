package com.tcl.dias.oms.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Entity class for Opportunity table
 *
 * AVALLAPI
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "opportunity")
@NamedQuery(name = "Opportunity.findAll", query = "SELECT o FROM Opportunity o")
public class Opportunity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "tps_opty_id")
    private String tpsOptyId;

    @Column(name = "tps_opty_system")
    private String tpsOptySystem;

    @Column(name = "opty_classification")
    private String optyClassification;

    @Column(name = "expected_mrc")
    private Double expectedMrc;

    @Column(name = "expected_nrc")
    private Double expectedNrc;

    @Column(name = "expected_currency")
    private String expectedCurrency;

    @Column(name = "currency_iso_code")
    private String currencyIsoCode;

    @Column(name = "campaign_name")
    private String campaignName;

    @Column(name = "opty_summary")
    private String optySummary;

    @Column(name = "created_by", updatable = false)
    private Integer createdBy;

    @Column(name = "created_date", updatable = false)
    private Timestamp createdDate;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_date")
    private Timestamp updatedDate;

    @Column(name = "status")
    private String status;

    @Column(name = "is_active")
    private String isActive;

    @Column(name = "customer_le_name")
    private String customerLeName;

    @Column(name = "is_deal_registration")
    private String isDealRegistration;

    @Column(name = "deal_registration_date")
    private String dealRegistrationDate;

    @Column(name = "campaign_id")
    private String campaignId;

    @Column(name = "temp_customer_le_id")
    private Integer tempCustomerLeId;

    @Column(name = "psam_email_id")
    private String psamEmailId;

    @Column(name = "is_orderlite")
    private String isOrderLite;

    @Column(name = "end_customer_cuid")
    private String endCustomerCuid;


    public String getIsOrderLite() {
        return isOrderLite;
    }

    public void setIsOrderLite(String isOrderLite) {
        this.isOrderLite = isOrderLite;
    }

    public String getPsamEmailId() { return psamEmailId; }

    public void setPsamEmailId(String psamEmailId) { this.psamEmailId = psamEmailId; }

    public Integer getTempCustomerLeId() {
        return tempCustomerLeId;
    }

    public void setTempCustomerLeId(Integer tempCustomerLeId) {
        this.tempCustomerLeId = tempCustomerLeId;
    }

    public String getCustomerLeName() {
        return customerLeName;
    }

    public void setCustomerLeName(String customerLeName) {
        this.customerLeName = customerLeName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTpsOptyId() {
        return tpsOptyId;
    }

    public void setTpsOptyId(String tpsOptyId) {
        this.tpsOptyId = tpsOptyId;
    }

    public String getTpsOptySystem() {
        return tpsOptySystem;
    }

    public void setTpsOptySystem(String tpsOptySystem) {
        this.tpsOptySystem = tpsOptySystem;
    }

    public String getOptyClassification() {
        return optyClassification;
    }

    public void setOptyClassification(String optyClassification) {
        this.optyClassification = optyClassification;
    }

    public Double getExpectedMrc() {
        return expectedMrc;
    }

    public void setExpectedMrc(Double expectedMrc) {
        this.expectedMrc = expectedMrc;
    }

    public Double getExpectedNrc() {
        return expectedNrc;
    }

    public void setExpectedNrc(Double expectedNrc) {
        this.expectedNrc = expectedNrc;
    }

    public String getCurrencyIsoCode() {
        return currencyIsoCode;
    }

    public void setCurrencyIsoCode(String currencyIsoCode) {
        this.currencyIsoCode = currencyIsoCode;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }
    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getOptySummary() {
        return optySummary;
    }

    public void setOptySummary(String optySummary) {
        this.optySummary = optySummary;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    @PrePersist
    public void setCreatedDate() {
        this.createdDate = Timestamp.valueOf(LocalDateTime.now());
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    @PreUpdate
    public void setUpdatedDate() {
        this.updatedDate = Timestamp.valueOf(LocalDateTime.now());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getExpectedCurrency() {
        return expectedCurrency;
    }

    public void setExpectedCurrency(String expectedCurrency) {
        this.expectedCurrency = expectedCurrency;
    }

    public String getIsDealRegistration() {
        return isDealRegistration;
    }

    public void setIsDealRegistration(String isDealRegistration) {
        this.isDealRegistration = isDealRegistration;
    }

    public String getDealRegistrationDate() {
        return dealRegistrationDate;
    }

    public void setDealRegistrationDate(String dealRegistrationDate) {
        this.dealRegistrationDate = dealRegistrationDate;
    }

    public String getEndCustomerCuid() {
        return endCustomerCuid;
    }

    public void setEndCustomerCuid(String endCustomerCuid) {
        this.endCustomerCuid = endCustomerCuid;
    }
}
