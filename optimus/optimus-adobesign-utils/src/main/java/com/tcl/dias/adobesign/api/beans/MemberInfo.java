
package com.tcl.dias.adobesign.api.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "email",
    "id",
    "securityOption"
})
public class MemberInfo {

    @JsonProperty("email")
    private String email;
    @JsonProperty("id")
    private String id;
    @JsonProperty("securityOption")
    private SecurityOption securityOption;

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("securityOption")
    public SecurityOption getSecurityOption() {
        return securityOption;
    }

    @JsonProperty("securityOption")
    public void setSecurityOption(SecurityOption securityOption) {
        this.securityOption = securityOption;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("email", email).append("id", id).append("securityOption", securityOption).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(email).append(securityOption).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof MemberInfo) == false) {
            return false;
        }
        MemberInfo rhs = ((MemberInfo) other);
        return new EqualsBuilder().append(id, rhs.id).append(email, rhs.email).append(securityOption, rhs.securityOption).isEquals();
    }

}
