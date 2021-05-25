
package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the SfdStagingBean.java class.
 * used to connect with sdfc
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "update_request_v1"
})
public class SfdStagingBean extends BaseBean {

    @JsonProperty("update_request_v1")
    private SfdcStagingOpportunityBean updateRequestV1;

    @JsonIgnore
	private Integer parentQuoteToLeId;

    @JsonProperty("update_request_v1")
    public SfdcStagingOpportunityBean getUpdateRequestV1() {
        return updateRequestV1;
    }

    @JsonProperty("update_request_v1")
    public void setUpdateRequestV1(SfdcStagingOpportunityBean updateRequestV1) {
        this.updateRequestV1 = updateRequestV1;
    }

	public Integer getParentQuoteToLeId() {
		return parentQuoteToLeId;
	}

	public void setParentQuoteToLeId(Integer parentQuoteToLeId) {
		this.parentQuoteToLeId = parentQuoteToLeId;
	}
}
