package com.tcl.dias.serviceinventory.entity.entities;

/*
    File Name : SiSolutionComponent.java
    
    @author Mayank Sharma
    First Created on 23-11-2020 at 20:10
*/

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "si_solution_components")
@NamedQuery(name="SiSolutionComponent.findAll", query="SELECT s FROM SiSolutionComponent s")
public class SiSolutionComponent implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="component_group")
    private String componentGroup;

    @Column(name="cpe_component_id")
    private Integer cpeComponentId;

    @Column(name="is_active")
    private String isActive;

    private String priority;

    @Column(name="order_code")
    private String orderCode;

    @Column(name="parent_service_code")
    private String parentServiceCode;

    @Column(name="solution_code")
    private String solutionCode;

    @Column(name="o2c_triggered_status")
    private String o2cTriggeredStatus;

    //bi-directional many-to-one association to ScOrder
    @ManyToOne
    @JoinColumn(name="si_order_id")
    private SIOrder siOrder;

    @Column(name="service_code")
    private String serviceCode;

    //bi-directional many-to-one association to ScServiceDetail
    @ManyToOne
    @JoinColumn(name="si_service_detail_id")
    private SIServiceDetail siServiceDetail1;

    //bi-directional many-to-one association to ScServiceDetail
    @ManyToOne
    @JoinColumn(name="parent_si_service_detail_id")
    private SIServiceDetail siServiceDetail2;

    //bi-directional many-to-one association to ScServiceDetail
    @ManyToOne
    @JoinColumn(name="solution_service_detail_id")
    private SIServiceDetail siServiceDetail3;

    public SiSolutionComponent() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComponentGroup() {
        return this.componentGroup;
    }

    public void setComponentGroup(String componentGroup) {
        this.componentGroup = componentGroup;
    }

    public Integer getCpeComponentId() {
        return this.cpeComponentId;
    }

    public void setCpeComponentId(Integer cpeComponentId) {
        this.cpeComponentId = cpeComponentId;
    }

    public String getIsActive() {
        return this.isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getOrderCode() {
        return this.orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getParentServiceCode() {
        return this.parentServiceCode;
    }

    public void setParentServiceCode(String parentServiceCode) {
        this.parentServiceCode = parentServiceCode;
    }

    public String getSolutionCode() {
        return this.solutionCode;
    }

    public void setSolutionCode(String solutionCode) {
        this.solutionCode = solutionCode;
    }

    public SIOrder getSiOrder() {
        return siOrder;
    }

    public void setSiOrder(SIOrder siOrder) {
        this.siOrder = siOrder;
    }

    public SIServiceDetail getSiServiceDetail1() {
        return siServiceDetail1;
    }

    public void setSiServiceDetail1(SIServiceDetail siServiceDetail1) {
        this.siServiceDetail1 = siServiceDetail1;
    }

    public SIServiceDetail getSiServiceDetail2() {
        return siServiceDetail2;
    }

    public void setSiServiceDetail2(SIServiceDetail siServiceDetail2) {
        this.siServiceDetail2 = siServiceDetail2;
    }

    public SIServiceDetail getSiServiceDetail3() {
        return siServiceDetail3;
    }

    public void setSiServiceDetail3(SIServiceDetail siServiceDetail3) {
        this.siServiceDetail3 = siServiceDetail3;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getO2cTriggeredStatus() {
        return o2cTriggeredStatus;
    }

    public void setO2cTriggeredStatus(String o2cTriggeredStatus) {
        this.o2cTriggeredStatus = o2cTriggeredStatus;
    }

}