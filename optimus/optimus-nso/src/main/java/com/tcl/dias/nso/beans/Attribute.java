
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
"name",
"value",
"attributeMasterId"
})
public class Attribute {

@JsonProperty("name")
private String name;
@JsonProperty("value")
private String value;
@JsonProperty("attributeMasterId")
private Integer attributeMasterId;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* No args constructor for use in serialization
*
*/
public Attribute() {
}

/**
*
* @param name
* @param value
* @param attributeMasterId
*/
public Attribute(String name, String value, Integer attributeMasterId) {
super();
this.name = name;
this.value = value;
this.attributeMasterId = attributeMasterId;
}

@JsonProperty("name")
public String getName() {
return name;
}

@JsonProperty("name")
public void setName(String name) {
this.name = name;
}

@JsonProperty("value")
public String getValue() {
return value;
}

@JsonProperty("value")
public void setValue(String value) {
this.value = value;
}

@JsonProperty("attributeMasterId")
public Integer getAttributeMasterId() {
return attributeMasterId;
}

@JsonProperty("attributeMasterId")
public void setAttributeMasterId(Integer attributeMasterId) {
this.attributeMasterId = attributeMasterId;
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
return new ToStringBuilder(this).append("name", name).append("value", value).append("attributeMasterId", attributeMasterId).append("additionalProperties", additionalProperties).toString();
}

}