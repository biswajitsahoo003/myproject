
package com.tcl.dias.adobesign.api.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "defaultValue",
    "fieldName"
})
public class MergeFieldInfo {

    @JsonProperty("defaultValue")
    private String defaultValue;
    @JsonProperty("fieldName")
    private String fieldName;

    @JsonProperty("defaultValue")
    public String getDefaultValue() {
        return defaultValue;
    }

    @JsonProperty("defaultValue")
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @JsonProperty("fieldName")
    public String getFieldName() {
        return fieldName;
    }

    @JsonProperty("fieldName")
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("defaultValue", defaultValue).append("fieldName", fieldName).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(defaultValue).append(fieldName).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof MergeFieldInfo) == false) {
            return false;
        }
        MergeFieldInfo rhs = ((MergeFieldInfo) other);
        return new EqualsBuilder().append(defaultValue, rhs.defaultValue).append(fieldName, rhs.fieldName).isEquals();
    }

}
