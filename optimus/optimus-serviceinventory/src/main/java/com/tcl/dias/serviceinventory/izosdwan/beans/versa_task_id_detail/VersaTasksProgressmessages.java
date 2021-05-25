
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_task_id_detail;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "versa-tasks.progressmessage"
})
public class VersaTasksProgressmessages {

    @JsonProperty("versa-tasks.progressmessage")
    private List<VersaTasksProgressmessage> versaTasksProgressmessage = null;

    @JsonProperty("versa-tasks.progressmessage")
    public List<VersaTasksProgressmessage> getVersaTasksProgressmessage() {
        return versaTasksProgressmessage;
    }

    @JsonProperty("versa-tasks.progressmessage")
    public void setVersaTasksProgressmessage(List<VersaTasksProgressmessage> versaTasksProgressmessage) {
        this.versaTasksProgressmessage = versaTasksProgressmessage;
    }

}
