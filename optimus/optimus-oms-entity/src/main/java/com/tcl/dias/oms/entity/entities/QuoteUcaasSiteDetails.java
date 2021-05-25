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
import java.io.Serializable;
import java.util.Date;

/**
 *
 * This file contains the QuoteUcaasSiteDetails.java class.
 *
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 *
 */
@Entity
@Table(name = "quote_ucaas_site_details")
@NamedQuery(name = "QuoteUcaasSiteDetails.findAll", query = "SELECT q FROM QuoteUcaasSiteDetails q")
public class QuoteUcaasSiteDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(name = "endpoint_location_id")
    private Integer endpointLocationId;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_solution_id")
    private ProductSolution productSolution;

    @Column(name = "site_code")
    private String siteCode;

    public QuoteUcaasSiteDetails() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public ProductSolution getProductSolution() {
        return productSolution;
    }

    public void setProductSolution(ProductSolution productSolution) {
        this.productSolution = productSolution;
    }

    public Integer getEndpointLocationId() {
        return endpointLocationId;
    }

    public void setEndpointLocationId(Integer endpointLocationId) {
        this.endpointLocationId = endpointLocationId;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }
}