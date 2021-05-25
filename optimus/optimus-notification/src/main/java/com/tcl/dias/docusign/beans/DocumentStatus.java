
package com.tcl.dias.docusign.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * 
 * This file contains the DocumentStatus.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "ID",
    "Name",
    "TemplateName",
    "Sequence"
})
public class DocumentStatus {

    @JsonProperty("ID")
    private String iD;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("TemplateName")
    private Object templateName;
    @JsonProperty("Sequence")
    private String sequence;

    @JsonProperty("ID")
    public String getID() {
        return iD;
    }

    @JsonProperty("ID")
    public void setID(String iD) {
        this.iD = iD;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("TemplateName")
    public Object getTemplateName() {
        return templateName;
    }

    @JsonProperty("TemplateName")
    public void setTemplateName(Object templateName) {
        this.templateName = templateName;
    }

    @JsonProperty("Sequence")
    public String getSequence() {
        return sequence;
    }

    @JsonProperty("Sequence")
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

}
