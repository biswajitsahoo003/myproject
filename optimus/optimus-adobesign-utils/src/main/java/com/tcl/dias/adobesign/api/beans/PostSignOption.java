
package com.tcl.dias.adobesign.api.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "redirectDelay",
    "redirectUrl"
})
public class PostSignOption {

    @JsonProperty("redirectDelay")
    private Integer redirectDelay;
    @JsonProperty("redirectUrl")
    private String redirectUrl;

    @JsonProperty("redirectDelay")
    public Integer getRedirectDelay() {
        return redirectDelay;
    }

    @JsonProperty("redirectDelay")
    public void setRedirectDelay(Integer redirectDelay) {
        this.redirectDelay = redirectDelay;
    }

    @JsonProperty("redirectUrl")
    public String getRedirectUrl() {
        return redirectUrl;
    }

    @JsonProperty("redirectUrl")
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("redirectDelay", redirectDelay).append("redirectUrl", redirectUrl).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(redirectDelay).append(redirectUrl).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PostSignOption) == false) {
            return false;
        }
        PostSignOption rhs = ((PostSignOption) other);
        return new EqualsBuilder().append(redirectDelay, rhs.redirectDelay).append(redirectUrl, rhs.redirectUrl).isEquals();
    }

}
