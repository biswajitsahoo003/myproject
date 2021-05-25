package com.tcl.dias.sfdc.bean;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tcl.dias.common.sfdc.response.bean.SfdcActiveCampaignResponseBean;
import java.util.List;

/**
 * SfdcSalesFunnelResponse.
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"status","message","data"})
public class SfdcCampaignReponseBean {

    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private List<SfdcActiveCampaignResponseBean> sfdcActiveCampaignResponseBeans;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SfdcActiveCampaignResponseBean> getSfdcSalesFunnelResponseBean() {
        return sfdcActiveCampaignResponseBeans;
    }

    public void setSfdcSalesFunnelResponseBean(List<SfdcActiveCampaignResponseBean> sfdcActiveCampaignResponseBeans) {
        this.sfdcActiveCampaignResponseBeans = sfdcActiveCampaignResponseBeans;
    }
}
