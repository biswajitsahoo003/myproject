
package com.tcl.dias.adobesign.api.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "openPassword"
})
public class SecurityOption_ {

    @JsonProperty("openPassword")
    private String openPassword;

    @JsonProperty("openPassword")
    public String getOpenPassword() {
        return openPassword;
    }

    @JsonProperty("openPassword")
    public void setOpenPassword(String openPassword) {
        this.openPassword = openPassword;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("openPassword", openPassword).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(openPassword).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SecurityOption_) == false) {
            return false;
        }
        SecurityOption_ rhs = ((SecurityOption_) other);
        return new EqualsBuilder().append(openPassword, rhs.openPassword).isEquals();
    }

}
