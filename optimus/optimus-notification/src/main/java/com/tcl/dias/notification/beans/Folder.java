
package com.tcl.dias.notification.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ownerUserName",
    "ownerEmail",
    "ownerUserId",
    "type",
    "name",
    "uri",
    "parentFolderId",
    "parentFolderUri",
    "folderId",
    "errorDetails",
    "folders",
    "filter"
})
public class Folder {

    @JsonProperty("ownerUserName")
    private String ownerUserName;
    @JsonProperty("ownerEmail")
    private String ownerEmail;
    @JsonProperty("ownerUserId")
    private String ownerUserId;
    @JsonProperty("type")
    private String type;
    @JsonProperty("name")
    private String name;
    @JsonProperty("uri")
    private String uri;
    @JsonProperty("parentFolderId")
    private String parentFolderId;
    @JsonProperty("parentFolderUri")
    private String parentFolderUri;
    @JsonProperty("folderId")
    private String folderId;
    @JsonProperty("errorDetails")
    private ErrorDetails errorDetails;
    @JsonProperty("folders")
    private List<Folder_> folders = null;
    @JsonProperty("filter")
    private Filter filter;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ownerUserName")
    public String getOwnerUserName() {
        return ownerUserName;
    }

    @JsonProperty("ownerUserName")
    public void setOwnerUserName(String ownerUserName) {
        this.ownerUserName = ownerUserName;
    }

    @JsonProperty("ownerEmail")
    public String getOwnerEmail() {
        return ownerEmail;
    }

    @JsonProperty("ownerEmail")
    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    @JsonProperty("ownerUserId")
    public String getOwnerUserId() {
        return ownerUserId;
    }

    @JsonProperty("ownerUserId")
    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("uri")
    public String getUri() {
        return uri;
    }

    @JsonProperty("uri")
    public void setUri(String uri) {
        this.uri = uri;
    }

    @JsonProperty("parentFolderId")
    public String getParentFolderId() {
        return parentFolderId;
    }

    @JsonProperty("parentFolderId")
    public void setParentFolderId(String parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    @JsonProperty("parentFolderUri")
    public String getParentFolderUri() {
        return parentFolderUri;
    }

    @JsonProperty("parentFolderUri")
    public void setParentFolderUri(String parentFolderUri) {
        this.parentFolderUri = parentFolderUri;
    }

    @JsonProperty("folderId")
    public String getFolderId() {
        return folderId;
    }

    @JsonProperty("folderId")
    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    @JsonProperty("errorDetails")
    public ErrorDetails getErrorDetails() {
        return errorDetails;
    }

    @JsonProperty("errorDetails")
    public void setErrorDetails(ErrorDetails errorDetails) {
        this.errorDetails = errorDetails;
    }

    @JsonProperty("folders")
    public List<Folder_> getFolders() {
        return folders;
    }

    @JsonProperty("folders")
    public void setFolders(List<Folder_> folders) {
        this.folders = folders;
    }

    @JsonProperty("filter")
    public Filter getFilter() {
        return filter;
    }

    @JsonProperty("filter")
    public void setFilter(Filter filter) {
        this.filter = filter;
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
