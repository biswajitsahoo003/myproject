
package com.tcl.dias.docusign.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * 
 * This file contains the EnvelopeStatus.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "RecipientStatuses",
    "TimeGenerated",
    "EnvelopeID",
    "Subject",
    "UserName",
    "Email",
    "Status",
    "Created",
    "Sent",
    "Delivered",
    "Signed",
    "Completed",
    "ACStatus",
    "ACStatusDate",
    "ACHolder",
    "ACHolderEmail",
    "ACHolderLocation",
    "SigningLocation",
    "SenderIPAddress",
    "EnvelopePDFHash",
    "CustomFields",
    "AutoNavigation",
    "EnvelopeIdStamping",
    "AuthoritativeCopy",
    "DocumentStatuses"
})
public class EnvelopeStatus {

    @JsonProperty("RecipientStatuses")
    private RecipientStatuses recipientStatuses;
    @JsonProperty("TimeGenerated")
    private String timeGenerated;
    @JsonProperty("EnvelopeID")
    private String envelopeID;
    @JsonProperty("Subject")
    private String subject;
    @JsonProperty("UserName")
    private String userName;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Created")
    private String created;
    @JsonProperty("Sent")
    private String sent;
    @JsonProperty("Delivered")
    private String delivered;
    @JsonProperty("Signed")
    private String signed;
    @JsonProperty("Completed")
    private String completed;
    @JsonProperty("ACStatus")
    private String aCStatus;
    @JsonProperty("ACStatusDate")
    private String aCStatusDate;
    @JsonProperty("ACHolder")
    private String aCHolder;
    @JsonProperty("ACHolderEmail")
    private String aCHolderEmail;
    @JsonProperty("ACHolderLocation")
    private String aCHolderLocation;
    @JsonProperty("SigningLocation")
    private String signingLocation;
    @JsonProperty("SenderIPAddress")
    private String senderIPAddress;
    @JsonProperty("EnvelopePDFHash")
    private Object envelopePDFHash;
    @JsonProperty("CustomFields")
    private CustomFields customFields;
    @JsonProperty("AutoNavigation")
    private String autoNavigation;
    @JsonProperty("EnvelopeIdStamping")
    private String envelopeIdStamping;
    @JsonProperty("AuthoritativeCopy")
    private String authoritativeCopy;
    @JsonProperty("DocumentStatuses")
    private DocumentStatuses documentStatuses;

    @JsonProperty("RecipientStatuses")
    public RecipientStatuses getRecipientStatuses() {
        return recipientStatuses;
    }

    @JsonProperty("RecipientStatuses")
    public void setRecipientStatuses(RecipientStatuses recipientStatuses) {
        this.recipientStatuses = recipientStatuses;
    }

    @JsonProperty("TimeGenerated")
    public String getTimeGenerated() {
        return timeGenerated;
    }

    @JsonProperty("TimeGenerated")
    public void setTimeGenerated(String timeGenerated) {
        this.timeGenerated = timeGenerated;
    }

    @JsonProperty("EnvelopeID")
    public String getEnvelopeID() {
        return envelopeID;
    }

    @JsonProperty("EnvelopeID")
    public void setEnvelopeID(String envelopeID) {
        this.envelopeID = envelopeID;
    }

    @JsonProperty("Subject")
    public String getSubject() {
        return subject;
    }

    @JsonProperty("Subject")
    public void setSubject(String subject) {
        this.subject = subject;
    }

    @JsonProperty("UserName")
    public String getUserName() {
        return userName;
    }

