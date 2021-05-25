
package com.tcl.dias.docusign.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * 
 * This file contains the DeclineReason.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "@nil"
})
public class DeclineReason {

    @JsonProperty("@nil")
    private String nil;

    @JsonProperty("@nil")
    public String getNil() {
        return nil;
    }

    @JsonProperty("@nil")
    public void setNil(String nil) {
        this.nil = nil;
    }

}
