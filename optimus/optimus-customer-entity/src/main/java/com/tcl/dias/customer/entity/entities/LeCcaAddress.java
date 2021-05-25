package com.tcl.dias.customer.entity.entities;

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
import java.sql.Timestamp;
import java.util.Date;


/**
 * Bean class
 *
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "le_cca")
@NamedQuery(name = "LeCcaAddress.findAll", query = "SELECT l FROM LeCcaAddress l")
public class LeCcaAddress implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // bi-directional many-to-one association to CustomerLegalEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "le_id")
    private CustomerLegalEntity customerLegalEntity;

    @Column(name = "mdm_cca_id")
    private String mmdCcaId;

    @Column(name = "mdm_address_id")
    private String mdmAddressId;

    @Column(name = "cc_address")
    private String ccAddress;

    @Column(name = "cc_state")
    private String ccState;

    @Column(name = "erf_loc_location_id")
    private Integer locationId;


    @Column(name = "is_default")
    private Integer isDefault;

    @Column(name = "is_active")
    private Integer isActive;

    @Column(name = "is_deleted")
    private Integer isDeleted;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    public LeCcaAddress() {
        // DO NOTHING
    }

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CustomerLegalEntity getCustomerLegalEntity() {
        return customerLegalEntity;
    }

    public void setCustomerLegalEntity(CustomerLegalEntity customerLegalEntity) {
        this.customerLegalEntity = customerLegalEntity;
    }

    public String getMmdCcaId() {
        return mmdCcaId;
    }

    public void setMmdCcaId(String mmdCcaId) {
        this.mmdCcaId = mmdCcaId;
    }

    public String getMdmAddressId() {
        return mdmAddressId;
    }

    public void setMdmAddressId(String mdmAddressId) {
        this.mdmAddressId = mdmAddressId;
    }

    public String getCcAddress() {
        return ccAddress;
    }

    public void setCcAddress(String ccAddress) {
        this.ccAddress = ccAddress;
    }

    public String getCcState() {
        return ccState;
    }

    public void setCcState(String ccState) {
        this.ccState = ccState;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
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

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return "LeCcaAddress{" +
                "id=" + id +
                ", customerLegalEntity=" + customerLegalEntity +
                ", mmdCcaId='" + mmdCcaId + '\'' +
                ", mdmAddressId='" + mdmAddressId + '\'' +
                ", ccAddress='" + ccAddress + '\'' +
                ", ccState='" + ccState + '\'' +
                ", locationId=" + locationId +
                ", isDefault=" + isDefault +
                ", isActive=" + isActive +
                ", isDeleted=" + isDeleted +
                ", createdBy=" + createdBy +
                ", createdTime=" + createdTime +
                ", updatedBy='" + updatedBy + '\'' +
                ", updatedTime=" + updatedTime +
                '}';
    }
}