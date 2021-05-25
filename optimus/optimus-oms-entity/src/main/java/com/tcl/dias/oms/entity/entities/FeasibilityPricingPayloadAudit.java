package com.tcl.dias.oms.entity.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * feasibility_pricing_payload_audit table for utility portal - save the request and response information
 * @author manisha manojkumar
 *
 */
@Entity
@Table(name = "feasibility_pricing_payload_audit")
@NamedQuery(name = "FeasibilityPricingPayloadAudit.findAll", query = "SELECT c FROM FeasibilityPricingPayloadAudit c")
public class FeasibilityPricingPayloadAudit implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "mdc_token")
    private String mdcToken;

    @Column(name = "quote_code")
    private String quoteCode;

    @Column(name = "audit_type")
    private String auditType;

    @Column(name = "request_payload")
    private String requestPayload;

    @Column(name = "response_payload")
    private String responsePayload;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_time")
    private Date createdTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMdcToken() {
        return mdcToken;
    }

    public void setMdcToken(String mdcToken) {
        this.mdcToken = mdcToken;
    }

    public String getQuoteCode() {
        return quoteCode;
    }

    public void setQuoteCode(String quoteCode) {
        this.quoteCode = quoteCode;
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public String getRequestPayload() {
        return requestPayload;
    }

    public void setRequestPayload(String requestPayload) {
        this.requestPayload = requestPayload;
    }

    public String getResponsePayload() {
        return responsePayload;
    }

    public void setResponsePayload(String responsePayload) {
        this.responsePayload = responsePayload;
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

    @Override
    public String toString() {
        return "UtilityAudit [id=" + id + ", mdcToken=" + mdcToken + ", quoteCode=" + quoteCode + ", requestPayload="
                + requestPayload + ", responsePayload=" + responsePayload + ", auditType=" + auditType + ", createdBy=" + createdBy
                + ", createdTime=" + createdTime + "]";
    }
}
