package com.tcl.dias.beans.conferenceUsageReport;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "auditKey",
    "customerName",
    "year",
    "month",
    "monthShortName",
    "conferenceCount",
    "participantMinutes",
    "avgParticpantMinutes",
    "conferenceMinutes",
    "conferenceDurationAvg",
    "participants",
    "avgParticipants",
    "loadFileName",
    "createdAt",
    "updatedAt"
})
public class UsageData {

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
    @JsonProperty("conferenceCount")
    private Integer conferenceCount;
    @JsonProperty("participantMinutes")
    private Double participantMinutes;
    @JsonProperty("avgParticpantMinutes")
    private Double avgParticpantMinutes;
    @JsonProperty("conferenceMinutes")
    private Double conferenceMinutes;
    @JsonProperty("conferenceDurationAvg")
    private Double conferenceDurationAvg;
    @JsonProperty("participants")
    private Integer participants;
    @JsonProperty("avgParticipants")
    private Double avgParticipants;
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

    @JsonProperty("participantMinutes")
    public Double getParticipantMinutes() {
        return participantMinutes;
    }

    @JsonProperty("participantMinutes")
    public void setParticipantMinutes(Double participantMinutes) {
        this.participantMinutes = participantMinutes;
    }

    @JsonProperty("avgParticpantMinutes")
    public Double getAvgParticpantMinutes() {
        return avgParticpantMinutes;
    }

    @JsonProperty("avgParticpantMinutes")
    public void setAvgParticpantMinutes(Double avgParticpantMinutes) {
        this.avgParticpantMinutes = avgParticpantMinutes;
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

    @JsonProperty("participants")
    public Integer getParticipants() {
        return participants;
    }

    @JsonProperty("participants")
    public void setParticipants(Integer participants) {
        this.participants = participants;
    }

    @JsonProperty("avgParticipants")
    public Double getAvgParticipants() {
        return avgParticipants;
    }

    @JsonProperty("avgParticipants")
    public void setAvgParticipants(Double avgParticipants) {
        this.avgParticipants = avgParticipants;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("auditKey", auditKey).append("customerName", customerName).append("year", year).append("month", month).append("monthShortName", monthShortName).append("conferenceCount", conferenceCount).append("participantMinutes", participantMinutes).append("avgParticpantMinutes", avgParticpantMinutes).append("conferenceMinutes", conferenceMinutes).append("conferenceDurationAvg", conferenceDurationAvg).append("participants", participants).append("avgParticipants", avgParticipants).append("loadFileName", loadFileName).append("createdAt", createdAt).append("updatedAt", updatedAt).append("additionalProperties", additionalProperties).toString();
    }

}
