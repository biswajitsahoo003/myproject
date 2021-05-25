
package com.tcl.dias.adobesign.api.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "mimeType",
    "name",
    "url"
})
public class UrlFileInfo_ {

    @JsonProperty("mimeType")
    private String mimeType;
    @JsonProperty("name")
    private String name;
    @JsonProperty("url")
    private String url;

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

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("mimeType", mimeType).append("name", name).append("url", url).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(mimeType).append(url).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof UrlFileInfo_) == false) {
            return false;
        }
        UrlFileInfo_ rhs = ((UrlFileInfo_) other);
        return new EqualsBuilder().append(name, rhs.name).append(mimeType, rhs.mimeType).append(url, rhs.url).isEquals();
    }

}
