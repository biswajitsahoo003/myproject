
package com.tcl.dias.adobesign.api.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "completionEmails",
    "inFlightEmails",
    "initEmails"
})
public class SendOptions {

    @JsonProperty("completionEmails")
    private String completionEmails;
    @JsonProperty("inFlightEmails")
    private String inFlightEmails;
    @JsonProperty("initEmails")
    private String initEmails;

    @JsonProperty("completionEmails")
    public String getCompletionEmails() {
        return completionEmails;
    }

    @JsonProperty("completionEmails")
    public void setCompletionEmails(String completionEmails) {
        this.completionEmails = completionEmails;
    }

    @JsonProperty("inFlightEmails")
    public String getInFlightEmails() {
        return inFlightEmails;
    }

    @JsonProperty("inFlightEmails")
    public void setInFlightEmails(String inFlightEmails) {
        this.inFlightEmails = inFlightEmails;
    }

    @JsonProperty("initEmails")
    public String getInitEmails() {
        return initEmails;
    }

    @JsonProperty("initEmails")
    public void setInitEmails(String initEmails) {
        this.initEmails = initEmails;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("completionEmails", completionEmails).append("inFlightEmails", inFlightEmails).append("initEmails", initEmails).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(inFlightEmails).append(completionEmails).append(initEmails).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SendOptions) == false) {
            return false;
        }
        SendOptions rhs = ((SendOptions) other);
        return new EqualsBuilder().append(inFlightEmails, rhs.inFlightEmails).append(completionEmails, rhs.completionEmails).append(initEmails, rhs.initEmails).isEquals();
    }

}
