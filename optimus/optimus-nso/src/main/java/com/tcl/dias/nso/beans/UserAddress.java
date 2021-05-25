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
"addressLineOne",
"city",
"country",
"locality",
"pincode",
"source",
"state",
"latLong",
"region"
})
public class UserAddress {

@JsonProperty("addressLineOne")
private String addressLineOne;
@JsonProperty("city")
private String city;
@JsonProperty("country")
private String country;
@JsonProperty("locality")
private String locality;
@JsonProperty("pincode")
private String pincode;
@JsonProperty("source")
private String source;
@JsonProperty("state")
private String state;
@JsonProperty("latLong")
private String latLong;
@JsonProperty("region")
private String region;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* No args constructor for use in serialization
*
*/
public UserAddress() {
}

/**
*
* @param country
* @param pincode
* @param city
* @param latLong
* @param addressLineOne
* @param locality
* @param source
* @param state
* @param region
*/
public UserAddress(String addressLineOne, String city, String country, String locality, String pincode, String source, String state, String latLong, String region) {
super();
this.addressLineOne = addressLineOne;
this.city = city;
this.country = country;
this.locality = locality;
this.pincode = pincode;
this.source = source;
this.state = state;
this.latLong = latLong;
this.region = region;
}

@JsonProperty("addressLineOne")
public String getAddressLineOne() {
return addressLineOne;
}

@JsonProperty("addressLineOne")
public void setAddressLineOne(String addressLineOne) {
this.addressLineOne = addressLineOne;
}

@JsonProperty("city")
public String getCity() {
return city;
}

@JsonProperty("city")
public void setCity(String city) {
this.city = city;
}

@JsonProperty("country")
public String getCountry() {
return country;
}

@JsonProperty("country")
public void setCountry(String country) {
this.country = country;
}

@JsonProperty("locality")
public String getLocality() {
return locality;
}

@JsonProperty("locality")
public void setLocality(String locality) {
this.locality = locality;
}

@JsonProperty("pincode")
public String getPincode() {
return pincode;
}

@JsonProperty("pincode")
public void setPincode(String pincode) {
this.pincode = pincode;
}

@JsonProperty("source")
public String getSource() {
return source;
}

@JsonProperty("source")
public void setSource(String source) {
this.source = source;
}

@JsonProperty("state")
public String getState() {
return state;
}

@JsonProperty("state")
public void setState(String state) {
this.state = state;
}

@JsonProperty("latLong")
public String getLatLong() {
return latLong;
}

@JsonProperty("latLong")
public void setLatLong(String latLong) {
this.latLong = latLong;
}

@JsonProperty("region")
public String getRegion() {
return region;
}

@JsonProperty("region")
public void setRegion(String region) {
this.region = region;
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
return new ToStringBuilder(this).append("addressLineOne", addressLineOne).append("city", city).append("country", country).append("locality", locality).append("pincode", pincode).append("source", source).append("state", state).append("latLong", latLong).append("region", region).append("additionalProperties", additionalProperties).toString();
}

}