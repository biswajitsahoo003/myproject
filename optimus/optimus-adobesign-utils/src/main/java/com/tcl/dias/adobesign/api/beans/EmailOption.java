
package com.tcl.dias.adobesign.api.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "sendOptions"
})
public class EmailOption {

    @JsonProperty("sendOptions")
    private SendOptions sendOptions;

    @JsonProperty("sendOptions")
    public SendOptions getSendOptions() {
        return sendOptions;
    }

    @JsonProperty("sendOptions")
    public void setSendOptions(SendOptions sendOptions) {
        this.sendOptions = sendOptions;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("sendOptions", sendOptions).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(sendOptions).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof EmailOption) == false) {
            return false;
        }
        EmailOption rhs = ((EmailOption) other);
        return new EqualsBuilder().append(sendOptions, rhs.sendOptions).isEquals();
    }

}
