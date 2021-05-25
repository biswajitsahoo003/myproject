
package com.tcl.dias.servicefulfillmentutils.beans.sap;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * 
 * This file contains the AutoPoHeader.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "HEADER_TEXT",
    "SCNRO_TYPE",
    "REQ_TYPE",
    "PO_TYPE",
    "EBELN",
    "BUKRS",
    "LIFNR",
    "BSART",
    "ZTERM",
    "WAERS",
    "VERKF",
    "EKORG",
    "EKGRP",
    "IHREZ",
    "ZZFES_REQ_ID",
    "ZZCPEOWNER",
    "ZZCOPFID",
    "ZZSERVICEID",
    "ZZSRVTYPE",
    "ZZCUST",
    "ZZPRODCOMP",
    "ZZPOCATEGORY",
    "ZZCIRCUIT",
    "ZSFDCID",
    "ZSFDC_OPP_ID",
    "UNSEZ",
    "ZTYPE_DEAL",
    "ZZREV_MRC_VALUE",
    "ZZREV_MRC_QUAN",
    "ZZREV_NRC_VALUE",
    "ZZREV_CURR",
    "ZZOLDPONO",
    "ZSITE_A",
    "ZSITE_B",
    "ZZINTTYPE",
    "ZSPEED",
    "ZBAND_WIDTH",
    "BILL_INDCTR",
    "CHILD_PO"
})
public class AutoPoHeader {

    @JsonProperty("HEADER_TEXT")
    private String hEADERTEXT;
    @JsonProperty("SCNRO_TYPE")
    private String sCNROTYPE;
    @JsonProperty("REQ_TYPE")
    private String rEQTYPE;
    @JsonProperty("PO_TYPE")
    private String pOTYPE;
    @JsonProperty("EBELN")
    private String eBELN;
    @JsonProperty("BUKRS")
    private String bUKRS;
    @JsonProperty("LIFNR")
    private String lIFNR;
    @JsonProperty("BSART")
    private String bSART;
    @JsonProperty("ZTERM")
    private String zTERM;
    @JsonProperty("WAERS")
    private String wAERS;
    @JsonProperty("VERKF")
    private String vERKF;
    @JsonProperty("EKORG")
    private String eKORG;
    @JsonProperty("EKGRP")
    private String eKGRP;
    @JsonProperty("IHREZ")
    private String iHREZ;
    @JsonProperty("ZZFES_REQ_ID")
    private String zZFESREQID;
    @JsonProperty("ZZCPEOWNER")
    private String zZCPEOWNER;
    @JsonProperty("ZZCOPFID")
    private String zZCOPFID;
    @JsonProperty("ZZSERVICEID")
    private String zZSERVICEID;
    @JsonProperty("ZZSRVTYPE")
    private String zZSRVTYPE;
    @JsonProperty("ZZCUST")
    private String zZCUST;
    @JsonProperty("ZZPRODCOMP")
    private String zZPRODCOMP;
    @JsonProperty("ZZPOCATEGORY")
    private String zZPOCATEGORY;
    @JsonProperty("ZZCIRCUIT")
    private String zZCIRCUIT;
    @JsonProperty("ZSFDCID")
    private String zSFDCID;
    @JsonProperty("ZSFDC_OPP_ID")
    private String zSFDCOPPID;
    @JsonProperty("UNSEZ")
    private String uNSEZ;
    @JsonProperty("ZTYPE_DEAL")
    private String zTYPEDEAL;
    @JsonProperty("ZZREV_MRC_VALUE")
    private String zZREVMRCVALUE;
    @JsonProperty("ZZREV_MRC_QUAN")
    private String zZREVMRCQUAN;
    @JsonProperty("ZZREV_NRC_VALUE")
    private String zZREVNRCVALUE;
    @JsonProperty("ZZREV_CURR")
    private String zZREVCURR;
    @JsonProperty("ZZOLDPONO")
    private String zZOLDPONO;
    @JsonProperty("ZSITE_A")
    private String zSITEA;
    @JsonProperty("ZSITE_B")
    private String zSITEB;
    @JsonProperty("ZZINTTYPE")
    private String zZINTTYPE;
    @JsonProperty("ZSPEED")
    private String zSPEED;
    @JsonProperty("ZBAND_WIDTH")
    private String zBANDWIDTH;
    @JsonProperty("BILL_INDCTR")
    private String bILLINDCTR;
    @JsonProperty("CHILD_PO")
    private String cHILDPO;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("HEADER_TEXT")
    public String getHEADERTEXT() {
        return hEADERTEXT;
    }

    @JsonProperty("HEADER_TEXT")
    public void setHEADERTEXT(String hEADERTEXT) {
        this.hEADERTEXT = hEADERTEXT;
    }

    @JsonProperty("SCNRO_TYPE")
    public String getSCNROTYPE() {
        return sCNROTYPE;
    }

    @JsonProperty("SCNRO_TYPE")
    public void setSCNROTYPE(String sCNROTYPE) {
        this.sCNROTYPE = sCNROTYPE;
    }

    @JsonProperty("REQ_TYPE")
    public String getREQTYPE() {
        return rEQTYPE;
    }

