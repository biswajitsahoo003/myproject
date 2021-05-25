package com.tcl.dias.oms.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Entity Class for Purchase Order
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "purchase_order")
@NamedQuery(name = "PurchaseOrder.findAll", query = "SELECT o FROM PurchaseOrder o")
public class PurchaseOrder {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "po_number")
    private String purchaseOrderNumber;

    @Column(name = "po_date")
    private Timestamp purchaseOrderDate;

    @Column(name = "po_commit_date")
    private String purchaseOrderCommitDate;

    @Column(name = "po_status")
    private String purchaseOrderStatus;

    @Column(name = "po_expiry_date")
    private Timestamp purchaseOrderExpiryDate;

    @Column(name = "po_initiated_by")
    private String purchaseOrderIntialitedBy;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "is_active")
    private Byte isActive;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public Timestamp getPurchaseOrderDate() {
        return purchaseOrderDate;
    }

    public void setPurchaseOrderDate(Timestamp purchaseOrderDate) {
        this.purchaseOrderDate = purchaseOrderDate;
    }

    public String getPurchaseOrderCommitDate() {
        return purchaseOrderCommitDate;
    }

    public void setPurchaseOrderCommitDate(String purchaseOrderCommitDate) {
        this.purchaseOrderCommitDate = purchaseOrderCommitDate;
    }

    public String getPurchaseOrderStatus() {
        return purchaseOrderStatus;
    }

    public void setPurchaseOrderStatus(String purchaseOrderStatus) {
        this.purchaseOrderStatus = purchaseOrderStatus;
    }

    public Timestamp getPurchaseOrderExpiryDate() {
        return purchaseOrderExpiryDate;
    }

    public void setPurchaseOrderExpiryDate(Timestamp purchaseOrderExpiryDate) {
        this.purchaseOrderExpiryDate = purchaseOrderExpiryDate;
    }

    public String getPurchaseOrderIntialitedBy() {
        return purchaseOrderIntialitedBy;
    }

    public void setPurchaseOrderIntialitedBy(String purchaseOrderIntialitedBy) {
        this.purchaseOrderIntialitedBy = purchaseOrderIntialitedBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Byte getIsActive() {
        return isActive;
    }

    public void setIsActive(Byte isActive) {
        this.isActive = isActive;
    }
}
