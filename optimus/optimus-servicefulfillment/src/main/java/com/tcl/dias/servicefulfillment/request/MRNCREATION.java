package com.tcl.dias.servicefulfillment.request;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Optimus_Id",
    "Circuit_Id",
    "Project_Name",
    "Rental_or_Sale",
    "Distribution_Center",
    "MRN_Date",
    "Issue_To",
    "Customer_Name",
    "Delivery_Address",
    "Delivery_Location_City",
    "Delivery_State",
    "Pin_Code",
    "Contact_Name",
    "Contact_No",
    "Cust_in_SEZ_or_NONSEZ",
    "SEZ_Cust_GST_No",
    "COF_Id",
    "BM_Bundle",
    "Material_Code",
    "Equipment_Desc",
    "Quantity",
    "TCL_PO_No",
    "WBS_No",
    "GL_Account",
    "Cost_Center",
    "SAP_Serial_Number",
    "Outright_Sale_Inv_No",
    "Reqstr_or_Prog_Mgr",
    "Approved_By",
    "Product",
    "Posting_Date",
    "Document_Date",
    "Material_Slip",
    "Doc_Header_Text",
    "MRN_No",
    "Rec_State",
    "Iss_State",
    "Movement_Type",
    "Stock_Category",
    "Plant",
    "Storage_Location",
    "Unloading_Point",
    "Vendor",
    "Purchasing_Doc"
})
public class MRNCREATION {

    @JsonProperty("Optimus_Id")
    private String optimusId;
    @JsonProperty("Circuit_Id")
    private String circuitId;
    @JsonProperty("Project_Name")
    private String projectName;
    @JsonProperty("Rental_or_Sale")
    private String rentalOrSale;
    @JsonProperty("Distribution_Center")
    private String distributionCenter;
    @JsonProperty("MRN_Date")
    private String mRNDate;
    @JsonProperty("Issue_To")
    private String issueTo;
    @JsonProperty("Customer_Name")
    private String customerName;
    @JsonProperty("Delivery_Address")
    private String deliveryAddress;
    @JsonProperty("Delivery_Location_City")
    private String deliveryLocationCity;
    @JsonProperty("Delivery_State")
    private String deliveryState;
    @JsonProperty("Pin_Code")
    private String pinCode;
    @JsonProperty("Contact_Name")
    private String contactName;
    @JsonProperty("Contact_No")
    private String contactNo;
    @JsonProperty("Cust_in_SEZ_or_NONSEZ")
    private String custInSEZOrNONSEZ;
    @JsonProperty("SEZ_Cust_GST_No")
    private String sEZCustGSTNo;
    @JsonProperty("COF_Id")
    private String cOFId;
    @JsonProperty("BM_Bundle")
    private String bMBundle;
    @JsonProperty("Material_Code")
    private String materialCode;
    @JsonProperty("Equipment_Desc")
    private String equipmentDesc;
    @JsonProperty("Quantity")
    private String quantity;
    @JsonProperty("TCL_PO_No")
    private String tCLPONo;
    @JsonProperty("WBS_No")
    private String wBSNo;
    @JsonProperty("GL_Account")
    private String gLAccount;
    @JsonProperty("Cost_Center")
    private String costCenter;
    @JsonProperty("SAP_Serial_Number")
    private String sAPSerialNumber;
    @JsonProperty("Outright_Sale_Inv_No")
    private String outrightSaleInvNo;
    @JsonProperty("Reqstr_or_Prog_Mgr")
    private String reqstrOrProgMgr;
    @JsonProperty("Approved_By")
    private String approvedBy;
    @JsonProperty("Product")
    private String product;
    @JsonProperty("Posting_Date")
    private String postingDate;
    @JsonProperty("Document_Date")
    private String documentDate;
    @JsonProperty("Material_Slip")
    private String materialSlip;
    @JsonProperty("Doc_Header_Text")
    private String docHeaderText;
    @JsonProperty("MRN_No")
    private String mRNNo;
    @JsonProperty("Rec_State")
    private String recState;
    @JsonProperty("Iss_State")
    private String issState;
    @JsonProperty("Movement_Type")
    private String movementType;
    @JsonProperty("Stock_Category")
    private String stockCategory;
    @JsonProperty("Plant")
    private String plant;
    @JsonProperty("Storage_Location")
    private String storageLocation;
    @JsonProperty("Unloading_Point")
    private String unloadingPoint;
    @JsonProperty("Vendor")
    private String vendor;
    @JsonProperty("Purchasing_Doc")
    private String purchasingDoc;

