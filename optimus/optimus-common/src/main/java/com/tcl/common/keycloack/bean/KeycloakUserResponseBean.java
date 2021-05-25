
package com.tcl.common.keycloack.bean;

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
    "id",
    "createdTimestamp",
    "username",
    "enabled",
    "totp",
    "emailVerified",
    "disableableCredentialTypes",
    "requiredActions",
    "notBefore",
    "access"
})
public class KeycloakUserResponseBean {

    @JsonProperty("id")
    private String id;
    @JsonProperty("createdTimestamp")
    private Long createdTimestamp;
    @JsonProperty("username")
    private String username;
    @JsonProperty("enabled")
    private Boolean enabled;
    @JsonProperty("totp")
    private Boolean totp;
    @JsonProperty("emailVerified")
    private Boolean emailVerified;
    @JsonProperty("disableableCredentialTypes")
    private List<String> disableableCredentialTypes = null;
    @JsonProperty("requiredActions")
    private List<Object> requiredActions = null;
    @JsonProperty("notBefore")
    private Integer notBefore;
    @JsonProperty("access")
    private Access access;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("createdTimestamp")
    public Long getCreatedTimestamp() {
        return createdTimestamp;
    }

    @JsonProperty("createdTimestamp")
    public void setCreatedTimestamp(Long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("enabled")
    public Boolean getEnabled() {
        return enabled;
    }

    @JsonProperty("enabled")
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @JsonProperty("totp")
    public Boolean getTotp() {
        return totp;
    }

    @JsonProperty("totp")
    public void setTotp(Boolean totp) {
        this.totp = totp;
    }

    @JsonProperty("emailVerified")
    public Boolean getEmailVerified() {
        return emailVerified;
    }

    @JsonProperty("emailVerified")
    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    @JsonProperty("disableableCredentialTypes")
    public List<String> getDisableableCredentialTypes() {
        return disableableCredentialTypes;
    }

    @JsonProperty("disableableCredentialTypes")
    public void setDisableableCredentialTypes(List<String> disableableCredentialTypes) {
        this.disableableCredentialTypes = disableableCredentialTypes;
    }

    @JsonProperty("requiredActions")
    public List<Object> getRequiredActions() {
        return requiredActions;
    }

    @JsonProperty("requiredActions")
    public void setRequiredActions(List<Object> requiredActions) {
        this.requiredActions = requiredActions;
    }

    @JsonProperty("notBefore")
    public Integer getNotBefore() {
        return notBefore;
    }

    @JsonProperty("notBefore")
    public void setNotBefore(Integer notBefore) {
        this.notBefore = notBefore;
    }

    @JsonProperty("access")
    public Access getAccess() {
        return access;
    }

    @JsonProperty("access")
    public void setAccess(Access access) {
        this.access = access;
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
