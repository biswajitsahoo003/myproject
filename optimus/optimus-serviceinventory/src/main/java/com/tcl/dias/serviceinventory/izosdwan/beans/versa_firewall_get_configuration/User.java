package com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration;

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
    "local-database",
    "external-database",
    "user-type"
})
public class User {

    @JsonProperty("local-database")
    private LocalDatabase localDatabase;
    @JsonProperty("external-database")
    private ExternalDatabase externalDatabase;
    @JsonProperty("user-type")
    private String userType;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("local-database")
    public LocalDatabase getLocalDatabase() {
        return localDatabase;
    }

    @JsonProperty("local-database")
    public void setLocalDatabase(LocalDatabase localDatabase) {
        this.localDatabase = localDatabase;
    }

    @JsonProperty("external-database")
    public ExternalDatabase getExternalDatabase() {
        return externalDatabase;
    }

    @JsonProperty("external-database")
    public void setExternalDatabase(ExternalDatabase externalDatabase) {
        this.externalDatabase = externalDatabase;
    }

    @JsonProperty("user-type")
    public String getUserType() {
        return userType;
    }

    @JsonProperty("user-type")
    public void setUserType(String userType) {
        this.userType = userType;
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
