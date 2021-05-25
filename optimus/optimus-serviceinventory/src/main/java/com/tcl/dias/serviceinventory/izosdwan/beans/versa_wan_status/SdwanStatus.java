
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_wan_status;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "site-name",
    "path-status"
})
public class SdwanStatus {

    @JsonProperty("site-name")
    private String siteName;
    @JsonProperty("path-status")
    private List<PathStatus> pathStatus = null;

    @JsonProperty("site-name")
    public String getSiteName() {
        return siteName;
    }

    @JsonProperty("site-name")
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    @JsonProperty("path-status")
    public List<PathStatus> getPathStatus() {
        return pathStatus;
    }

    @JsonProperty("path-status")
    public void setPathStatus(List<PathStatus> pathStatus) {
        this.pathStatus = pathStatus;
    }

}
