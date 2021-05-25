package com.tcl.dias.oms.gsc.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.validation.constraints.NotNull;

/**
 * GscOrderStatusStageUpdate.java
 *
 * @author VISHESH AWASTHI
 */
@JsonInclude(Include.NON_NULL)
public class GscOrderStatusStageUpdate {

    @NotNull
    private String configurationStatusName;
    private String configurationStageName;

    public GscOrderStatusStageUpdate() {
    }

    public String getConfigurationStatusName() {
        return configurationStatusName;
    }

    public void setConfigurationStatusName(String configurationStatusName) {
        this.configurationStatusName = configurationStatusName;
    }

    public String getConfigurationStageName() {
        return configurationStageName;
    }

    public void setConfigurationStageName(String configurationStageName) {
        this.configurationStageName = configurationStageName;
    }

}
