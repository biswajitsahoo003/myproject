package com.tcl.dias.networkaugment.entity.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "nwa_order_details")
public class NwaOrderDetails implements Serializable {
    /**
     * This file contains the Entity for table: ......
     * Name correction
     *
     * @author Anuj Maurya
     * @link http://www.tatacommunications.com/
     * @copyright 2018 Tata Communications Limited
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id",unique = true)
    private Integer orderId;

    @Column(name = "order_code")
    private String orderCode;

    @Column(name = "order_type")
    private String orderType;

    @Column(name = "bts_order_id")
    private String btsOrderId;

    @Column(name = "is_iprm_required")
    private String isIprmRequired;

    @Column(name = "is_pe_required")
    private String isPeRequired;

    @Column(name = "m_and_l_required")
    private String mAndLRequired;

    @Column(name = "order_creation_date")
    private Timestamp orderCreationDate;

    @Column(name = "order_status")
    private String orderStatus;

    @Column(name = "originator_contact_number")
    private String originatOrContactNumber;

    @Column(name = "originator_group_id")
    private String originatorGroupId;

    @Column(name = "originator_name")
    private String originatorName;

    @Column(name = "pm_contact_email")
    private String pmContactEmail;

    @Column(name = "pm_contact_phone_no")
    private String pmContactPhoneNo;

    @Column(name = "pm_name")
    private String pmName;

    @Column(name = "priority")
    private String priority;

    @Column(name = "project_type")
    private String projectType;

    @Column(name = "scenario_type")
    private String scenarioType;

    @Column(name = "subject")
    private String subject;

    @Column(name = "service_id")
    private String serviceId;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId= orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getBtsOrderId() {
        return btsOrderId;
    }

    public void setBtsOrderId(String btsOrderId) {
        this.btsOrderId = btsOrderId;
    }

    public String getIsIprmRequired() {
        return isIprmRequired;
    }

    public void setIsIprmRequired(String isIprmRequired) {
        this.isIprmRequired = isIprmRequired;
    }

    public String getIsPeRequired() {
        return isPeRequired;
    }

    public void setIsPeRequired(String isPeRequired) {
        this.isPeRequired = isPeRequired;
    }

    public String getmAndLRequired() {
        return mAndLRequired;
    }

    public void setmAndLRequired(String mAndLRequired) {
        this.mAndLRequired = mAndLRequired;
    }

    public Timestamp getOrderCreationDate() {
        return orderCreationDate;
    }

    public void setOrderCreationDate(Timestamp orderCreationDate) {
        this.orderCreationDate = orderCreationDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOriginatOrContactNumber() {
        return originatOrContactNumber;
    }

    public void setOriginatOrContactNumber(String originatOrContactNumber) {
        this.originatOrContactNumber = originatOrContactNumber;
    }

    public String getOriginatorGroupId() {
        return originatorGroupId;
    }

    public void setOriginatorGroupId(String originatorGroupId) {
        this.originatorGroupId = originatorGroupId;
    }

    public String getOriginatorName() {
        return originatorName;
    }

    public void setOriginatorName(String originatorName) {
        this.originatorName = originatorName;
    }

    public String getPmContactEmail() {
        return pmContactEmail;
    }

    public void setPmContactEmail(String pmContactEmail) {
        this.pmContactEmail = pmContactEmail;
    }

    public String getPmContactPhoneNo() {
        return pmContactPhoneNo;
    }

    public void setPmContactPhoneNo(String pmContactPhoneNo) {
        this.pmContactPhoneNo = pmContactPhoneNo;
    }

    public String getPmName() {
        return pmName;
    }

    public void setPmName(String pmName) {
        this.pmName = pmName;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getScenarioType() {
        return scenarioType;
    }

    public void setScenarioType(String scenarioType) {
        this.scenarioType = scenarioType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "NwaOrderDdetails [orderId=" + orderId + ", orderCode=" + orderCode + ", orderType=" + orderType + ", btsOrderId="
                + btsOrderId + ", isIprmRequired=" + isIprmRequired + ", isPeRequired=" + isPeRequired
                + ", mAndLRequired=" + mAndLRequired + ", orderCreationDate=" + orderCreationDate + ", orderStatus="
                + orderStatus + ", originatOrContactNumber=" + originatOrContactNumber + ", originatorGroupId="
                + originatorGroupId + ", originatorName=" + originatorName + ", pmContactEmail=" + pmContactEmail
                + ", pmContactPhoneNo=" + pmContactPhoneNo + ", pmName=" + pmName + ", priority=" + priority
                + ", projectType=" + projectType + ", scenarioType=" + scenarioType + ", subject=" + subject
                + ", serviceId=" + serviceId + ", getOrderId()=" + getOrderId() + ", getOrderCode()=" + getOrderCode()
                + ", getOrderType()=" + getOrderType() + ", getBtsOrderId()=" + getBtsOrderId()
                + ", getIsIprmRequired()=" + getIsIprmRequired() + ", getIsPeRequired()=" + getIsPeRequired()
                + ", getmAndLRequired()=" + getmAndLRequired() + ", getOrderCreationDate()=" + getOrderCreationDate()
                + ", getOrderStatus()=" + getOrderStatus() + ", getOriginatOrContactNumber()="
                + getOriginatOrContactNumber() + ", getOriginatorGroupId()=" + getOriginatorGroupId()
                + ", getOriginatorName()=" + getOriginatorName() + ", getPmContactEmail()=" + getPmContactEmail()
                + ", getPmContactPhoneNo()=" + getPmContactPhoneNo() + ", getPmName()=" + getPmName()
                + ", getPriority()=" + getPriority() + ", getProjectType()=" + getProjectType() + ", getScenarioType()="
                + getScenarioType() + ", getSubject()=" + getSubject() + ", getServiceId()=" + getServiceId()
                + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
                + "]";
    }

}
