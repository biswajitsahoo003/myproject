package com.tcl.dias.common.beans;

import com.tcl.dias.common.beans.DemarcationBean;

import java.util.List;

/**
 * Cross Connect Demarcation
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2020 TATA Communications Limited
 */
public class CrossConnectDemarcations {
    List<DemarcationBean> siteA;
    List<DemarcationBean> siteZ;

    public List<DemarcationBean> getSiteA() {
        return siteA;
    }

    public void setSiteA(List<DemarcationBean> siteA) {
        this.siteA = siteA;
    }

    public List<DemarcationBean> getSiteZ() {
        return siteZ;
    }

    public void setSiteZ(List<DemarcationBean> siteZ) {
        this.siteZ = siteZ;
    }

    @Override
    public String toString() {
        return "CrossConnectDemarcations{" +
                "siteA=" + siteA +
                ", siteZ=" + siteZ +
                '}';
    }
}
