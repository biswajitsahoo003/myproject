
package com.tcl.dias.servicefulfillmentutils.beans.sap.hana;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "OLA_NUMBER",
    "OLA_ITEM_NO",
    "MATERIAL",
    "DESCRIPTION",
    "UOM",
    "OPEN_QTY",
    "PURCHASE_GROUP",
    "TRACKING_NUMBER",
    "PRICE",
    "CURRENCY",
    "CREATION_DATE",
    "VALIDITY_START_DATE",
    "VALIDITY_END_DATE",
    "MATERIAL_RECOVERED_DATE",
    "STATUS",
    "REMARK"
})
public class VMISTOCKUPDATE {

    @JsonProperty("OLA_NUMBER")
    private double oLANUMBER;
    @JsonProperty("OLA_ITEM_NO")
    private String oLAITEMNO;
    @JsonProperty("MATERIAL")
    private String mATERIAL;
    @JsonProperty("DESCRIPTION")
    private String dESCRIPTION;
    @JsonProperty("UOM")
    private String uOM;
    @JsonProperty("OPEN_QTY")
    private String oPENQTY;
    @JsonProperty("PURCHASE_GROUP")
    private String pURCHASEGROUP;
    @JsonProperty("TRACKING_NUMBER")
    private String tRACKINGNUMBER;
    @JsonProperty("PRICE")
    private String pRICE;
    @JsonProperty("CURRENCY")
    private String cURRENCY;
    @JsonProperty("CREATION_DATE")
    private int cREATIONDATE;
    @JsonProperty("VALIDITY_START_DATE")
    private int vALIDITYSTARTDATE;
    @JsonProperty("VALIDITY_END_DATE")
    private int vALIDITYENDDATE;
    @JsonProperty("MATERIAL_RECOVERED_DATE")
    private String mATERIALRECOVEREDDATE;
    @JsonProperty("STATUS")
    private String sTATUS;
    @JsonProperty("REMARK")
    private String rEMARK;

    @JsonProperty("OLA_NUMBER")
    public double getOLANUMBER() {
        return oLANUMBER;
    }

    @JsonProperty("OLA_NUMBER")
    public void setOLANUMBER(double oLANUMBER) {
        this.oLANUMBER = oLANUMBER;
    }

    @JsonProperty("OLA_ITEM_NO")
    public String getOLAITEMNO() {
        return oLAITEMNO;
    }

    @JsonProperty("OLA_ITEM_NO")
    public void setOLAITEMNO(String oLAITEMNO) {
        this.oLAITEMNO = oLAITEMNO;
    }

    @JsonProperty("MATERIAL")
    public String getMATERIAL() {
        return mATERIAL;
    }

    @JsonProperty("MATERIAL")
    public void setMATERIAL(String mATERIAL) {
        this.mATERIAL = mATERIAL;
    }

    @JsonProperty("DESCRIPTION")
    public String getDESCRIPTION() {
        return dESCRIPTION;
    }

    @JsonProperty("DESCRIPTION")
    public void setDESCRIPTION(String dESCRIPTION) {
        this.dESCRIPTION = dESCRIPTION;
    }

    @JsonProperty("UOM")
    public String getUOM() {
        return uOM;
    }

    @JsonProperty("UOM")
    public void setUOM(String uOM) {
        this.uOM = uOM;
    }

    @JsonProperty("OPEN_QTY")
    public String getOPENQTY() {
        return oPENQTY;
    }

    @JsonProperty("OPEN_QTY")
    public void setOPENQTY(String oPENQTY) {
        this.oPENQTY = oPENQTY;
    }

    @JsonProperty("PURCHASE_GROUP")
    public String getPURCHASEGROUP() {
        return pURCHASEGROUP;
    }

    @JsonProperty("PURCHASE_GROUP")
    public void setPURCHASEGROUP(String pURCHASEGROUP) {
        this.pURCHASEGROUP = pURCHASEGROUP;
    }

    @JsonProperty("TRACKING_NUMBER")
    public String getTRACKINGNUMBER() {
        return tRACKINGNUMBER;
    }

    @JsonProperty("TRACKING_NUMBER")
    public void setTRACKINGNUMBER(String tRACKINGNUMBER) {
        this.tRACKINGNUMBER = tRACKINGNUMBER;
    }

    @JsonProperty("PRICE")
    public String getPRICE() {
        return pRICE;
    }

    @JsonProperty("PRICE")
    public void setPRICE(String pRICE) {
        this.pRICE = pRICE;
    }

    @JsonProperty("CURRENCY")
    public String getCURRENCY() {
        return cURRENCY;
    }

    @JsonProperty("CURRENCY")
    public void setCURRENCY(String cURRENCY) {
        this.cURRENCY = cURRENCY;
    }

    @JsonProperty("CREATION_DATE")
    public int getCREATIONDATE() {
        return cREATIONDATE;
    }

    @JsonProperty("CREATION_DATE")
    public void setCREATIONDATE(int cREATIONDATE) {
        this.cREATIONDATE = cREATIONDATE;
    }

    @JsonProperty("VALIDITY_START_DATE")
    public int getVALIDITYSTARTDATE() {
        return vALIDITYSTARTDATE;
    }

    @JsonProperty("VALIDITY_START_DATE")
    public void setVALIDITYSTARTDATE(int vALIDITYSTARTDATE) {
        this.vALIDITYSTARTDATE = vALIDITYSTARTDATE;
    }

    @JsonProperty("VALIDITY_END_DATE")
    public int getVALIDITYENDDATE() {
        return vALIDITYENDDATE;
    }

    @JsonProperty("VALIDITY_END_DATE")
    public void setVALIDITYENDDATE(int vALIDITYENDDATE) {
        this.vALIDITYENDDATE = vALIDITYENDDATE;
    }

    @JsonProperty("MATERIAL_RECOVERED_DATE")
    public String getMATERIALRECOVEREDDATE() {
        return mATERIALRECOVEREDDATE;
    }

    @JsonProperty("MATERIAL_RECOVERED_DATE")
    public void setMATERIALRECOVEREDDATE(String mATERIALRECOVEREDDATE) {
        this.mATERIALRECOVEREDDATE = mATERIALRECOVEREDDATE;
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

}
