
package com.tcl.dias.docusign.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * 
 * This file contains the DocumentStatuses.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "DocumentStatus"
})
public class DocumentStatuses {

    @JsonProperty("DocumentStatus")
    private DocumentStatus documentStatus;

    @JsonProperty("DocumentStatus")
    public DocumentStatus getDocumentStatus() {
        return documentStatus;
    }

    @JsonProperty("DocumentStatus")
    public void setDocumentStatus(DocumentStatus documentStatus) {
        this.documentStatus = documentStatus;
    }

}
