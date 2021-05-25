package com.tcl.dias.oms.partner.thirdpartysystem.relayware;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Relayware Service Session Response
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelayWareServiceSessionResponse {

    @JsonProperty("SESSIONID")
    private String SESSIONID;

    public String getSESSIONID() {
        return SESSIONID;
    }

    public void setSESSIONID(String SESSIONID) {
        this.SESSIONID = SESSIONID;
    }
}
