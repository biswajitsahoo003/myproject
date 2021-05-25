package com.tcl.dias.oms.partner.thirdpartysystem.dnb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationNumbers {
    public RegistrationNumbers() {
    }

    ;

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    private String registrationNumber;
}
