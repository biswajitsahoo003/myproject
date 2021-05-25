
package com.tcl.dias.servicefulfillmentutils.beans.sap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 * Bean Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "Acct_Assignment_Category", "Item_Category", "Req_Tracking_Number", "Created_By", "Material_Code",
		"Short_Text", "Unit_of_Measurement", "Quantity", "Delivery_Date", "Currency", "Plant", "Storage_Location",
		"Requisitioner", "Purchasing_Organisation", "Valuation_Price", "WBS_Element", "GL_Account", "Cost_Center",
		"Item_Text", "Opportunity_Id", "Site_Id", "COF_Ref_No", "List_Price", "List_Price_Currency", "COPF_Id_1",
		"Service_Id", "Product", "Discount_Percentage", "Service_No", "Short_Text_1", "Quantity_1",
		"Unit_of_Measurement_1", "Currency_1", "Gross_Price", "Material_Group", "Demand_Id", "Name1", "Name2", "Name3",
		"Name4", "House_Num", "Street", "City", "State", "Country", "Postal_Code","Vendor_Id" })
public class PRLine {

	@JsonProperty("Acct_Assignment_Category")
	private String acctAssignmentCategory = "";
	@JsonProperty("Item_Category")
	private String itemCategory = "";
	@JsonProperty("Req_Tracking_Number")
	private String reqTrackingNumber = "";
	@JsonProperty("Created_By")
	private String createdBy = "";
	@JsonProperty("Material_Code")
	private String materialCode = "";
	@JsonProperty("Short_Text")
	private String shortText = "";
	@JsonProperty("Unit_of_Measurement")
	private String unitOfMeasurement = "";
	@JsonProperty("Quantity")
	private String quantity = "";
	@JsonProperty("Delivery_Date")
	private String deliveryDate = "";
	@JsonProperty("Currency")
	private String currency = "";
	@JsonProperty("Plant")
	private String plant = "";
	@JsonProperty("Storage_Location")
	private String storageLocation = "";
	@JsonProperty("Requisitioner")
	private String requisitioner = "";
	@JsonProperty("Purchasing_Organisation")
	private String purchasingOrganisation = "";
	@JsonProperty("Valuation_Price")
	private String valuationPrice = "";
	@JsonProperty("WBS_Element")
	private String wBSElement = "";
	@JsonProperty("GL_Account")
	private String gLAccount = "";
	@JsonProperty("Cost_Center")
	private String costCenter = "";
	@JsonProperty("Item_Text")
	private String itemText = "";
	@JsonProperty("Opportunity_Id")
	private String opportunityId = "";
	@JsonProperty("Site_Id")
	private String siteId = "";
	@JsonProperty("COF_Ref_No")
	private String cOFRefNo = "";
	@JsonProperty("List_Price")
	private String listPrice = "";
	@JsonProperty("List_Price_Currency")
	private String listPriceCurrency = "";
	@JsonProperty("COPF_Id_1")
	private String cOPFId1 = "";
	@JsonProperty("Service_Id")
	private String serviceId = "";
	@JsonProperty("Product")
	private String product = "";
	@JsonProperty("Discount_Percentage")
	private String discountPercentage = "";
	@JsonProperty("Service_No")
	private String serviceNo = "";
	@JsonProperty("Short_Text_1")
	private String shortText1 = "";
	@JsonProperty("Quantity_1")
	private String quantity1 = "";
	@JsonProperty("Unit_of_Measurement_1")
	private String unitOfMeasurement1 = "";
	@JsonProperty("Currency_1")
	private String currency1 = "";
	@JsonProperty("Gross_Price")
	private String grossPrice = "";
	@JsonProperty("Material_Group")
	private String materialGroup = "";
	@JsonProperty("Demand_Id")
	private String demandId = "";
	@JsonProperty("Name1")
	private String name1 = "";
	@JsonProperty("Name2")
	private String name2 = "";
	@JsonProperty("Name3")
	private String name3 = "";
	@JsonProperty("Name4")
	private String name4 = "";
	@JsonProperty("House_Num")
	private String houseNum = "";
	@JsonProperty("Street")
	private String street = "";
	@JsonProperty("City")
	private String city = "";
	@JsonProperty("State")
	private String state = "";
	@JsonProperty("Country")
	private String country = "";
	@JsonProperty("Postal_Code")
	private String postalCode = "";
	@JsonProperty("Vendor_Id")
	private String vendorId = "";

	@JsonProperty("Acct_Assignment_Category")
	public String getAcctAssignmentCategory() {
		return acctAssignmentCategory;
	}

