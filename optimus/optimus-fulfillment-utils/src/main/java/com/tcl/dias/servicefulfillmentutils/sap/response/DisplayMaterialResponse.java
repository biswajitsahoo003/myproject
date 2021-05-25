package com.tcl.dias.servicefulfillmentutils.sap.response;




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
    "Plant Name",
    "Plant_Description",
    "Address_Line1",
    "Address_Line2",
    "City",
    "State",
    "Country",
    "Zipcode",
    "Storage_Location",
    "Storage_Location_Description",
    "WBS_Number",
    "Item_Category",
    "Part_Codes",
    "SAP_Serial_Number",
    "Vendor_Number",
    "Vendor_Name",
    "PO_Number",
    "Date_of_Order",
    "Lead_Time_to_Deliver_PO_WH",
    "Lead_Time_to_Deliver_WH_CUST",
    "Quantity_Available",
    "Unit_of_Measure",
    "Purchase_Group",
    "Status",
    "Remark"
})
public class DisplayMaterialResponse {

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
    private String zipCode;
    @JsonProperty("Plant_Description")
    private String plantDescription;
    @JsonProperty("Storage_Location")
    private String storageLocation;
    @JsonProperty("Storage_Location_Description")
    private String storageLocationDescription;
    @JsonProperty("WBS_Number")
    private String wBSNumber;
    @JsonProperty("Item_Category")
    private String itemCategory;
    @JsonProperty("Part_Codes")
    private String partCodes;
    @JsonProperty("SAP_Serial_Number")
    private String sAPSerialNumber;
    @JsonProperty("Vendor_Number")
    private String vendorNumber;
    @JsonProperty("Vendor_Name")
    private String vendorName;
    @JsonProperty("PO_Number")
    private String pONumber;
    @JsonProperty("Date_of_Order")
    private String dateOfOrder;
    @JsonProperty("Lead_Time_to_Deliver_PO_WH")
    private String leadTimeToDeliverPOWH;
    @JsonProperty("Lead_Time_to_Deliver_WH_CUST")
    private String leadTimeToDeliverWHCUST;
    @JsonProperty("Quantity_Available")
    private String quantityAvailable;
    @JsonProperty("Unit_of_Measure")
    private String unitOfMeasure;
    @JsonProperty("Purchase_Group")
    private String purchaseGroup;
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

    @JsonProperty("Plant_Description")
    public String getPlantDescription() {
        return plantDescription;
    }

    @JsonProperty("Plant_Description")
    public void setPlantDescription(String plantDescription) {
        this.plantDescription = plantDescription;
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

    @JsonProperty("Part_Codes")
    public String getPartCodes() {
        return partCodes;
    }

    @JsonProperty("Part_Codes")
    public void setPartCodes(String partCodes) {
        this.partCodes = partCodes;
    }

    @JsonProperty("SAP_Serial_Number")
    public String getSAPSerialNumber() {
        return sAPSerialNumber;
    }

    @JsonProperty("SAP_Serial_Number")
    public void setSAPSerialNumber(String sAPSerialNumber) {
        this.sAPSerialNumber = sAPSerialNumber;
    }

    @JsonProperty("Vendor_Number")
    public String getVendorNumber() {
        return vendorNumber;
    }

    @JsonProperty("Vendor_Number")
    public void setVendorNumber(String vendorNumber) {
        this.vendorNumber = vendorNumber;
    }

    @JsonProperty("Vendor_Name")
    public String getVendorName() {
        return vendorName;
    }

    @JsonProperty("Vendor_Name")
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    @JsonProperty("PO_Number")
    public String getPONumber() {
        return pONumber;
    }

    @JsonProperty("PO_Number")
    public void setPONumber(String pONumber) {
        this.pONumber = pONumber;
    }

    @JsonProperty("Date_of_Order")
    public String getDateOfOrder() {
        return dateOfOrder;
    }

    @JsonProperty("Date_of_Order")
    public void setDateOfOrder(String dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }

    @JsonProperty("Lead_Time_to_Deliver_PO_WH")
    public String getLeadTimeToDeliverPOWH() {
        return leadTimeToDeliverPOWH;
    }

    @JsonProperty("Lead_Time_to_Deliver_PO_WH")
    public void setLeadTimeToDeliverPOWH(String leadTimeToDeliverPOWH) {
        this.leadTimeToDeliverPOWH = leadTimeToDeliverPOWH;
    }

    @JsonProperty("Lead_Time_to_Deliver_WH_CUST")
    public String getLeadTimeToDeliverWHCUST() {
        return leadTimeToDeliverWHCUST;
    }

    @JsonProperty("Lead_Time_to_Deliver_WH_CUST")
    public void setLeadTimeToDeliverWHCUST(String leadTimeToDeliverWHCUST) {
        this.leadTimeToDeliverWHCUST = leadTimeToDeliverWHCUST;
    }

    @JsonProperty("Quantity_Available")
    public String getQuantityAvailable() {
        return quantityAvailable;
    }

    @JsonProperty("Quantity_Available")
    public void setQuantityAvailable(String quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    @JsonProperty("Unit_of_Measure")
    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    @JsonProperty("Unit_of_Measure")
    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    @JsonProperty("Purchase_Group")
    public String getPurchaseGroup() {
        return purchaseGroup;
    }

    @JsonProperty("Purchase_Group")
    public void setPurchaseGroup(String purchaseGroup) {
        this.purchaseGroup = purchaseGroup;
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
    public String getZipCode() {
        return zipCode;
    }

    @JsonProperty("Zipcode")
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

  
 
}
