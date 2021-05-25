package com.tcl.dias.oms.npl.beans;

/**
 * This bean stores site addresss for intracity exception rules
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * @Author Chetan Chaudhary
 */

public class IntracityExceptionRulesBean {

    private String endA;
    private String endB;
    private String isActive;

    public String getEndA() {
        return endA;
    }

    public void setEndA(String endA) {
        this.endA = endA;
    }

    public String getEndB() {
        return endB;
    }

    public void setEndB(String endB) {
        this.endB = endB;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "IntracityExceptionRulesBean{" +
                "endA='" + endA + '\'' +
                ", endB='" + endB + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}