    @JsonProperty("UserName")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonProperty("Email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("Email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("Status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("Created")
    public String getCreated() {
        return created;
    }

    @JsonProperty("Created")
    public void setCreated(String created) {
        this.created = created;
    }

    @JsonProperty("Sent")
    public String getSent() {
        return sent;
    }

    @JsonProperty("Sent")
    public void setSent(String sent) {
        this.sent = sent;
    }

    @JsonProperty("Delivered")
    public String getDelivered() {
        return delivered;
    }

    @JsonProperty("Delivered")
    public void setDelivered(String delivered) {
        this.delivered = delivered;
    }

    @JsonProperty("Signed")
    public String getSigned() {
        return signed;
    }

    @JsonProperty("Signed")
    public void setSigned(String signed) {
        this.signed = signed;
    }

    @JsonProperty("Completed")
    public String getCompleted() {
        return completed;
    }

    @JsonProperty("Completed")
    public void setCompleted(String completed) {
        this.completed = completed;
    }

    @JsonProperty("ACStatus")
    public String getACStatus() {
        return aCStatus;
    }

    @JsonProperty("ACStatus")
    public void setACStatus(String aCStatus) {
        this.aCStatus = aCStatus;
    }

    @JsonProperty("ACStatusDate")
    public String getACStatusDate() {
        return aCStatusDate;
    }

    @JsonProperty("ACStatusDate")
    public void setACStatusDate(String aCStatusDate) {
        this.aCStatusDate = aCStatusDate;
    }

    @JsonProperty("ACHolder")
    public String getACHolder() {
        return aCHolder;
    }

    @JsonProperty("ACHolder")
    public void setACHolder(String aCHolder) {
        this.aCHolder = aCHolder;
    }

    @JsonProperty("ACHolderEmail")
    public String getACHolderEmail() {
        return aCHolderEmail;
    }

    @JsonProperty("ACHolderEmail")
    public void setACHolderEmail(String aCHolderEmail) {
        this.aCHolderEmail = aCHolderEmail;
    }

    @JsonProperty("ACHolderLocation")
    public String getACHolderLocation() {
        return aCHolderLocation;
    }

    @JsonProperty("ACHolderLocation")
    public void setACHolderLocation(String aCHolderLocation) {
        this.aCHolderLocation = aCHolderLocation;
    }

    @JsonProperty("SigningLocation")
    public String getSigningLocation() {
        return signingLocation;
    }

    @JsonProperty("SigningLocation")
    public void setSigningLocation(String signingLocation) {
        this.signingLocation = signingLocation;
    }

    @JsonProperty("SenderIPAddress")
    public String getSenderIPAddress() {
        return senderIPAddress;
    }

    @JsonProperty("SenderIPAddress")
    public void setSenderIPAddress(String senderIPAddress) {
        this.senderIPAddress = senderIPAddress;
    }

    @JsonProperty("EnvelopePDFHash")
    public Object getEnvelopePDFHash() {
        return envelopePDFHash;
    }

    @JsonProperty("EnvelopePDFHash")
    public void setEnvelopePDFHash(Object envelopePDFHash) {
        this.envelopePDFHash = envelopePDFHash;
    }

    @JsonProperty("CustomFields")
    public CustomFields getCustomFields() {
        return customFields;
    }

    @JsonProperty("CustomFields")
    public void setCustomFields(CustomFields customFields) {
        this.customFields = customFields;
    }

    @JsonProperty("AutoNavigation")
    public String getAutoNavigation() {
        return autoNavigation;
    }

    @JsonProperty("AutoNavigation")
    public void setAutoNavigation(String autoNavigation) {
        this.autoNavigation = autoNavigation;
    }

    @JsonProperty("EnvelopeIdStamping")
    public String getEnvelopeIdStamping() {
        return envelopeIdStamping;
    }

    @JsonProperty("EnvelopeIdStamping")
    public void setEnvelopeIdStamping(String envelopeIdStamping) {
        this.envelopeIdStamping = envelopeIdStamping;
    }

    @JsonProperty("AuthoritativeCopy")
    public String getAuthoritativeCopy() {
        return authoritativeCopy;
    }

    @JsonProperty("AuthoritativeCopy")
    public void setAuthoritativeCopy(String authoritativeCopy) {
        this.authoritativeCopy = authoritativeCopy;
    }

    @JsonProperty("DocumentStatuses")
    public DocumentStatuses getDocumentStatuses() {
        return documentStatuses;
    }

    @JsonProperty("DocumentStatuses")
    public void setDocumentStatuses(DocumentStatuses documentStatuses) {
        this.documentStatuses = documentStatuses;
    }

}
