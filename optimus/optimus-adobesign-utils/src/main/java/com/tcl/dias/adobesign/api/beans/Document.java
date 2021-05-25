
package com.tcl.dias.adobesign.api.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "createdDate",
    "id",
    "label",
    "numPages",
    "mimeType",
    "name"
})
public class Document {

    @JsonProperty("createdDate")
    private String createdDate;
    @JsonProperty("id")
    private String id;
    @JsonProperty("label")
    private String label;
    @JsonProperty("numPages")
    private Integer numPages;
    @JsonProperty("mimeType")
    private String mimeType;
    @JsonProperty("name")
    private String name;

    @JsonProperty("createdDate")
    public String getCreatedDate() {
        return createdDate;
    }

    @JsonProperty("createdDate")
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    @JsonProperty("label")
    public void setLabel(String label) {
        this.label = label;
    }

    @JsonProperty("numPages")
    public Integer getNumPages() {
        return numPages;
    }

    @JsonProperty("numPages")
    public void setNumPages(Integer numPages) {
        this.numPages = numPages;
    }

    @JsonProperty("mimeType")
    public String getMimeType() {
        return mimeType;
    }

    @JsonProperty("mimeType")
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("createdDate", createdDate).append("id", id).append("label", label).append("numPages", numPages).append("mimeType", mimeType).append("name", name).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(numPages).append(createdDate).append(name).append(id).append(label).append(mimeType).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Document) == false) {
            return false;
        }
        Document rhs = ((Document) other);
        return new EqualsBuilder().append(numPages, rhs.numPages).append(createdDate, rhs.createdDate).append(name, rhs.name).append(id, rhs.id).append(label, rhs.label).append(mimeType, rhs.mimeType).isEquals();
    }

}
