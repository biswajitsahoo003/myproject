package com.tcl.dias.common.beans;

import com.tcl.dias.common.beans.LocationItContact;

import java.util.ArrayList;
import java.util.List;

/**
 * Cross Connect Local IT Contacts
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2020 TATA Communications Limited
 */
public class CrossConnectLocalITContacts {

    List<LocationItContact> siteA = new ArrayList<>();
    List<LocationItContact> siteZ = new ArrayList<>();

    public List<LocationItContact> getSiteA() {
        return siteA;
    }

    public void setSiteA(List<LocationItContact> siteA) {
        this.siteA = siteA;
    }

    public List<LocationItContact> getSiteZ() {
        return siteZ;
    }

    public void setSiteZ(List<LocationItContact> siteZ) {
        this.siteZ = siteZ;
    }

    @Override
    public String toString() {
        return "CrossConnectLocalITContacts{" +
                "siteA=" + siteA +
                ", siteZ=" + siteZ +
                '}';
    }
}
