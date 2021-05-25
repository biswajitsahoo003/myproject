package com.tcl.dias.common.teamsdr.beans;

import java.util.List;

/**
 * This file contains the TeamsDRRulesRequest.java
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class TeamsDRRulesRequest {
    List<SolutionBean> solutions;

    public TeamsDRRulesRequest() {
    }

    public List<SolutionBean> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<SolutionBean> solutions) {
        this.solutions = solutions;
    }

    @Override
    public String toString() {
        return "TeamsDRRulesRequest{" +
                "solutions=" + solutions +
                '}';
    }
}
