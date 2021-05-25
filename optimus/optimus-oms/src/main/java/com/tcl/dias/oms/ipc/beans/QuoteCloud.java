package com.tcl.dias.oms.ipc.beans;

import java.util.List;

import com.tcl.dias.oms.beans.SolutionDetail;

public class QuoteCloud {

    private String productName;

    List<SolutionDetail> solutions;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<SolutionDetail> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<SolutionDetail> solutions) {
        this.solutions = solutions;
    }
}
