package com.tcl.dias.oms.partner.thirdpartysystem.dnb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * DNB Partner Response
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"candidatesMatchedQuantity", "matchCandidates"})
public class DnBCustomerLeResponse {

    private Integer candidatesMatchedQuantity;

    private List<MatchCandidates> matchCandidates;

    public Integer getCandidatesMatchedQuantity() {
        return candidatesMatchedQuantity;
    }

    public void setCandidatesMatchedQuantity(Integer candidatesMatchedQuantity) {
        this.candidatesMatchedQuantity = candidatesMatchedQuantity;
    }

    public List<MatchCandidates> getMatchCandidates() {
        return matchCandidates;
    }

    public void setMatchCandidates(List<MatchCandidates> matchCandidates) {
        this.matchCandidates = matchCandidates;
    }

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
