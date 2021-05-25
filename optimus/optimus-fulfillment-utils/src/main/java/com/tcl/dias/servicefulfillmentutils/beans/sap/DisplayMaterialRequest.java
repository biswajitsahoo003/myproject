package com.tcl.dias.servicefulfillmentutils.beans.sap;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "BM_Bundle",
    "Material_Code",
    "Category_of_Inventory",
    "Purchase_Group",
    "Plant",
    "Storage_Location"
})
public class DisplayMaterialRequest {

    @JsonProperty("BM_Bundle")
    private String bMBundle;
    @JsonProperty("Material_Code")
    private String materialCode;
    @JsonProperty("Category_of_Inventory")
    private String categoryOfInventory;
    @JsonProperty("Purchase_Group")
    private String purchaseGroup;
    @JsonProperty("Plant")
    private String plant;
    @JsonProperty("Storage_Location")
    private String storageLocation;
    
    @JsonIgnore
    private String materialQuantity;

    public String getMaterialQuantity() {
		return materialQuantity;
	}

	public void setMaterialQuantity(String materialQuantity) {
		this.materialQuantity = materialQuantity;
	}

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

    @JsonProperty("Purchase_Group")
    public String getPurchaseGroup() {
        return purchaseGroup;
    }

    @JsonProperty("Purchase_Group")
    public void setPurchaseGroup(String purchaseGroup) {
        this.purchaseGroup = purchaseGroup;
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
