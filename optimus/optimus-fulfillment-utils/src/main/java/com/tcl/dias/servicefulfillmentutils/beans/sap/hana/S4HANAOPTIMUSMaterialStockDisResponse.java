
package com.tcl.dias.servicefulfillmentutils.beans.sap.hana;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "BM_BUNDLE",
    "MATERIAL_CODE",
    "CATEGORY_OF_INVENTORY",
    "MATERIAL_DESCRIPTION",
    "MATERIAL_GROUP",
    "PLANT",
    "PLANT_NAME",
    "ADDRESS_LINE1",
    "ADDRESS_LINE2",
    "CITY",
    "STATE",
    "COUNTRY",
    "ZIPCODE",
    "STORAGE_LOCATION",
    "STORAGE_LOCATION_DESCRIPTION",
    "UNIT_OF_MEASURE",
    "QUANTITY_AVAILABLE",
    "WBS_NUMBER",
    "ITEM_CATEGORY",
    "Batch_Valuation_type",
    "Part_Codes",
    "SAP_Serial_Number",
    "OEM_Serial_number",
    "Vendor_Number",
    "Vendor_Name",
    "PO_Number",
    "Date_of_Order",
    "Lead_Time_to_Deliver_PO_WH",
    "Lead_Time_to_Deliver_WH_CUST",
    "STATUS",
    "REMARK",
    "PURCHASE_GROUP"
})
public class S4HANAOPTIMUSMaterialStockDisResponse {

    @JsonProperty("BM_BUNDLE")
    private String bMBUNDLE;
    @JsonProperty("MATERIAL_CODE")
    private String mATERIALCODE;
    @JsonProperty("CATEGORY_OF_INVENTORY")
    private String cATEGORYOFINVENTORY;
    @JsonProperty("MATERIAL_DESCRIPTION")
    private String mATERIALDESCRIPTION;
    @JsonProperty("MATERIAL_GROUP")
    private String mATERIALGROUP;
    @JsonProperty("PLANT")
    private String pLANT;
    @JsonProperty("PLANT_NAME")
    private String pLANTNAME;
    @JsonProperty("ADDRESS_LINE1")
    private String aDDRESSLINE1;
    @JsonProperty("ADDRESS_LINE2")
    private String aDDRESSLINE2;
    @JsonProperty("CITY")
    private String cITY;
    @JsonProperty("STATE")
    private String sTATE;
    @JsonProperty("COUNTRY")
    private String cOUNTRY;
    @JsonProperty("ZIPCODE")
    private String zIPCODE;
    @JsonProperty("STORAGE_LOCATION")
    private String sTORAGELOCATION;
    @JsonProperty("STORAGE_LOCATION_DESCRIPTION")
    private String sTORAGELOCATIONDESCRIPTION;
    @JsonProperty("UNIT_OF_MEASURE")
    private String uNITOFMEASURE;
    @JsonProperty("QUANTITY_AVAILABLE")
    private String qUANTITYAVAILABLE;
    @JsonProperty("WBS_NUMBER")
    private String wBSNUMBER;
    @JsonProperty("ITEM_CATEGORY")
    private String iTEMCATEGORY;
    @JsonProperty("Batch_Valuation_type")
    private String batchValuationType;
    @JsonProperty("Part_Codes")
    private String partCodes;
    @JsonProperty("SAP_Serial_Number")
    private String sAPSerialNumber;
    @JsonProperty("OEM_Serial_number")
    private String oEMSerialNumber;
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
    @JsonProperty("STATUS")
    private String sTATUS;
    @JsonProperty("REMARK")
    private String rEMARK;
    @JsonProperty("PURCHASE_GROUP")
    private String pURCHASEGROUP;

    @JsonProperty("BM_BUNDLE")
    public String getBMBUNDLE() {
        return bMBUNDLE;
    }

    @JsonProperty("BM_BUNDLE")
    public void setBMBUNDLE(String bMBUNDLE) {
        this.bMBUNDLE = bMBUNDLE;
    }

    @JsonProperty("MATERIAL_CODE")
    public String getMATERIALCODE() {
        return mATERIALCODE;
    }

    @JsonProperty("MATERIAL_CODE")
    public void setMATERIALCODE(String mATERIALCODE) {
        this.mATERIALCODE = mATERIALCODE;
    }

    @JsonProperty("CATEGORY_OF_INVENTORY")
    public String getCATEGORYOFINVENTORY() {
        return cATEGORYOFINVENTORY;
    }

    @JsonProperty("CATEGORY_OF_INVENTORY")
    public void setCATEGORYOFINVENTORY(String cATEGORYOFINVENTORY) {
        this.cATEGORYOFINVENTORY = cATEGORYOFINVENTORY;
    }

    @JsonProperty("MATERIAL_DESCRIPTION")
    public String getMATERIALDESCRIPTION() {
        return mATERIALDESCRIPTION;
    }

    @JsonProperty("MATERIAL_DESCRIPTION")
    public void setMATERIALDESCRIPTION(String mATERIALDESCRIPTION) {
        this.mATERIALDESCRIPTION = mATERIALDESCRIPTION;
    }

    @JsonProperty("MATERIAL_GROUP")
    public String getMATERIALGROUP() {
        return mATERIALGROUP;
    }

    @JsonProperty("MATERIAL_GROUP")
    public void setMATERIALGROUP(String mATERIALGROUP) {
        this.mATERIALGROUP = mATERIALGROUP;
    }

    @JsonProperty("PLANT")
    public String getPLANT() {
        return pLANT;
    }

