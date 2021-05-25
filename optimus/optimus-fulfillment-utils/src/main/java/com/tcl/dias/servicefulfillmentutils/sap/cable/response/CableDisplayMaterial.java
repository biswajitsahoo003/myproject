
package com.tcl.dias.servicefulfillmentutils.sap.cable.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "BM_Bundle",
    "Material_Code",
    "Category_of_Inventory",
    "Material_Description",
    "Material_Group",
    "Plant",
    "Plant_Name",
    "Address_Line1",
    "Address_Line2",
    "City",
    "State",
    "Country",
    "Zipcode",
    "Storage_Location",
    "Storage_Location_Description",
    "Unit_of_Measure",
    "Quantity_Available",
    "WBS_Number",
    "Item_Category",
    "Status",
    "Remark"
})
public class CableDisplayMaterial {

    @JsonProperty("BM_Bundle")
    private String bMBundle;
    @JsonProperty("Material_Code")
    private String materialCode;
    @JsonProperty("Category_of_Inventory")
    private String categoryOfInventory;
    @JsonProperty("Material_Description")
    private String materialDescription;
    @JsonProperty("Material_Group")
    private String materialGroup;
    @JsonProperty("Plant")
    private String plant;
    @JsonProperty("Plant_Name")
    private String plantName;
    @JsonProperty("Address_Line1")
    private String addressLine1;
    @JsonProperty("Address_Line2")
    private String addressLine2;
    @JsonProperty("City")
    private String city;
    @JsonProperty("State")
    private String state;
    @JsonProperty("Country")
    private String country;
    @JsonProperty("Zipcode")
    private String zipcode;
    @JsonProperty("Storage_Location")
    private String storageLocation;
    @JsonProperty("Storage_Location_Description")
    private String storageLocationDescription;
    @JsonProperty("Unit_of_Measure")
    private String unitOfMeasure;
    @JsonProperty("Quantity_Available")
    private String quantityAvailable;
    @JsonProperty("WBS_Number")
    private String wBSNumber;
    @JsonProperty("Item_Category")
    private String itemCategory;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Remark")
    private String remark;

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

    @JsonProperty("Category_of_Inventory")
    public String getCategoryOfInventory() {
        return categoryOfInventory;
    }

    @JsonProperty("Category_of_Inventory")
    public void setCategoryOfInventory(String categoryOfInventory) {
        this.categoryOfInventory = categoryOfInventory;
    }

    @JsonProperty("Material_Description")
    public String getMaterialDescription() {
        return materialDescription;
    }

    @JsonProperty("Material_Description")
    public void setMaterialDescription(String materialDescription) {
        this.materialDescription = materialDescription;
    }

    @JsonProperty("Material_Group")
    public String getMaterialGroup() {
        return materialGroup;
    }

    @JsonProperty("Material_Group")
    public void setMaterialGroup(String materialGroup) {
        this.materialGroup = materialGroup;
    }

    @JsonProperty("Plant")
    public String getPlant() {
        return plant;
    }

    @JsonProperty("Plant")
    public void setPlant(String plant) {
        this.plant = plant;
    }

    @JsonProperty("Plant_Name")
    public String getPlantName() {
        return plantName;
    }

    @JsonProperty("Plant_Name")
    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    @JsonProperty("Address_Line1")
    public String getAddressLine1() {
        return addressLine1;
    }

    @JsonProperty("Address_Line1")
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    @JsonProperty("Address_Line2")
    public String getAddressLine2() {
        return addressLine2;
    }

    @JsonProperty("Address_Line2")
    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    @JsonProperty("City")
    public String getCity() {
        return city;
    }

    @JsonProperty("City")
    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("State")
    public String getState() {
        return state;
    }

    @JsonProperty("State")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("Country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("Country")
    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("Zipcode")
    public String getZipcode() {
        return zipcode;
    }

    @JsonProperty("Zipcode")
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @JsonProperty("Storage_Location")
    public String getStorageLocation() {
        return storageLocation;
    }

    @JsonProperty("Storage_Location")
    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    @JsonProperty("Storage_Location_Description")
    public String getStorageLocationDescription() {
        return storageLocationDescription;
    }

    @JsonProperty("Storage_Location_Description")
    public void setStorageLocationDescription(String storageLocationDescription) {
        this.storageLocationDescription = storageLocationDescription;
    }

    @JsonProperty("Unit_of_Measure")
    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    @JsonProperty("Unit_of_Measure")
    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    @JsonProperty("Quantity_Available")
    public String getQuantityAvailable() {
        return quantityAvailable;
    }

    @JsonProperty("Quantity_Available")
    public void setQuantityAvailable(String quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    @JsonProperty("WBS_Number")
    public String getWBSNumber() {
        return wBSNumber;
    }

    @JsonProperty("WBS_Number")
    public void setWBSNumber(String wBSNumber) {
        this.wBSNumber = wBSNumber;
    }

    @JsonProperty("Item_Category")
    public String getItemCategory() {
        return itemCategory;
    }

    @JsonProperty("Item_Category")
    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("Status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("Remark")
    public String getRemark() {
        return remark;
    }

    @JsonProperty("Remark")
    public void setRemark(String remark) {
        this.remark = remark;
    }

}
