package com.tcl.dias.servicefulfillmentutils.beans;

/*
    This file is used for saving ScComponent Attributes
    @author Mayank Sharma
*/

import java.util.List;

public class ComponentAttributes {

    private List<ComponentAttributesBean> componentAttributesA;
    private List<ComponentAttributesBean> componentAttributesB;

    public List<ComponentAttributesBean> getComponentAttributesA() {
        return componentAttributesA;
    }

    public void setComponentAttributesA(List<ComponentAttributesBean> componentAttributesA) {
        this.componentAttributesA = componentAttributesA;
    }

    public List<ComponentAttributesBean> getComponentAttributesB() {
        return componentAttributesB;
    }

    public void setComponentAttributesB(List<ComponentAttributesBean> componentAttributesB) {
        this.componentAttributesB = componentAttributesB;
    }
}
