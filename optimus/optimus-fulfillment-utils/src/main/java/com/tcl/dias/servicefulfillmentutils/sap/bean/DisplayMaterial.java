
package com.tcl.dias.servicefulfillmentutils.sap.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "BM_Bundle",
    "Material_Code",
    "WBS_Number",
    "Plant",
    "Storage_Location"
})
public class DisplayMaterial {

    @JsonProperty("BM_Bundle")
    private String bMBundle;
    @JsonProperty("Material_Code")
    private String materialCode;
    @JsonProperty("WBS_Number")
    private String wBSNumber;
    @JsonProperty("Plant")
    private String plant;
    @JsonProperty("Storage_Location")
    private String storageLocation;

    @JsonProperty("BM_Bundle")
    public String getBMBundle() {
        return bMBundle;
    }

    @JsonProperty("BM_Bundle")
    public void setBMBundle(String bMBundle) {
        this.bMBundle = bMBundle;
    }

    @JsonProperty("Material_Code")
    public String getMaterialCode() {
        return materialCode;
    }

    @JsonProperty("Material_Code")
    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    @JsonProperty("WBS_Number")
    public String getWBSNumber() {
        return wBSNumber;
    }

    @JsonProperty("WBS_Number")
    public void setWBSNumber(String wBSNumber) {
        this.wBSNumber = wBSNumber;
    }

    @JsonProperty("Plant")
    public String getPlant() {
        return plant;
    }

    @JsonProperty("Plant")
    public void setPlant(String plant) {
        this.plant = plant;
    }

    @JsonProperty("Storage_Location")
    public String getStorageLocation() {
        return storageLocation;
    }

    @JsonProperty("Storage_Location")
    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

}
