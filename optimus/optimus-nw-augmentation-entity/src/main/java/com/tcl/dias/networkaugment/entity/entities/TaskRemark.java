package com.tcl.dias.networkaugment.entity.entities;

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
import java.io.Serializable;
import java.sql.Timestamp;


/**
 * This class is used to save remarks against a specific task
 *
 */
@Entity
@Table(name="task_remarks")
@NamedQuery(name="TaskRemark.findAll", query="SELECT t FROM TaskRemark t")
public class TaskRemark implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "username")
    private String username;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="task_id", referencedColumnName="id")
    private Task task;
    
    @Column(name = "service_id")
    private Integer serviceId;

    @Column(name = "jeopardy_category")
    private String jeopardyCategory ;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "current_jeopardy_owner")
    private String currentJeopardyOwner;

    @Column(name = "targeted_completion_date")
    private Timestamp targetedCompletionDate;


    public TaskRemark() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

    public String getJeopardyCategory() {
        return jeopardyCategory;
    }

    public void setJeopardyCategory(String jeopardyCategory) {
        this.jeopardyCategory = jeopardyCategory;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCurrentJeopardyOwner() {
        return currentJeopardyOwner;
    }

    public void setCurrentJeopardyOwner(String currentJeopardyOwner) {
        this.currentJeopardyOwner = currentJeopardyOwner;
    }

    public Timestamp getTargetedCompletionDate() {
        return targetedCompletionDate;
    }

    public void setTargetedCompletionDate(Timestamp targetedCompletionDate) {
        this.targetedCompletionDate = targetedCompletionDate;
    }
    
}