    @JsonProperty("REQ_TYPE")
    public void setREQTYPE(String rEQTYPE) {
        this.rEQTYPE = rEQTYPE;
    }

    @JsonProperty("PO_TYPE")
    public String getPOTYPE() {
        return pOTYPE;
    }

    @JsonProperty("PO_TYPE")
    public void setPOTYPE(String pOTYPE) {
        this.pOTYPE = pOTYPE;
    }

    @JsonProperty("EBELN")
    public String getEBELN() {
        return eBELN;
    }

    @JsonProperty("EBELN")
    public void setEBELN(String eBELN) {
        this.eBELN = eBELN;
    }

    @JsonProperty("BUKRS")
    public String getBUKRS() {
        return bUKRS;
    }

    @JsonProperty("BUKRS")
    public void setBUKRS(String bUKRS) {
        this.bUKRS = bUKRS;
    }

    @JsonProperty("LIFNR")
    public String getLIFNR() {
        return lIFNR;
    }

    @JsonProperty("LIFNR")
    public void setLIFNR(String lIFNR) {
        this.lIFNR = lIFNR;
    }

    @JsonProperty("BSART")
    public String getBSART() {
        return bSART;
    }

    @JsonProperty("BSART")
    public void setBSART(String bSART) {
        this.bSART = bSART;
    }

    @JsonProperty("ZTERM")
    public String getZTERM() {
        return zTERM;
    }

    @JsonProperty("ZTERM")
    public void setZTERM(String zTERM) {
        this.zTERM = zTERM;
    }

    @JsonProperty("WAERS")
    public String getWAERS() {
        return wAERS;
    }

    @JsonProperty("WAERS")
    public void setWAERS(String wAERS) {
        this.wAERS = wAERS;
    }

    @JsonProperty("VERKF")
    public String getVERKF() {
        return vERKF;
    }

    @JsonProperty("VERKF")
    public void setVERKF(String vERKF) {
        this.vERKF = vERKF;
    }

    @JsonProperty("EKORG")
    public String getEKORG() {
        return eKORG;
    }

    @JsonProperty("EKORG")
    public void setEKORG(String eKORG) {
        this.eKORG = eKORG;
    }

    @JsonProperty("EKGRP")
    public String getEKGRP() {
        return eKGRP;
    }

    @JsonProperty("EKGRP")
    public void setEKGRP(String eKGRP) {
        this.eKGRP = eKGRP;
    }

    @JsonProperty("IHREZ")
    public String getIHREZ() {
        return iHREZ;
    }

    @JsonProperty("IHREZ")
    public void setIHREZ(String iHREZ) {
        this.iHREZ = iHREZ;
    }

    @JsonProperty("ZZFES_REQ_ID")
    public String getZZFESREQID() {
        return zZFESREQID;
    }

    @JsonProperty("ZZFES_REQ_ID")
    public void setZZFESREQID(String zZFESREQID) {
        this.zZFESREQID = zZFESREQID;
    }

    @JsonProperty("ZZCPEOWNER")
    public String getZZCPEOWNER() {
        return zZCPEOWNER;
    }

    @JsonProperty("ZZCPEOWNER")
    public void setZZCPEOWNER(String zZCPEOWNER) {
        this.zZCPEOWNER = zZCPEOWNER;
    }

    @JsonProperty("ZZCOPFID")
    public String getZZCOPFID() {
        return zZCOPFID;
    }

    @JsonProperty("ZZCOPFID")
    public void setZZCOPFID(String zZCOPFID) {
        this.zZCOPFID = zZCOPFID;
    }

    @JsonProperty("ZZSERVICEID")
    public String getZZSERVICEID() {
        return zZSERVICEID;
    }

    @JsonProperty("ZZSERVICEID")
    public void setZZSERVICEID(String zZSERVICEID) {
        this.zZSERVICEID = zZSERVICEID;
    }

    @JsonProperty("ZZSRVTYPE")
    public String getZZSRVTYPE() {
        return zZSRVTYPE;
    }

    @JsonProperty("ZZSRVTYPE")
    public void setZZSRVTYPE(String zZSRVTYPE) {
        this.zZSRVTYPE = zZSRVTYPE;
    }

    @JsonProperty("ZZCUST")
    public String getZZCUST() {
        return zZCUST;
    }

    @JsonProperty("ZZCUST")
    public void setZZCUST(String zZCUST) {
        this.zZCUST = zZCUST;
    }

    @JsonProperty("ZZPRODCOMP")
    public String getZZPRODCOMP() {
        return zZPRODCOMP;
    }

    @JsonProperty("ZZPRODCOMP")
    public void setZZPRODCOMP(String zZPRODCOMP) {
        this.zZPRODCOMP = zZPRODCOMP;
    }

    @JsonProperty("ZZPOCATEGORY")
    public String getZZPOCATEGORY() {
        return zZPOCATEGORY;
    }

    @JsonProperty("ZZPOCATEGORY")
    public void setZZPOCATEGORY(String zZPOCATEGORY) {
        this.zZPOCATEGORY = zZPOCATEGORY;
    }

