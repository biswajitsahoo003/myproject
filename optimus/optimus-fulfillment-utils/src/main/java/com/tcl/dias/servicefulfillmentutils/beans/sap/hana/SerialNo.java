
package com.tcl.dias.servicefulfillmentutils.beans.sap.hana;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Serial_Number"
})
public class SerialNo {

    @JsonProperty("Serial_Number")
    private String serialNumber;

    @JsonProperty("Serial_Number")
    public String getSerialNumber() {
        return serialNumber;
    }

    @JsonProperty("Serial_Number")
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

}
