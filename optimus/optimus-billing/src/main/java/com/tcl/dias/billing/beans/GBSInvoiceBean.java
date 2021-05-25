package com.tcl.dias.billing.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonPropertyOrder({ "INVCE_PRIOD_START_DT" })
public class GBSInvoiceBean {

	@JsonProperty("INVCE_PRIOD_START_DT")
	private String invoicePeriodStartDt;
	
	@JsonProperty("INVCE_PRIOD_END_DT")
	private String invoicePeriodEndDt;
	
	@JsonProperty("INTNL_INVCE_NO")
	private String intnlInvoiceNo;
	
	@JsonProperty("ORG_NO")
	private String orgNo;
	
	@JsonProperty("CPNY_ID")
	private String cpnyId;
	
	@JsonProperty("INVCE_YEAR")
	private String invceYear;
	
	@JsonProperty("INVCE_NO")
	private String invceNo;
	
	@JsonProperty("INVCE_DT")
	private String invceDt;
	
	@JsonProperty("PRIM_LANG_CD")
	private String primLangCd;
	
	@JsonProperty("BILL_GRP_CD")
	private String billGrpCd;
	
	@JsonProperty("PRFILCURRENCY")
	private String prfilCurrency;
	
	@JsonProperty("NBCURRENCY")
	private String nbCurrency;
	
	@JsonProperty("RAPIDCDR_ID")
	private String rapidCdrId;
	
	@JsonProperty("AMT")
	private String amt;
	
	@JsonProperty("NBCALLS")
	private String nbCalls;
	
	@JsonProperty("FILE_NM")
	private String fileNm;
	
	@JsonProperty("MDIUM_CD")
	private String mdiumCd;
	
	@JsonProperty("DUE_DT")
	private String dueDt;
	
	@JsonProperty("FILE_PATH")
	private String filePath;
	
	@JsonProperty("FULL_INVCE_NO")
	private String fullInvceNo;
	
	@JsonProperty("FOLDER PATH")
	private String folderPath;
	
	@JsonProperty("ORG_LEGAL_NM")
	private String orgLegNm;
	
