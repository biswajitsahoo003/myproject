
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_task_id_detail;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "versa-tasks.message",
    "versa-tasks.time"
})
public class VersaTasksProgressmessage {

    @JsonProperty("versa-tasks.message")
    private String versaTasksMessage;
    @JsonProperty("versa-tasks.time")
    private String versaTasksTime;

    @JsonProperty("versa-tasks.message")
    public String getVersaTasksMessage() {
        return versaTasksMessage;
    }

    @JsonProperty("versa-tasks.message")
    public void setVersaTasksMessage(String versaTasksMessage) {
        this.versaTasksMessage = versaTasksMessage;
    }

    @JsonProperty("versa-tasks.time")
    public String getVersaTasksTime() {
        return versaTasksTime;
    }

    @JsonProperty("versa-tasks.time")
    public void setVersaTasksTime(String versaTasksTime) {
        this.versaTasksTime = versaTasksTime;
    }

}
