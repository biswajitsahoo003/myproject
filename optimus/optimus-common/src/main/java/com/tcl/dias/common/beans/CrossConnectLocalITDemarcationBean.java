package com.tcl.dias.common.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Cross Connect Local IT Contact and Demarcation
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2020 TATA Communications Limited
 */
public class CrossConnectLocalITDemarcationBean {

    private List<CrossConnectLocalITContacts> contacts = new ArrayList<>();

    private List<CrossConnectDemarcations> demarcations = new ArrayList<>();

    public List<CrossConnectLocalITContacts> getContacts() {
        return contacts;
    }

    public void setContacts(List<CrossConnectLocalITContacts> contacts) {
        this.contacts = contacts;
    }

    public List<CrossConnectDemarcations> getDemarcations() {
        return demarcations;
    }

    public void setDemarcations(List<CrossConnectDemarcations> demarcations) {
        this.demarcations = demarcations;
    }

    @Override
    public String toString() {
        return "CrossConnectLocalITDemarcationBean{" +
                "contacts=" + contacts +
                ", demarcations=" + demarcations +
                '}';
    }
}
