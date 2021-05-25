package com.tcl.dias.servicefulfillmentutils.beans;

/*
    This bean is used to expose Component Attributes for a circuit
    @author Mayank Sharma
 */

public class ComponentAttributesBean {

    private String name;

    private String value;

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

    public ComponentAttributesBean(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
