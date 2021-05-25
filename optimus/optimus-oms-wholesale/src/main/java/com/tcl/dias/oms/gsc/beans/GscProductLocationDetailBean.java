package com.tcl.dias.oms.gsc.beans;

import java.util.List;

/**
 * Country code and its name for GSC Products
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscProductLocationDetailBean {

    private String name;
    private String code;
    private String isdCode;
    private List<String> areaCode;

    public GscProductLocationDetailBean() {

    }

    public GscProductLocationDetailBean(String name) {
        this.name = name;
    }

    public GscProductLocationDetailBean(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public GscProductLocationDetailBean(String name, String code, String isdCode) {
        this.name = name;
        this.code = code;
        this.isdCode = isdCode;
    }

    public GscProductLocationDetailBean(String name, String code, String isdCode, List<String> areaCode) {
        this.name = name;
        this.code = code;
        this.isdCode = isdCode;
        this.areaCode = areaCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIsdCode() {
        return isdCode;
    }

    public void setIsdCode(String isdCode) {
        this.isdCode = isdCode;
    }

    public List<String> getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(List<String> areaCode) {
        this.areaCode = areaCode;
    }

    @Override
    public String toString() {
        return "GscProductLocationDetailBean{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", isdCode='" + isdCode + '\'' +
                ", areaCode='" + areaCode + '\'' +
                '}';
    }
}