    @JsonProperty("ZZCIRCUIT")
    public String getZZCIRCUIT() {
        return zZCIRCUIT;
    }

    @JsonProperty("ZZCIRCUIT")
    public void setZZCIRCUIT(String zZCIRCUIT) {
        this.zZCIRCUIT = zZCIRCUIT;
    }

    @JsonProperty("ZSFDCID")
    public String getZSFDCID() {
        return zSFDCID;
    }

    @JsonProperty("ZSFDCID")
    public void setZSFDCID(String zSFDCID) {
        this.zSFDCID = zSFDCID;
    }

    @JsonProperty("ZSFDC_OPP_ID")
    public String getZSFDCOPPID() {
        return zSFDCOPPID;
    }

    @JsonProperty("ZSFDC_OPP_ID")
    public void setZSFDCOPPID(String zSFDCOPPID) {
        this.zSFDCOPPID = zSFDCOPPID;
    }

    @JsonProperty("UNSEZ")
    public String getUNSEZ() {
        return uNSEZ;
    }

    @JsonProperty("UNSEZ")
    public void setUNSEZ(String uNSEZ) {
        this.uNSEZ = uNSEZ;
    }

    @JsonProperty("ZTYPE_DEAL")
    public String getZTYPEDEAL() {
        return zTYPEDEAL;
    }

    @JsonProperty("ZTYPE_DEAL")
    public void setZTYPEDEAL(String zTYPEDEAL) {
        this.zTYPEDEAL = zTYPEDEAL;
    }

    @JsonProperty("ZZREV_MRC_VALUE")
    public String getZZREVMRCVALUE() {
        return zZREVMRCVALUE;
    }

    @JsonProperty("ZZREV_MRC_VALUE")
    public void setZZREVMRCVALUE(String zZREVMRCVALUE) {
        this.zZREVMRCVALUE = zZREVMRCVALUE;
    }

    @JsonProperty("ZZREV_MRC_QUAN")
    public String getZZREVMRCQUAN() {
        return zZREVMRCQUAN;
    }

    @JsonProperty("ZZREV_MRC_QUAN")
    public void setZZREVMRCQUAN(String zZREVMRCQUAN) {
        this.zZREVMRCQUAN = zZREVMRCQUAN;
    }

    @JsonProperty("ZZREV_NRC_VALUE")
    public String getZZREVNRCVALUE() {
        return zZREVNRCVALUE;
    }

    @JsonProperty("ZZREV_NRC_VALUE")
    public void setZZREVNRCVALUE(String zZREVNRCVALUE) {
        this.zZREVNRCVALUE = zZREVNRCVALUE;
    }

    @JsonProperty("ZZREV_CURR")
    public String getZZREVCURR() {
        return zZREVCURR;
    }

    @JsonProperty("ZZREV_CURR")
    public void setZZREVCURR(String zZREVCURR) {
        this.zZREVCURR = zZREVCURR;
    }

    @JsonProperty("ZZOLDPONO")
    public String getZZOLDPONO() {
        return zZOLDPONO;
    }

    @JsonProperty("ZZOLDPONO")
    public void setZZOLDPONO(String zZOLDPONO) {
        this.zZOLDPONO = zZOLDPONO;
    }

    @JsonProperty("ZSITE_A")
    public String getZSITEA() {
        return zSITEA;
    }

    @JsonProperty("ZSITE_A")
    public void setZSITEA(String zSITEA) {
        this.zSITEA = zSITEA;
    }

    @JsonProperty("ZSITE_B")
    public String getZSITEB() {
        return zSITEB;
    }

    @JsonProperty("ZSITE_B")
    public void setZSITEB(String zSITEB) {
        this.zSITEB = zSITEB;
    }

    @JsonProperty("ZZINTTYPE")
    public String getZZINTTYPE() {
        return zZINTTYPE;
    }

    @JsonProperty("ZZINTTYPE")
    public void setZZINTTYPE(String zZINTTYPE) {
        this.zZINTTYPE = zZINTTYPE;
    }

    @JsonProperty("ZSPEED")
    public String getZSPEED() {
        return zSPEED;
    }

    @JsonProperty("ZSPEED")
    public void setZSPEED(String zSPEED) {
        this.zSPEED = zSPEED;
    }

    @JsonProperty("ZBAND_WIDTH")
    public String getZBANDWIDTH() {
        return zBANDWIDTH;
    }

    @JsonProperty("ZBAND_WIDTH")
    public void setZBANDWIDTH(String zBANDWIDTH) {
        this.zBANDWIDTH = zBANDWIDTH;
    }

    @JsonProperty("BILL_INDCTR")
    public String getBILLINDCTR() {
        return bILLINDCTR;
    }

    @JsonProperty("BILL_INDCTR")
    public void setBILLINDCTR(String bILLINDCTR) {
        this.bILLINDCTR = bILLINDCTR;
    }

    @JsonProperty("CHILD_PO")
    public String getCHILDPO() {
        return cHILDPO;
    }

    @JsonProperty("CHILD_PO")
    public void setCHILDPO(String cHILDPO) {
        this.cHILDPO = cHILDPO;
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
