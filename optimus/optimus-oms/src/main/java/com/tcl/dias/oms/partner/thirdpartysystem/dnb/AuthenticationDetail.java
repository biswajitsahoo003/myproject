package com.tcl.dias.oms.partner.thirdpartysystem.dnb;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Authentication Detail to get Token
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class AuthenticationDetail {
    @JsonProperty("Token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
