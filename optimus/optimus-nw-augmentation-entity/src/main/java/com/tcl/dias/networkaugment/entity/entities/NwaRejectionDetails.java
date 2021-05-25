package com.tcl.dias.networkaugment.entity.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "nwa_rejection_details")
public class NwaRejectionDetails implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name =  "rejection_type")
    String rejectionType;

    @Column(name =  "rejection_reason")
    String rejectionReason;

    @Column(name = "rejection_to_task")
    String rejectionToTask;

    @Column(name = "rejected_by_user")
    String rejectedByUser;

    @Column(name = "order_id")
    Integer orderId;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "rejection_task_id")
    private Task task;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private ScOrder scOrder;*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRejectionType() {
        return rejectionType;
    }

    public void setRejectionType(String rejectionType) {
        this.rejectionType = rejectionType;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getRejectionToTask() {
        return rejectionToTask;
    }

    public void setRejectionToTask(String rejectionToTask) {
        this.rejectionToTask = rejectionToTask;
    }

    public String getRejectedByUser() {
        return rejectedByUser;
    }

    public void setRejectedByUser(String rejectedByUser) {
        this.rejectedByUser = rejectedByUser;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
