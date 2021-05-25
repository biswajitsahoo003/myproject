package com.tcl.dias.networkaugment.entity.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Table(name = "nwa_remarks_details")
public class NwaRemarksDetails implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_code")
    private String orderCode;

    @Column(name = "by_user")
    private String byUser;

    @Column(name = "by_user_group")
    private String byUserGroup;

    @Column(name = "jeopardy_reason")
    private String jeopardyReason;

    @Column(name = "reason")
    private String reason;

    @Column(name = "remarks_date")
    private Timestamp remarksDate;

    @Column(name = "scenario_type")
    private String scenarioType;

    @Column(name = "task_name")
    private String taskName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private	ScOrder scOrder;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getByUser() {
        return byUser;
    }

    public void setByUser(String byUser) {
        this.byUser = byUser;
    }

    public String getByUserGroup() {
        return byUserGroup;
    }

    public void setByUserGroup(String byUserGroup) {
        this.byUserGroup = byUserGroup;
    }

    public String getJeopardyReason() {
        return jeopardyReason;
    }

    public void setJeopardyReason(String jeopardyReason) {
        this.jeopardyReason = jeopardyReason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Timestamp getRemarksDate() {
        return remarksDate;
    }

    public void setRemarksDate(Timestamp remarksDate) {
        this.remarksDate = remarksDate;
    }

    public String getScenarioType() {
        return scenarioType;
    }

    public void setScenarioType(String scenarioType) {
        this.scenarioType = scenarioType;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public ScOrder getScOrder() {
        return scOrder;
    }

    public void setScOrder(ScOrder scOrder) {
        this.scOrder = scOrder;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }


}
