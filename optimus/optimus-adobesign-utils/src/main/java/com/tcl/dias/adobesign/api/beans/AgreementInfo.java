
package com.tcl.dias.adobesign.api.beans;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "fileInfos",
    "name",
    "participantSetsInfo",
    "signatureType",
    "state",
    "ccs",
    "createdDate",
    "deviceInfo",
    "documentVisibilityEnabled",
    "emailOption",
    "expirationTime",
    "externalId",
    "firstReminderDelay",
    "formFieldLayerTemplates",
    "groupId",
    "hasFormFieldData",
    "hasSignerIdentityReport",
    "id",
    "isDocumentRetentionApplied",
    "lastEventDate",
    "locale",
    "mergeFieldInfo",
    "message",
    "parentId",
    "postSignOption",
    "reminderFrequency",
    "securityOption",
    "senderEmail",
    "status",
    "type",
    "vaultingInfo",
    "workflowId"
})
public class AgreementInfo {

    @JsonProperty("fileInfos")
    private List<FileInfo> fileInfos = null;
    @JsonProperty("name")
    private String name;
    @JsonProperty("participantSetsInfo")
    private List<ParticipantSetsInfo> participantSetsInfo = null;
    @JsonProperty("signatureType")
    private String signatureType;
    @JsonProperty("state")
    private String state;
    @JsonProperty("ccs")
    private List<Cc> ccs = null;
    @JsonProperty("createdDate")
    private String createdDate;
    @JsonProperty("deviceInfo")
    private DeviceInfo deviceInfo;
    @JsonProperty("documentVisibilityEnabled")
    private Boolean documentVisibilityEnabled;
    @JsonProperty("emailOption")
    private EmailOption emailOption;
    @JsonProperty("expirationTime")
    private String expirationTime;
    @JsonProperty("externalId")
    private ExternalId externalId;
    @JsonProperty("firstReminderDelay")
    private Integer firstReminderDelay;
    @JsonProperty("formFieldLayerTemplates")
    private List<FormFieldLayerTemplate> formFieldLayerTemplates = null;
    @JsonProperty("groupId")
    private String groupId;
    @JsonProperty("hasFormFieldData")
    private Boolean hasFormFieldData;
    @JsonProperty("hasSignerIdentityReport")
    private Boolean hasSignerIdentityReport;
    @JsonProperty("id")
    private String id;
    @JsonProperty("isDocumentRetentionApplied")
    private Boolean isDocumentRetentionApplied;
    @JsonProperty("lastEventDate")
    private String lastEventDate;
    @JsonProperty("locale")
    private String locale;
    @JsonProperty("mergeFieldInfo")
    private List<MergeFieldInfo> mergeFieldInfo = null;
    @JsonProperty("message")
    private String message;
    @JsonProperty("parentId")
    private String parentId;
    @JsonProperty("postSignOption")
    private PostSignOption postSignOption;
    @JsonProperty("reminderFrequency")
    private String reminderFrequency;
    @JsonProperty("securityOption")
    private SecurityOption_ securityOption;
    @JsonProperty("senderEmail")
    private String senderEmail;
    @JsonProperty("status")
    private String status;
    @JsonProperty("type")
    private String type;
    @JsonProperty("vaultingInfo")
    private VaultingInfo vaultingInfo;
    @JsonProperty("workflowId")
    private String workflowId;

    @JsonProperty("fileInfos")
    public List<FileInfo> getFileInfos() {
        return fileInfos;
    }

