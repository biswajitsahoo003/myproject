
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_task_id_detail;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "versa-tasks.task"
})
public class TaskDetailById {

    @JsonProperty("versa-tasks.task")
    private VersaTasksTask versaTasksTask;

    @JsonProperty("versa-tasks.task")
    public VersaTasksTask getVersaTasksTask() {
        return versaTasksTask;
    }

    @JsonProperty("versa-tasks.task")
    public void setVersaTasksTask(VersaTasksTask versaTasksTask) {
        this.versaTasksTask = versaTasksTask;
    }

}
