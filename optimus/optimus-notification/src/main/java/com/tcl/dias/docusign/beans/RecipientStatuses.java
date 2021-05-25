
package com.tcl.dias.docusign.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * 
 * This file contains the RecipientStatuses.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "RecipientStatus"
})
public class RecipientStatuses {

    @JsonProperty("RecipientStatus")
    private Object recipientStatus;

    @JsonProperty("RecipientStatus")
    public Object getRecipientStatus() {
        return recipientStatus;
    }

    @JsonProperty("RecipientStatus")
    public void setRecipientStatus(Object recipientStatus) {
        this.recipientStatus = recipientStatus;
    }

}
