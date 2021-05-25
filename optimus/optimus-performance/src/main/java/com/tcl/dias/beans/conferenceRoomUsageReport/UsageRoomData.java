package com.tcl.dias.beans.conferenceRoomUsageReport;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "auditKey",
    "customerName",
    "year",
    "month",
    "monthShortName",
    "roomName",
    "conferenceCount",
    "conferenceMinutes",
    "conferenceDurationAvg",
    "loadFileName",
    "createdAt",
    "updatedAt"
})
public class UsageRoomData {

    @JsonProperty("id")
    private String id;
    @JsonProperty("auditKey")
    private String auditKey;
    @JsonProperty("customerName")
    private String customerName;
    @JsonProperty("year")
    private Integer year;
    @JsonProperty("month")
    private Integer month;
	@JsonProperty("monthShortName")
    private String monthShortName;
    @JsonProperty("roomName")
    private String roomName;
    @JsonProperty("conferenceCount")
    private Integer conferenceCount;
    @JsonProperty("conferenceMinutes")
    private Double conferenceMinutes;
    @JsonProperty("conferenceDurationAvg")
    private Double conferenceDurationAvg;
    @JsonProperty("loadFileName")
    private String loadFileName;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("updatedAt")
    private String updatedAt;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("auditKey")
    public String getAuditKey() {
        return auditKey;
    }

    @JsonProperty("auditKey")
    public void setAuditKey(String auditKey) {
        this.auditKey = auditKey;
    }

    @JsonProperty("customerName")
    public String getCustomerName() {
        return customerName;
    }

    @JsonProperty("customerName")
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}

	@JsonProperty("year")
    public Integer getYear() {
        return year;
    }

    @JsonProperty("year")
    public void setYear(Integer year) {
        this.year = year;
    }

    @JsonProperty("month")
    public Integer getMonth() {
        return month;
    }

    @JsonProperty("month")
    public void setMonth(Integer month) {
        this.month = month;
    }

    @JsonProperty("monthShortName")
    public String getMonthShortName() {
        return monthShortName;
    }

    @JsonProperty("monthShortName")
    public void setMonthShortName(String monthShortName) {
        this.monthShortName = monthShortName;
    }

    @JsonProperty("conferenceCount")
    public Integer getConferenceCount() {
        return conferenceCount;
    }

    @JsonProperty("conferenceCount")
    public void setConferenceCount(Integer conferenceCount) {
        this.conferenceCount = conferenceCount;
    }

    @JsonProperty("conferenceMinutes")
    public Double getConferenceMinutes() {
        return conferenceMinutes;
    }

    @JsonProperty("conferenceMinutes")
    public void setConferenceMinutes(Double conferenceMinutes) {
        this.conferenceMinutes = conferenceMinutes;
    }

    @JsonProperty("conferenceDurationAvg")
    public Double getConferenceDurationAvg() {
        return conferenceDurationAvg;
    }

    @JsonProperty("conferenceDurationAvg")
    public void setConferenceDurationAvg(Double conferenceDurationAvg) {
        this.conferenceDurationAvg = conferenceDurationAvg;
    }

    @JsonProperty("loadFileName")
    public String getLoadFileName() {
        return loadFileName;
    }

    @JsonProperty("loadFileName")
    public void setLoadFileName(String loadFileName) {
        this.loadFileName = loadFileName;
    }

    @JsonProperty("createdAt")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("updatedAt")
    public String getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("updatedAt")
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
