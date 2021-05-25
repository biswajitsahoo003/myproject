
package com.tcl.dias.servicefulfillmentutils.beans.sap.hana;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "BM_BUNDLE",
    "MATERIAL_CODE",
    "PLANT",
    "STORAGE_LOCATION",
    "Valuation_type",
    "PURCHASE_GROUP",
    "CATEGORY_OF_INVENTORY"
})
public class OPTIMUSMaterialStockDi {

    @JsonProperty("BM_BUNDLE")
    private String bMBUNDLE;
    @JsonProperty("MATERIAL_CODE")
    private String mATERIALCODE;
    @JsonProperty("PLANT")
    private String pLANT;
    @JsonProperty("STORAGE_LOCATION")
    private String sTORAGELOCATION;
    @JsonProperty("Valuation_type")
    private String valuationType;
    @JsonProperty("PURCHASE_GROUP")
    private String pURCHASEGROUP;
    @JsonProperty("CATEGORY_OF_INVENTORY")
    private String cATEGORYOFINVENTORY;

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

    @JsonProperty("PLANT")
    public String getPLANT() {
        return pLANT;
    }

    @JsonProperty("PLANT")
    public void setPLANT(String pLANT) {
        this.pLANT = pLANT;
    }

    @JsonProperty("STORAGE_LOCATION")
    public String getSTORAGELOCATION() {
        return sTORAGELOCATION;
    }

    @JsonProperty("STORAGE_LOCATION")
    public void setSTORAGELOCATION(String sTORAGELOCATION) {
        this.sTORAGELOCATION = sTORAGELOCATION;
    }

    @JsonProperty("Valuation_type")
    public String getValuationType() {
        return valuationType;
    }

    @JsonProperty("Valuation_type")
    public void setValuationType(String valuationType) {
        this.valuationType = valuationType;
    }

    @JsonProperty("PURCHASE_GROUP")
    public String getPURCHASEGROUP() {
        return pURCHASEGROUP;
    }

    @JsonProperty("PURCHASE_GROUP")
    public void setPURCHASEGROUP(String pURCHASEGROUP) {
        this.pURCHASEGROUP = pURCHASEGROUP;
    }

    @JsonProperty("CATEGORY_OF_INVENTORY")
    public String getCATEGORYOFINVENTORY() {
        return cATEGORYOFINVENTORY;
    }

    @JsonProperty("CATEGORY_OF_INVENTORY")
    public void setCATEGORYOFINVENTORY(String cATEGORYOFINVENTORY) {
        this.cATEGORYOFINVENTORY = cATEGORYOFINVENTORY;
    }

}
