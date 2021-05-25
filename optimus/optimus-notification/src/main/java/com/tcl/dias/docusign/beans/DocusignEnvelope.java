
package com.tcl.dias.docusign.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * 
 * This file contains the DocusignEnvelope.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "EnvelopeStatus"
})
public class DocusignEnvelope {

    @JsonProperty("EnvelopeStatus")
    private EnvelopeStatus envelopeStatus;

    @JsonProperty("EnvelopeStatus")
    public EnvelopeStatus getEnvelopeStatus() {
        return envelopeStatus;
    }

    @JsonProperty("EnvelopeStatus")
    public void setEnvelopeStatus(EnvelopeStatus envelopeStatus) {
        this.envelopeStatus = envelopeStatus;
    }

}