    @JsonProperty("Optimus_Id")
    public String getOptimusId() {
        return optimusId;
    }

    @JsonProperty("Optimus_Id")
    public void setOptimusId(String optimusId) {
        this.optimusId = optimusId;
    }

    @JsonProperty("Circuit_Id")
    public String getCircuitId() {
        return circuitId;
    }

    @JsonProperty("Circuit_Id")
    public void setCircuitId(String circuitId) {
        this.circuitId = circuitId;
    }

    @JsonProperty("Project_Name")
    public String getProjectName() {
        return projectName;
    }

    @JsonProperty("Project_Name")
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @JsonProperty("Rental_or_Sale")
    public String getRentalOrSale() {
        return rentalOrSale;
    }

    @JsonProperty("Rental_or_Sale")
    public void setRentalOrSale(String rentalOrSale) {
        this.rentalOrSale = rentalOrSale;
    }

    @JsonProperty("Distribution_Center")
    public String getDistributionCenter() {
        return distributionCenter;
    }

    @JsonProperty("Distribution_Center")
    public void setDistributionCenter(String distributionCenter) {
        this.distributionCenter = distributionCenter;
    }

    @JsonProperty("MRN_Date")
    public String getMRNDate() {
        return mRNDate;
    }

    @JsonProperty("MRN_Date")
    public void setMRNDate(String mRNDate) {
        this.mRNDate = mRNDate;
    }

    @JsonProperty("Issue_To")
    public String getIssueTo() {
        return issueTo;
    }

    @JsonProperty("Issue_To")
    public void setIssueTo(String issueTo) {
        this.issueTo = issueTo;
    }

    @JsonProperty("Customer_Name")
    public String getCustomerName() {
        return customerName;
    }

    @JsonProperty("Customer_Name")
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @JsonProperty("Delivery_Address")
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    @JsonProperty("Delivery_Address")
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    @JsonProperty("Delivery_Location_City")
    public String getDeliveryLocationCity() {
        return deliveryLocationCity;
    }

    @JsonProperty("Delivery_Location_City")
    public void setDeliveryLocationCity(String deliveryLocationCity) {
        this.deliveryLocationCity = deliveryLocationCity;
    }

    @JsonProperty("Delivery_State")
    public String getDeliveryState() {
        return deliveryState;
    }

    @JsonProperty("Delivery_State")
    public void setDeliveryState(String deliveryState) {
        this.deliveryState = deliveryState;
    }

    @JsonProperty("Pin_Code")
    public String getPinCode() {
        return pinCode;
    }

    @JsonProperty("Pin_Code")
    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    @JsonProperty("Contact_Name")
    public String getContactName() {
        return contactName;
    }

    @JsonProperty("Contact_Name")
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @JsonProperty("Contact_No")
    public String getContactNo() {
        return contactNo;
    }

    @JsonProperty("Contact_No")
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    @JsonProperty("Cust_in_SEZ_or_NONSEZ")
    public String getCustInSEZOrNONSEZ() {
        return custInSEZOrNONSEZ;
    }

    @JsonProperty("Cust_in_SEZ_or_NONSEZ")
    public void setCustInSEZOrNONSEZ(String custInSEZOrNONSEZ) {
        this.custInSEZOrNONSEZ = custInSEZOrNONSEZ;
    }

    @JsonProperty("SEZ_Cust_GST_No")
    public String getSEZCustGSTNo() {
        return sEZCustGSTNo;
    }

    @JsonProperty("SEZ_Cust_GST_No")
    public void setSEZCustGSTNo(String sEZCustGSTNo) {
        this.sEZCustGSTNo = sEZCustGSTNo;
    }

    @JsonProperty("COF_Id")
    public String getCOFId() {
        return cOFId;
    }

