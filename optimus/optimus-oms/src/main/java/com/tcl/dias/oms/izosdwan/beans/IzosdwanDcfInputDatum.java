
package com.tcl.dias.oms.izosdwan.beans;

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
 * This file contains the IzosdwanDcfInputDatum.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "markup_pct",
    "opportunity_term",
    "cpe_supply_type",
    "cost_license_mrc",
    "cost_cpe_nrc",
    "port_arc",
    "port_nrc",
    "lm_arc",
    "lm_nrc",
    "cloud_gateway_mrc",
    "vproxy_arc",
    "vutm_arc"
})
public class IzosdwanDcfInputDatum {

    @JsonProperty("markup_pct")
    private String markupPct;
    @JsonProperty("opportunity_term")
    private Integer opportunityTerm;
    @JsonProperty("cpe_supply_type")
    private String cpeSupplyType;
    @JsonProperty("cost_license_mrc")
    private String costLicenseMrc;
    @JsonProperty("cost_cpe_nrc")
    private String costCpeNrc;
    @JsonProperty("port_arc")
    private String portArc;
    @JsonProperty("port_nrc")
    private String portNrc;
    @JsonProperty("lm_arc")
    private String lmArc;
    @JsonProperty("lm_nrc")
    private String lmNrc;
    @JsonProperty("cloud_gateway_mrc")
    private String cloudGatewayMrc;
    
    public String getCloudGatewayMrc() {
		return cloudGatewayMrc;
	}

	public void setCloudGatewayMrc(String cloudGatewayMrc) {
		this.cloudGatewayMrc = cloudGatewayMrc;
	}

	public String getVproxyArc() {
		return vproxyArc;
	}

	public void setVproxyArc(String vproxyArc) {
		this.vproxyArc = vproxyArc;
	}

	public String getVutmArc() {
		return vutmArc;
	}

	public void setVutmArc(String vutmArc) {
		this.vutmArc = vutmArc;
	}

	@JsonProperty("vproxy_arc")
    private String vproxyArc;
    @JsonProperty("vutm_arc")
    private String vutmArc;
    
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("markup_pct")
    public String getMarkupPct() {
        return markupPct;
    }

    @JsonProperty("markup_pct")
    public void setMarkupPct(String markupPct) {
        this.markupPct = markupPct;
    }

    @JsonProperty("opportunity_term")
    public Integer getOpportunityTerm() {
        return opportunityTerm;
    }

    @JsonProperty("opportunity_term")
    public void setOpportunityTerm(Integer opportunityTerm) {
        this.opportunityTerm = opportunityTerm;
    }

    @JsonProperty("cpe_supply_type")
    public String getCpeSupplyType() {
        return cpeSupplyType;
    }

    @JsonProperty("cpe_supply_type")
    public void setCpeSupplyType(String cpeSupplyType) {
        this.cpeSupplyType = cpeSupplyType;
    }

    @JsonProperty("cost_license_mrc")
    public String getCostLicenseMrc() {
        return costLicenseMrc;
    }

    @JsonProperty("cost_license_mrc")
    public void setCostLicenseMrc(String costLicenseMrc) {
        this.costLicenseMrc = costLicenseMrc;
    }

    @JsonProperty("cost_cpe_nrc")
    public String getCostCpeNrc() {
        return costCpeNrc;
    }

    @JsonProperty("cost_cpe_nrc")
    public void setCostCpeNrc(String costCpeNrc) {
        this.costCpeNrc = costCpeNrc;
    }

    @JsonProperty("port_arc")
    public String getPortArc() {
        return portArc;
    }

    @JsonProperty("port_arc")
    public void setPortArc(String portArc) {
        this.portArc = portArc;
    }

    @JsonProperty("port_nrc")
    public String getPortNrc() {
        return portNrc;
    }

    @JsonProperty("port_nrc")
    public void setPortNrc(String portNrc) {
        this.portNrc = portNrc;
    }

    @JsonProperty("lm_arc")
    public String getLmArc() {
        return lmArc;
    }

    @JsonProperty("lm_arc")
    public void setLmArc(String lmArc) {
        this.lmArc = lmArc;
    }

    @JsonProperty("lm_nrc")
    public String getLmNrc() {
        return lmNrc;
    }

    @JsonProperty("lm_nrc")
    public void setLmNrc(String lmNrc) {
        this.lmNrc = lmNrc;
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
