
package com.tcl.common.keycloack.bean;

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
    "manageGroupMembership",
    "view",
    "mapRoles",
    "impersonate",
    "manage"
})
public class Access {

    @JsonProperty("manageGroupMembership")
    private Boolean manageGroupMembership;
    @JsonProperty("view")
    private Boolean view;
    @JsonProperty("mapRoles")
    private Boolean mapRoles;
    @JsonProperty("impersonate")
    private Boolean impersonate;
    @JsonProperty("manage")
    private Boolean manage;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("manageGroupMembership")
    public Boolean getManageGroupMembership() {
        return manageGroupMembership;
    }

    @JsonProperty("manageGroupMembership")
    public void setManageGroupMembership(Boolean manageGroupMembership) {
        this.manageGroupMembership = manageGroupMembership;
    }

    @JsonProperty("view")
    public Boolean getView() {
        return view;
    }

    @JsonProperty("view")
    public void setView(Boolean view) {
        this.view = view;
    }

    @JsonProperty("mapRoles")
    public Boolean getMapRoles() {
        return mapRoles;
    }

    @JsonProperty("mapRoles")
    public void setMapRoles(Boolean mapRoles) {
        this.mapRoles = mapRoles;
    }

    @JsonProperty("impersonate")
    public Boolean getImpersonate() {
        return impersonate;
    }

    @JsonProperty("impersonate")
    public void setImpersonate(Boolean impersonate) {
        this.impersonate = impersonate;
    }

    @JsonProperty("manage")
    public Boolean getManage() {
        return manage;
    }

    @JsonProperty("manage")
    public void setManage(Boolean manage) {
        this.manage = manage;
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
