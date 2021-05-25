package com.tcl.dias.common.sfdc.response.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * This file contains the FeasibilityResponseBean.java class.
 * 
 *
 * @author PARUNACH
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "status",
    "message",
    "fReqResponseList",
    "errorcode",
    "customReqId"
})
public class FeasibilityResponseBean {

    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("fReqResponseList")
    private List<FResponse> fReqResponseList = null;
    @JsonProperty("errorcode")
    private String errorcode;
    @JsonProperty("customReqId")
    private List<String> customReqId = null;
    private Integer sfdcServiceJobId;
    private boolean isError;
    private Integer siteId;
   
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("errorcode")
    public String getErrorcode() {
        return errorcode;
    }

    @JsonProperty("errorcode")
    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    @JsonProperty("customReqId")
    public List<String> getCustomReqId() {
        return customReqId;
    }

    @JsonProperty("customReqId")
    public void setCustomReqId(List<String> customReqId) {
        this.customReqId = customReqId;
    }

	/**
	 * @return the fReqResponseList
	 */
	public List<FResponse> getfReqResponseList() {
		return fReqResponseList;
	}

	/**
	 * @param fReqResponseList the fReqResponseList to set
	 */
	public void setfReqResponseList(List<FResponse> fReqResponseList) {
		this.fReqResponseList = fReqResponseList;
	}

	/**
	 * @return the sfdcServiceJobId
	 */
	public Integer getSfdcServiceJobId() {
		return sfdcServiceJobId;
	}

	/**
	 * @param sfdcServiceJobId the sfdcServiceJobId to set
	 */
	public void setSfdcServiceJobId(Integer sfdcServiceJobId) {
		this.sfdcServiceJobId = sfdcServiceJobId;
	}

	/**
	 * @return the isError
	 */
	public boolean isError() {
		return isError;
	}

	/**
	 * @param isError the isError to set
	 */
	public void setError(boolean isError) {
		this.isError = isError;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}


}