    @JsonProperty("fileInfos")
    public void setFileInfos(List<FileInfo> fileInfos) {
        this.fileInfos = fileInfos;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("participantSetsInfo")
    public List<ParticipantSetsInfo> getParticipantSetsInfo() {
        return participantSetsInfo;
    }

    @JsonProperty("participantSetsInfo")
    public void setParticipantSetsInfo(List<ParticipantSetsInfo> participantSetsInfo) {
        this.participantSetsInfo = participantSetsInfo;
    }

    @JsonProperty("signatureType")
    public String getSignatureType() {
        return signatureType;
    }

    @JsonProperty("signatureType")
    public void setSignatureType(String signatureType) {
        this.signatureType = signatureType;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("ccs")
    public List<Cc> getCcs() {
        return ccs;
    }

    @JsonProperty("ccs")
    public void setCcs(List<Cc> ccs) {
        this.ccs = ccs;
    }

    @JsonProperty("createdDate")
    public String getCreatedDate() {
        return createdDate;
    }

    @JsonProperty("createdDate")
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @JsonProperty("deviceInfo")
    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    @JsonProperty("deviceInfo")
    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    @JsonProperty("documentVisibilityEnabled")
    public Boolean getDocumentVisibilityEnabled() {
        return documentVisibilityEnabled;
    }

    @JsonProperty("documentVisibilityEnabled")
    public void setDocumentVisibilityEnabled(Boolean documentVisibilityEnabled) {
        this.documentVisibilityEnabled = documentVisibilityEnabled;
    }

    @JsonProperty("emailOption")
    public EmailOption getEmailOption() {
        return emailOption;
    }

    @JsonProperty("emailOption")
    public void setEmailOption(EmailOption emailOption) {
        this.emailOption = emailOption;
    }

    @JsonProperty("expirationTime")
    public String getExpirationTime() {
        return expirationTime;
    }

    @JsonProperty("expirationTime")
    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

    @JsonProperty("externalId")
    public ExternalId getExternalId() {
        return externalId;
    }

    @JsonProperty("externalId")
    public void setExternalId(ExternalId externalId) {
        this.externalId = externalId;
    }

    @JsonProperty("firstReminderDelay")
    public Integer getFirstReminderDelay() {
        return firstReminderDelay;
    }

    @JsonProperty("firstReminderDelay")
    public void setFirstReminderDelay(Integer firstReminderDelay) {
        this.firstReminderDelay = firstReminderDelay;
    }

    @JsonProperty("formFieldLayerTemplates")
    public List<FormFieldLayerTemplate> getFormFieldLayerTemplates() {
        return formFieldLayerTemplates;
    }

    @JsonProperty("formFieldLayerTemplates")
    public void setFormFieldLayerTemplates(List<FormFieldLayerTemplate> formFieldLayerTemplates) {
        this.formFieldLayerTemplates = formFieldLayerTemplates;
    }

    @JsonProperty("groupId")
    public String getGroupId() {
        return groupId;
    }

    @JsonProperty("groupId")
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @JsonProperty("hasFormFieldData")
    public Boolean getHasFormFieldData() {
        return hasFormFieldData;
    }

    @JsonProperty("hasFormFieldData")
    public void setHasFormFieldData(Boolean hasFormFieldData) {
        this.hasFormFieldData = hasFormFieldData;
    }

    @JsonProperty("hasSignerIdentityReport")
    public Boolean getHasSignerIdentityReport() {
        return hasSignerIdentityReport;
    }

    @JsonProperty("hasSignerIdentityReport")
    public void setHasSignerIdentityReport(Boolean hasSignerIdentityReport) {
        this.hasSignerIdentityReport = hasSignerIdentityReport;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("isDocumentRetentionApplied")
    public Boolean getIsDocumentRetentionApplied() {
        return isDocumentRetentionApplied;
    }

    @JsonProperty("isDocumentRetentionApplied")
    public void setIsDocumentRetentionApplied(Boolean isDocumentRetentionApplied) {
        this.isDocumentRetentionApplied = isDocumentRetentionApplied;
    }

    @JsonProperty("lastEventDate")
    public String getLastEventDate() {
        return lastEventDate;
    }

    @JsonProperty("lastEventDate")
    public void setLastEventDate(String lastEventDate) {
        this.lastEventDate = lastEventDate;
    }

    @JsonProperty("locale")
    public String getLocale() {
        return locale;
    }

    @JsonProperty("locale")
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @JsonProperty("mergeFieldInfo")
    public List<MergeFieldInfo> getMergeFieldInfo() {
        return mergeFieldInfo;
    }

    @JsonProperty("mergeFieldInfo")
    public void setMergeFieldInfo(List<MergeFieldInfo> mergeFieldInfo) {
        this.mergeFieldInfo = mergeFieldInfo;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("parentId")
    public String getParentId() {
        return parentId;
    }

    @JsonProperty("parentId")
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @JsonProperty("postSignOption")
    public PostSignOption getPostSignOption() {
        return postSignOption;
    }

    @JsonProperty("postSignOption")
    public void setPostSignOption(PostSignOption postSignOption) {
        this.postSignOption = postSignOption;
    }

    @JsonProperty("reminderFrequency")
    public String getReminderFrequency() {
        return reminderFrequency;
    }

    @JsonProperty("reminderFrequency")
    public void setReminderFrequency(String reminderFrequency) {
        this.reminderFrequency = reminderFrequency;
    }

    @JsonProperty("securityOption")
    public SecurityOption_ getSecurityOption() {
        return securityOption;
    }

    @JsonProperty("securityOption")
    public void setSecurityOption(SecurityOption_ securityOption) {
        this.securityOption = securityOption;
    }

    @JsonProperty("senderEmail")
    public String getSenderEmail() {
        return senderEmail;
    }

    @JsonProperty("senderEmail")
    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("vaultingInfo")
    public VaultingInfo getVaultingInfo() {
        return vaultingInfo;
    }

    @JsonProperty("vaultingInfo")
    public void setVaultingInfo(VaultingInfo vaultingInfo) {
        this.vaultingInfo = vaultingInfo;
    }

    @JsonProperty("workflowId")
    public String getWorkflowId() {
        return workflowId;
    }

    @JsonProperty("workflowId")
    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("fileInfos", fileInfos).append("name", name).append("participantSetsInfo", participantSetsInfo).append("signatureType", signatureType).append("state", state).append("ccs", ccs).append("createdDate", createdDate).append("deviceInfo", deviceInfo).append("documentVisibilityEnabled", documentVisibilityEnabled).append("emailOption", emailOption).append("expirationTime", expirationTime).append("externalId", externalId).append("firstReminderDelay", firstReminderDelay).append("formFieldLayerTemplates", formFieldLayerTemplates).append("groupId", groupId).append("hasFormFieldData", hasFormFieldData).append("hasSignerIdentityReport", hasSignerIdentityReport).append("id", id).append("isDocumentRetentionApplied", isDocumentRetentionApplied).append("lastEventDate", lastEventDate).append("locale", locale).append("mergeFieldInfo", mergeFieldInfo).append("message", message).append("parentId", parentId).append("postSignOption", postSignOption).append("reminderFrequency", reminderFrequency).append("securityOption", securityOption).append("senderEmail", senderEmail).append("status", status).append("type", type).append("vaultingInfo", vaultingInfo).append("workflowId", workflowId).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(groupId).append(locale).append(type).append(vaultingInfo).append(securityOption).append(postSignOption).append(ccs).append(documentVisibilityEnabled).append(isDocumentRetentionApplied).append(hasSignerIdentityReport).append(lastEventDate).append(senderEmail).append(state).append(id).append(mergeFieldInfo).append(firstReminderDelay).append(signatureType).append(emailOption).append(externalId).append(message).append(deviceInfo).append(parentId).append(reminderFrequency).append(participantSetsInfo).append(createdDate).append(hasFormFieldData).append(expirationTime).append(formFieldLayerTemplates).append(name).append(fileInfos).append(workflowId).append(status).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AgreementInfo) == false) {
            return false;
        }
        AgreementInfo rhs = ((AgreementInfo) other);
        return new EqualsBuilder().append(groupId, rhs.groupId).append(locale, rhs.locale).append(type, rhs.type).append(vaultingInfo, rhs.vaultingInfo).append(securityOption, rhs.securityOption).append(postSignOption, rhs.postSignOption).append(ccs, rhs.ccs).append(documentVisibilityEnabled, rhs.documentVisibilityEnabled).append(isDocumentRetentionApplied, rhs.isDocumentRetentionApplied).append(hasSignerIdentityReport, rhs.hasSignerIdentityReport).append(lastEventDate, rhs.lastEventDate).append(senderEmail, rhs.senderEmail).append(state, rhs.state).append(id, rhs.id).append(mergeFieldInfo, rhs.mergeFieldInfo).append(firstReminderDelay, rhs.firstReminderDelay).append(signatureType, rhs.signatureType).append(emailOption, rhs.emailOption).append(externalId, rhs.externalId).append(message, rhs.message).append(deviceInfo, rhs.deviceInfo).append(parentId, rhs.parentId).append(reminderFrequency, rhs.reminderFrequency).append(participantSetsInfo, rhs.participantSetsInfo).append(createdDate, rhs.createdDate).append(hasFormFieldData, rhs.hasFormFieldData).append(expirationTime, rhs.expirationTime).append(formFieldLayerTemplates, rhs.formFieldLayerTemplates).append(name, rhs.name).append(fileInfos, rhs.fileInfos).append(workflowId, rhs.workflowId).append(status, rhs.status).isEquals();
    }

}
