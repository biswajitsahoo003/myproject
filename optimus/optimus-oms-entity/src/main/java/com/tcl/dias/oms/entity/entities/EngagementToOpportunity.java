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
 * Entity class for Engagement to Opportunity table
 *
 * AVALLAPI
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "engagement_to_opportunity")
@NamedQuery(name = "EngagementToOpportunity.findAll", query = "SELECT o FROM EngagementToOpportunity o")
public class EngagementToOpportunity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(name = "is_active")
    private String isActive;

    @Column(name = "created_by", updatable = false)
    private Integer createdBy;

    @Column(name = "created_date", updatable = false)
    private Timestamp createdDate;

    @OneToOne
    @JoinColumn(name = "engagement_id")
    private Engagement engagement;

    @OneToOne
    @JoinColumn(name = "opty_id")
    private Opportunity opportunity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Engagement getEngagement() {
        return engagement;
    }

    public void setEngagement(Engagement engagement) {
        this.engagement = engagement;
    }

    public Opportunity getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(Opportunity opportunity) {
        this.opportunity = opportunity;
    }
}
