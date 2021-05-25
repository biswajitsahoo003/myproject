
package com.tcl.dias.nso.beans;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"locationId",
"userAddress",
"apiAddress",
"latLong"
})
public class LocationDetail {

@JsonProperty("locationId")
private Integer locationId;
@JsonProperty("userAddress")
private UserAddress userAddress;
@JsonProperty("apiAddress")
private ApiAddress apiAddress;
@JsonProperty("latLong")
private String latLong;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* No args constructor for use in serialization
*
*/
public LocationDetail() {
}

/**
*
* @param userAddress
* @param locationId
* @param latLong
* @param apiAddress
*/
public LocationDetail(Integer locationId, UserAddress userAddress, ApiAddress apiAddress, String latLong) {
super();
this.locationId = locationId;
this.userAddress = userAddress;
this.apiAddress = apiAddress;
this.latLong = latLong;
}

@JsonProperty("locationId")
public Integer getLocationId() {
return locationId;
}

@JsonProperty("locationId")
public void setLocationId(Integer locationId) {
this.locationId = locationId;
}

@JsonProperty("userAddress")
public UserAddress getUserAddress() {
return userAddress;
}

@JsonProperty("userAddress")
public void setUserAddress(UserAddress userAddress) {
this.userAddress = userAddress;
}

@JsonProperty("apiAddress")
public ApiAddress getApiAddress() {
return apiAddress;
}

@JsonProperty("apiAddress")
public void setApiAddress(ApiAddress apiAddress) {
this.apiAddress = apiAddress;
}

@JsonProperty("latLong")
public String getLatLong() {
return latLong;
}

@JsonProperty("latLong")
public void setLatLong(String latLong) {
this.latLong = latLong;
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
return new ToStringBuilder(this).append("locationId", locationId).append("userAddress", userAddress).append("apiAddress", apiAddress).append("latLong", latLong).append("additionalProperties", additionalProperties).toString();
}

}