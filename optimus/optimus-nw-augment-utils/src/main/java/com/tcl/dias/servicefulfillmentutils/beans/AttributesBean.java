package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

public class AttributesBean {

    private List<ComponentAttributesBean> componentAttributesA;

    private List<ComponentAttributesBean> componentAttributesB;

    private List<ServiceAttributesBean> serviceAttributesBeans;

    private ScServiceDetailBean scServiceDetailBean;

    public List<ServiceAttributesBean> getServiceAttributesBeans() {
        return serviceAttributesBeans;
    }

    public void setServiceAttributesBeans(List<ServiceAttributesBean> serviceAttributesBeans) {
        this.serviceAttributesBeans = serviceAttributesBeans;
    }

    public ScServiceDetailBean getScServiceDetailBean() {
        return scServiceDetailBean;
    }

    public void setScServiceDetailBean(ScServiceDetailBean scServiceDetailBean) {
        this.scServiceDetailBean = scServiceDetailBean;
    }

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
