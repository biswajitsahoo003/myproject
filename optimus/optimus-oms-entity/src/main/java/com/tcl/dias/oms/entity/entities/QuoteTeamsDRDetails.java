package com.tcl.dias.oms.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

/**
 * This file contains the QuoteTeamsDRDetails.java
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name = "quote_teamsdr_details")
@NamedQuery(name = "QuoteTeamsDRDetails.findAll", query = "SELECT q FROM QuoteTeamsDRDetails q")
public class QuoteTeamsDRDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    private String country;

    @Column(name = "no_of_named_users")
    private Integer noOfNamedusers;

    @Column(name = "total_users")
    private Integer totalUsers;

    @Column(name = "mrc")
    private BigDecimal mrc;

    @Column(name = "nrc")
    private BigDecimal nrc;

    @Column(name = "arc")
    private BigDecimal arc;

    @Column(name = "tcv")
    private BigDecimal tcv;

    @Column(name = "no_of_common_area_devices")
    private Integer noOfCommonAreaDevices;

    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    // bi-directional many-to-one association to QuoteTeamsDR
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_teamsdr_id")
    private QuoteTeamsDR quoteTeamsDR;

    @Column(name = "created_by")
    private String createdBy;

    public QuoteTeamsDRDetails() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getNoOfNamedusers() {
        return noOfNamedusers;
    }

    public void setNoOfNamedusers(Integer noOfNamedusers) {
        this.noOfNamedusers = noOfNamedusers;
    }

    public Integer getNoOfCommonAreaDevices() {
        return noOfCommonAreaDevices;
    }

    public void setNoOfCommonAreaDevices(Integer noOfCommonAreaDevices) {
        this.noOfCommonAreaDevices = noOfCommonAreaDevices;
    }

    public BigDecimal getMrc() {
        return mrc;
    }

    public void setMrc(BigDecimal mrc) {
        this.mrc = mrc;
    }

    public BigDecimal getNrc() {
        return nrc;
    }

    public void setNrc(BigDecimal nrc) {
        this.nrc = nrc;
    }

    public BigDecimal getArc() {
        return arc;
    }

    public void setArc(BigDecimal arc) {
        this.arc = arc;
    }

    public BigDecimal getTcv() {
        return tcv;
    }

    public void setTcv(BigDecimal tcv) {
        this.tcv = tcv;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public QuoteTeamsDR getQuoteTeamsDR() {
        return quoteTeamsDR;
    }

    public void setQuoteTeamsDR(QuoteTeamsDR quoteTeamsDR) {
        this.quoteTeamsDR = quoteTeamsDR;
    }

    public Integer getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Integer totalUsers) {
        this.totalUsers = totalUsers;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
