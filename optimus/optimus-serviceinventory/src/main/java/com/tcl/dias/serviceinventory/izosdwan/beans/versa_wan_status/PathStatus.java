
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_wan_status;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "path-handle",
    "fwd-class",
    "local-wan-link",
    "remote-wan-link",
    "local-wan-link-id",
    "remote-wan-link-id",
    "adaptive-monitoring",
    "damp-state",
    "damp-flaps",
    "conn-state",
    "flaps",
    "last-flapped"
})
public class PathStatus {

    @JsonProperty("path-handle")
    private Integer pathHandle;
    @JsonProperty("fwd-class")
    private String fwdClass;
    @JsonProperty("local-wan-link")
    private String localWanLink;
    @JsonProperty("remote-wan-link")
    private String remoteWanLink;
    @JsonProperty("local-wan-link-id")
    private Integer localWanLinkId;
    @JsonProperty("remote-wan-link-id")
    private Integer remoteWanLinkId;
    @JsonProperty("adaptive-monitoring")
    private String adaptiveMonitoring;
    @JsonProperty("damp-state")
    private String dampState;
    @JsonProperty("damp-flaps")
    private Integer dampFlaps;
    @JsonProperty("conn-state")
    private String connState;
    @JsonProperty("flaps")
    private Integer flaps;
    @JsonProperty("last-flapped")
    private String lastFlapped;

    @JsonProperty("path-handle")
    public Integer getPathHandle() {
        return pathHandle;
    }

    @JsonProperty("path-handle")
    public void setPathHandle(Integer pathHandle) {
        this.pathHandle = pathHandle;
    }

    @JsonProperty("fwd-class")
    public String getFwdClass() {
        return fwdClass;
    }

    @JsonProperty("fwd-class")
    public void setFwdClass(String fwdClass) {
        this.fwdClass = fwdClass;
    }

    @JsonProperty("local-wan-link")
    public String getLocalWanLink() {
        return localWanLink;
    }

    @JsonProperty("local-wan-link")
    public void setLocalWanLink(String localWanLink) {
        this.localWanLink = localWanLink;
    }

    @JsonProperty("remote-wan-link")
    public String getRemoteWanLink() {
        return remoteWanLink;
    }

    @JsonProperty("remote-wan-link")
    public void setRemoteWanLink(String remoteWanLink) {
        this.remoteWanLink = remoteWanLink;
    }

    @JsonProperty("local-wan-link-id")
    public Integer getLocalWanLinkId() {
        return localWanLinkId;
    }

    @JsonProperty("local-wan-link-id")
    public void setLocalWanLinkId(Integer localWanLinkId) {
        this.localWanLinkId = localWanLinkId;
    }

    @JsonProperty("remote-wan-link-id")
    public Integer getRemoteWanLinkId() {
        return remoteWanLinkId;
    }

    @JsonProperty("remote-wan-link-id")
    public void setRemoteWanLinkId(Integer remoteWanLinkId) {
        this.remoteWanLinkId = remoteWanLinkId;
    }

    @JsonProperty("adaptive-monitoring")
    public String getAdaptiveMonitoring() {
        return adaptiveMonitoring;
    }

    @JsonProperty("adaptive-monitoring")
    public void setAdaptiveMonitoring(String adaptiveMonitoring) {
        this.adaptiveMonitoring = adaptiveMonitoring;
    }

    @JsonProperty("damp-state")
    public String getDampState() {
        return dampState;
    }

    @JsonProperty("damp-state")
    public void setDampState(String dampState) {
        this.dampState = dampState;
    }

    @JsonProperty("damp-flaps")
    public Integer getDampFlaps() {
        return dampFlaps;
    }

    @JsonProperty("damp-flaps")
    public void setDampFlaps(Integer dampFlaps) {
        this.dampFlaps = dampFlaps;
    }

    @JsonProperty("conn-state")
    public String getConnState() {
        return connState;
    }

    @JsonProperty("conn-state")
    public void setConnState(String connState) {
        this.connState = connState;
    }

    @JsonProperty("flaps")
    public Integer getFlaps() {
        return flaps;
    }

    @JsonProperty("flaps")
    public void setFlaps(Integer flaps) {
        this.flaps = flaps;
    }

    @JsonProperty("last-flapped")
    public String getLastFlapped() {
        return lastFlapped;
    }

    @JsonProperty("last-flapped")
    public void setLastFlapped(String lastFlapped) {
        this.lastFlapped = lastFlapped;
    }

}