	@JsonProperty("INVCE_PRIOD_START_DT")
	public String getInvoicePeriodStartDt() {
		return invoicePeriodStartDt;
	}
	@JsonProperty("INVCE_PRIOD_START_DT")
	public void setInvoicePeriodStartDt(String invoicePeriodStartDt) {
		this.invoicePeriodStartDt = invoicePeriodStartDt;
	}
	@JsonProperty("INVCE_PRIOD_END_DT")
	public String getInvoicePeriodEndDt() {
		return invoicePeriodEndDt;
	}
	@JsonProperty("INVCE_PRIOD_END_DT")
	public void setInvoicePeriodEndDt(String invoicePeriodEndDt) {
		this.invoicePeriodEndDt = invoicePeriodEndDt;
	}
	@JsonProperty("INTNL_INVCE_NO")
	public String getIntnlInvoiceNo() {
		return intnlInvoiceNo;
	}
	@JsonProperty("INTNL_INVCE_NO")
	public void setIntnlInvoiceNo(String intnlInvoiceNo) {
		this.intnlInvoiceNo = intnlInvoiceNo;
	}
	@JsonProperty("ORG_NO")
	public String getOrgNo() {
		return orgNo;
	}
	@JsonProperty("ORG_NO")
	public void setOrgNo(String orgNo) {
		this.orgNo = orgNo;
	}
	@JsonProperty("CPNY_ID")
	public String getCpnyId() {
		return cpnyId;
	}
	@JsonProperty("CPNY_ID")
	public void setCpnyId(String cpnyId) {
		this.cpnyId = cpnyId;
	}
	@JsonProperty("INVCE_YEAR")
	public String getInvceYear() {
		return invceYear;
	}
	@JsonProperty("INVCE_YEAR")
	public void setInvceYear(String invceYear) {
		this.invceYear = invceYear;
	}
	@JsonProperty("INVCE_NO")
	public String getInvceNo() {
		return invceNo;
	}
	@JsonProperty("INVCE_NO")
	public void setInvceNo(String invceNo) {
		this.invceNo = invceNo;
	}
	@JsonProperty("INVCE_DT")
	public String getInvceDt() {
		return invceDt;
	}
	@JsonProperty("INVCE_DT")
	public void setInvceDt(String invceDt) {
		this.invceDt = invceDt;
	}
	@JsonProperty("PRIM_LANG_CD")
	public String getPrimLangCd() {
		return primLangCd;
	}
	@JsonProperty("PRIM_LANG_CD")
	public void setPrimLangCd(String primLangCd) {
		this.primLangCd = primLangCd;
	}
	@JsonProperty("BILL_GRP_CD")
	public String getBillGrpCd() {
		return billGrpCd;
	}
	@JsonProperty("BILL_GRP_CD")
	public void setBillGrpCd(String billGrpCd) {
		this.billGrpCd = billGrpCd;
	}
	@JsonProperty("PRFILCURRENCY")
	public String getPrfilCurrency() {
		return prfilCurrency;
	}
	@JsonProperty("PRFILCURRENCY")
	public void setPrfilCurrency(String prfilCurrency) {
		this.prfilCurrency = prfilCurrency;
	}
	@JsonProperty("NBCURRENCY")
	public String getNbCurrency() {
		return nbCurrency;
	}
	@JsonProperty("NBCURRENCY")
	public void setNbCurrency(String nbCurrency) {
		this.nbCurrency = nbCurrency;
	}
	@JsonProperty("RAPIDCDR_ID")
	public String getRapidCdrId() {
		return rapidCdrId;
	}
	@JsonProperty("RAPIDCDR_ID")
	public void setRapidCdrId(String rapidCdrId) {
		this.rapidCdrId = rapidCdrId;
	}
	@JsonProperty("AMT")
	public String getAmt() {
		return amt;
	}
	@JsonProperty("AMT")
	public void setAmt(String amt) {
		this.amt = amt;
	}
	@JsonProperty("NBCALLS")
	public String getNbCalls() {
		return nbCalls;
	}
	@JsonProperty("NBCALLS")
	public void setNbCalls(String nbCalls) {
		this.nbCalls = nbCalls;
	}
	@JsonProperty("FILE_NM")
	public String getFileNm() {
		return fileNm;
	}
	@JsonProperty("FILE_NM")
	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}
	@JsonProperty("MDIUM_CD")
	public String getMdiumCd() {
		return mdiumCd;
	}
	@JsonProperty("MDIUM_CD")
	public void setMdiumCd(String mdiumCd) {
		this.mdiumCd = mdiumCd;
	}
	@JsonProperty("DUE_DT")
	public String getDueDt() {
		return dueDt;
	}
	@JsonProperty("DUE_DT")
	public void setDueDt(String dueDt) {
		this.dueDt = dueDt;
	}
	@JsonProperty("FILE_PATH")
	public String getFilePath() {
		return filePath;
	}
	@JsonProperty("FILE_PATH")
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	@JsonProperty("FULL_INVCE_NO")
	public String getFullInvceNo() {
		return fullInvceNo;
	}
	@JsonProperty("FULL_INVCE_NO")
	public void setFullInvceNo(String fullInvceNo) {
		this.fullInvceNo = fullInvceNo;
	}
	@JsonProperty("FOLDER PATH")
	public String getFolderPath() {
		return folderPath;
	}
	@JsonProperty("FOLDER PATH")
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}
	@JsonProperty("ORG_LEGAL_NM")
	public String getOrgLegNm() {
		return orgLegNm;
	}
	@JsonProperty("ORG_LEGAL_NM")
	public void setOrgLegNm(String orgLegNm) {
		this.orgLegNm = orgLegNm;
	}

	@Override
	public String toString() {
		return "GBSInvoiceBean [invoicePeriodStartDt=" + invoicePeriodStartDt + ", invoicePeriodEndDt="
				+ invoicePeriodEndDt + ", intnlInvoiceNo=" + intnlInvoiceNo + ", orgNo=" + orgNo + ", cpnyId=" + cpnyId
				+ ", invceYear=" + invceYear + ", invceNo=" + invceNo + ", invceDt=" + invceDt + ", primLangCd="
				+ primLangCd + ", billGrpCd=" + billGrpCd + ", prfilCurrency=" + prfilCurrency + ", nbCurrency="
				+ nbCurrency + ", rapidCdrId=" + rapidCdrId + ", amt=" + amt + ", nbCalls=" + nbCalls + ", fileNm="
				+ fileNm + ", mdiumCd=" + mdiumCd + ", dueDt=" + dueDt + ", filePath=" + filePath + ", fullInvceNo="
				+ fullInvceNo + ", folderPath=" + folderPath + ", orgLegNm=" + orgLegNm + "]";
	}
	
	
	
	}
