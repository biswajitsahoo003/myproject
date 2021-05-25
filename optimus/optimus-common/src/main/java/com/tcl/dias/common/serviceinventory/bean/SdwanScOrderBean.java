package com.tcl.dias.common.serviceinventory.bean;

/*
    File Name : SdwanScOrderBean.java
    
    @author Mayank Sharma
<<<<<<< HEAD
    First Created on 21-10-2020 at 10:43
=======
    First Created on 23-11-2020 at 19:47
>>>>>>> dev/order2cash_uat
*/

import java.util.List;
import java.util.Map;

public class SdwanScOrderBean {

    private String solutionCode;
    private String parentOrderCode;
    private List<ScSolutionComponentBean> scSolutionComponentBeans;
    private List<ScOrderBean> scOrderBeans;
    private Map<String, String> cpeSiteReference;

    public List<ScSolutionComponentBean> getScSolutionComponentBeans() {
        return scSolutionComponentBeans;
    }

    public String getSolutionCode() {
        return solutionCode;
    }

    public void setSolutionCode(String solutionCode) {
        this.solutionCode = solutionCode;
    }

    public void setScSolutionComponentBeans(List<ScSolutionComponentBean> scSolutionComponentBeans) {
        this.scSolutionComponentBeans = scSolutionComponentBeans;
    }

    public List<ScOrderBean> getScOrderBeans() {
        return scOrderBeans;
    }

    public void setScOrderBeans(List<ScOrderBean> scOrderBeans) {
        this.scOrderBeans = scOrderBeans;
    }

    public String getParentOrderCode() {
        return parentOrderCode;
    }

    public void setParentOrderCode(String parentOrderCode) {
        this.parentOrderCode = parentOrderCode;
    }

    public Map<String, String> getCpeSiteReference() {
        return cpeSiteReference;
    }

    public void setCpeSiteReference(Map<String, String> cpeSiteReference) {
        this.cpeSiteReference = cpeSiteReference;
    }

}

