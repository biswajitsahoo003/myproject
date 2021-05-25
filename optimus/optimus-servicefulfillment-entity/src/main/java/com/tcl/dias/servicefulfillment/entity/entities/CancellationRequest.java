package com.tcl.dias.servicefulfillment.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "cancellation_request")
@NamedQuery(name = "CancellationRequest.findAll", query = "SELECT s FROM CancellationRequest s")
public class CancellationRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "service_id")
    private Integer serviceId;

    @Column(name = "order_code")
    private String orderCode;

    @Column(name = "cancellation_order_code")
    private String cancellationOrderCode;

    @Column(name = "cancellation_service_code")
    private String cancellationServiceCode;

    @Column(name = "sfdc_opportunity_id")
    private String sfdcOpportunityId;

    @Column(name = "cancellation_charges")
    private Double cancellationCharges;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @Column(name = "lead_to_rfs_days")
    private String leadToRFSDays;

    @Column(name = "effective_change_date")
    private Timestamp effectiveDateOfChange;

    @Column(name = "charges_absorbed_or_passed")
    private String  chargesAbsorbedOrpassed;

    @Column(name = "cancellation_created_by")
    private String cancellationCreatedBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getCancellationOrderCode() {
        return cancellationOrderCode;
    }

    public void setCancellationOrderCode(String cancellationOrderCode) {
        this.cancellationOrderCode = cancellationOrderCode;
    }

    public String getCancellationServiceCode() {
        return cancellationServiceCode;
    }

    public void setCancellationServiceCode(String cancellationServiceCode) {
        this.cancellationServiceCode = cancellationServiceCode;
    }

    public String getSfdcOpportunityId() {
        return sfdcOpportunityId;
    }

    public void setSfdcOpportunityId(String sfdcOpportunityId) {
        this.sfdcOpportunityId = sfdcOpportunityId;
    }
    public Double getCancellationCharges() {
        return cancellationCharges;
    }

    public void setCancellationCharges(Double cancellationCharges) {
        this.cancellationCharges = cancellationCharges;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getLeadToRFSDays() {
        return leadToRFSDays;
    }

    public void setLeadToRFSDays(String leadToRFSDays) {
        this.leadToRFSDays = leadToRFSDays;
    }

    public Timestamp getEffectiveDateOfChange() {
        return effectiveDateOfChange;
    }

    public void setEffectiveDateOfChange(Timestamp effectiveDateOfChange) {
        this.effectiveDateOfChange = effectiveDateOfChange;
    }
    public String getChargesAbsorbedOrpassed() {
        return chargesAbsorbedOrpassed;
    }

    public void setChargesAbsorbedOrpassed(String chargesAbsorbedOrpassed) {
        this.chargesAbsorbedOrpassed = chargesAbsorbedOrpassed;
    }

    public String getCancellationCreatedBy() {
        return cancellationCreatedBy;
    }

    public void setCancellationCreatedBy(String cancellationCreatedBy) {
        this.cancellationCreatedBy = cancellationCreatedBy;
    }
}
