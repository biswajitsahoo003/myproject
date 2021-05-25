package com.tcl.dias.oms.gsc.beans;

import java.util.List;

/**
 * Bean class for GscCityNumber
 *
 * @author VISHESH AWASTHI
 */
public class GscCityNumber {
    private String originCity;
    private List<GscPorting> lnsProtings;

    public String getOriginCity() {
        return originCity;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public List<GscPorting> getLnsPortings() {
        return lnsProtings;
    }

    public void setLnsPortings(List<GscPorting> lnsProtings) {
        this.lnsProtings = lnsProtings;
    }

    @Override
    public String toString() {
        return "GscCityNumber [originCity=" + originCity + ", lnsProtings=" + lnsProtings + "]";
    }

}
