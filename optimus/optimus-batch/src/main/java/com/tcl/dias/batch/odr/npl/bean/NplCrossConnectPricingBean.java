package com.tcl.dias.batch.odr.npl.bean;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "bucket_adjustment_type",
    "cross_connect_arc",
    "cross_connect_nrc",
    "fiber_entry_arc",
    "fiber_entry_nrc",
    "rate_card_flag",
    "error_code",
    "error_flag",
    "error_msg",
    "error_msg_display",
    "site_id",
    "tcv",
    "contract_term"
   
})

public class NplCrossConnectPricingBean {
	@JsonProperty("bucket_adjustment_type")
    private String bucketAdjustmenType;
	@JsonProperty("cross_connect_arc")
    private String crossConnectArc;
	@JsonProperty("cross_connect_nrc")
    private String crossConnectNrc;
	@JsonProperty("fiber_entry_arc")
    private String fiberntEryArc;
	@JsonProperty("fiber_entry_nrc")
    private String fiberEntryNrc;
	@JsonProperty("rate_card_flag")
    private String rateCardFlag;
	@JsonProperty("error_code")
    private String errorCode;
	@JsonProperty("error_flag")
    private String errorFlag;
	@JsonProperty("error_msg")
    private String errorMsg;
	@JsonProperty("error_msg_display")
    private String errorMsgDisplay;
	@JsonProperty("site_id")
    private String siteId;
	@JsonProperty("tcv")
    private String tcv;
	@JsonProperty("contract_term")
    private Integer contractTerm;
	@JsonProperty("unit_fiber_entry_arc")
	private Double unitFiberEntryArc;
	@JsonProperty("unit_fiber_entry_nrc")
	private Double unitFiberEntryNrc;
	@JsonProperty("unit_cross_connect_arc")
	private Double unitCrossConnectArc;
	@JsonProperty("unit_cross_connect_nrc")
	private Double unitCrossConnectNrc;

	@JsonProperty("contract_term")
	public Integer getContractTerm() {
		return contractTerm;
	}
	@JsonProperty("contract_term")
	public void setContractTerm(Integer contractTerm) {
		this.contractTerm = contractTerm;
	}
	
	
	
	@JsonProperty("tcv")
	public String getTcv() {
		return tcv;
	}
	@JsonProperty("tcv")
	public void setTcv(String tcv) {
		this.tcv = tcv;
	}
	@JsonProperty("site_id")
	public String getSiteId() {
		return siteId;
	}
	@JsonProperty("site_id")
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	
	@JsonProperty("bucket_adjustment_type")
	public String getBucketAdjustmenType() {
		return bucketAdjustmenType;
	}
	@JsonProperty("bucket_adjustment_type")
	public void setBucketAdjustmenType(String bucketAdjustmenType) {
		this.bucketAdjustmenType = bucketAdjustmenType;
	}
	@JsonProperty("cross_connect_arc")
	public String getCrossConnectArc() {
		return crossConnectArc;
	}
	@JsonProperty("cross_connect_arc")
	public void setCrossConnectArc(String crossConnectArc) {
		this.crossConnectArc = crossConnectArc;
	}
	@JsonProperty("cross_connect_nrc")
	public String getCrossConnectNrc() {
		return crossConnectNrc;
	}
	@JsonProperty("cross_connect_nrc")
	public void setCrossConnectNrc(String crossConnectNrc) {
		this.crossConnectNrc = crossConnectNrc;
	}
	@JsonProperty("fiber_entry_arc")
	public String getFiberntEryArc() {
		return fiberntEryArc;
	}
	@JsonProperty("fiber_entry_arc")
	public void setFiberntEryArc(String fiberntEryArc) {
		this.fiberntEryArc = fiberntEryArc;
	}
	@JsonProperty("fiber_entry_nrc")
	public String getFiberEntryNrc() {
		return fiberEntryNrc;
	}
	@JsonProperty("fiber_entry_nrc")
	public void setFiberEntryNrc(String fiberEntryNrc) {
		this.fiberEntryNrc = fiberEntryNrc;
	}
	@JsonProperty("rate_card_flag")
	public String getRateCardFlag() {
		return rateCardFlag;
	}
	@JsonProperty("rate_card_flag")
	public void setRateCardFlag(String rateCardFlag) {
		this.rateCardFlag = rateCardFlag;
	}
	@JsonProperty("error_code")
	public String getErrorCode() {
		return errorCode;
	}
	@JsonProperty("error_code")
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	@JsonProperty("error_flag")
	public String getErrorFlag() {
		return errorFlag;
	}
	@JsonProperty("error_flag")
	public void setErrorFlag(String errorFlag) {
		this.errorFlag = errorFlag;
	}
	@JsonProperty("error_msg")
	public String getErrorMsg() {
		return errorMsg;
	}
	@JsonProperty("error_msg")
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	@JsonProperty("error_msg_display")
	public String getErrorMsgDisplay() {
		return errorMsgDisplay;
	}
	@JsonProperty("error_msg_display")
	public void setErrorMsgDisplay(String errorMsgDisplay) {
		this.errorMsgDisplay = errorMsgDisplay;
	}

	public Double getUnitFiberEntryArc() {
		return unitFiberEntryArc;
	}

	public void setUnitFiberEntryArc(Double unitFiberEntryArc) {
		this.unitFiberEntryArc = unitFiberEntryArc;
	}

	public Double getUnitFiberEntryNrc() {
		return unitFiberEntryNrc;
	}

	public void setUnitFiberEntryNrc(Double unitFiberEntryNrc) {
		this.unitFiberEntryNrc = unitFiberEntryNrc;
	}

	public Double getUnitCrossConnectArc() {
		return unitCrossConnectArc;
	}

	public void setUnitCrossConnectArc(Double unitCrossConnectArc) {
		this.unitCrossConnectArc = unitCrossConnectArc;
	}

	public Double getUnitCrossConnectNrc() {
		return unitCrossConnectNrc;
	}

	public void setUnitCrossConnectNrc(Double unitCrossConnectNrc) {
		this.unitCrossConnectNrc = unitCrossConnectNrc;
	}
}
