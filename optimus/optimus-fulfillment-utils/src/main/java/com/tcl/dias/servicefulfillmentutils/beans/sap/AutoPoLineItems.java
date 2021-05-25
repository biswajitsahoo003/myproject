
package com.tcl.dias.servicefulfillmentutils.beans.sap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * 
 * This file contains the AutoPoLineItems.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "CUST_SEGMENT",
    "EBELP",
    "TXZ01",
    "EPSTP",
    "KNTTP",
    "WERKS",
    "AFNAM",
    "MENGE",
    "MATKL",
    "SAKNR",
    "KOSTL",
    "ZZTERMTY",
    "ZZFRMEND",
    "ZZTOEND",
    "ZZCHARD",
    "ZVEN_REFNO",
    "ZZOPNAME",
    "ZZCOMMDT",
    "ZZTERMDT",
    "ZCOMM_DT",
    "ZTERM_DT",
    "ZZCRFSDT",
    "ZZRFSDT",
    "SERVICE_ITEMS"
})
public class AutoPoLineItems {

    @JsonProperty("CUST_SEGMENT")
    private String cUSTSEGMENT;
    @JsonProperty("EBELP")
    private String eBELP;
    @JsonProperty("TXZ01")
    private String tXZ01;
    @JsonProperty("EPSTP")
    private String ePSTP;
    @JsonProperty("KNTTP")
    private String kNTTP;
    @JsonProperty("WERKS")
    private String wERKS;
    @JsonProperty("AFNAM")
    private String aFNAM;
    @JsonProperty("MENGE")
    private String mENGE;
    @JsonProperty("MATKL")
    private String mATKL;
    @JsonProperty("SAKNR")
    private String sAKNR;
    @JsonProperty("KOSTL")
    private String kOSTL;
    @JsonProperty("ZZTERMTY")
    private String zZTERMTY;
    @JsonProperty("ZZFRMEND")
    private String zZFRMEND;
    @JsonProperty("ZZTOEND")
    private String zZTOEND;
    @JsonProperty("ZZCHARD")
    private String zZCHARD;
    @JsonProperty("ZVEN_REFNO")
    private String zVENREFNO;
    @JsonProperty("ZZOPNAME")
    private String zZOPNAME;
    @JsonProperty("ZZCOMMDT")
    private String zZCOMMDT;
    @JsonProperty("ZZTERMDT")
    private String zZTERMDT;
    @JsonProperty("ZCOMM_DT")
    private String zCOMMDT;
    @JsonProperty("ZTERM_DT")
    private String zTERMDT;
    @JsonProperty("ZZCRFSDT")
    private String zZCRFSDT;
    @JsonProperty("ZZRFSDT")
    private String zZRFSDT;
    @JsonProperty("SERVICE_ITEMS")
    private List<AutoPoServiceItems> sERVICEITEMS;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("CUST_SEGMENT")
    public String getCUSTSEGMENT() {
        return cUSTSEGMENT;
    }

    @JsonProperty("CUST_SEGMENT")
    public void setCUSTSEGMENT(String cUSTSEGMENT) {
        this.cUSTSEGMENT = cUSTSEGMENT;
    }

    @JsonProperty("EBELP")
    public String getEBELP() {
        return eBELP;
    }

    @JsonProperty("EBELP")
    public void setEBELP(String eBELP) {
        this.eBELP = eBELP;
    }

    @JsonProperty("TXZ01")
    public String getTXZ01() {
        return tXZ01;
    }

    @JsonProperty("TXZ01")
    public void setTXZ01(String tXZ01) {
        this.tXZ01 = tXZ01;
    }

    @JsonProperty("EPSTP")
    public String getEPSTP() {
        return ePSTP;
    }

    @JsonProperty("EPSTP")
    public void setEPSTP(String ePSTP) {
        this.ePSTP = ePSTP;
    }

    @JsonProperty("KNTTP")
    public String getKNTTP() {
        return kNTTP;
    }

    @JsonProperty("KNTTP")
    public void setKNTTP(String kNTTP) {
        this.kNTTP = kNTTP;
    }

    @JsonProperty("WERKS")
    public String getWERKS() {
        return wERKS;
    }

    @JsonProperty("WERKS")
    public void setWERKS(String wERKS) {
        this.wERKS = wERKS;
    }

    @JsonProperty("AFNAM")
    public String getAFNAM() {
        return aFNAM;
    }

    @JsonProperty("AFNAM")
    public void setAFNAM(String aFNAM) {
        this.aFNAM = aFNAM;
    }

    @JsonProperty("MENGE")
    public String getMENGE() {
        return mENGE;
    }

