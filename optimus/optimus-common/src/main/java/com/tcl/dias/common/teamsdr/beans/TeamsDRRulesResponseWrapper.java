package com.tcl.dias.common.teamsdr.beans;

import java.util.List;

/**
 * This file contains the TeamsDRRulesResponseWrapper.java
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class TeamsDRRulesResponseWrapper {
    private List<TeamsDRRulesResponse> response;

    public List<TeamsDRRulesResponse> getResponse() {
        return response;
    }

    public void setResponse(List<TeamsDRRulesResponse> response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "TeamsDRRulesResponseWrapper{" +
                "response=" + response +
                '}';
    }
}
