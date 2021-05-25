package com.tcl.dias.servicefulfillmentutils.beans;

/*
    Used for exposing Service Attributes for a circuit
    @author Mayank Sharma
*/

public class ServiceAttributesBean {

    private String name;

    private String value;

    private String category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