    @JsonProperty("PLANT")
    public void setPLANT(String pLANT) {
        this.pLANT = pLANT;
    }

    @JsonProperty("PLANT_NAME")
    public String getPLANTNAME() {
        return pLANTNAME;
    }

    @JsonProperty("PLANT_NAME")
    public void setPLANTNAME(String pLANTNAME) {
        this.pLANTNAME = pLANTNAME;
    }

    @JsonProperty("ADDRESS_LINE1")
    public String getADDRESSLINE1() {
        return aDDRESSLINE1;
    }

    @JsonProperty("ADDRESS_LINE1")
    public void setADDRESSLINE1(String aDDRESSLINE1) {
        this.aDDRESSLINE1 = aDDRESSLINE1;
    }

    @JsonProperty("ADDRESS_LINE2")
    public String getADDRESSLINE2() {
        return aDDRESSLINE2;
    }

    @JsonProperty("ADDRESS_LINE2")
    public void setADDRESSLINE2(String aDDRESSLINE2) {
        this.aDDRESSLINE2 = aDDRESSLINE2;
    }

    @JsonProperty("CITY")
    public String getCITY() {
        return cITY;
    }

    @JsonProperty("CITY")
    public void setCITY(String cITY) {
        this.cITY = cITY;
    }

    @JsonProperty("STATE")
    public String getSTATE() {
        return sTATE;
    }

    @JsonProperty("STATE")
    public void setSTATE(String sTATE) {
        this.sTATE = sTATE;
    }

    @JsonProperty("COUNTRY")
    public String getCOUNTRY() {
        return cOUNTRY;
    }

    @JsonProperty("COUNTRY")
    public void setCOUNTRY(String cOUNTRY) {
        this.cOUNTRY = cOUNTRY;
    }

    @JsonProperty("ZIPCODE")
    public String getZIPCODE() {
        return zIPCODE;
    }

    @JsonProperty("ZIPCODE")
    public void setZIPCODE(String zIPCODE) {
        this.zIPCODE = zIPCODE;
    }

    @JsonProperty("STORAGE_LOCATION")
    public String getSTORAGELOCATION() {
        return sTORAGELOCATION;
    }

    @JsonProperty("STORAGE_LOCATION")
    public void setSTORAGELOCATION(String sTORAGELOCATION) {
        this.sTORAGELOCATION = sTORAGELOCATION;
    }

    @JsonProperty("STORAGE_LOCATION_DESCRIPTION")
    public String getSTORAGELOCATIONDESCRIPTION() {
        return sTORAGELOCATIONDESCRIPTION;
    }

    @JsonProperty("STORAGE_LOCATION_DESCRIPTION")
    public void setSTORAGELOCATIONDESCRIPTION(String sTORAGELOCATIONDESCRIPTION) {
        this.sTORAGELOCATIONDESCRIPTION = sTORAGELOCATIONDESCRIPTION;
    }

    @JsonProperty("UNIT_OF_MEASURE")
    public String getUNITOFMEASURE() {
        return uNITOFMEASURE;
    }

    @JsonProperty("UNIT_OF_MEASURE")
    public void setUNITOFMEASURE(String uNITOFMEASURE) {
        this.uNITOFMEASURE = uNITOFMEASURE;
    }

    @JsonProperty("QUANTITY_AVAILABLE")
    public String getQUANTITYAVAILABLE() {
        return qUANTITYAVAILABLE;
    }

    @JsonProperty("QUANTITY_AVAILABLE")
    public void setQUANTITYAVAILABLE(String qUANTITYAVAILABLE) {
        this.qUANTITYAVAILABLE = qUANTITYAVAILABLE;
    }

    @JsonProperty("WBS_NUMBER")
    public String getWBSNUMBER() {
        return wBSNUMBER;
    }

    @JsonProperty("WBS_NUMBER")
    public void setWBSNUMBER(String wBSNUMBER) {
        this.wBSNUMBER = wBSNUMBER;
    }

    @JsonProperty("ITEM_CATEGORY")
    public String getITEMCATEGORY() {
        return iTEMCATEGORY;
    }

    @JsonProperty("ITEM_CATEGORY")
    public void setITEMCATEGORY(String iTEMCATEGORY) {
        this.iTEMCATEGORY = iTEMCATEGORY;
    }

    @JsonProperty("Batch_Valuation_type")
    public String getBatchValuationType() {
        return batchValuationType;
    }

    @JsonProperty("Batch_Valuation_type")
    public void setBatchValuationType(String batchValuationType) {
        this.batchValuationType = batchValuationType;
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

    @JsonProperty("OEM_Serial_number")
    public String getOEMSerialNumber() {
        return oEMSerialNumber;
    }

    @JsonProperty("OEM_Serial_number")
    public void setOEMSerialNumber(String oEMSerialNumber) {
        this.oEMSerialNumber = oEMSerialNumber;
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

    @JsonProperty("STATUS")
    public String getSTATUS() {
        return sTATUS;
    }

    @JsonProperty("STATUS")
    public void setSTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }

    @JsonProperty("REMARK")
    public String getREMARK() {
        return rEMARK;
    }

    @JsonProperty("REMARK")
    public void setREMARK(String rEMARK) {
        this.rEMARK = rEMARK;
    }

    @JsonProperty("PURCHASE_GROUP")
    public String getPURCHASEGROUP() {
        return pURCHASEGROUP;
    }

    @JsonProperty("PURCHASE_GROUP")
    public void setPURCHASEGROUP(String pURCHASEGROUP) {
        this.pURCHASEGROUP = pURCHASEGROUP;
    }

}
