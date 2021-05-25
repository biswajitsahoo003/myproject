package com.tcl.dias.oms.beans;

import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.ProductSolution;

import java.util.List;

public class SolutionToSiteMapping {

    ProductSolution solution;
    List<OrderIllSite> sites;

    public ProductSolution getSolution() {
        return solution;
    }

    public void setSolution(ProductSolution solution) {
        this.solution = solution;
    }

    public List<OrderIllSite> getSites() {
        return sites;
    }

    public void setSites(List<OrderIllSite> sites) {
        this.sites = sites;
    }

    @Override
    public String toString() {
        return "SolutionToSiteMapping{" +
                "solution=" + solution +
                ", sites=" + sites +
                '}';
    }
}
