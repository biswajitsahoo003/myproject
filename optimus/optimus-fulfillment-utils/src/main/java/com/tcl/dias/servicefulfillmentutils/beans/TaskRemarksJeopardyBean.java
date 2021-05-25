package com.tcl.dias.servicefulfillmentutils.beans;

import com.tcl.dias.servicefulfillment.entity.entities.TaskRemark;

import java.sql.Timestamp;

/**
 * POJO for Task Remarks
 *
 * @author Mayank Sharma
 *
 */
public class TaskRemarksJeopardyBean {

    private String userComments;
    private String username;
    private Timestamp createdDate;
    private String isJeopardy;
    private Integer taskId;
    private Integer serviceId;
    private String jeopardyCategory ;
    private String groupName;
    private String currentJeopardyOwner;
    private Timestamp targetedCompletionDate;
    
    


    public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getUserComments() {
        return userComments;
    }

    public void setUserComments(String userComments) {
        this.userComments = userComments;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getIsJeopardy() {
        return isJeopardy;
    }

    public void setIsJeopardy(String isJeopardy) {
        this.isJeopardy = isJeopardy;
    }

	public static TaskRemarksJeopardyBean mapToBean(TaskRemark taskRemark) {
		TaskRemarksJeopardyBean taskRemarksJeopardyBean = new TaskRemarksJeopardyBean();
		taskRemarksJeopardyBean.setUserComments(taskRemark.getRemarks());
		if (taskRemark.getTask() != null) {
			taskRemarksJeopardyBean.setTaskId(taskRemark.getTask().getId());
		}
		taskRemarksJeopardyBean.setUsername(taskRemark.getUsername());
		taskRemarksJeopardyBean.setCreatedDate(taskRemark.getCreatedDate());
		if (taskRemark.getTask() != null) {
			taskRemarksJeopardyBean.setIsJeopardy((taskRemark.getTask().getIsJeopardyTask() == (byte) 1) ? "Y" : "N");
		}
        taskRemarksJeopardyBean.setJeopardyCategory(taskRemark.getJeopardyCategory());
		taskRemarksJeopardyBean.setGroupName(taskRemark.getGroupName());
        taskRemarksJeopardyBean.setCurrentJeopardyOwner(taskRemark.getCurrentJeopardyOwner());
        taskRemarksJeopardyBean.setTargetedCompletionDate(taskRemark.getTargetedCompletionDate());
		return taskRemarksJeopardyBean;
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
