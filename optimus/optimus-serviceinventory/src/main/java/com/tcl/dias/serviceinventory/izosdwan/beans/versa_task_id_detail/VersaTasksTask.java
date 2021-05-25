
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_task_id_detail;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "versa-tasks.id",
    "versa-tasks.task-name",
    "versa-tasks.object-name",
    "versa-tasks.domain-name",
    "versa-tasks.user",
    "versa-tasks.task-description",
    "versa-tasks.task-status",
    "versa-tasks.percentage-completion",
    "versa-tasks.progressmessages",
    "versa-tasks.errormessages",
    "versa-tasks.start-time",
    "versa-tasks.end-time"
})
public class VersaTasksTask {

    @JsonProperty("versa-tasks.id")
    private Integer versaTasksId;
    @JsonProperty("versa-tasks.task-name")
    private String versaTasksTaskName;
    @JsonProperty("versa-tasks.object-name")
    private String versaTasksObjectName;
    @JsonProperty("versa-tasks.domain-name")
    private List<String> versaTasksDomainName;
    @JsonProperty("versa-tasks.user")
    private String versaTasksUser;
    @JsonProperty("versa-tasks.task-description")
    private String versaTasksTaskDescription;
    @JsonProperty("versa-tasks.task-status")
    private String versaTasksTaskStatus;
    @JsonProperty("versa-tasks.percentage-completion")
    private Integer versaTasksPercentageCompletion;
    @JsonProperty("versa-tasks.progressmessages")
    private VersaTasksProgressmessages versaTasksProgressmessages;
    @JsonProperty("versa-tasks.errormessages")
    private VersaTasksErrormessage versaTasksErrormessages;
    @JsonProperty("versa-tasks.start-time")
    private String versaTasksStartTime;
    @JsonProperty("versa-tasks.end-time")
    private String versaTasksEndTime;

    @JsonProperty("versa-tasks.id")
    public Integer getVersaTasksId() {
        return versaTasksId;
    }

    @JsonProperty("versa-tasks.id")
    public void setVersaTasksId(Integer versaTasksId) {
        this.versaTasksId = versaTasksId;
    }

    @JsonProperty("versa-tasks.task-name")
    public String getVersaTasksTaskName() {
        return versaTasksTaskName;
    }

    @JsonProperty("versa-tasks.task-name")
    public void setVersaTasksTaskName(String versaTasksTaskName) {
        this.versaTasksTaskName = versaTasksTaskName;
    }

    @JsonProperty("versa-tasks.object-name")
    public String getVersaTasksObjectName() {
        return versaTasksObjectName;
    }

    @JsonProperty("versa-tasks.object-name")
    public void setVersaTasksObjectName(String versaTasksObjectName) {
        this.versaTasksObjectName = versaTasksObjectName;
    }

//    @JsonProperty("versa-tasks.domain-name")
//    public String getVersaTasksDomainName() {
//        return versaTasksDomainName;
//    }
//
//    @JsonProperty("versa-tasks.domain-name")
//    public void setVersaTasksDomainName(String versaTasksDomainName) {
//        this.versaTasksDomainName = versaTasksDomainName;
//    }
    
    

    @JsonProperty("versa-tasks.user")
    public String getVersaTasksUser() {
        return versaTasksUser;
    }

    public List<String> getVersaTasksDomainName() {
		return versaTasksDomainName;
	}

	public void setVersaTasksDomainName(List<String> versaTasksDomainName) {
		this.versaTasksDomainName = versaTasksDomainName;
	}

    @JsonProperty("versa-tasks.user")
    public void setVersaTasksUser(String versaTasksUser) {
        this.versaTasksUser = versaTasksUser;
    }

    @JsonProperty("versa-tasks.task-description")
    public String getVersaTasksTaskDescription() {
        return versaTasksTaskDescription;
    }

    @JsonProperty("versa-tasks.task-description")
    public void setVersaTasksTaskDescription(String versaTasksTaskDescription) {
        this.versaTasksTaskDescription = versaTasksTaskDescription;
    }

    @JsonProperty("versa-tasks.task-status")
    public String getVersaTasksTaskStatus() {
        return versaTasksTaskStatus;
    }

    @JsonProperty("versa-tasks.task-status")
    public void setVersaTasksTaskStatus(String versaTasksTaskStatus) {
        this.versaTasksTaskStatus = versaTasksTaskStatus;
    }

    @JsonProperty("versa-tasks.percentage-completion")
    public Integer getVersaTasksPercentageCompletion() {
        return versaTasksPercentageCompletion;
    }

    @JsonProperty("versa-tasks.percentage-completion")
    public void setVersaTasksPercentageCompletion(Integer versaTasksPercentageCompletion) {
        this.versaTasksPercentageCompletion = versaTasksPercentageCompletion;
    }

    @JsonProperty("versa-tasks.progressmessages")
    public VersaTasksProgressmessages getVersaTasksProgressmessages() {
        return versaTasksProgressmessages;
    }

    @JsonProperty("versa-tasks.progressmessages")
    public void setVersaTasksProgressmessages(VersaTasksProgressmessages versaTasksProgressmessages) {
        this.versaTasksProgressmessages = versaTasksProgressmessages;
    }

    @JsonProperty("versa-tasks.errormessages")
    public VersaTasksErrormessage getVersaTasksErrormessages() {
        return versaTasksErrormessages;
    }

    @JsonProperty("versa-tasks.errormessages")
    public void setVersaTasksErrormessages(VersaTasksErrormessage versaTasksErrormessages) {
        this.versaTasksErrormessages = versaTasksErrormessages;
    }

    @JsonProperty("versa-tasks.start-time")
    public String getVersaTasksStartTime() {
        return versaTasksStartTime;
    }

    @JsonProperty("versa-tasks.start-time")
    public void setVersaTasksStartTime(String versaTasksStartTime) {
        this.versaTasksStartTime = versaTasksStartTime;
    }

    @JsonProperty("versa-tasks.end-time")
    public String getVersaTasksEndTime() {
        return versaTasksEndTime;
    }

    @JsonProperty("versa-tasks.end-time")
    public void setVersaTasksEndTime(String versaTasksEndTime) {
        this.versaTasksEndTime = versaTasksEndTime;
    }

}
