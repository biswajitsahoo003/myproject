package com.tcl.dias.oms.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Entity class for Engagement Opportunity to Product table
 *
 * AVALLAPI
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "engagement_opportunity_to_product")
@NamedQuery(name = "EngagementOpportunityToProduct.findAll", query = "SELECT o FROM EngagementOpportunityToProduct o")
public class EngagementOpportunityToProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(name = "mst_product_id")
    private Integer mstProductId;

    @Column(name = "is_active")
    private String isActive;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @OneToOne
    @JoinColumn(name = "engagement_to_opty_id")
    private EngagementToOpportunity engagementToOpportunity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMstProductId() {
        return mstProductId;
    }

    public void setMstProductId(Integer mstProductId) {
        this.mstProductId = mstProductId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
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

    public EngagementToOpportunity getEngagementToOpportunity() {
        return engagementToOpportunity;
    }

    public void setEngagementToOpportunity(EngagementToOpportunity engagementToOpportunity) {
        this.engagementToOpportunity = engagementToOpportunity;
    }
}
