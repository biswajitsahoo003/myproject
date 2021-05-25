
package com.tcl.dias.adobesign.api.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "countryCode",
    "countryIsoCode",
    "phone"
})
public class PhoneInfo {

    @JsonProperty("countryCode")
    private String countryCode;
    @JsonProperty("countryIsoCode")
    private String countryIsoCode;
    @JsonProperty("phone")
    private String phone;

    @JsonProperty("countryCode")
    public String getCountryCode() {
        return countryCode;
    }

    @JsonProperty("countryCode")
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @JsonProperty("countryIsoCode")
    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    @JsonProperty("countryIsoCode")
    public void setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
    }

    @JsonProperty("phone")
    public String getPhone() {
        return phone;
    }

    @JsonProperty("phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("countryCode", countryCode).append("countryIsoCode", countryIsoCode).append("phone", phone).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(countryIsoCode).append(phone).append(countryCode).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PhoneInfo) == false) {
            return false;
        }
        PhoneInfo rhs = ((PhoneInfo) other);
        return new EqualsBuilder().append(countryIsoCode, rhs.countryIsoCode).append(phone, rhs.phone).append(countryCode, rhs.countryCode).isEquals();
    }

}
