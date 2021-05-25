
package com.tcl.dias.nso.beans;

import java.util.HashMap;
import java.util.List;
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
"componentMasterId",
"attributes",
"type"
})
public class Component {

@JsonProperty("name")
private String name;
@JsonProperty("componentMasterId")
private Integer componentMasterId;
@JsonProperty("attributes")
private List<Attribute> attributes = null;
@JsonProperty("type")
private String type;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* No args constructor for use in serialization
*
*/
public Component() {
}

/**
*
* @param componentMasterId
* @param name
* @param attributes
* @param type
*/
public Component(String name, Integer componentMasterId, List<Attribute> attributes, String type) {
super();
this.name = name;
this.componentMasterId = componentMasterId;
this.attributes = attributes;
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

@JsonProperty("componentMasterId")
public Integer getComponentMasterId() {
return componentMasterId;
}

@JsonProperty("componentMasterId")
public void setComponentMasterId(Integer componentMasterId) {
this.componentMasterId = componentMasterId;
}

@JsonProperty("attributes")
public List<Attribute> getAttributes() {
return attributes;
}

@JsonProperty("attributes")
public void setAttributes(List<Attribute> attributes) {
this.attributes = attributes;
}

@JsonProperty("type")
public String getType() {
return type;
}

@JsonProperty("type")
public void setType(String type) {
this.type = type;
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
return new ToStringBuilder(this).append("name", name).append("componentMasterId", componentMasterId).append("attributes", attributes).append("type", type).append("additionalProperties", additionalProperties).toString();
}

}