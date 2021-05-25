package com.tcl.dias.servicefulfillmentutils.beans.sap;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InventoryDetailResponseBean {

    @JsonProperty("BM_Bundled")
    private String bMBundled;
    @JsonProperty("Category_of_Inventory")
    private String categoryOfInventory;
    @JsonProperty("Data_of_order")
    private String dataOfOrder;
    @JsonProperty("Expected_Time_To_Deliver")
    private String expectedTimeToDeliver;
    @JsonProperty("Location_of_vendor_warehouse")
    private String locationOfVendorWarehouse;
    @JsonProperty("Material_code")
    private String materialCode;
    @JsonProperty("Material_description")
    private String materialDescription;
    @JsonProperty("Material_Group")
    private String materialGroup;
    @JsonProperty("Optimus_Reference_Number")
    private String optimusReferenceNumber;
    @JsonProperty("PO_Number")
    private String pONumber;
    @JsonProperty("Part_Codes_Serial_number")
    private String partCodesSerialNumber;
    @JsonProperty("Purchase_Group")
    private String purchaseGroup;
    @JsonProperty("Quantity_available")
    private String quantityAvailable;
    @JsonProperty("Quantity_Blocked")
    private String quantityBlocked;
    @JsonProperty("Serial_Number")
    private String serialNumber;
    @JsonProperty("Storage_location")
    private Long storageLocation;
    @JsonProperty("Total_Quantity")
    private String totalQuantity;
    @JsonProperty("Unit_of_Measure")
    private String unitOfMeasure;
    @JsonProperty("Vendor_Name")
    private String vendorName;
    @JsonProperty("WBS_Number")
    private String wBSNumber;

    public String getBMBundled() {
        return bMBundled;
    }

    public void setBMBundled(String bMBundled) {
        this.bMBundled = bMBundled;
    }

    public String getCategoryOfInventory() {
        return categoryOfInventory;
    }

    public void setCategoryOfInventory(String categoryOfInventory) {
        this.categoryOfInventory = categoryOfInventory;
    }

    public String getDataOfOrder() {
        return dataOfOrder;
    }

    public void setDataOfOrder(String dataOfOrder) {
        this.dataOfOrder = dataOfOrder;
    }

    public String getExpectedTimeToDeliver() {
        return expectedTimeToDeliver;
    }

    public void setExpectedTimeToDeliver(String expectedTimeToDeliver) {
        this.expectedTimeToDeliver = expectedTimeToDeliver;
    }

    public String getLocationOfVendorWarehouse() {
        return locationOfVendorWarehouse;
    }

    public void setLocationOfVendorWarehouse(String locationOfVendorWarehouse) {
        this.locationOfVendorWarehouse = locationOfVendorWarehouse;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialDescription() {
        return materialDescription;
    }

    public void setMaterialDescription(String materialDescription) {
        this.materialDescription = materialDescription;
    }

    public String getMaterialGroup() {
        return materialGroup;
    }

    public void setMaterialGroup(String materialGroup) {
        this.materialGroup = materialGroup;
    }

    public String getOptimusReferenceNumber() {
        return optimusReferenceNumber;
    }

    public void setOptimusReferenceNumber(String optimusReferenceNumber) {
        this.optimusReferenceNumber = optimusReferenceNumber;
    }

    public String getPONumber() {
        return pONumber;
    }

    public void setPONumber(String pONumber) {
        this.pONumber = pONumber;
    }

    public String getPartCodesSerialNumber() {
        return partCodesSerialNumber;
    }

    public void setPartCodesSerialNumber(String partCodesSerialNumber) {
        this.partCodesSerialNumber = partCodesSerialNumber;
    }

    public String getPurchaseGroup() {
        return purchaseGroup;
    }

    public void setPurchaseGroup(String purchaseGroup) {
        this.purchaseGroup = purchaseGroup;
    }

    public String getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(String quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public String getQuantityBlocked() {
        return quantityBlocked;
    }

    public void setQuantityBlocked(String quantityBlocked) {
        this.quantityBlocked = quantityBlocked;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Long getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(Long storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getWBSNumber() {
        return wBSNumber;
    }

    public void setWBSNumber(String wBSNumber) {
        this.wBSNumber = wBSNumber;
    }

}
