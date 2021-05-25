package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

import com.tcl.dias.common.servicefulfillment.beans.ScContractInfoBean;
import com.tcl.dias.common.servicefulfillment.beans.ScOrderBean;

public class AttributesBean {

    private List<ComponentAttributesBean> componentAttributesA;

    private List<ComponentAttributesBean> componentAttributesB;

    private List<ServiceAttributesBean> serviceAttributesBeans;

    private ScServiceDetailBean scServiceDetailBean;

    private ScOrderBean scOrderBean;

    private ScContractInfoBean scContractInfoBean;

    private List<ScOrderAttributesBean> scOrderAttributesBeans;

    private TaskAdminBean taskAdminBean;

    public List<ScChargeLineItemBean> scChargeLineItemBeans;

    public List<ScChargeLineItemBean> getScChargeLineItemBeans() {
        return scChargeLineItemBeans;
    }

    public void setScChargeLineItemBeans(List<ScChargeLineItemBean> scChargeLineItemBeans) {
        this.scChargeLineItemBeans = scChargeLineItemBeans;
    }


    public TaskAdminBean getTaskAdminBean() {
        return taskAdminBean;
    }

    public void setTaskAdminBean(TaskAdminBean taskAdminBean) {
        this.taskAdminBean = taskAdminBean;
    }

    public ScContractInfoBean getScContractInfoBean() {
        return scContractInfoBean;
    }

    public void setScContractInfoBean(ScContractInfoBean scContractInfoBean) {
        this.scContractInfoBean = scContractInfoBean;
    }

    public List<ScOrderAttributesBean> getScOrderAttributesBeans() {
        return scOrderAttributesBeans;
    }

    public void setScOrderAttributesBeans(List<ScOrderAttributesBean> scOrderAttributesBeans) {
        this.scOrderAttributesBeans = scOrderAttributesBeans;
    }


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

    public ScOrderBean getScOrderBean() {
        return scOrderBean;
    }

    public void setScOrderBean(ScOrderBean scOrderBean) {
        this.scOrderBean = scOrderBean;
    }
}
