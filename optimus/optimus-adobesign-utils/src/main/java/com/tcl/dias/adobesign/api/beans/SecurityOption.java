
package com.tcl.dias.adobesign.api.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "authenticationMethod",
    "password",
    "phoneInfo"
})
public class SecurityOption {

    @JsonProperty("authenticationMethod")
    private String authenticationMethod;
    @JsonProperty("password")
    private String password;
    @JsonProperty("phoneInfo")
    private PhoneInfo phoneInfo;

    @JsonProperty("authenticationMethod")
    public String getAuthenticationMethod() {
        return authenticationMethod;
    }

    @JsonProperty("authenticationMethod")
    public void setAuthenticationMethod(String authenticationMethod) {
        this.authenticationMethod = authenticationMethod;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("phoneInfo")
    public PhoneInfo getPhoneInfo() {
        return phoneInfo;
    }

    @JsonProperty("phoneInfo")
    public void setPhoneInfo(PhoneInfo phoneInfo) {
        this.phoneInfo = phoneInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("authenticationMethod", authenticationMethod).append("password", password).append("phoneInfo", phoneInfo).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(phoneInfo).append(password).append(authenticationMethod).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SecurityOption) == false) {
            return false;
        }
        SecurityOption rhs = ((SecurityOption) other);
        return new EqualsBuilder().append(phoneInfo, rhs.phoneInfo).append(password, rhs.password).append(authenticationMethod, rhs.authenticationMethod).isEquals();
    }

}
