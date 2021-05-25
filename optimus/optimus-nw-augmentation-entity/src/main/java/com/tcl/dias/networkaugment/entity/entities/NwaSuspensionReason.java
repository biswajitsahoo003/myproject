package com.tcl.dias.networkaugment.entity.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "nwa_suspension_reason")
public class NwaSuspensionReason {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_code")
    private String orderCode;

    @Column(name = "suspend_till_date")
    private Timestamp suspendTillDate;

    @Column(name = "suspension_reason")
    private String suspensionReason;

    @Column(name = "assign_transfer")
    private String assignTransfer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private	ScOrder scOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private	Task task;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Timestamp getSuspendTillDate() {
        return suspendTillDate;
    }

    public void setSuspendTillDate(Timestamp suspendTillDate) {
        this.suspendTillDate = suspendTillDate;
    }

    public String getSuspensionReason() {
        return suspensionReason;
    }

    public void setSuspensionReason(String suspensionReason) {
        this.suspensionReason = suspensionReason;
    }

    public String getAssignTransfer() {
        return assignTransfer;
    }

    public void setAssignTransfer(String assignTransfer) {
        this.assignTransfer = assignTransfer;
    }

    public ScOrder getScOrder() {
        return scOrder;
    }

    public void setScOrder(ScOrder scOrder) {
        this.scOrder = scOrder;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