	@JsonProperty("Acct_Assignment_Category")
	public void setAcctAssignmentCategory(String acctAssignmentCategory) {
		this.acctAssignmentCategory = acctAssignmentCategory;
	}

	@JsonProperty("Item_Category")
	public String getItemCategory() {
		return itemCategory;
	}

	@JsonProperty("Item_Category")
	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	@JsonProperty("Req_Tracking_Number")
	public String getReqTrackingNumber() {
		return reqTrackingNumber;
	}

	@JsonProperty("Req_Tracking_Number")
	public void setReqTrackingNumber(String reqTrackingNumber) {
		this.reqTrackingNumber = reqTrackingNumber;
	}

	@JsonProperty("Created_By")
	public String getCreatedBy() {
		return createdBy;
	}

	@JsonProperty("Created_By")
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@JsonProperty("Material_Code")
	public String getMaterialCode() {
		return materialCode;
	}

	@JsonProperty("Material_Code")
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	@JsonProperty("Short_Text")
	public String getShortText() {
		return shortText;
	}

	@JsonProperty("Short_Text")
	public void setShortText(String shortText) {
		this.shortText = shortText;
	}

	@JsonProperty("Unit_of_Measurement")
	public String getUnitOfMeasurement() {
		return unitOfMeasurement;
	}

	@JsonProperty("Unit_of_Measurement")
	public void setUnitOfMeasurement(String unitOfMeasurement) {
		this.unitOfMeasurement = unitOfMeasurement;
	}

	@JsonProperty("Quantity")
	public String getQuantity() {
		return quantity;
	}

	@JsonProperty("Quantity")
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	@JsonProperty("Delivery_Date")
	public String getDeliveryDate() {
		return deliveryDate;
	}

	@JsonProperty("Delivery_Date")
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	@JsonProperty("Currency")
	public String getCurrency() {
		return currency;
	}

	@JsonProperty("Currency")
	public void setCurrency(String currency) {
		this.currency = currency;
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

	@JsonProperty("Requisitioner")
	public String getRequisitioner() {
		return requisitioner;
	}

	@JsonProperty("Requisitioner")
	public void setRequisitioner(String requisitioner) {
		this.requisitioner = requisitioner;
	}

	@JsonProperty("Purchasing_Organisation")
	public String getPurchasingOrganisation() {
		return purchasingOrganisation;
	}

	@JsonProperty("Purchasing_Organisation")
	public void setPurchasingOrganisation(String purchasingOrganisation) {
		this.purchasingOrganisation = purchasingOrganisation;
	}

	@JsonProperty("Valuation_Price")
	public String getValuationPrice() {
		return valuationPrice;
	}

	@JsonProperty("Valuation_Price")
	public void setValuationPrice(String valuationPrice) {
		this.valuationPrice = valuationPrice;
	}

	@JsonProperty("WBS_Element")
	public String getWBSElement() {
		return wBSElement;
	}

	@JsonProperty("WBS_Element")
	public void setWBSElement(String wBSElement) {
		this.wBSElement = wBSElement;
	}

	@JsonProperty("GL_Account")
	public String getGLAccount() {
		return gLAccount;
	}

	@JsonProperty("GL_Account")
	public void setGLAccount(String gLAccount) {
		this.gLAccount = gLAccount;
	}

	@JsonProperty("Cost_Center")
	public String getCostCenter() {
		return costCenter;
	}

	@JsonProperty("Cost_Center")
	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	@JsonProperty("Item_Text")
	public String getItemText() {
		return itemText;
	}

	@JsonProperty("Item_Text")
	public void setItemText(String itemText) {
		this.itemText = itemText;
	}

	@JsonProperty("Opportunity_Id")
	public String getOpportunityId() {
		return opportunityId;
	}

	@JsonProperty("Opportunity_Id")
	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}

	@JsonProperty("Site_Id")
	public String getSiteId() {
		return siteId;
	}

	@JsonProperty("Site_Id")
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	@JsonProperty("COF_Ref_No")
	public String getCOFRefNo() {
		return cOFRefNo;
	}

	@JsonProperty("COF_Ref_No")
	public void setCOFRefNo(String cOFRefNo) {
		this.cOFRefNo = cOFRefNo;
	}

	@JsonProperty("List_Price")
	public String getListPrice() {
		return listPrice;
	}

	@JsonProperty("List_Price")
	public void setListPrice(String listPrice) {
		this.listPrice = listPrice;
	}

