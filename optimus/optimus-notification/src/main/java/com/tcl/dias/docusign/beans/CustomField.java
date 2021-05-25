
package com.tcl.dias.docusign.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * 
 * This file contains the CustomField.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "Name",
    "Show",
    "Required",
    "Value"
})
public class CustomField {

    @JsonProperty("Name")
    private String name;
    @JsonProperty("Show")
    private String show;
    @JsonProperty("Required")
    private String required;
    @JsonProperty("Value")
    private Object value;

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("Show")
    public String getShow() {
        return show;
    }

    @JsonProperty("Show")
    public void setShow(String show) {
        this.show = show;
    }

    @JsonProperty("Required")
    public String getRequired() {
        return required;
    }

    @JsonProperty("Required")
    public void setRequired(String required) {
        this.required = required;
    }

    @JsonProperty("Value")
    public Object getValue() {
        return value;
    }

    @JsonProperty("Value")
    public void setValue(Object value) {
        this.value = value;
    }

}
