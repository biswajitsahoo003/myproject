
package com.tcl.dias.docusign.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * 
 * This file contains the CustomFields.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "CustomField"
})
public class CustomFields {

    @JsonProperty("CustomField")
    private List<CustomField> customField = null;

    @JsonProperty("CustomField")
    public List<CustomField> getCustomField() {
        return customField;
    }

    @JsonProperty("CustomField")
    public void setCustomField(List<CustomField> customField) {
        this.customField = customField;
    }

}
