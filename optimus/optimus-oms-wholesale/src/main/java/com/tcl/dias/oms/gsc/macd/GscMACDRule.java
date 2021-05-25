package com.tcl.dias.oms.gsc.macd;

import java.util.Set;

/**
 * Class to contain specific business rules based on MACD request type
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscMACDRule {
    private String requestType;

    private Set<String> disallowedCurrentRequestTypes;

    public GscMACDRule(String requestType, Set<String> disallowedCurrentRequestTypes) {
        this.requestType = requestType;
        this.disallowedCurrentRequestTypes = disallowedCurrentRequestTypes;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public Set<String> getDisallowedCurrentRequestTypes() {
        return disallowedCurrentRequestTypes;
    }

    public void setDisallowedCurrentRequestTypes(Set<String> disallowedCurrentRequestTypes) {
        this.disallowedCurrentRequestTypes = disallowedCurrentRequestTypes;
    }

    public boolean isThisRequestTypeAllowed(String existingMACDRequestType) {
        return !disallowedCurrentRequestTypes.contains(existingMACDRequestType);
    }
}