    @JsonProperty("MENGE")
    public void setMENGE(String mENGE) {
        this.mENGE = mENGE;
    }

    @JsonProperty("MATKL")
    public String getMATKL() {
        return mATKL;
    }

    @JsonProperty("MATKL")
    public void setMATKL(String mATKL) {
        this.mATKL = mATKL;
    }

    @JsonProperty("SAKNR")
    public String getSAKNR() {
        return sAKNR;
    }

    @JsonProperty("SAKNR")
    public void setSAKNR(String sAKNR) {
        this.sAKNR = sAKNR;
    }

    @JsonProperty("KOSTL")
    public String getKOSTL() {
        return kOSTL;
    }

    @JsonProperty("KOSTL")
    public void setKOSTL(String kOSTL) {
        this.kOSTL = kOSTL;
    }

    @JsonProperty("ZZTERMTY")
    public String getZZTERMTY() {
        return zZTERMTY;
    }

    @JsonProperty("ZZTERMTY")
    public void setZZTERMTY(String zZTERMTY) {
        this.zZTERMTY = zZTERMTY;
    }

    @JsonProperty("ZZFRMEND")
    public String getZZFRMEND() {
        return zZFRMEND;
    }

    @JsonProperty("ZZFRMEND")
    public void setZZFRMEND(String zZFRMEND) {
        this.zZFRMEND = zZFRMEND;
    }

    @JsonProperty("ZZTOEND")
    public String getZZTOEND() {
        return zZTOEND;
    }

    @JsonProperty("ZZTOEND")
    public void setZZTOEND(String zZTOEND) {
        this.zZTOEND = zZTOEND;
    }

    @JsonProperty("ZZCHARD")
    public String getZZCHARD() {
        return zZCHARD;
    }

    @JsonProperty("ZZCHARD")
    public void setZZCHARD(String zZCHARD) {
        this.zZCHARD = zZCHARD;
    }

    @JsonProperty("ZVEN_REFNO")
    public String getZVENREFNO() {
        return zVENREFNO;
    }

    @JsonProperty("ZVEN_REFNO")
    public void setZVENREFNO(String zVENREFNO) {
        this.zVENREFNO = zVENREFNO;
    }

    @JsonProperty("ZZOPNAME")
    public String getZZOPNAME() {
        return zZOPNAME;
    }

    @JsonProperty("ZZOPNAME")
    public void setZZOPNAME(String zZOPNAME) {
        this.zZOPNAME = zZOPNAME;
    }

    @JsonProperty("ZZCOMMDT")
    public String getZZCOMMDT() {
        return zZCOMMDT;
    }

    @JsonProperty("ZZCOMMDT")
    public void setZZCOMMDT(String zZCOMMDT) {
        this.zZCOMMDT = zZCOMMDT;
    }

    @JsonProperty("ZZTERMDT")
    public String getZZTERMDT() {
        return zZTERMDT;
    }

    @JsonProperty("ZZTERMDT")
    public void setZZTERMDT(String zZTERMDT) {
        this.zZTERMDT = zZTERMDT;
    }

    @JsonProperty("ZCOMM_DT")
    public String getZCOMMDT() {
        return zCOMMDT;
    }

    @JsonProperty("ZCOMM_DT")
    public void setZCOMMDT(String zCOMMDT) {
        this.zCOMMDT = zCOMMDT;
    }

    @JsonProperty("ZTERM_DT")
    public String getZTERMDT() {
        return zTERMDT;
    }

    @JsonProperty("ZTERM_DT")
    public void setZTERMDT(String zTERMDT) {
        this.zTERMDT = zTERMDT;
    }

    @JsonProperty("ZZCRFSDT")
    public String getZZCRFSDT() {
        return zZCRFSDT;
    }

    @JsonProperty("ZZCRFSDT")
    public void setZZCRFSDT(String zZCRFSDT) {
        this.zZCRFSDT = zZCRFSDT;
    }

    @JsonProperty("ZZRFSDT")
    public String getZZRFSDT() {
        return zZRFSDT;
    }

    @JsonProperty("ZZRFSDT")
    public void setZZRFSDT(String zZRFSDT) {
        this.zZRFSDT = zZRFSDT;
    }

    @JsonProperty("SERVICE_ITEMS")
    public List<AutoPoServiceItems> getSERVICEITEMS() {
        return sERVICEITEMS;
    }

    @JsonProperty("SERVICE_ITEMS")
    public void setSERVICEITEMS(List<AutoPoServiceItems> sERVICEITEMS) {
        this.sERVICEITEMS = sERVICEITEMS;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
