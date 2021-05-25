
package com.tcl.dias.adobesign.api.beans;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "email",
    "label",
    "visiblePages"
})
public class Cc {

    @JsonProperty("email")
    private String email;
    @JsonProperty("label")
    private String label;
    @JsonProperty("visiblePages")
    private List<String> visiblePages = null;

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    @JsonProperty("label")
    public void setLabel(String label) {
        this.label = label;
    }

    @JsonProperty("visiblePages")
    public List<String> getVisiblePages() {
        return visiblePages;
    }

    @JsonProperty("visiblePages")
    public void setVisiblePages(List<String> visiblePages) {
        this.visiblePages = visiblePages;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("email", email).append("label", label).append("visiblePages", visiblePages).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(label).append(visiblePages).append(email).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Cc) == false) {
            return false;
        }
        Cc rhs = ((Cc) other);
        return new EqualsBuilder().append(label, rhs.label).append(visiblePages, rhs.visiblePages).append(email, rhs.email).isEquals();
    }

}