    @JsonProperty("COF_Id")
    public void setCOFId(String cOFId) {
        this.cOFId = cOFId;
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

    @JsonProperty("Equipment_Desc")
    public String getEquipmentDesc() {
        return equipmentDesc;
    }

    @JsonProperty("Equipment_Desc")
    public void setEquipmentDesc(String equipmentDesc) {
        this.equipmentDesc = equipmentDesc;
    }

    @JsonProperty("Quantity")
    public String getQuantity() {
        return quantity;
    }

    @JsonProperty("Quantity")
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("TCL_PO_No")
    public String getTCLPONo() {
        return tCLPONo;
    }

    @JsonProperty("TCL_PO_No")
    public void setTCLPONo(String tCLPONo) {
        this.tCLPONo = tCLPONo;
    }

    @JsonProperty("WBS_No")
    public String getWBSNo() {
        return wBSNo;
    }

    @JsonProperty("WBS_No")
    public void setWBSNo(String wBSNo) {
        this.wBSNo = wBSNo;
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

    @JsonProperty("SAP_Serial_Number")
    public String getSAPSerialNumber() {
        return sAPSerialNumber;
    }

    @JsonProperty("SAP_Serial_Number")
    public void setSAPSerialNumber(String sAPSerialNumber) {
        this.sAPSerialNumber = sAPSerialNumber;
    }

    @JsonProperty("Outright_Sale_Inv_No")
    public String getOutrightSaleInvNo() {
        return outrightSaleInvNo;
    }

    @JsonProperty("Outright_Sale_Inv_No")
    public void setOutrightSaleInvNo(String outrightSaleInvNo) {
        this.outrightSaleInvNo = outrightSaleInvNo;
    }

    @JsonProperty("Reqstr_or_Prog_Mgr")
    public String getReqstrOrProgMgr() {
        return reqstrOrProgMgr;
    }

    @JsonProperty("Reqstr_or_Prog_Mgr")
    public void setReqstrOrProgMgr(String reqstrOrProgMgr) {
        this.reqstrOrProgMgr = reqstrOrProgMgr;
    }

    @JsonProperty("Approved_By")
    public String getApprovedBy() {
        return approvedBy;
    }

    @JsonProperty("Approved_By")
    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    @JsonProperty("Product")
    public String getProduct() {
        return product;
    }

    @JsonProperty("Product")
    public void setProduct(String product) {
        this.product = product;
    }

    @JsonProperty("Posting_Date")
    public String getPostingDate() {
        return postingDate;
    }

    @JsonProperty("Posting_Date")
    public void setPostingDate(String postingDate) {
        this.postingDate = postingDate;
    }

    @JsonProperty("Document_Date")
    public String getDocumentDate() {
        return documentDate;
    }

    @JsonProperty("Document_Date")
    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }

    @JsonProperty("Material_Slip")
    public String getMaterialSlip() {
        return materialSlip;
    }

    @JsonProperty("Material_Slip")
    public void setMaterialSlip(String materialSlip) {
        this.materialSlip = materialSlip;
    }

    @JsonProperty("Doc_Header_Text")
    public String getDocHeaderText() {
        return docHeaderText;
    }

    @JsonProperty("Doc_Header_Text")
    public void setDocHeaderText(String docHeaderText) {
        this.docHeaderText = docHeaderText;
    }

    @JsonProperty("MRN_No")
    public String getMRNNo() {
        return mRNNo;
    }

    @JsonProperty("MRN_No")
    public void setMRNNo(String mRNNo) {
        this.mRNNo = mRNNo;
    }

    @JsonProperty("Rec_State")
    public String getRecState() {
        return recState;
    }

    @JsonProperty("Rec_State")
    public void setRecState(String recState) {
        this.recState = recState;
    }

    @JsonProperty("Iss_State")
    public String getIssState() {
        return issState;
    }

    @JsonProperty("Iss_State")
    public void setIssState(String issState) {
        this.issState = issState;
    }

    @JsonProperty("Movement_Type")
    public String getMovementType() {
        return movementType;
    }

    @JsonProperty("Movement_Type")
    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }

    @JsonProperty("Stock_Category")
    public String getStockCategory() {
        return stockCategory;
    }

    @JsonProperty("Stock_Category")
    public void setStockCategory(String stockCategory) {
        this.stockCategory = stockCategory;
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

    @JsonProperty("Unloading_Point")
    public String getUnloadingPoint() {
        return unloadingPoint;
    }

    @JsonProperty("Unloading_Point")
    public void setUnloadingPoint(String unloadingPoint) {
        this.unloadingPoint = unloadingPoint;
    }

    @JsonProperty("Vendor")
    public String getVendor() {
        return vendor;
    }

    @JsonProperty("Vendor")
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    @JsonProperty("Purchasing_Doc")
    public String getPurchasingDoc() {
        return purchasingDoc;
    }

    @JsonProperty("Purchasing_Doc")
    public void setPurchasingDoc(String purchasingDoc) {
        this.purchasingDoc = purchasingDoc;
    }

}
