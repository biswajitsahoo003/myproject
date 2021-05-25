package com.tcl.dias.servicefulfillmentutils.beans.wireless;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetPingStability {

    @JsonProperty("packet_duplicates")
    private String packetDuplicates;
    @JsonProperty("packet_loss")
    private String packetLoss;
    @JsonProperty("packet_received")
    private String packetReceived;
    @JsonProperty("packet_transmitted")
    private String packetTransmitted;
    @JsonProperty("ss_latency_avg")
    private String ssLatencyAvg;
    @JsonProperty("ss_latency_max")
    private String ssLatencyMax;
    @JsonProperty("ss_latency_mdev")
    private String ssLatencyMdev;
    @JsonProperty("ss_latency_min")
    private String ssLatencyMin;
    private String time;
    @JsonProperty("error_code")
    private Integer errorCode;
    @JsonProperty("error_response")
    private Integer errorResponse;

    public String getPacketDuplicates() {
        return packetDuplicates;
    }

    public void setPacketDuplicates(String packetDuplicates) {
        this.packetDuplicates = packetDuplicates;
    }

    public String getPacketLoss() {
        return packetLoss;
    }

    public void setPacketLoss(String packetLoss) {
        this.packetLoss = packetLoss;
    }

    public String getPacketReceived() {
        return packetReceived;
    }

    public void setPacketReceived(String packetReceived) {
        this.packetReceived = packetReceived;
    }

    public String getPacketTransmitted() {
        return packetTransmitted;
    }

    public void setPacketTransmitted(String packetTransmitted) {
        this.packetTransmitted = packetTransmitted;
    }

    public String getSsLatencyAvg() {
        return ssLatencyAvg;
    }

    public void setSsLatencyAvg(String ssLatencyAvg) {
        this.ssLatencyAvg = ssLatencyAvg;
    }

    public String getSsLatencyMax() {
        return ssLatencyMax;
    }

    public void setSsLatencyMax(String ssLatencyMax) {
        this.ssLatencyMax = ssLatencyMax;
    }

    public String getSsLatencyMdev() {
        return ssLatencyMdev;
    }

    public void setSsLatencyMdev(String ssLatencyMdev) {
        this.ssLatencyMdev = ssLatencyMdev;
    }

    public String getSsLatencyMin() {
        return ssLatencyMin;
    }

    public void setSsLatencyMin(String ssLatencyMin) {
        this.ssLatencyMin = ssLatencyMin;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public Integer getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(Integer errorResponse) {
        this.errorResponse = errorResponse;
    }
}
