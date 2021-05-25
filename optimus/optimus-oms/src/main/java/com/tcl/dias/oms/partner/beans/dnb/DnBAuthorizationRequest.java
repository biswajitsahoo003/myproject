package com.tcl.dias.oms.partner.beans.dnb;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DnB Authorization  Request
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DnBAuthorizationRequest {
    @JsonProperty("grant_type")
    private String grantType;

    @JsonProperty("grant_type")
    public String getGrantType() {
        return grantType;
    }

    @JsonProperty("grant_type")
    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }
}
