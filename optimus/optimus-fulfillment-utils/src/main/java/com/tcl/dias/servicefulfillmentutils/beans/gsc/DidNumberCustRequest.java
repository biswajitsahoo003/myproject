package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DidNumberCustRequest {

    private String  countryAbbr;
    private String cityAbbr;
    private List<DidNumber> numbers;

    public String getCountryAbbr() {
        return countryAbbr;
    }

    public void setCountryAbbr(String countryAbbr) {
        this.countryAbbr = countryAbbr;
    }

    public String getCityAbbr() {
        return cityAbbr;
    }

    public void setCityAbbr(String cityAbbr) {
        this.cityAbbr = cityAbbr;
    }

    public List<DidNumber> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<DidNumber> numbers) {
        this.numbers = numbers;
    }
}
