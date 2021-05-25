
package com.tcl.dias.adobesign.api.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "document",
    "label",
    "libraryDocumentId",
    "transientDocumentId",
    "urlFileInfo"
})
public class FormFieldLayerTemplate {

    @JsonProperty("document")
    private Document_ document;
    @JsonProperty("label")
    private String label;
    @JsonProperty("libraryDocumentId")
    private String libraryDocumentId;
    @JsonProperty("transientDocumentId")
    private String transientDocumentId;
    @JsonProperty("urlFileInfo")
    private UrlFileInfo_ urlFileInfo;

    @JsonProperty("document")
    public Document_ getDocument() {
        return document;
    }

    @JsonProperty("document")
    public void setDocument(Document_ document) {
        this.document = document;
    }

    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    @JsonProperty("label")
    public void setLabel(String label) {
        this.label = label;
    }

    @JsonProperty("libraryDocumentId")
    public String getLibraryDocumentId() {
        return libraryDocumentId;
    }

    @JsonProperty("libraryDocumentId")
    public void setLibraryDocumentId(String libraryDocumentId) {
        this.libraryDocumentId = libraryDocumentId;
    }

    @JsonProperty("transientDocumentId")
    public String getTransientDocumentId() {
        return transientDocumentId;
    }

    @JsonProperty("transientDocumentId")
    public void setTransientDocumentId(String transientDocumentId) {
        this.transientDocumentId = transientDocumentId;
    }

    @JsonProperty("urlFileInfo")
    public UrlFileInfo_ getUrlFileInfo() {
        return urlFileInfo;
    }

    @JsonProperty("urlFileInfo")
    public void setUrlFileInfo(UrlFileInfo_ urlFileInfo) {
        this.urlFileInfo = urlFileInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("document", document).append("label", label).append("libraryDocumentId", libraryDocumentId).append("transientDocumentId", transientDocumentId).append("urlFileInfo", urlFileInfo).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(libraryDocumentId).append(label).append(transientDocumentId).append(document).append(urlFileInfo).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FormFieldLayerTemplate) == false) {
            return false;
        }
        FormFieldLayerTemplate rhs = ((FormFieldLayerTemplate) other);
        return new EqualsBuilder().append(libraryDocumentId, rhs.libraryDocumentId).append(label, rhs.label).append(transientDocumentId, rhs.transientDocumentId).append(document, rhs.document).append(urlFileInfo, rhs.urlFileInfo).isEquals();
    }

}
