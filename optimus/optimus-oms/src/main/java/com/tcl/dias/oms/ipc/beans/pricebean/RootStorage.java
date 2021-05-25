
package com.tcl.dias.oms.ipc.beans.pricebean;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "size"
})
public class RootStorage {

    @JsonProperty("type")
    private RootStorage.Type type;
    @JsonProperty("size")
    private String size;

    @JsonProperty("type")
    public RootStorage.Type getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(RootStorage.Type type) {
        this.type = type;
    }

    @JsonProperty("size")
    public String getSize() {
        return size;
    }

    @JsonProperty("size")
    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("type", type).append("size", size).toString();
    }

    public enum Type {

        STORAGE_TYPE_SAS("SAS"), STORAGE_TYPE_SATA("SATA"), STORAGE_TYPE_SSD("SSD");
        private final String value;
        private final static Map<String, RootStorage.Type> CONSTANTS = new HashMap<String, RootStorage.Type>();

        static {
            for (RootStorage.Type c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static RootStorage.Type fromValue(String value) {
            RootStorage.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }
    }

}
