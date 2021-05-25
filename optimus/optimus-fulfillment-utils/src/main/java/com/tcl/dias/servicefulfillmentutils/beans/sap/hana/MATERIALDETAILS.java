
package com.tcl.dias.servicefulfillmentutils.beans.sap.hana;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "MATERIAL_CODE",
    "Plant",
    "Storage_Location",
    "Unit_of_Entry",
    "WBS_Element",
    "Recvg_Storage_Location",
    "Quantity",
    "Recvg_WBS_Element",
    "Serial_No",
    "From_Valuation_type",
    "Receiving_Valuation_type"
})
public class MATERIALDETAILS {

    @JsonProperty("MATERIAL_CODE")
    private String mATERIALCODE;
    @JsonProperty("Plant")
    private String plant;
    @JsonProperty("Storage_Location")
    private String storageLocation;
    @JsonProperty("Unit_of_Entry")
    private String unitOfEntry;
    @JsonProperty("WBS_Element")
    private String wBSElement;
    @JsonProperty("Recvg_Storage_Location")
    private String recvgStorageLocation;
    @JsonProperty("Quantity")
    private String quantity;
    @JsonProperty("Recvg_WBS_Element")
    private String recvgWBSElement;
    @JsonProperty("Serial_No")
    private SerialNo serialNo;
    @JsonProperty("From_Valuation_type")
    private String fromValuationType;
    @JsonProperty("Receiving_Valuation_type")
    private String receivingValuationType;

    @JsonProperty("MATERIAL_CODE")
    public String getMATERIALCODE() {
        return mATERIALCODE;
    }

    @JsonProperty("MATERIAL_CODE")
    public void setMATERIALCODE(String mATERIALCODE) {
        this.mATERIALCODE = mATERIALCODE;
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

    @JsonProperty("Unit_of_Entry")
    public String getUnitOfEntry() {
        return unitOfEntry;
    }

    @JsonProperty("Unit_of_Entry")
    public void setUnitOfEntry(String unitOfEntry) {
        this.unitOfEntry = unitOfEntry;
    }

    @JsonProperty("WBS_Element")
    public String getWBSElement() {
        return wBSElement;
    }

    @JsonProperty("WBS_Element")
    public void setWBSElement(String wBSElement) {
        this.wBSElement = wBSElement;
    }

    @JsonProperty("Recvg_Storage_Location")
    public String getRecvgStorageLocation() {
        return recvgStorageLocation;
    }

    @JsonProperty("Recvg_Storage_Location")
    public void setRecvgStorageLocation(String recvgStorageLocation) {
        this.recvgStorageLocation = recvgStorageLocation;
    }

    @JsonProperty("Quantity")
    public String getQuantity() {
        return quantity;
    }

    @JsonProperty("Quantity")
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("Recvg_WBS_Element")
    public String getRecvgWBSElement() {
        return recvgWBSElement;
    }

    @JsonProperty("Recvg_WBS_Element")
    public void setRecvgWBSElement(String recvgWBSElement) {
        this.recvgWBSElement = recvgWBSElement;
    }

    @JsonProperty("Serial_No")
    public SerialNo getSerialNo() {
        return serialNo;
    }

    @JsonProperty("Serial_No")
    public void setSerialNo(SerialNo serialNo) {
        this.serialNo = serialNo;
    }

    @JsonProperty("From_Valuation_type")
    public String getFromValuationType() {
        return fromValuationType;
    }

    @JsonProperty("From_Valuation_type")
    public void setFromValuationType(String fromValuationType) {
        this.fromValuationType = fromValuationType;
    }

    @JsonProperty("Receiving_Valuation_type")
    public String getReceivingValuationType() {
        return receivingValuationType;
    }

    @JsonProperty("Receiving_Valuation_type")
    public void setReceivingValuationType(String receivingValuationType) {
        this.receivingValuationType = receivingValuationType;
    }

}
