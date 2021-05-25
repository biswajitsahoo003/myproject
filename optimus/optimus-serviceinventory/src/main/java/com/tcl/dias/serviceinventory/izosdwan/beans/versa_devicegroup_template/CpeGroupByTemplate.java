
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_devicegroup_template;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "devicegroup-template-mapping"
})
public class CpeGroupByTemplate {

    @JsonProperty("devicegroup-template-mapping")
    private DevicegroupTemplateMapping devicegroupTemplateMapping;

    @JsonProperty("devicegroup-template-mapping")
    public DevicegroupTemplateMapping getDevicegroupTemplateMapping() {
        return devicegroupTemplateMapping;
    }

    @JsonProperty("devicegroup-template-mapping")
    public void setDevicegroupTemplateMapping(DevicegroupTemplateMapping devicegroupTemplateMapping) {
        this.devicegroupTemplateMapping = devicegroupTemplateMapping;
    }

}
