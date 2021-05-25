package com.tcl.dias.servicefulfillmentutils.beans.wireless;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PingTestResult {

    @JsonProperty("ss_latency_avg")
    private String ssLatencyAvg;
    @JsonProperty("ss_latency_max")
    private String ssLatencyMax;
    @JsonProperty("ss_latency_min")
    private String ssLatencyMin;
    @JsonProperty("ss_pd_avg")
    private String ssPdAvg;
    @JsonProperty("ss_pd_max")
    private String ssPdMax;
    @JsonProperty("ss_pd_min")
    private String ssPdMin;

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

    public String getSsLatencyMin() {
        return ssLatencyMin;
    }

    public void setSsLatencyMin(String ssLatencyMin) {
        this.ssLatencyMin = ssLatencyMin;
    }

    public String getSsPdAvg() {
        return ssPdAvg;
    }

    public void setSsPdAvg(String ssPdAvg) {
        this.ssPdAvg = ssPdAvg;
    }

    public String getSsPdMax() {
        return ssPdMax;
    }

    public void setSsPdMax(String ssPdMax) {
        this.ssPdMax = ssPdMax;
    }

    public String getSsPdMin() {
        return ssPdMin;
    }

    public void setSsPdMin(String ssPdMin) {
        this.ssPdMin = ssPdMin;
    }

}
