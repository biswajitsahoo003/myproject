package com.tcl.dias.common.teamsdr.beans;

import java.util.List;

/**
 * This file contains the TeamsDRRulesResponse.java
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class TeamsDRRulesResponse {
    private Integer key;
    private List<SolutionBean> solutionBeans;

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public List<SolutionBean> getSolutionBeans() {
        return solutionBeans;
    }

    public void setSolutionBeans(List<SolutionBean> solutionBeans) {
        this.solutionBeans = solutionBeans;
    }

    @Override
    public String toString() {
        return "TeamsDRRulesResponse{" +
                "key=" + key +
                ", solutionBeans=" + solutionBeans +
                '}';
    }
}