	@JsonProperty("List_Price_Currency")
	public String getListPriceCurrency() {
		return listPriceCurrency;
	}

	@JsonProperty("List_Price_Currency")
	public void setListPriceCurrency(String listPriceCurrency) {
		this.listPriceCurrency = listPriceCurrency;
	}

	@JsonProperty("COPF_Id_1")
	public String getCOPFId1() {
		return cOPFId1;
	}

	@JsonProperty("COPF_Id_1")
	public void setCOPFId1(String cOPFId1) {
		this.cOPFId1 = cOPFId1;
	}

	@JsonProperty("Service_Id")
	public String getServiceId() {
		return serviceId;
	}

	@JsonProperty("Service_Id")
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	@JsonProperty("Product")
	public String getProduct() {
		return product;
	}

	@JsonProperty("Product")
	public void setProduct(String product) {
		this.product = product;
	}

	@JsonProperty("Discount_Percentage")
	public String getDiscountPercentage() {
		return discountPercentage;
	}

	@JsonProperty("Discount_Percentage")
	public void setDiscountPercentage(String discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	@JsonProperty("Service_No")
	public String getServiceNo() {
		return serviceNo;
	}

	@JsonProperty("Service_No")
	public void setServiceNo(String serviceNo) {
		this.serviceNo = serviceNo;
	}

	@JsonProperty("Short_Text_1")
	public String getShortText1() {
		return shortText1;
	}

	@JsonProperty("Short_Text_1")
	public void setShortText1(String shortText1) {
		this.shortText1 = shortText1;
	}

	@JsonProperty("Quantity_1")
	public String getQuantity1() {
		return quantity1;
	}

	@JsonProperty("Quantity_1")
	public void setQuantity1(String quantity1) {
		this.quantity1 = quantity1;
	}

	@JsonProperty("Unit_of_Measurement_1")
	public String getUnitOfMeasurement1() {
		return unitOfMeasurement1;
	}

	@JsonProperty("Unit_of_Measurement_1")
	public void setUnitOfMeasurement1(String unitOfMeasurement1) {
		this.unitOfMeasurement1 = unitOfMeasurement1;
	}

	@JsonProperty("Currency_1")
	public String getCurrency1() {
		return currency1;
	}

	@JsonProperty("Currency_1")
	public void setCurrency1(String currency1) {
		this.currency1 = currency1;
	}

	@JsonProperty("Gross_Price")
	public String getGrossPrice() {
		return grossPrice;
	}

	@JsonProperty("Gross_Price")
	public void setGrossPrice(String grossPrice) {
		this.grossPrice = grossPrice;
	}

	@JsonProperty("Material_Group")
	public String getMaterialGroup() {
		return materialGroup;
	}

	@JsonProperty("Material_Group")
	public void setMaterialGroup(String materialGroup) {
		this.materialGroup = materialGroup;
	}

	@JsonProperty("Demand_Id")
	public String getDemandId() {
		return demandId;
	}

	@JsonProperty("Demand_Id")
	public void setDemandId(String demandId) {
		this.demandId = demandId;
	}

	@JsonProperty("Name1")
	public String getName1() {
		return name1;
	}

	@JsonProperty("Name1")
	public void setName1(String name1) {
		this.name1 = name1;
	}

	@JsonProperty("Name2")
	public String getName2() {
		return name2;
	}

	@JsonProperty("Name2")
	public void setName2(String name2) {
		this.name2 = name2;
	}

	@JsonProperty("Name3")
	public String getName3() {
		return name3;
	}

	@JsonProperty("Name3")
	public void setName3(String name3) {
		this.name3 = name3;
	}

	@JsonProperty("Name4")
	public String getName4() {
		return name4;
	}

	@JsonProperty("Name4")
	public void setName4(String name4) {
		this.name4 = name4;
	}

	@JsonProperty("House_Num")
	public String getHouseNum() {
		return houseNum;
	}

	@JsonProperty("House_Num")
	public void setHouseNum(String houseNum) {
		this.houseNum = houseNum;
	}

	@JsonProperty("Street")
	public String getStreet() {
		return street;
	}

	@JsonProperty("Street")
	public void setStreet(String street) {
		this.street = street;
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

	@JsonProperty("Postal_Code")
	public String getPostalCode() {
		return postalCode;
	}

	@JsonProperty("Postal_Code")
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	@JsonProperty("Vendor_Id")
	public String getVendorId() {
		return vendorId;
	}

	@JsonProperty("Vendor_Id")
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

}
