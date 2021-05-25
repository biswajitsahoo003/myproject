
package com.tcl.dias.servicefulfillmentutils.beans.sap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "Circuit_Id", "Material_Code", "Quantity", "Plant_Name", "Storage_Location", "GRN_No", "GRN_Date",
		"PO_Number", "Item_Number", "Status", "Remark" })
public class GrnResponse {

	@JsonProperty("Circuit_Id")
	private String circuitId;
	@JsonProperty("Material_Code")
	private Integer materialCode;
	@JsonProperty("Quantity")
	private String quantity;
	@JsonProperty("Plant_Name")
	private String plantName;
	@JsonProperty("Storage_Location")
	private String storageLocation;
	@JsonProperty("GRN_No")
	private Integer gRNNo;
	@JsonProperty("GRN_Date")
	private Integer gRNDate;
	@JsonProperty("PO_Number")
	private Integer pONumber;
	@JsonProperty("Item_Number")
	private Integer itemNumber;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("Remark")
	private String remark;

	@JsonProperty("Circuit_Id")
	public String getCircuitId() {
		return circuitId;
	}

	@JsonProperty("Circuit_Id")
	public void setCircuitId(String circuitId) {
		this.circuitId = circuitId;
	}

	@JsonProperty("Material_Code")
	public Integer getMaterialCode() {
		return materialCode;
	}

	@JsonProperty("Material_Code")
	public void setMaterialCode(Integer materialCode) {
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

	@JsonProperty("Plant_Name")
	public String getPlantName() {
		return plantName;
	}

	@JsonProperty("Plant_Name")
	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	@JsonProperty("Storage_Location")
	public String getStorageLocation() {
		return storageLocation;
	}

	@JsonProperty("Storage_Location")
	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}

	@JsonProperty("GRN_No")
	public Integer getGRNNo() {
		return gRNNo;
	}

	@JsonProperty("GRN_No")
	public void setGRNNo(Integer gRNNo) {
		this.gRNNo = gRNNo;
	}

	@JsonProperty("GRN_Date")
	public Integer getGRNDate() {
		return gRNDate;
	}

	@JsonProperty("GRN_Date")
	public void setGRNDate(Integer gRNDate) {
		this.gRNDate = gRNDate;
	}

	@JsonProperty("PO_Number")
	public Integer getPONumber() {
		return pONumber;
	}

	@JsonProperty("PO_Number")
	public void setPONumber(Integer pONumber) {
		this.pONumber = pONumber;
	}

	@JsonProperty("Item_Number")
	public Integer getItemNumber() {
		return itemNumber;
	}

	@JsonProperty("Item_Number")
	public void setItemNumber(Integer itemNumber) {
		this.itemNumber = itemNumber;
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
