
package com.tcl.dias.notification.beans;

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
    "actionRequired",
    "expires",
    "isTemplate",
    "status",
    "fromDateTime",
    "toDateTime",
    "searchTarget",
    "searchText",
    "folderIds",
    "orderBy",
    "order"
})
public class Filter {

    @JsonProperty("actionRequired")
    private String actionRequired;
    @JsonProperty("expires")
    private String expires;
    @JsonProperty("isTemplate")
    private String isTemplate;
    @JsonProperty("status")
    private String status;
    @JsonProperty("fromDateTime")
    private String fromDateTime;
    @JsonProperty("toDateTime")
    private String toDateTime;
    @JsonProperty("searchTarget")
    private String searchTarget;
    @JsonProperty("searchText")
    private String searchText;
    @JsonProperty("folderIds")
    private String folderIds;
    @JsonProperty("orderBy")
    private String orderBy;
    @JsonProperty("order")
    private String order;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("actionRequired")
    public String getActionRequired() {
        return actionRequired;
    }

    @JsonProperty("actionRequired")
    public void setActionRequired(String actionRequired) {
        this.actionRequired = actionRequired;
    }

    @JsonProperty("expires")
    public String getExpires() {
        return expires;
    }

    @JsonProperty("expires")
    public void setExpires(String expires) {
        this.expires = expires;
    }

    @JsonProperty("isTemplate")
    public String getIsTemplate() {
        return isTemplate;
    }

    @JsonProperty("isTemplate")
    public void setIsTemplate(String isTemplate) {
        this.isTemplate = isTemplate;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("fromDateTime")
    public String getFromDateTime() {
        return fromDateTime;
    }

    @JsonProperty("fromDateTime")
    public void setFromDateTime(String fromDateTime) {
        this.fromDateTime = fromDateTime;
    }

    @JsonProperty("toDateTime")
    public String getToDateTime() {
        return toDateTime;
    }

    @JsonProperty("toDateTime")
    public void setToDateTime(String toDateTime) {
        this.toDateTime = toDateTime;
    }

    @JsonProperty("searchTarget")
    public String getSearchTarget() {
        return searchTarget;
    }

    @JsonProperty("searchTarget")
    public void setSearchTarget(String searchTarget) {
        this.searchTarget = searchTarget;
    }

    @JsonProperty("searchText")
    public String getSearchText() {
        return searchText;
    }

    @JsonProperty("searchText")
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @JsonProperty("folderIds")
    public String getFolderIds() {
        return folderIds;
    }

    @JsonProperty("folderIds")
    public void setFolderIds(String folderIds) {
        this.folderIds = folderIds;
    }

    @JsonProperty("orderBy")
    public String getOrderBy() {
        return orderBy;
    }

    @JsonProperty("orderBy")
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    @JsonProperty("order")
    public String getOrder() {
        return order;
    }

    @JsonProperty("order")
    public void setOrder(String order) {
        this.order = order;
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
