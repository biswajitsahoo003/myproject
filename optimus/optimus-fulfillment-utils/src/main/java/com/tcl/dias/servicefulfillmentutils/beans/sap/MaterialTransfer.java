package com.tcl.dias.servicefulfillmentutils.beans.sap;



import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
	"Optimus_Id",
    "Document_Date",
    "Posting_Date",
    "Plant",
    "Storage_Location",
    "Unit_of_Entry",
    "WBS_Element",
    "Recvg_Storage_Location",
    "Material_Code",
    "Quantity",
    "Recvg_WBS_Element",
    "Serial_Number"
})
public class MaterialTransfer {
	
	@JsonProperty("Optimus_Id")
	private String optimusId;

    @JsonProperty("Document_Date")
    private String documentDate;
    @JsonProperty("Posting_Date")
    private String postingDate;
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
    @JsonProperty("Material_Code")
    private String materialCode;
    @JsonProperty("Quantity")
    private String quantity;
    @JsonProperty("Recvg_WBS_Element")
    private String recvgWBSElement;
    @JsonProperty("Serial_Number")
    private String serialNumber;
    @JsonProperty("Document_No")
    private String documentNo;

    @JsonProperty("Document_Date")
    public String getDocumentDate() {
        return documentDate;
    }

    @JsonProperty("Document_Date")
    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }

    @JsonProperty("Posting_Date")
    public String getPostingDate() {
        return postingDate;
    }

    @JsonProperty("Posting_Date")
    public void setPostingDate(String postingDate) {
        this.postingDate = postingDate;
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

    @JsonProperty("Material_Code")
    public String getMaterialCode() {
        return materialCode;
    }

    @JsonProperty("Material_Code")
    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
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

    @JsonProperty("Serial_Number")
    public String getSerialNumber() {
        return serialNumber;
    }

    @JsonProperty("Serial_Number")
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @JsonProperty("Optimus_Id")
	public String getOptimusId() {
		return optimusId;
	}
    @JsonProperty("Optimus_Id")
	public void setOptimusId(String optimusId) {
		this.optimusId = optimusId;
	}
    

}
